package com.biyesheji.android.robot.config;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AppInfo {
    public static String ACTION_CONNECTACTIVITY = "json.askanswer.action";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_CONNECT_FAILED = 7;
    public static final int MESSAGE_SWITCHTO_MAINACTIVITY = 8;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final int PORT = 8090;
    public static boolean isCompleteRecvMsg = false;
    public static String ACTION_DATABASE = "json.database.action";
    public static String ACTION_MAINACTIVITY = "json.askanswer.action";
    public static String ACTION_GRIDVIEW = "com.gridview";
    public static String ACTION_ROBOT_OPERATEMODE_CHANGE = "com.robotoperatemode";
    public static String ACTION_SETTING_BUTTON = "com.settingbutton";
    public static String ssid_wifi = "default";
    public static String pwd_wifi= "default";
    public static String isAPPinAPmode = "false";
    public static final String AP_SSID = "weiwei";
    public static final String AP_PWD = "1234567890";
    public static int robotCurrentOprtionMode = 0;
    public static  final int  constRobotOptStandardMode= 0;  //0:默认为标准动作模式
    public static  final int  constRobotOptCustomizedMode= 1;  //1：个性动作模式
    public static final int  constRobotOptOnlineProgramMode= 2;  //2：图形化编程模式
}
