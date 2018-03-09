package com.biyesheji.android.robot.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.socket.GsonList;
import com.biyesheji.android.robot.ui.ActionActivity;


/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class BiaozhunFragment extends Fragment implements View.OnClickListener {


    private Button btn_zuozhuan,btn_qianjing,btn_youzhuan,btn_zoupingyi,btn_fuwocheng,btn_youpingyi,btn_zuotitui,btn_yangwoqizuo,btn_youtitui,
            btn_zuojingli,btn_houtui,btn_qiangunfan,btn_qianpaxia,btn_daoli,btn_houpaxia,btn_hougunfan,btn_youjingli,btn_mode_biaozhun,btn_qiaoxiyang,btn_jiewu,btn_ticao;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.biaozhun_fragment, null);

        initeView();
        setListener();
        IntentFilter intentFiltergexin = new IntentFilter(AppInfo.ACTION_ROBOT_OPERATEMODE_CHANGE);
        getActivity().registerReceiver(broadcastReceiverbiaozhun,intentFiltergexin);
        if(ActionActivity.robotCurrentOprtionMode == ActionActivity.constRobotOptStandardMode){
            //btn_mode_biaozhun.setBackgroundResource(R.drawable.modepressreverse);
//            btn_mode_biaozhun.setBackgroundResource(R.drawable.wifiaploginpress);
        }else {
           // btn_mode_biaozhun.setBackgroundResource(R.drawable.modepress);
//            btn_mode_biaozhun.setBackgroundResource(R.drawable.wifiaploginpressfalse);
        }
        return view;
    }

    BroadcastReceiver broadcastReceiverbiaozhun = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int robotoptmode = intent.getIntExtra("robotoptmode", 0);
            Log.d("wxwx","-----------SettingFragment  robotoptmode-------------"+robotoptmode);
            if(robotoptmode == ActionActivity.constRobotOptStandardMode){
//                btn_mode_biaozhun.setBackgroundResource(R.drawable.wifiaploginpress);
            }else {
//                btn_mode_biaozhun.setBackgroundResource(R.drawable.wifiaploginpressfalse);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_zuozhuan:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zuozhuan");
                break;
            case R.id.btn_qianjing:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qianjing");
                break;
            case R.id.btn_youzhuan:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youzhuan");
                break;
            case R.id.btn_zoupingyi:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zoupingyi");
                break;
            case R.id.btn_fuwocheng:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","fuwocheng");
                break;
            case R.id.btn_youpingyi:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youpingyi");
                break;
            case R.id.btn_zuotitui:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zuotitui");
                break;
            case R.id.btn_yangwoqizuo:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","yangwoqizuo");
                break;
            case R.id.btn_youtitui:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youtitui");
                break;
            case R.id.btn_zuojingli:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","zuojingli");
                break;
            case R.id.btn_houtui:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","houtui");
                break;

            case R.id.btn_qiangunfan:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qiangunfan");
                break;
            case R.id.btn_daoli:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","daoli");
                break;
            case R.id.btn_houpaxia:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","houpaxia");
                break;
            case R.id.btn_hougunfan:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","hougunfan");
                break;
            case R.id.btn_youjingli:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","youjingli");
                break;
            case R.id.btn_mode_biaozhun:  //标准模式
                GsonList.gsonList("actioncontrol",255,"default","setting","mode_biaozhun");
               // ActionActivity.robotCurrentOprtionMode = ActionActivity.constRobotOptStandardMode;
                if(ActionActivity.robotCurrentOprtionMode == 0){

                }else {
//                    btn_mode_biaozhun.setBackgroundResource(R.drawable.wifiaploginpressfalse);
                }
                break;
            case R.id.btn_qianpaxia: //前趴下
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qianpaxia");
                break;
            case R.id.btn_qiaoxiyang:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","qiaoxiyang");
                break;
            case R.id.btn_ticao:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","ticao");
                break;
            case R.id.btn_jiewu:
                GsonList.gsonList("actioncontrol",255,"default","biaozhun","jiewu");
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiverbiaozhun);
    }

    private void setListener() {

        btn_zuozhuan.setOnClickListener(this);
        btn_qianjing.setOnClickListener(this);
        btn_youzhuan.setOnClickListener(this);
        btn_zoupingyi.setOnClickListener(this);
        btn_fuwocheng.setOnClickListener(this);
        btn_youpingyi.setOnClickListener(this);
        btn_zuotitui.setOnClickListener(this);
        btn_yangwoqizuo.setOnClickListener(this);
        btn_youtitui.setOnClickListener(this);
        btn_zuojingli.setOnClickListener(this);
        btn_houtui.setOnClickListener(this);
        btn_qiangunfan.setOnClickListener(this);
        btn_qianpaxia.setOnClickListener(this);
        btn_daoli.setOnClickListener(this);
        btn_houpaxia.setOnClickListener(this);
        btn_hougunfan.setOnClickListener(this);
        btn_youjingli.setOnClickListener(this);
        btn_mode_biaozhun.setOnClickListener(this);
        btn_qiaoxiyang.setOnClickListener(this);
        btn_jiewu.setOnClickListener(this);
        btn_ticao.setOnClickListener(this);
    }

    private void initeView() {

        btn_zuozhuan = (Button) view.findViewById(R.id.btn_zuozhuan);
        btn_qianjing = (Button) view.findViewById(R.id.btn_qianjing);
        btn_youzhuan = (Button) view.findViewById(R.id.btn_youzhuan);
        btn_zoupingyi = (Button) view.findViewById(R.id.btn_zoupingyi);
        btn_fuwocheng = (Button) view.findViewById(R.id.btn_fuwocheng);
        btn_youpingyi = (Button) view.findViewById(R.id.btn_youpingyi);
        btn_zuotitui = (Button) view.findViewById(R.id.btn_zuotitui);
        btn_yangwoqizuo = (Button) view.findViewById(R.id.btn_yangwoqizuo);
        btn_youtitui = (Button) view.findViewById(R.id.btn_youtitui);
        btn_zuojingli = (Button) view.findViewById(R.id.btn_zuojingli);
        btn_houtui = (Button) view.findViewById(R.id.btn_houtui);
        btn_qiangunfan = (Button) view.findViewById(R.id.btn_qiangunfan);
        btn_qianpaxia = (Button) view.findViewById(R.id.btn_qianpaxia);
        btn_daoli = (Button) view.findViewById(R.id.btn_daoli);
        btn_houpaxia = (Button) view.findViewById(R.id.btn_houpaxia);
        btn_hougunfan = (Button) view.findViewById(R.id.btn_hougunfan);
        btn_youjingli = (Button) view.findViewById(R.id.btn_youjingli);
        btn_mode_biaozhun = (Button) view.findViewById(R.id.btn_mode_biaozhun);
        btn_qiaoxiyang = (Button) view.findViewById(R.id.btn_qiaoxiyang);
        btn_jiewu = (Button) view.findViewById(R.id.btn_jiewu);
        btn_ticao = (Button) view.findViewById(R.id.btn_ticao);
    }
}
