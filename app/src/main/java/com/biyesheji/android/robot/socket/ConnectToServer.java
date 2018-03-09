package com.biyesheji.android.robot.socket;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.biyesheji.android.robot.MyApp;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class ConnectToServer {

    public  static SocketClient socketClient = new SocketClient();
    private static String getwayIpS;
    private static String netmaskIpS;
    public static boolean isRurning = true;
    private  static String ssid1;


    public static void connectServer(String ip, int port){
        /*getWifiInfo(); //获取wifi信息
        Log.d("wxwx","------connectServer  ssid-----------------"+ssid1);
        String ww = "\"weiwei\"";

        Log.d("wxwx","-----connectServer  ssid.length----------"+ssid1.length());
        Log.d("wxwx","-----connectServer ww.length----------"+ww.length());
        if((ssid1.equals(ww))){
            socketClient.clintValue(MyApplication.getContextObject(),getwayIpS,port);//服务端的IP地址和端口号
            Log.d("wxwx","-----getwayIpS-------"+getwayIpS);
            socketClient.openClientThread();//开启客户端接收消息线程
            Log.d("wxwx","-----ap---------");
        }else */{
            socketClient.clintValue(MyApp.getApp().getApplicationContext(),ip,port);
            Log.d("wxwx","----ip=--------"+ip+"-----port-----"+port);
            socketClient.openClientThread();
            Log.d("wxwx","-----station---------");
        }
    }

    public static String long2ip(long ip){
        StringBuffer sb=new StringBuffer();
        sb.append(String.valueOf((int)(ip&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>8)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>16)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>24)&0xff)));
        return sb.toString();
    }

    public static void getWifiInfo(){
        WifiManager wm = (WifiManager) MyApp.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wm.getConnectionInfo();
        ssid1 = connectionInfo.getSSID().trim();
        Log.d("wxwx","------getWifiInfo  ssid-----------------"+ssid1);
        DhcpInfo di = wm.getDhcpInfo();
        long getewayIpL=di.gateway;
        getwayIpS = long2ip(getewayIpL);  //网关地址
//        Log.d("wxwx","----网关地址-------"+ getwayIpS);

        long netmaskIpL= di.netmask;
        //子网掩码地址
        netmaskIpS = long2ip(netmaskIpL);
//        Log.d("wxwx","----子网掩码地址-------"+ netmaskIpS);
    }

}
