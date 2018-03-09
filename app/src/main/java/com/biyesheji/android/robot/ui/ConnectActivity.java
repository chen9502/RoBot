package com.biyesheji.android.robot.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.socket.ConnectToServer;
import com.biyesheji.android.robot.socket.GsonList;
import com.biyesheji.android.robot.socket.SocketClient;
import com.biyesheji.android.robot.utils.ClsUtils;
import com.biyesheji.android.robot.utils.PermissionUtils;
import com.biyesheji.android.robot.utils.WifiHotUtil;
import com.biyesheji.android.robot.utils.WifiUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConnectActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    // Debugging
    private static final String TAG = "wxwx";
    private static final boolean D = true;

    // @Bind(R.id.btn_connectRobot)
    // Button btnConnectRobot;

    @BindView(R.id.rb_wifimode)
    Button rbWifimode;
    @BindView(R.id.btn_connectRobot)
    Button btn_connectRobot;

    private int GPS_REQUEST_CODE = 10;
    private Intent intent = null;
    private WifiReceiver wifiReceiver;
    private static final String ACTION = "com.wifi";



    private Timer checkWIfiApStateTimer = null;
    private TimerTask checkWifiApStateTask = null;
    public static TextView textstatus;
    public static TextView textstatus2;

    //蓝牙
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    private boolean isFoundRobot = false;
    public static Handler viewUpdateHandler = new Handler();
    private EditText edit_pwd;
    private List<String> result;
    private List<String> result_old = null;
    private ProgressDialog progressdlg = null;
    private WifiUtils mUtils;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private CheckBox check_box;
    private TextView tv;
    private TextView wifiname;
    public static ProgressDialog dialog;
    private Unbinder unbind;

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(ConnectActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:


                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if(Build.VERSION.SDK_INT>=23){
            //开始请求权限
            PermissionUtils.requestPermission(this,PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE,mPermissionGrant);

        }*/

        setContentView(R.layout.activity_connect);


        mUtils = new WifiUtils(ConnectActivity.this);
        result = mUtils.getScanWifiResult();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        ConnectActivity.this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        ConnectActivity.this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        ConnectActivity.this.registerReceiver(mReceiver, filter);

        IntentFilter intentFilter = new IntentFilter(AppInfo.ACTION_CONNECTACTIVITY);
        registerReceiver(broadcastReceiver, intentFilter);

        wifiReceiver = new WifiReceiver();
        intentFilter = new IntentFilter(ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver, intentFilter);

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d("wxwx", "--------pairedDevices---------------" + device.getName() + "\n" + device.getAddress());
                if ("weiweiRobot".equals(device.getName())) {
                    Log.d("wxwx", "-----------删除weiweiRobot------------");
                    try {
                        ClsUtils.removeBond(device.getClass(), device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }



        Log.d("wxwx", "------ConnectActivity onCreate()----");

        unbind = ButterKnife.bind(this);
//        if(Build.VERSION.SDK_INT>=23){
//            //开始请求权限
//            getWifiPermisson();
//
//        }
         //wifi权限

    }

//    public void getWifiPermisson() {
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            Log.d("wxwx", "--------MyAsyncTask  申请wifi权限--------------------");
//            // 没有写的权限，去申请写的权限，会弹出对话框
//            ActivityCompat.requestPermissions(ConnectActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE}, 1);
//            // ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUEST_EXTERNAL_STORAGE);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        Log.d("wxwx", "--------onRequestPermissionsResult----requestCode----------------" + requestCode);
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        //result = mUtils.getScanWifiResult();
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 允许
//                    Log.d("wxwx", "--------onRequestPermissionsResult----Accepted----------------");
//                    result = mUtils.getScanWifiResult();
//                    Log.d("wxwx", "--------onRequestPermissionsResult----result----------------" + result);
//                } else {
//                    // 不允许
//                    Log.d("wxwx", "--------onRequestPermissionsResult----Rejected----------------");
//                }
//                break;
//        }
//    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getExtras().getString("result");
            Toast.makeText(ConnectActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // If it's already paired, skip it, because it's been listed already
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) { //没有配对
                        Log.d(TAG, "------------------found btdevice==============" + device.getName());
                        if ("weiweiRobot".equals(device.getName())) {
                            isFoundRobot = true;
                            mAdapter.cancelDiscovery(); //停止机器人扫描
                           /* Message msg = MyApplication.mh.obtainMessage(MyApplication.MESSAGE_TOAST);
                            Bundle bundle = new Bundle();
                            bundle.putString(MyApplication.TOAST, "正在连接到维维机器人");
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);*/
                            Log.d(TAG, "------------------正在连接到维维机器人 ==============");
                            MyApp.robotBTdevice = device;
                            connectDevice(device.getAddress(), true);
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (isFoundRobot == false) {
                        Log.d(TAG, "------------------not found robot==============");
                        //textstatus.setText("没有找到维维机器人");
                    } else {
                        Log.d(TAG, "------------------------- found robot------------------------");
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    //  textstatus.setText("开始寻找维维机器人");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            Log.d(TAG, "--------------------正在配对......");
                            //   textstatus.setText("正在配对");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d(TAG, "-------------------完成配对");
                            // textstatus.setText("维维机器人正在连接WIFI，请稍等");
                            break;
                        case BluetoothDevice.BOND_NONE:
                            Log.d(TAG, "----------------------取消配对");
                            //   textstatus.setText("取消配对");
                        default:
                            break;
                    }
            }
        }
    };


    /**
     * Start device discover with the BluetoothAdapter  开始发现设备
     */
    private void doDiscovery() {
        if (D)
            Log.d(TAG, "--------------doDiscovery()");
        viewUpdateHandler.post(new Runnable() {
            @Override
            public void run() {
                setProgressBarIndeterminateVisibility(true);
                //textstatus.setText("开始寻找维维机器人");
            }
        });

        // If we're already discovering, stop it
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mAdapter.startDiscovery(); //该方法会进行蓝牙设备的搜索，持续12秒。
    }

    private void connectDevice(String address, boolean secure) {
        // Get the device MAC address
        // Get the BluetoothDevice object
        BluetoothDevice device = mAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        MyApp.mChatService.connect(device, secure);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_connectRobot.setEnabled(false);

        Log.d("wxwx", "======Mainactivity onResume=============");

        ConnectToServer.isRurning = false;
        try {
            if (MyApp.client != null) {
                MyApp.client.close();
                MyApp.client = null;
                SocketClient.br.close();
                SocketClient.out.close();
            }
            SocketClient.isClient = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wxwx", "------Mainactivity onStop()----");
        GsonList.gsonList("querymode", 255, "default", "default", "default");  //查询机器人模式

        //textstatus.setText("蓝牙连接已断开，请重新连接");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wxwx", "======Mainactivity paused()=============");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wxwx", "======Mainactivity onDestroy=============");
        unbind.unbind();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(mReceiver);
        unregisterReceiver(wifiReceiver);
    }

    private WifiHotUtil wifiHotUtil = new WifiHotUtil();
    private void showToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("您确定退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private TimerTask creatNewWifiAPStateCheckTask() {
        TimerTask timerTask = new TimerTask() {
            private boolean isConnected = false;

            @Override
            public void run() {
                Log.d("wxwx", "-----------MainAcitivity WifiAPStateCheckTask run------------");
                switch (AppInfo.isAPPinAPmode) {
                    case "false":
                        switch (wifiHotUtil.getWifiState()) {
                            case CONNECTING:
                                Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask wifi CONNECTING------------");
                                break;
                            case CONNECTED:
                                Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask wifi CONNECTED------------");
                                isConnected = true;
                                break;
                            case SUSPENDED:
                                Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask wifi SUSPENDED------------");
                                break;
                            case DISCONNECTING:
                                Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask wifi DISCONNECTING------------");
                                break;
                            case DISCONNECTED:
                                Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask wifi DISCONNECTED------------");
                                break;
                            case UNKNOWN:
                                Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask wifi UNKNOWN------------");
                                break;
                        }
                        break;
                }
                if (isConnected) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("wxwx", "-----------MainAcitivity creatNewWifiAPStateCheckTask Wifi/AP 已连接，准备连接蓝牙------------");
                            // Get a set of currently paired devices
                            Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();

                            // If there are paired devices, add each one to the ArrayAdapter
                            if (pairedDevices.size() > 0) {
                                for (BluetoothDevice device : pairedDevices) {
                                    Log.d("wxwx", "--------pairedDevices---------------" + device.getName() + "\n" + device.getAddress());
                                    if ("weiweiRobot".equals(device.getName())) {
                                        Log.d("wxwx", "-----------删除weiweiRobot------------");
                                        try {
                                            ClsUtils.removeBond(device.getClass(), device);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            if (!mAdapter.isEnabled()) {//蓝牙没开启
                                viewUpdateHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        showToast("请在手机里打开蓝牙");
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        try {
                                            startActivity(intent);
                                        } catch (ActivityNotFoundException ex) {
                                            ex.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                            } else {

                                if (mAdapter.isDiscovering()) {
                                    Log.d(TAG, "-----------正在寻找weiweiRobot--------------------------");
                               /* Message msg = mHandler.obtainMessage(MyApplication.MESSAGE_TOAST);
/*                                Bundle bundle = new Bundle();
                                bundle.putString(MyApplication.TOAST, "不急不急，正在找你在维维呢^_^");
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);*/
                                } else {
                                /*Message msg = mHandler.obtainMessage(MyApplication.MESSAGE_TOAST);
/*                                Bundle bundle = new Bundle();
                                bundle.putString(MyApplication.TOAST, "正在努力寻找维维机器人");
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);*/
                                    if (D)
                                        Log.d(TAG, "-----------寻找weiweiRobot--------------------------");
                                    doDiscovery(); //进行蓝牙设备的搜索，持续12秒
                                }
                            }

                        }
                    }).start();

                    //停止wifi状态检测定时器
                    if (checkWifiApStateTask != null) {
                        checkWifiApStateTask.cancel();
                        checkWifiApStateTask = null;
                    }
                    if (checkWIfiApStateTimer != null) {
                        checkWIfiApStateTimer.purge();
                        checkWIfiApStateTimer.cancel();
                        checkWIfiApStateTimer = null;
                    }
                }
            }
        };
        return timerTask;
    }


    @OnClick({R.id.btn_connectRobot, R.id.rb_wifimode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
          /*  case R.id.rb_apmode:
                Log.d("wxwx", "------R.id.rb_apmode 是否打开热点 ---------------------" + wifiHotUtil.isWifiApEnabled());

                if (!wifiHotUtil.isWifiApEnabled()) {
                    showToast("请打开热点,再连接机器人");
                    btn_connectRobot.setFocusable(true);
                    btn_connectRobot.setEnabled(false);
                } else if (mAdapter.isEnabled()) {
                    btn_connectRobot.setEnabled(true);
                    btn_connectRobot.setBackgroundColor(Color.parseColor("#02CBFF"));
                    Log.d("wxwx", "-----------你选择了手机热点模式------------");
                    MyApplication.isAPPinAPmode = "true";
                    // showToast("请点击连接机器人");
                    Toast toast = Toast.makeText(this, "请点击连接机器人", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    showToast("请打开蓝牙,再连接机器人");
                    btn_connectRobot.setFocusable(true);
                    btn_connectRobot.setEnabled(false);
                }*/
               /* if(mAdapter.isEnabled()) {
                    btn_connectRobot.setEnabled(true);
                    btn_connectRobot.setBackgroundColor(Color.parseColor("#02CBFF"));
                    Log.d("wxwx", "-----------你选择了手机热点模式------------");
                    MyApplication.isAPPinAPmode = "true";
                   // showToast("请点击连接机器人");
                    Toast toast = Toast.makeText(this,"请点击连接机器人",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    showToast("请打开蓝牙");
                }*/
//                break;
            case R.id.rb_wifimode:
                Log.d("wxwx", "-----------你选择了路由器模式------------");
                if (!mUtils.isConnectWifi()) {
                    showToast("手机请打开WIFI,并连接WIFI");
                    btn_connectRobot.setEnabled(false);
                    /*Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                    startActivity(wifiSettingsIntent);*/
                } else if (!mAdapter.isEnabled()) {
                    showToast("请打开蓝牙,再连接机器人");
                } else {
                    btn_connectRobot.setEnabled(true);
                    btn_connectRobot.setBackgroundColor(Color.parseColor("#02CBFF"));

                    AppInfo.isAPPinAPmode = "false";

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    View v = LayoutInflater.from(this).inflate(R.layout.dialogwifi, null);
                    wifiname = (TextView) v.findViewById(R.id.wifiname);
                    edit_pwd = (EditText) v.findViewById(R.id.editpwd);
                    check_box = (CheckBox) v.findViewById(R.id.checkBox);
                    check_box.setOnCheckedChangeListener(this);

                    String connnectWifiSsid = mUtils.getConnnectWifiSsid().replace("\"", "").replace("\"", "");
                    wifiname.setText(connnectWifiSsid);
                    AppInfo.ssid_wifi = connnectWifiSsid.toString();
//                    Log.d("wxwx", "-----MyApplication.ssid_wifi---------------" + MyApplication.ssid_wifi.toString());

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppInfo.pwd_wifi = edit_pwd.getText().toString();

                            if (AppInfo.pwd_wifi.isEmpty()) {
                                Log.d("wxwx", "----------MyApplication.pwd_wifi.isEmpty()---------");
                                showToast("请输入WIFI密码");
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btn_connectRobot.setFocusable(true);
                            btn_connectRobot.setEnabled(false);

                        }
                    });
                    builder.setView(v);
                    builder.setCancelable(false);
                    builder.create().show();
                }
                break;
            case R.id.btn_connectRobot:

                /*if(edit_pwd.getText().toString().isEmpty()){
                    showToast("请输入WIFI密码");
                }else {*/
                switch (AppInfo.isAPPinAPmode) {
                    case "false":
                        progress();

                        //  MyApplication.pwd_wifi = editpwd.getText().toString();
                      /*  if ("请点击".equals(textviewSsid.getText().toString())) {
                            showToast("请点击选择WIFI");
                        } else if (MyApplication.pwd_wifi.isEmpty()) {
                            showToast("请输入WIFI密码");
                        } else*/
                    /*{
                        if (wifiHotUtil.isWifiConnected()) {
                            if (wifiHotUtil.getCurrentWifiInfo().getSSID().equals(MyApplication.ssid_wifi)) {
                                if (D)
                                    Log.d(TAG, "-----------btn_connectRobot  已连接到SSID:---" + wifiHotUtil.getCurrentWifiInfo().getSSID());
                            } else {
                              *//*  new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                      //  wifiHotUtil.connectWifiTest(MyApplication.ssid_wifi, MyApplication.pwd_wifi);
                                    }
                                }).start();*//*
                            }
                        } else {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    wifiHotUtil.connectWifiTest(MyApplication.ssid_wifi, MyApplication.pwd_wifi);
                                }
                            }).start();
                        }
                    }*/
                }
                //开始定时检测wifi链接检测状态
                if (checkWifiApStateTask != null) {
                    checkWifiApStateTask.cancel();
                    checkWifiApStateTask = null;
                }
                if (checkWIfiApStateTimer != null) {
                    checkWIfiApStateTimer.purge();
                    checkWIfiApStateTimer.cancel();
                    checkWIfiApStateTimer = null;
                }
                checkWIfiApStateTimer = new Timer();
                checkWifiApStateTask = creatNewWifiAPStateCheckTask();
                checkWIfiApStateTimer.schedule(checkWifiApStateTask, 500, 2000);  //启动wifiAP状态定时器，如果wifi或者AP已经链接成功，则启动蓝牙链接过程
                // }

        }
        //  }
    }

    public void progress() {
        //final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        // dismiss监听
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }
        });
        // 监听Key事件被传递给dialog
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        // 监听cancel事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }
        });

        dialog.setMessage("正在连接机器人，请稍等");
        dialog.show();

    }




    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("wxwx", "--------MyAsyncTask  已具有wifi权限--------------------");
            result = mUtils.getScanWifiResult();
            Log.d("wxwx", "--------MyAsyncTask  android 6.0 以上result--------------------" + result.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDismiss();
            if (result_old == null) {
                result_old = result;
                Log.d("wxwx", "--------MyAsyncTask  onPostExecute SSID 初始化：列表有更新--------------------" + result_old.toString());
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, result_old);
                spinner.setAdapter(arrayAdapter);
                // arrayAdapter.notifyDataSetChanged();
            } else {
                if (result_old.equals(result)) {
                    Log.d("wxwx", "--------MyAsyncTask  onPostExecute SSID 列表未刷新--------------------");
                } else {
                    result_old = result;
                    Log.d("wxwx", "--------MyAsyncTask  onPostExecute SSID 非初始化：列表有更新--------------------" + result_old.toString());
                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_textview, result_old);
                    spinner.setAdapter(arrayAdapter);
                    //   arrayAdapter.notifyDataSetChanged();
                }
            }
        }

    }


    /**
     * dismiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }


    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 监听wifi的连接状态即是否连上了一个有效无线路由
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    // 获取联网状态的NetWorkInfo对象
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    //获取的State对象则代表着连接成功与否等状态
                    NetworkInfo.State state = networkInfo.getState();
                    //判断网络是否已经连接
                    boolean isConnected = state == NetworkInfo.State.CONNECTED;
                    if (D)
                        Log.d("wxwx", "============WifiReceiver isConnected========" + isConnected);
                    if (isConnected) {
                        if (D)
                            Log.d("wxwx", "============WifiReceiver 连接上WIFI========");
                        // dialog.dismiss();
                    } else {
                        if (D)
                            Log.d("wxwx", "============WifiReceiver 断开WIFI iswifiStateTimerStarted========");
                        //dialog.dismiss();
                    }
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            //设置明文显示
            edit_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //设置密文显示
            edit_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        //保证每次切换明文密文后光标都在最后面，默认是会切换到最前端
        edit_pwd.setSelection(edit_pwd.getText().length());
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen(Context context) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前应用需要打开定位功能。\\n\\n请点击\\\"设置\\\"-\\\"定位服务\\\"-打开定位功能")
                    // 拒绝, 退出应用
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish();
                                }
                            })

                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();
        }
    }
}

