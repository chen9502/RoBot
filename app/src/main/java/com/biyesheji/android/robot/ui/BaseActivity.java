package com.biyesheji.android.robot.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.socket.ConnectToServer;
import com.biyesheji.android.robot.socket.SocketClient;

import java.io.IOException;


/**
 * Created by Administrator on 2018/2/27.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
                    if(MyApp.client!=null){
                        MyApp.client.close();
                        MyApp.client = null;
                    }

                    ConnectToServer.isRurning = false;
                    SocketClient.isClient = false;
                    if(SocketClient.br!=null){
                        SocketClient.br.close();
                    }
                    if(SocketClient.out!=null){
                        SocketClient.out.close();
                    }
                    // dialog.dismiss();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        builder.create().show();
    }



}
