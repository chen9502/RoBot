package com.biyesheji.android.robot;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.entity.UserAccount;
import com.biyesheji.android.robot.socket.ConnectToServer;
import com.biyesheji.android.robot.socket.GsonList;
import com.biyesheji.android.robot.socket.SocketClient;
import com.biyesheji.android.robot.ui.ConnectActivity;
import com.biyesheji.android.robot.ui.LoginActivity;
import com.biyesheji.android.robot.utils.BluetoothChatService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.biyesheji.android.robot.config.AppInfo.ACTION_MAINACTIVITY;
import static com.biyesheji.android.robot.config.AppInfo.AP_PWD;
import static com.biyesheji.android.robot.config.AppInfo.AP_SSID;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_CONNECTION_LOST;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_CONNECT_FAILED;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_DEVICE_NAME;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_READ;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_STATE_CHANGE;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_SWITCHTO_MAINACTIVITY;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_TOAST;
import static com.biyesheji.android.robot.config.AppInfo.MESSAGE_WRITE;
import static com.biyesheji.android.robot.config.AppInfo.PORT;
import static com.biyesheji.android.robot.config.AppInfo.isCompleteRecvMsg;
import static com.biyesheji.android.robot.config.AppInfo.pwd_wifi;
import static com.biyesheji.android.robot.config.AppInfo.ssid_wifi;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MyApp extends Application{
    private static MyApp app;
    public static Context context;
    public static Context getContextObject() {
        return context;
    }
    public static List<Map<String, Object>> gxdzList = new ArrayList<>();
    public static Map<String, Object> gxdzMap;

    //蓝牙
    public static BluetoothChatService mChatService;
    // Debugging
    private static final String TAG = "wxwx";
    private static final boolean D = true;
    private JSONArray jsonArray;
    private String type;
    private Message msg = Message.obtain();
    private Intent intent;
    public static BluetoothDevice robotBTdevice = null;
    public static Handler MyAppHandler;
    public static Socket client;
    public static boolean isFirstToMainactivity = true;
    private boolean isNeedToReConnectToRobot = false;
    public static boolean isNormalBackToMainActivity = false;
    private int reConnectTorobotCounter = 0;
    private Timer reConnectTorobotTimer = null;
    public final int getReConnectTorobotTryTimes = 15;
    public static MyApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        
        
        super.onCreate();
        this.app = this;
        initDButils();
        sharep();
        initbluetooth();

    }

    private void initbluetooth() {
        mChatService = new BluetoothChatService(this, mHandler);

        context = getApplicationContext();

        IntentFilter filter =  new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(BluetoothStatusReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(BluetoothStatusReceiver, filter);

        filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        registerReceiver(BluetoothStatusReceiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(BluetoothStatusReceiver, filter);

        MyAppHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String strIp = msg.obj.toString();
                int msgType = msg.arg1;
                switch (msgType){
                    case 1: //连接到机器人
                        //关闭已有的socket线程
                        if ((MyApp.client != null)) {
                            if (MyApp.client.isClosed()) {
                                try {
                                    MyApp.client.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        ConnectToServer.isRurning = false;
                        SystemClock.sleep(500);
                        ConnectToServer.isRurning = true;
                        ConnectToServer.connectServer(strIp,PORT);
                        break;
                }

            }
        };
    }
    // The Handler that gets information back from the BluetoothChatService
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String ip = null;
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.d("wxwx", "BT MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            switch (AppInfo.isAPPinAPmode) {
                                case "true":
                                    GsonList.gsonListViaBT("apstation",255,AP_SSID,AP_PWD,"default");
                                    // GsonList.gsonListViaBT("apstation",255,"gfdtek2G","gfd@xian2","default");
                                    break;
                                case "false":
                                    GsonList.gsonListViaBT("router",255,ssid_wifi,pwd_wifi,"default");
                                    break;
                            }
                            if(reConnectTorobotTimer!=null) {
                                reConnectTorobotTimer.cancel();
                                reConnectTorobotTimer.purge();
                                reConnectTorobotTimer = null;
                            }
                            isNeedToReConnectToRobot = false;
                            if(reConnectTorobotTimertask !=null) {
                                reConnectTorobotTimertask.cancel();
                                reConnectTorobotTimertask = null;
                                reConnectTorobotTimertask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (D) Log.d(TAG, "--------reConnectTorobotTimertask isNormalBackToMainActivity  ------------- "+isNormalBackToMainActivity);
                                        if (isNormalBackToMainActivity == false)
                                        {
                                            if (D) Log.d(TAG, "--------reConnectTorobotTimertask Running ------------- ");
                                            if (robotBTdevice != null && isNeedToReConnectToRobot == true) {
                                                if (reConnectTorobotCounter < getReConnectTorobotTryTimes) {
                                                    MyApp.mChatService.connect(robotBTdevice, true);
                                                    reConnectTorobotCounter++;
                                                } else {
                                                    if (D) Log.d(TAG, "--------reConnectTorobotTimertask Stopped !!! ,reConnectTorobotCounter is: "+reConnectTorobotCounter);
                                                    isNeedToReConnectToRobot = false;
                                                    reConnectTorobotTimer.cancel();
                                                    reConnectTorobotTimer.purge();
                                                    reConnectTorobotTimer= null;

                                                    reConnectTorobotTimertask.cancel();
                                                    reConnectTorobotTimertask = null;
                                                    reConnectTorobotCounter = 0;

                                           /*         Message msg = new Message();
                                                    msg.what = MyApp.MESSAGE_SWITCHTO_MAINACTIVITY;
                                                    mHandler.sendMessage(msg);*/
                                                }
                                            }
                                        }
                                        else
                                        {
                                            isNeedToReConnectToRobot = false;
                                            reConnectTorobotTimer.cancel();
                                            reConnectTorobotTimer.purge();
                                            reConnectTorobotTimer= null;

                                            reConnectTorobotTimertask.cancel();
                                            reConnectTorobotTimertask = null;
                                        }
                                    }
                                };
                            }

                           /* if(isFirstToMainactivity){
                                isFirstToMainactivity = false;
                                intent = new Intent(context,ActionActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }*/
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_CONNECTION_LOST:
                    if(D) Log.d(TAG, "--------MESSAGE_CONNECTION_LOST ------------- ");
                    ConnectActivity.dialog.dismiss();
                    ConnectActivity.dialog.cancel();
                    isNeedToReConnectToRobot = true;
                    if(reConnectTorobotTimer == null) {
                        reConnectTorobotTimer = new Timer();
                    }
                    reConnectTorobotTimertask = new TimerTask() {
                        @Override
                        public void run() {
                            if (D) Log.d(TAG, "--------reConnectTorobotTimertask isNormalBackToMainActivity  ------------- "+isNormalBackToMainActivity);
                            if (isNormalBackToMainActivity == false)
                            {
                                if (D) Log.d(TAG, "--------reConnectTorobotTimertask Running ------------- ");
                                if (robotBTdevice != null && isNeedToReConnectToRobot == true) {
                                    if (reConnectTorobotCounter < getReConnectTorobotTryTimes) {
                                        MyApp.mChatService.connect(robotBTdevice, true);
                                        reConnectTorobotCounter++;
                                    } else {
                                        if (D) Log.d(TAG, "--------reConnectTorobotTimertask Stopped !!! ,reConnectTorobotCounter is: "+reConnectTorobotCounter);
                                        isNeedToReConnectToRobot = false;
                                        reConnectTorobotTimer.cancel();
                                        reConnectTorobotTimer.purge();
                                        reConnectTorobotTimer= null;
                                        reConnectTorobotTimertask.cancel();
                                        reConnectTorobotTimertask = null;
                                        reConnectTorobotCounter = 0;

                          /*              Message msg = new Message();
                                        msg.what = MyApp.MESSAGE_SWITCHTO_MAINACTIVITY;
                                        mHandler.sendMessage(msg);*/
                                    }
                                }
                            }
                            else
                            {
                                isNeedToReConnectToRobot = false;
                                reConnectTorobotTimer.cancel();
                                reConnectTorobotTimer.purge();
                                reConnectTorobotTimer= null;

                                reConnectTorobotTimertask.cancel();
                                reConnectTorobotTimertask = null;
                            }

                        }
                    };
                    // reConnectTorobotTimer.schedule(reConnectTorobotTimertask,0,2000);
                    break;
                case MESSAGE_CONNECT_FAILED:
                    if(D) Log.d(TAG, "--------MESSAGE_CONNECT_FAILED ------------- ");
                    isNeedToReConnectToRobot = true;
                    if(reConnectTorobotTimer == null) {
                        reConnectTorobotTimer = new Timer();
                    }
                    reConnectTorobotTimertask = new TimerTask() {
                        @Override
                        public void run() {
                            if (D) Log.d(TAG, "--------reConnectTorobotTimertask isNormalBackToMainActivity  ------------- "+isNormalBackToMainActivity);
                            if (isNormalBackToMainActivity == false)
                            {
                                if (D) Log.d(TAG, "--------reConnectTorobotTimertask Running ------------- ");
                                if (robotBTdevice != null && isNeedToReConnectToRobot == true) {
                                    if (reConnectTorobotCounter < getReConnectTorobotTryTimes) {
                                        MyApp.mChatService.connect(robotBTdevice, true);
                                        reConnectTorobotCounter++;
                                    } else {
                                        if (D) Log.d(TAG, "--------reConnectTorobotTimertask Stopped !!! ,reConnectTorobotCounter is: "+reConnectTorobotCounter);
                                        isNeedToReConnectToRobot = false;
                                        reConnectTorobotTimer.cancel();
                                        reConnectTorobotTimer.purge();
                                        reConnectTorobotTimer= null;

                                        reConnectTorobotTimertask.cancel();
                                        reConnectTorobotTimertask = null;
                                        reConnectTorobotCounter = 0;
                                  /*      Message msg = new Message();
                                        msg.what = MyApp.MESSAGE_SWITCHTO_MAINACTIVITY;
                                        mHandler.sendMessage(msg);*/
                                    }
                                }
                            }
                            else
                            {
                                isNeedToReConnectToRobot = false;
                                reConnectTorobotTimer.cancel();
                                reConnectTorobotTimer.purge();
                                reConnectTorobotTimer= null;

                                reConnectTorobotTimertask.cancel();
                                reConnectTorobotTimertask = null;
                                reConnectTorobotCounter = 0;

                  /*              Message msg = new Message();
                                msg.what = MyApp.MESSAGE_SWITCHTO_MAINACTIVITY;
                                mHandler.sendMessage(msg);*/
                            }

                        }
                    };
                    //reConnectTorobotTimer.schedule(reConnectTorobotTimertask,0,3000);


                    break;
                case MESSAGE_SWITCHTO_MAINACTIVITY:
/*                    intent = new Intent(context,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:
                    //Bundle databundle = msg.getData();

                    // String readMessage = databundle.getString("BTdata");



                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Log.d("wxwx", "-----mHandler  收到数据---------" + readMessage +"长度是:"+readMessage.length());
                    /*if(fmsg != null) {
                        if (fmsg.length() >= 1000) {
                            if(readMessage == null)
                            {
                                readMessage = fmsg;
                            }else {
                                readMessage += fmsg;
                            }
                        } else {
                            isCompleteRecvMsg = true;
                            if(readMessage == null)
                            {
                                readMessage = fmsg;
                            }else {
                                readMessage += fmsg;
                            }
                        }
                        fmsg = null;
                    }*/
                    isCompleteRecvMsg = true;
                    Log.d(TAG, "-------------------------handleMessage isCompleteRecvMsg:"+isCompleteRecvMsg);
                    if (isCompleteRecvMsg == true) {
                        Log.d(TAG, "-------------------------handleMessage  readMessage-------------- "+readMessage+"---- 长度是："+readMessage.length());
                        isCompleteRecvMsg = false;
                        try {
                            jsonArray = new JSONArray(readMessage);
                            readMessage = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                type = object.optString("type");
                                Log.d("wxwx", "======jsonArray==type===========" + type);
                                ip = object.optString("para2");
                                Log.d("wxwx", "======jsonArray==ip===========" + ip);
                                intent = new Intent(ACTION_MAINACTIVITY);
                                switch (type){
                                    case "serviceIP":
                                        GsonList.gsonListViaBT("ServiceIPResp",255,"default","default","default");
                                        Message message = Message.obtain();
                                        message.obj = ip;
                                        message.arg1 = 1;   //连接到机器人类型
                                        MyApp.MyAppHandler.sendMessage(message);
                                        IntentFilter intentFilter = new IntentFilter(SocketClient.ACTION_CONNECTION);
                                        registerReceiver(broadcastReceiver1,intentFilter);
                                        break;
                                    case "wifiConnectFailed":
                                        ConnectActivity.dialog.dismiss();
                                        ConnectActivity.dialog.cancel();
                                        ConnectActivity.viewUpdateHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //   MainActivity.textstatus.setText("维维连接wifi失败");
                                            }
                                        });
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    break;
                case MESSAGE_TOAST:
                    //   Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                    //            Toast.LENGTH_SHORT).show();
                    ConnectActivity.dialog.dismiss();
                    ConnectActivity.dialog.cancel();
                    break;
            }
        }
    };
    private TimerTask reConnectTorobotTimertask = new TimerTask() {
        @Override
        public void run() {
            if (D) Log.d(TAG, "--------reConnectTorobotTimertask isNormalBackToMainActivity  ------------- "+isNormalBackToMainActivity);
            if (isNormalBackToMainActivity == false)
            {
                if (D) Log.d(TAG, "--------reConnectTorobotTimertask Running ------------- ");
                if (robotBTdevice != null && isNeedToReConnectToRobot == true) {
                    if (reConnectTorobotCounter < getReConnectTorobotTryTimes) {
                        MyApp.mChatService.connect(robotBTdevice, true);
                        reConnectTorobotCounter++;
                    } else {
                        if (D) Log.d(TAG, "--------reConnectTorobotTimertask Stopped !!! ,reConnectTorobotCounter is: "+reConnectTorobotCounter);
                        isNeedToReConnectToRobot = false;
                        reConnectTorobotTimer.cancel();
                        reConnectTorobotTimer.purge();
                        reConnectTorobotTimer= null;

                        reConnectTorobotTimertask.cancel();
                        reConnectTorobotTimertask = null;
                        reConnectTorobotCounter = 0;
/*
                        Message msg = new Message();
                        msg.what = MyApp.MESSAGE_SWITCHTO_MAINACTIVITY;
                        mHandler.sendMessage(msg);*/
                    }
                }
            }
            else
            {
                isNeedToReConnectToRobot = false;
                reConnectTorobotTimer.cancel();
                reConnectTorobotTimer.purge();
                reConnectTorobotTimer= null;

                reConnectTorobotTimertask.cancel();
                reConnectTorobotTimertask = null;
                reConnectTorobotCounter = 0;

               /* Message msg = new Message();
                msg.what = MyApp.MESSAGE_SWITCHTO_MAINACTIVITY;
                mHandler.sendMessage(msg);*/
            }

        }
    };

    BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean socketclient = intent.getBooleanExtra("socketclient", true);
            if(socketclient == true){
                Log.d("wxwx","------broadcastReceiver1  连接到维维---------------");
                showToast("连接机器人成功");
                intent = new Intent(MyApp.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("wxwx","------broadcastReceiver1  连接到维维  intent---------------"+intent);
                startActivity(intent);
            }else {
                showToast("连接机器人失败");
                ConnectActivity.dialog.dismiss();
                ConnectActivity.dialog.cancel();
            }

        }
    };


    private final BroadcastReceiver BluetoothStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            switch (action){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:

                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  BluetoothAdapter.STATE_TURNING_OFF  mChatService.stop()-------------------");

                            break;
                        case BluetoothAdapter.STATE_ON:
                            if(D) Log.d(TAG, "--------------BluetotthStatusReceiver  BluetoothAdapter.STATE_ON-------------------");

                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  STATE_TURNING_ON-----------------");
                            break;
                    }
                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    final int stateConnect = intent.getIntExtra(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED,BluetoothAdapter.ERROR);
                    switch (stateConnect){
                        case BluetoothAdapter.STATE_DISCONNECTED:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  STATE_DISCONNECTED-----------------");
                            break;
                        case BluetoothAdapter.STATE_CONNECTING:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  STATE_CONNECTING-----------------");
                            break;
                        case BluetoothAdapter.STATE_CONNECTED:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  STATE_CONNECTED-----------------");
                            break;
                        case BluetoothAdapter.STATE_DISCONNECTING:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  STATE_DISCONNECTING-----------------");
                            break;
                        default:
                            if(D) Log.d(TAG, "---------------BluetotthStatusReceiver default:stateConnect-----------------"+stateConnect);
                            break;
                    }

                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    if(D) Log.d(TAG, "---------------BluetotthStatusReceiver  BluetoothDevice.ACTION_ACL_CONNECTED-----------------");

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    if(D) Log.d(TAG, "---------------MyApp BluetoothDevice.ACTION_ACL_DISCONNECTED-----------------");
                    Message msg = mHandler.obtainMessage(AppInfo.MESSAGE_CONNECTION_LOST);
                    mHandler.sendMessage(msg);
                    break;
            }
        }
    };

    private void showToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    
    
    
    
    
    
    
    
    

    private void sharep() {
        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
        boolean first = setting.getBoolean("first", true);
        if (first){
            //创建数据库并加入数据
            UserAccount account1 = new UserAccount("Admin","123456","1");
            UserAccount account2 = new UserAccount("teacher1","123456","2");
            UserAccount account3 = new UserAccount("teacher2","123456","3");
            UserAccount account4 = new UserAccount("student1","123456","4");
            UserAccount account5 = new UserAccount("student2","123456","5");
            DbManager db = x.getDb(daoConfig);

            try {
                db.saveOrUpdate(account1);
                db.saveOrUpdate(account2);
                db.saveOrUpdate(account3);
                db.saveOrUpdate(account4);
                db.saveOrUpdate(account5);
            } catch (DbException e) {
                e.printStackTrace();
            }


            setting.edit().putBoolean("first",false).commit();
        }
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    private DbManager.DaoConfig daoConfig;
    private void initDButils() {
        x.Ext.init(this);//Xutils初始化
        daoConfig= new DbManager.DaoConfig()
                .setDbName("robot")//创建数据库的名称
                .setDbVersion(1)//数据库版本号
               ;//数据库更新操作
    }


}
