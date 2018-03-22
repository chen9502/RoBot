package com.biyesheji.android.robot.socket;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.entity.AddButton;
import com.biyesheji.android.robot.fragment.GexinFragment;
import com.biyesheji.android.robot.ui.ConnectActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class SocketClient {
    private Context context;
    private int port;           //IP
    private String site;            //端口
    private Thread thread;
    public static boolean isClient = false;
    public static PrintWriter out;
    public static BufferedReader br;
    private String line;
    private JSONArray jsonArray;
    private String type;
    private Message msg = Message.obtain();
    public static String ACTION_DATABASE = "json.database.action";
    public static String ACTION_MAINACTIVITY = "json.askanswer.action";
    public static String ACTION_CONNECTION = "com.socketconnect";
    public static String ACTION_GRIDVIEW = "com.gridview";
    public static String ACTION_SOCKETDISCONNETED = "com.disconnected";
    public static String ACTION_ROBOT_OPERATEMODE_CHANGE = "com.robotoperatemode";
    public static String ACTION_SETTING_BUTTON = "com.settingbutton";
    private Intent intent;
    private Handler handler = new Handler();
    private boolean isWifiConncetionFailed = true;
    private int WifiConncetionFailedCounter = 0;
    private Timer timer = null;


    /**
     * @effect 开启线程建立连接开启客户端
     */
    public void openClientThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("wxwx", "----start openClientThread  runningflag---------------" + ConnectToServer.isRurning);
                while (ConnectToServer.isRurning) {
                    try {
                        /**
                         *  connect()步骤
                         * */
                        //if (client == null || client.isClosed())
                        if ((MyApp.client != null)) {
                            if (!MyApp.client.isClosed()) {
                                MyApp.client.close();
                            }
                        }
                        Log.d("wxwx", "----Create Socket : Site is  " + site + "      Port is:" + port);
                        MyApp.client = new Socket(site, port);
                        // 发送数据包，默认为 false，即客户端发送数据采用 Nagle 算法；
                        // 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去
                       /* client.setTcpNoDelay(true);
                        client.setSoLinger(true,30); // 设置客户端 socket 关闭时，close() 方法起作用时延迟 30 秒关闭，如果 30 秒内尽量将未发送的数据包发送出去
                        client.setSendBufferSize(4096); // 设置输入流的接收缓冲区大小，默认是4KB，即4096字节
                        // 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket
                        // 防止服务器端无效时，客户端长时间处于连接状态
                        client.setKeepAlive(true);*/
                        if (MyApp.client != null) {
                            if(MyApp.client.isConnected()){
                                intent = new Intent(ACTION_CONNECTION);
                                intent.putExtra("socketclient", true);
                                MyApp.getContextObject().sendBroadcast(intent);
                                Log.d("wxwx", "----Socket connected successfully ---------------");
                            }
                            Log.d("wxwx", "----Create Socket obj successfully ---------------");
                            Log.d("wxwx", "----socket连接后关闭蓝牙socket连接 ---------------");
                            MyApp.mChatService.stopService();

                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {

                                    if (MyApp.client != null && MyApp.client.isConnected()) {
                                        //Log.d("wxwx", "------打印测试---------");
                                        if(isWifiConncetionFailed == true) {
                                            WifiConncetionFailedCounter++;
                                        }else
                                        {
                                            WifiConncetionFailedCounter = 0;
                                        }
                                        Log.d("wxwx", "------收到来自robot的Heartbeat包 失败计数器:---------"+WifiConncetionFailedCounter);
                                        if(WifiConncetionFailedCounter > 10)
                                        {
                                            WifiConncetionFailedCounter = 0;
                                            Log.d("wxwx", "------未收到来自robot的Heartbeat包，断开socket连接---------");
                                            intent = new Intent(ACTION_SOCKETDISCONNETED);
                                            intent.putExtra("disconnected", true);
                                            MyApp.getContextObject().sendBroadcast(intent);
                                            try {
                                                MyApp.client.close();
                                                MyApp.client = null;
                                                ConnectToServer.isRurning = false;
                                                isClient = false;
                                                br.close();
                                                out.close();
                                            } catch (Exception e1) {
                                                e1.printStackTrace();

                                            }
                                            timer.purge();
                                            timer.cancel();
                                            timer = null;
                                        }
                                        //如果没有收到robot的心跳包，则该标志位会变为true
                                            isWifiConncetionFailed = true; //将标志位重置为true，该标志位会在收到robot的heartbeat后置为false
                                    }
                                }
                            },4000,4000);
                            //  client.setSoTimeout(50000);//设置超时时间
                            isClient = true;
                            forOut();
                            forIn();
                        } else {
                            isClient = false;
                            Toast.makeText(context, "网络连接失败", Toast.LENGTH_LONG).show();
                            ConnectActivity.dialog.dismiss();
                            ConnectActivity.dialog.cancel();
                        }
                        Log.d("wxwx", "site=" + site + " ,port=" + port);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Log.d("wxwx", "7");
                    }
                }
                Log.d("wxwx", "----openClientThread  runningflag---------------" + ConnectToServer.isRurning);
            }
        });
        thread.start();
    }

    /**
     * 调用时向类里传值
     */
    public void clintValue(Context context, String site, int port) {
        this.context = context;
        this.site = site;
        this.port = port;
    }

    /**
     * @effect 得到输出字符串
     */
    public void forOut() {
        try {
            //  out=new PrintWriter ( client.getOutputStream () );
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MyApp.client.getOutputStream())));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("wxwx", "8");
        }
    }

    /**
     * @steps read();
     * @effect 得到输入字符串
     */
    public void forIn() {
        boolean bIsSuccessful = false;
        int resultid;
        String resultask;
        String resultanswer;
        String opt;
        String optresult;
        String data;
        String optdata;
        try {

            br = new BufferedReader(new InputStreamReader(MyApp.client.getInputStream()));
            while (isClient) {
                //  in=client.getInputStream ();
                line = null;
                if ((line = br.readLine()) != null) {
                    Log.d("wxwx", "-----收到数据---------" + line);
                    if (line != null) {
                        try {
                            jsonArray = new JSONArray(line);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                type = object.optString("type");
                                Log.d("wxwx", "======jsonArray==type===========" + type);
                                data = object.optString("para5");
                                intent = new Intent(ACTION_MAINACTIVITY);
                                switch (data) {
                                    case "1": //执行结果成功
                                        bIsSuccessful = true;
                                        if(type.equals("heartbeat")){

                                        }else {
                                            if (jsonArray.length() > 1) {
                                                if ((i % 100) == 0)//每50条记录，打印一次结果
                                                {
                                                    intent.putExtra("result", "指令执行成功");
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                }
                                            } else {
                                                intent.putExtra("result", "指令执行成功");
                                                MyApp.getContextObject().sendBroadcast(intent);
                                            }
                                        }

                                        break;
                                    case "2": //执行失败
                                        intent.putExtra("result", "指令执行失败");
                                        MyApp.getContextObject().sendBroadcast(intent);
                                        bIsSuccessful = false;
                                        break;
                                    default: //缺省无效值
                                        intent.putExtra("result", "缺省无效值");
                                        MyApp.getContextObject().sendBroadcast(intent);
                                        break;
                                }
                                if (bIsSuccessful) {
                                    optdata = object.optString("para4");
                                    switch (type) {
                                        case "yuyinzhuantaichaxun":
                                            String para3 =  object.optString("para3");
                                            if(optdata.equals("true")){
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                                        SettingFragment.yuyinkongzhiKG.setChecked(true);
                                                    }
                                                });
                                            }else
                                            {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                                        SettingFragment.yuyinkongzhiKG.setChecked(false);
                                                    }
                                                });
                                            }
                                            if(para3.equals("true")){
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                                        SettingFragment.askanswerKG.setChecked(true);
                                                    }
                                                });
                                            }else
                                            {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                                        SettingFragment.askanswerKG.setChecked(false);
                                                    }
                                                });
                                            }
                                            break;
                                        case "status":
                                            switch (optdata){
                                                case "oprateMode":
                                                    Log.d("wxwx","------socketclient-status oprateMode---------------------");
                                                    String moshi = object.optString("para1");
                                                    Log.d("wxwx","------socketclient-status oprateMode--para1-------------------"+moshi);
                                                    intent = new Intent(ACTION_ROBOT_OPERATEMODE_CHANGE);
                                                    intent.putExtra("robotoptmode", Integer.parseInt(moshi));
                                                    AppInfo.robotCurrentOprtionMode = Integer.parseInt(moshi); //将机器人运行模式记录到全局变量
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    break;
                                            }
                                            break;
                                        case "heartbeat":  //心跳
                                            isWifiConncetionFailed = false;
                                            Log.d("wxwx","-------heartbeat recv------- -----------------");
                                            GsonList.gsonList("heartbeatEcho",255,"default","default","heartbeatEcho");

                                            break;
                                        case "database": //查询操作
                                        {
                                            intent = new Intent(ACTION_DATABASE);
                                            switch (optdata) {
                                                case "1"://查询
                                                    resultid = object.optInt("para1");
                                                    resultask = object.optString("para2");
                                                    resultanswer = object.optString("para3");
                                                    opt = object.optString("para4");
                                                    optresult = object.optString("para5");

                                                    Log.d("wxwx", "------resultid-----------" + resultid);
                                                    Log.d("wxwx", "------resultask-----------" + resultask);
                                                    Log.d("wxwx", "------resultanswer-----------" + resultanswer);
                                                    Log.d("wxwx", "------opt-----------" + opt);
                                                    Log.d("wxwx", "------optresult-----------" + optresult);

                                                    intent.putExtra("para1", resultid);
                                                    intent.putExtra("para2", resultask);
                                                    intent.putExtra("para3", resultanswer);
                                                    intent.putExtra("para4", opt);
                                                    intent.putExtra("para5", optresult);
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    Log.d("wxwx", "----------------" + intent.toString());
                                                    break;
                                                case "2": //修改
                                                    resultid = object.optInt("para1");
                                                    resultask = object.optString("para2");
                                                    resultanswer = object.optString("para3");
                                                    opt = object.optString("para4");
                                                    optresult = object.optString("para5");

                                                    intent.putExtra("para1", resultid);
                                                    intent.putExtra("para2", resultask);
                                                    intent.putExtra("para3", resultanswer);
                                                    intent.putExtra("para4", opt);
                                                    intent.putExtra("para5", optresult);
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    break;
                                                case "3": //删除
                                                    resultid = object.optInt("para1");
                                                    resultask = object.getString("para2");
                                                    resultanswer = object.optString("para3");
                                                    opt = object.optString("para4");
                                                    optresult = object.optString("para5");

                                                    intent.putExtra("para1", resultid);
                                                    intent.putExtra("para2", resultask);
                                                    intent.putExtra("para3", resultanswer);
                                                    intent.putExtra("para4", opt);
                                                    intent.putExtra("para5", optresult);
                                                    MyApp.getContextObject().sendBroadcast(intent);

                                                    break;
                                                case "4"://添加
                                                    resultid = object.optInt("para1");
                                                    resultask = object.getString("para2");
                                                    resultanswer = object.optString("para3");
                                                    opt = object.optString("para4");
                                                    optresult = object.optString("para5");

                                                    intent.putExtra("para1", resultid);
                                                    intent.putExtra("para2", resultask);
                                                    intent.putExtra("para3", resultanswer);
                                                    intent.putExtra("para4", opt);
                                                    intent.putExtra("para5", optresult);
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    break;
                                                case "5":
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        break;
                                        case "askanswer":
                                            Log.d("wxwx","------askanswer----------------");
                                            Log.d("wxwx","------askanswer optdata----------------"+optdata);
                                            intent = new Intent(ACTION_SETTING_BUTTON);
                                            switch (optdata){
                                                case "dakaiyuyinwenda":
                                                    intent.putExtra("yuyin","dakaiyuyinwenda");
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    break;
                                                case "guanbiyuyinwenda":
                                                    intent.putExtra("yuyin","guanbiyuyinwenda");
                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    break;
                                            }
                                            break;
                                        case "setting":
                                            intent = new Intent(ACTION_SETTING_BUTTON);
                                            switch (optdata){
                                            case "kaiqiyuyinkongzhi":
                                                intent.putExtra("yuyin","kaiqiyuyinkongzhi");
                                                MyApp.getContextObject().sendBroadcast(intent);
                                                break;
                                            case "guanbiyuyinkongzhi":
                                                intent.putExtra("yuyin","guanbiyuyinkongzhi");
                                                MyApp.getContextObject().sendBroadcast(intent);
                                                break;
                                        }
                                            break;

                                        case "speaker":

                                            break;
                                        case "yingliangtiaozheng":

                                            break;
                                        case "serialport":

                                            break;
                                        case "apstation":
                                            break;
                                        case "gexindongzuoanniupeizhi":
                                            resultid = object.optInt("para1");
                                            resultask = object.optString("para2");
                                            resultanswer = object.optString("para3");
                                            opt = object.optString("para4");
                                            intent = new Intent(ACTION_GRIDVIEW);
                                            switch (opt) {
                                                case "queryall":
                                                    AddButton addButton = new AddButton(resultid,resultask,resultanswer);
                                                    GexinFragment.listGridview.add(addButton);

                                                    MyApp.gxdzMap = new HashMap<>();
                                                    MyApp.gxdzMap.put("databaseid", resultid);
                                                    MyApp.gxdzMap.put("mingcheng", resultask);
                                                    MyApp.gxdzMap.put("bianhao", resultanswer);
                                                    MyApp.gxdzList.add( MyApp.gxdzMap);

                                                    Log.d("wxwx","=======gxdz===resultask========"+resultask);
                                                    Log.d("wxwx","=======gxdz===resultanswer========"+resultanswer);
                                                    Log.d("wxwx","-----socketclient----ActionActivity.gxdzList-------------"+ MyApp.gxdzList.size());
                                                    break;
                                                case "add":
                                                    intent.putExtra("gridview","refresh");
                                                    AddButton addButtonadd = new AddButton(resultid,resultask,resultanswer);
                                                    Log.d("wxwx","=======socketclient===resultask  GexinFragment.listGridview.size========"+ GexinFragment.listGridview.size());
                                                    GexinFragment.listGridview.add(addButtonadd);
                                                    Log.d("wxwx","=======socketclient===resultask  GexinFragment.listGridview.size  after Add========"+ GexinFragment.listGridview.size());

                                                    MyApp.getContextObject().sendBroadcast(intent);
                                                    break;
                                                case "delete":
                                                    intent.putExtra("gridview","refresh");
                                                    Log.d("wxwx","---socket----GexinFragment.itemlongclick_currentPosition------------------------"+ GexinFragment.itemlongclick_currentPosition);
                                                    Log.d("wxwx","---socket----GexinFragment.listGridview.size()------------------------"+ GexinFragment.listGridview.size());

                                                    if (GexinFragment.listGridview.size() == 1) {
                                                        GexinFragment.listGridview.clear();
                                                    }
                                                    else
                                                    {
                                                        GexinFragment.listGridview.remove(GexinFragment.itemlongclick_currentPosition);
                                                    }

                                                    MyApp.getContextObject().sendBroadcast(intent);

                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        default://对于不需要后续处理的Type，使用defaultType类型，在default case中空转

                                            break;
                                    }
                                }
                            }
                            bIsSuccessful = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

              /*   //得到的是16进制数，需要进行解析
               byte[] bt = new byte[50];
                in.read ( bt );
                str=new String ( bt,"UTF-8" );*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @steps write();
     * @effect 发送消息
     */
    public void sendMsg(final String str) {

        new Thread(new Runnable() {
            @Override
            public void run() {
               // Looper.prepare();
                Log.d("wxwx", "-----sendmessage--------------");
                if (MyApp.client != null) {
                    out.println(str);
                    out.flush();
                    Log.d("wxwx", "----发数据---------" + out + "");
                } else {
                    isClient = false;
                    Log.d("wxwx", "---------send message--client is null---------");
                    //Toast.makeText ( context,"网络连接失败", Toast.LENGTH_LONG ).show ();
                }
                //Looper.loop();
            }
        }).start();

    }
}
