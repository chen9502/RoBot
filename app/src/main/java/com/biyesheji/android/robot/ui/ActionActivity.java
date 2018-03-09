package com.biyesheji.android.robot.ui;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.fragment.BiaozhunFragment;
import com.biyesheji.android.robot.fragment.GexinFragment;
import com.biyesheji.android.robot.fragment.SettingFragment;
import com.biyesheji.android.robot.socket.ConnectToServer;
import com.biyesheji.android.robot.socket.GsonList;
import com.biyesheji.android.robot.socket.SocketClient;

import java.io.IOException;

public class ActionActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static int robotCurrentOprtionMode = 0;
    public static  final int  constRobotOptStandardMode= 0;  //0:默认为标准动作模式
    public static  final int  constRobotOptCustomizedMode= 1;  //1：个性动作模式
    public static final int  constRobotOptOnlineProgramMode= 2;  //2：图形化编程模式
    private Screen screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        initView();
        GsonList.gsonList("gexindongzuoanniupeizhi",255,"default","default","queryall"); //返回个性动作配置记录
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout,new BiaozhunFragment()).commit();
        rg.setOnCheckedChangeListener(this);

        MyApp.gxdzList.clear();
        GexinFragment.listGridview.clear();


        screen = new Screen();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        this.registerReceiver(screen,filter);
        MyApp.isNormalBackToMainActivity = false;

        IntentFilter intentFilter = new IntentFilter(SocketClient.ACTION_SOCKETDISCONNETED);
        registerReceiver(broadcastReceiver,intentFilter);

    }

    private void initView() {
        rg = (RadioGroup) findViewById(R.id.rg);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean disconnected = intent.getExtras().getBoolean("disconnected");
            Log.d("wxwx","=======接收到socket连接断开 广播====================="+disconnected);
            if(disconnected){
                /*intent = new Intent(ActionActivity.this,MainActivity.class);
                startActivity(intent);
                MainActivity.textstatus.setText("");
                MainActivity.dialog.dismiss();
                MainActivity.dialog.cancel();*/

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.gxdzList.clear();
        GexinFragment.listGridview.clear();
        GsonList.gsonList("gexindongzuoanniupeizhi",255,"default","default","queryall"); //返回个性动作配置记录
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("wxwx", "-------ActionActivity onDestroy()-------------");
        unregisterReceiver(screen);
        unregisterReceiver(broadcastReceiver);
        //MyApplication.mChatService.setState(MyApplication.MESSAGE_CONNECTION_LOST);
        MyApp.mChatService.stopService();  //关闭蓝牙SOCKET连接
        MyApp.isNormalBackToMainActivity = true;
//        Log.d("wxwx", "--------onDestroy isNormalBackToMainActivity ------------- "+MyApplication.isNormalBackToMainActivity);
        MyApp.isFirstToMainactivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("wxwx", "-------ActionActivity onPause()-------------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("wxwx", "-------ActionActivity  onStop()-------------");
    }

    //radiobutton的点击事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_gexin:
                manager.beginTransaction().replace(R.id.frameLayout,new GexinFragment()).commit();
                break;
            case R.id.rb_biaozhun:
                manager.beginTransaction().replace(R.id.frameLayout,new BiaozhunFragment()).commit();
                break;
            case R.id.rb_yuyin:
                manager.beginTransaction().replace(R.id.frameLayout,new SettingFragment()).commit();
                break;
        }
    }
    public class Screen extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("android.intent.action.SCREEN_OFF")){
                Log.d("wxwx","------屏幕锁屏了-------");
                MyApp.mChatService.stopService();  //关闭蓝牙SOCKET连接
            }else {
                Log.d("wxwx","------屏幕解锁了-------");
            }
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("维维机器人将会断开连接");
        builder.setMessage("确定返回吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    MyApp.client.close();
                    MyApp.client = null;
                    ConnectToServer.isRurning = false;
                    SocketClient.isClient = false;
                    SocketClient.br.close();
                    SocketClient.out.close();
                   // dialog.dismiss();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                ConnectActivity.dialog.dismiss();
                ConnectActivity.dialog.cancel();
            }
        });
        builder.create().show();
    }


}
