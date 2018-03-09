package com.biyesheji.android.robot.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.socket.GsonList;
import com.biyesheji.android.robot.ui.ActionActivity;
import com.biyesheji.android.robot.ui.SqliteActivity;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    // private Button btn_login = null;
    private static final int PORT = 8090;
    //wifi之ssid
    private String ssid1;
    private String pwd;
    private ProgressDialog progressdlg = null;

    private String[] cloudVoicersEntries;
    private String[] cloudVoicersValue ;
    private int selectedNum=0;
    private Intent intent;
    private List<Map<String,Object>> listap;
    private Gson gson = new Gson();
    // 默认发音人
    private String voicer="xiaoyan";
    private Button btnSpeaker,btnAskAnswer,btnStopAskAnswer,btnDatabase,btn_jiadayingliang,btn_jianxiaoyingliang,btn_mode_programe,btn_wifiupdate,btn_tuoluoyi,btn_voicecmdkai,btn_voicecmdguan = null;
    private View view;
    private TextView textviewSsid;
    private EditText editpwd;
    public static final int REQUSET = 1;
    private CheckBox checkBox;
    private IntentFilter intentFilter;
    public static ToggleButton askanswerKG,yuyinkongzhiKG;
    private int GPS_REQUEST_CODE = 10;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ////从这里开始改

        view = inflater.inflate(R.layout.yuyin_fragment, null);

        initView();
        setLister(); //设置监听

        //查询语音状态
        GsonList.gsonList("yuyinzhuantaichaxun",255,"default","default","default");


        intentFilter = new IntentFilter(AppInfo.ACTION_ROBOT_OPERATEMODE_CHANGE);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

            if(ActionActivity.robotCurrentOprtionMode == ActionActivity.constRobotOptOnlineProgramMode){
//                btn_mode_programe.setBackgroundResource(R.drawable.wifiaploginpress);
            }else {
//                btn_mode_programe.setBackgroundResource(R.drawable.wifiaploginpressfalse);
            }
        intentFilter = new IntentFilter(AppInfo.ACTION_SETTING_BUTTON);
        getActivity().registerReceiver(broadcastReceiverYuyin, intentFilter);

        //智能问答开关
        askanswerKG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){  //选中
                    GsonList.gsonList("askanswer",1,"0x00","AAAA","AAAA");
                    if(yuyinkongzhiKG.isChecked())
                    {
                        yuyinkongzhiKG.performClick();
                        yuyinkongzhiKG.setChecked(false);
                    }


                }else {  //未选中
                    GsonList.gsonList("stopanswer",1,"0x11","AAAA","AAAA");
                }

            }
        });

        //语音控制 开关
        yuyinkongzhiKG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){  //选中
                    GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdkai");
                    if(askanswerKG.isChecked())
                    {
                        askanswerKG.performClick();
                        askanswerKG.setChecked(false);
                    }
                }else {  //未选中
                    GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdguan");
                }
            }
        });

        return view;
    }

    BroadcastReceiver broadcastReceiverYuyin = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strYuyin = intent.getStringExtra("yuyin");
            switch (strYuyin){
                case "dakaiyuyinwenda": //智能问答
                    Log.d("wxwx","------------打开智能问答响应-------------");
                    askanswerKG.setChecked(true);
                    break;
                case "guanbiyuyinwenda": //关闭智能问答
                    Log.d("wxwx","------------关闭智能问答响应-------------");
                    askanswerKG.setChecked(false);
                    break;
                case "kaiqiyuyinkongzhi": //开启语音控制
                    Log.d("wxwx","------------开启语音控制响应-------------");
                    yuyinkongzhiKG.setChecked(true);
                    break;
                case "guanbiyuyinkongzhi": //关闭语音控制
                    Log.d("wxwx","------------关闭语音控制响应-------------");
                    yuyinkongzhiKG.setChecked(false);
                    break;
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int robotoptmode = intent.getIntExtra("robotoptmode", 0);
            Log.d("wxwx","-----------SettingFragment  robotoptmode-------------"+robotoptmode);
            if(robotoptmode == ActionActivity.constRobotOptOnlineProgramMode){
//                btn_mode_programe.setBackgroundResource(R.drawable.wifiaploginpress);
            }else {
//                btn_mode_programe.setBackgroundResource(R.drawable.wifiaploginpressfalse);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.d("wxwx","-------id-----------"+id);
        switch (id){
            case R.id.btnSpeaker://选择发音人
                showPresonSelectDialog();
                break;
           /* case R.id.btnAskAnswer: //智能问答
                GsonList.gsonList("askanswer",1,"0x00","AAAA","AAAA");
                btn_voicecmdguan.performClick();
                break;
            case R.id.btnStopAskAnswer: //停止智能问答
                GsonList.gsonList("stopanswer",1,"0x11","AAAA","AAAA");
                break;*/
            case R.id.btnDatabase: //数据库
                intent = new Intent(MyApp.getApp().getApplicationContext(),SqliteActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_jiadayingliang:
                GsonList.gsonList("yingliangtiaozheng",1,"ADJUST_RAISE","0xff","0xff");
                break;
            case R.id.btn_jianxiaoyingliang:
                GsonList.gsonList("yingliangtiaozheng",-1,"ADJUST_LOWER","0xff","0xff");
                break;
           /* case R.id.btn_wifiupdate:  //修改wifi
                pwd = editpwd.getText().toString();
                //判断密码输入情况
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(getContext(), "请输入WIFI密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                GsonList.gsonList("apstation",1,ssid1,pwd,"AAAA");
                break;*/
            /*case R.id.textviewSsid:
                Log.d("wxwx","----------textviewSsid----------------");
                if(checkGPSIsOpen(getContext())){
                    intent = new Intent(MyApplication.getContextObject(),WifiActivity.class);
                    startActivityForResult(intent,1);
            }else {
                    openGPSSettings();
                }

                break;*/
            case R.id.btn_mode_programe: //图形化动作编程模式
                GsonList.gsonList("actioncontrol",255,"default","setting","mode_programe");
             //   ActionActivity.robotCurrentOprtionMode = ActionActivity.constRobotOptOnlineProgramMode;
                if(ActionActivity.robotCurrentOprtionMode == 2){

                }else {
//                    btn_mode_programe.setBackgroundResource(R.drawable.wifiaploginpressfalse);
                }
                break;
           case R.id.btn_tuoluoyi: //陀螺仪
                GsonList.gsonList("actioncontrol",255,"default","setting","tuoluoyi");
                break;
           /* case R.id.btn_voicecmdkai: //语音动作开
                GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdkai");
                btnStopAskAnswer.performClick();
                break;
            case R.id.btn_voicecmdguan://语音动作关
                GsonList.gsonList("actioncontrol",255,"default","setting","voicecmdguan");
                break;*/
        }
    }

    /**
     * 发音人选择。
     */
    private void showPresonSelectDialog() {
        // 云端发音人名称列表
        cloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        cloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        new AlertDialog.Builder(getContext()).setTitle("在线合成发音人选项")
                .setSingleChoiceItems(cloudVoicersEntries, // 单选框有几项,各是什么名字
                        selectedNum, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog,
                                                int which) { // 点击了哪一项
                                Log.d("wxwx","------------------"+which);
                                GsonList.gsonList("speaker",52+which,"default","1","default");
                                voicer = cloudVoicersValue[which];
                                selectedNum = which;
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void setLister() {
        btnSpeaker.setOnClickListener(this);
        btnDatabase.setOnClickListener(this);
        btn_jiadayingliang.setOnClickListener(this);
        btn_jianxiaoyingliang.setOnClickListener(this);
        btn_mode_programe.setOnClickListener(this);
       // btn_wifiupdate.setOnClickListener(this);
       // textviewSsid.setOnClickListener(this);
       // checkBox.setOnCheckedChangeListener(this);
        btn_tuoluoyi.setOnClickListener(this);

    }

    private void initView() {
        btnSpeaker = (Button) view.findViewById(R.id.btnSpeaker);
        btnDatabase = (Button) view.findViewById(R.id.btnDatabase);
        btn_jiadayingliang = (Button) view.findViewById(R.id.btn_jiadayingliang);
        btn_jianxiaoyingliang = (Button) view.findViewById(R.id.btn_jianxiaoyingliang);
        btn_mode_programe = (Button) view.findViewById(R.id.btn_mode_programe);
       // textviewSsid = (TextView) view.findViewById(R.id.textviewSsid);
       // editpwd = (EditText) view.findViewById(R.id.editpwd);
       // btn_wifiupdate = (Button) view.findViewById(R.id.btn_wifiupdate);
       // checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        btn_tuoluoyi = (Button) view.findViewById(R.id.btn_tuoluoyi);
        askanswerKG = (ToggleButton) view.findViewById(R.id.askanswerKG);
        yuyinkongzhiKG = (ToggleButton) view.findViewById(R.id.yuyinkongzhiKG);
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SettingFragment.REQUSET && resultCode == getActivity().RESULT_OK ){
            ssid1 = data.getStringExtra("ssid");
            textviewSsid.setText("       "+ssid1);
        }

        if (requestCode == GPS_REQUEST_CODE) {
            //做需要做的事情，比如再次检测是否打开GPS了 或者定位
            intent = new Intent(MyApplication.getContextObject(),WifiActivity.class);
            startActivityForResult(intent,1);
        }
    }
*/

 /*   @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            //设置明文显示
            editpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            //设置密文显示
            editpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        //保证每次切换明文密文后光标都在最后面，默认是会切换到最前端
        editpwd.setSelection(editpwd.getText().length());
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(broadcastReceiverYuyin);
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
  /*  private boolean checkGPSIsOpen(Context context) {
        boolean isOpen;
        LocationManager locationManager =(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }*/

   /* *//**
     * 跳转GPS设置
     *//*
    private void openGPSSettings() {
        {
            //没有打开则弹出对话框
            new AlertDialog.Builder(getContext())
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
    }*/
}
