package com.biyesheji.android.robot.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.biyesheji.android.robot.MyApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class WifiHotUtil {
    public static final String TAG = "wxwx";
private  boolean D = true;
    public static WifiManager mWifiManager = null;
    private List<WifiConfiguration> wifiConfigurationList;
    private List<ScanResult> scanResultList;
    public static int netWorkId;


    public WifiHotUtil() {
        mWifiManager = (WifiManager) MyApp.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }



    public void startWifiAp(String ssid, String passwd) {
        //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }

        if (!isWifiApEnabled()) {
            stratWifiAp(ssid, passwd);
        }

    }

    /**
     * 设置热点名称及密码，并创建热点
     * @param mSSID
     * @param mPasswd
     */
    private void stratWifiAp(String mSSID, String mPasswd) {
        Method method1 = null;
        try {
            //通过反射机制打开热点
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;

            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            method1.invoke(mWifiManager, netConfig, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public enum WifiSecurityType {
        WIFICIPHER_NOPASS, WIFICIPHER_WPA, WIFICIPHER_WEP, WIFICIPHER_INVALID, WIFICIPHER_WPA2
    }
    public static int WIFI_AP_STATE_DISABLING = 10;
    public static int WIFI_AP_STATE_DISABLED = 11;
    public static int WIFI_AP_STATE_ENABLING = 12;
    public static int WIFI_AP_STATE_ENABLED = 13;
    public static int WIFI_AP_STATE_FAILED = 14;

    public boolean turnOnWifiAp(String str, String password, WifiSecurityType Type) {
        String ssid = str;
        //配置热点信息。
        WifiConfiguration wcfg = new WifiConfiguration();
        //wcg.SSID = """ + mSSID + """;
        wcfg.SSID = new String(ssid);
       // wcfg.SSID = "\"" + ssid + "\"";
        wcfg.networkId = 1;
        wcfg.allowedAuthAlgorithms.clear();
        wcfg.allowedGroupCiphers.clear();
        wcfg.allowedKeyManagement.clear();
        wcfg.allowedPairwiseCiphers.clear();
        wcfg.allowedProtocols.clear();

        if(Type == WifiSecurityType.WIFICIPHER_NOPASS) {
            if(D)
                Log.d(TAG, "wifi ap----no password");
            wcfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN, true);
            wcfg.wepKeys[0] = "";
            wcfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wcfg.wepTxKeyIndex = 0;
        } else if(Type == WifiSecurityType.WIFICIPHER_WPA) {
            if(D)
                Log.d(TAG, "wifi ap----wpa");
            //密码至少8位，否则使用默认密码
            if(null != password && password.length() >= 8){
                wcfg.preSharedKey = password;
               // wcfg.preSharedKey = "\""+ password +"\"";
                ;
            } else {
                wcfg.preSharedKey = "1234567890";
            }
            wcfg.hiddenSSID = false;
            wcfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wcfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            //wcfg.allowedKeyManagement.set(4);
            wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wcfg.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if(Type == WifiSecurityType.WIFICIPHER_WPA2) {
            if(D)
                Log.d(TAG, "wifi ap---- wpa2,passwd is :"+password);
            //密码至少8位，否则使用默认密码
            if(null != password && password.length() >= 8){
                wcfg.preSharedKey = password;
               // wcfg.preSharedKey = "\""+ password +"\"";
            } else {
                wcfg.preSharedKey = "1234567890";
            }
/*           // wcfg.hiddenSSID = true;
            wcfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            wcfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

            wcfg.allowedKeyManagement.set(4);
            //wcfg.allowedKeyManagement.set(4);
            wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wcfg.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            wcfg.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

            wcfg.status = WifiConfiguration.Status.ENABLED;*/


            wcfg.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wcfg.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wcfg.allowedKeyManagement.set(4);
           // wcfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            //wcfg.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wcfg.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wcfg.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wcfg.status = WifiConfiguration.Status.ENABLED;



        }
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApConfiguration",
                    wcfg.getClass());
            Boolean rt = (Boolean)method.invoke(mWifiManager, wcfg);
            if(D) Log.d(TAG, " rt = " + rt);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        }
        return setWifiApEnabled();
    }
    //获取热点状态
    public int getWifiAPState() {
        int state = -1;
        try {
            Method method2 = mWifiManager.getClass().getMethod("getWifiApState");
            state = (Integer) method2.invoke(mWifiManager);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if(D)
            Log.i("WifiAP", "getWifiAPState.state " + state);
        return state;
    }

    private boolean setWifiApEnabled() {
        //开启wifi热点需要关闭wifi
        while(mWifiManager.getWifiState() != WifiManager.WIFI_STATE_DISABLED){
            mWifiManager.setWifiEnabled(false);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        // 确保wifi 热点关闭。
        while(getWifiAPState() != WIFI_AP_STATE_DISABLED){
            try {
                Method method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                method1.invoke(mWifiManager, null, false);

                Thread.sleep(200);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }

        //开启wifi热点
        try {
            Method method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method1.invoke(mWifiManager, null, true);
            Thread.sleep(200);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 热点开关是否打开
     * @return
     */
    public boolean isWifiApEnabled() {
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭WiFi热点
     */
    public void closeWifiAp() {
        mWifiManager = (WifiManager) MyApp.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled()) {
            try {
                Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(mWifiManager);
                Method method2 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(mWifiManager, config, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /*
  * Wifi加密类型的描述类
  */
    public enum WifiCipherType {
        NONE, IEEE8021XEAP, WEP, WPA, WPA2, WPAWPA2;
    }
    /**
     * 连接wifi 参数：wifi的ssid及wifi的密码
     */
    public boolean connectWifiTest(final String ssid, final String pwd) {
        boolean isSuccess = false;
        boolean flag = false;
        int tryCounter = 0;
        mWifiManager.disconnect();
        //boolean addSucess = addNetwork(CreateWifiInfo(ssid, pwd, 3));
        //boolean addSucess = addNetwork(createWifiConfiguration(ssid, pwd, WifiCipherType.WPA2));
        boolean addSucess =   addNetWorkAndConnect(ssid, pwd, WifiCipherType.WPA2);
        if(D) Log.d("wxwx","---------connectWifiTest  addSucess----------------------"+addSucess);
        if (addSucess) {
            mWifiManager.saveConfiguration();
            while (tryCounter<3) {
                try {
                   // mWifiManager.reconnect();
                    //  Thread.sleep(10000);
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                String currSSID = getCurrentWifiInfo().getSSID();

                if (currSSID != null)
                    currSSID = currSSID.replace("\"", "");
                // DhcpInfo di = wm.getDhcpInfo();
                DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
                int currIp = dhcpInfo.ipAddress;
                if(D) Log.d("wxwx","---------connectWifiTest  currSSID----------------------"+currSSID);
                if(D) Log.d("wxwx","---------connectWifiTest  currIp----------------------"+currIp);
                if (currSSID != null && currSSID.equals(ssid) && currIp != 0) {
                    isSuccess = true;
                    break;
                }
                tryCounter++;
            }
            /*while (!flag && !isSuccess) {
                try {
                  //  Thread.sleep(10000);
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                String currSSID = getCurrentWifiInfo().getSSID();

                if (currSSID != null)
                    currSSID = currSSID.replace("\"", "");
                int currIp = getCurrentWifiInfo().getIpAddress();
                Log.d("wxwx","---------connectWifiTest  currSSID----------------------"+currSSID);
                Log.d("wxwx","---------connectWifiTest  currIp----------------------"+currIp);
                if (currSSID != null && currSSID.equals(ssid) && currIp != 0) {
                    isSuccess = true;
                } else {
                    flag = true;
                }
            }*/
        }
       // SystemClock.sleep(10000);
        return isSuccess;

    }

    /**
     * 连接一个WIFI
     *
     * @param ssid
     * @param password
     * @param wifiCipherType
     */
    public boolean addNetWorkAndConnect(String ssid, String password, WifiCipherType wifiCipherType) {
        if(D) Log.d("wxwx","--------addNetWorkAndConnect-------------------");
        if (mWifiManager != null && wifiCipherType != null) {
            WifiConfiguration wifiConfig = createWifiConfiguration(ssid, password, wifiCipherType);
            WifiConfiguration temp = isWifiConfigurationSaved(wifiConfig);
            if(D) Log.d("wxwx","---------addNetWorkAndConnect temp -------" +temp);
            if (temp != null) {
                mWifiManager.removeNetwork(temp.networkId);
                if(D) Log.d("wxwx","---------addNetWorkAndConnect removeNetwork -------");
            }
           return addNetWorkToConnect (wifiConfig);
        }
        else
        {
            if(D) Log.d("wxwx","---------addNetWorkAndConnect  return false-------");
            return false;

        }
    }
    /**
     * 执行一次Wifi的扫描
     */
    public synchronized void startScan() {
        if (mWifiManager!= null) {
            mWifiManager.startScan();
            scanResultList = mWifiManager.getScanResults();
            wifiConfigurationList = mWifiManager.getConfiguredNetworks();
        }
    }


    private WifiConfiguration isWifiConfigurationSaved(WifiConfiguration wifiConfig) {
        if (wifiConfigurationList == null) {
            this.startScan();
        }
        for (WifiConfiguration temp : wifiConfigurationList) {
            if (temp.SSID.equals(wifiConfig.SSID)) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 保存并连接到一个新的网络
     *
     * @param _wifiConfiguration
     */
    public boolean addNetWorkToConnect(WifiConfiguration _wifiConfiguration) {
        netWorkId = addNetWork(_wifiConfiguration);
        //
        mWifiManager.saveConfiguration();
        if(D)  Log.d("wxwx","---------addNetWorkToConnect  enableNetwork Network ID is -------"+ netWorkId);
        if (netWorkId != -255) {
            return mWifiManager.enableNetwork(netWorkId, true);
         //  return connetionConfiguration(netWorkId);
        }
        else
        {
            if(D) Log.d("wxwx","---------addNetWorkToConnect   return false-----------------");
            return false;

        }
    }

    /**
     * 保存一个新的网络
     *
     * @param _wifiConfiguration
     */
    public int addNetWork(WifiConfiguration _wifiConfiguration) {
        int netWorkId = -255;
        if (_wifiConfiguration != null && mWifiManager != null) {
            netWorkId = mWifiManager.addNetwork(_wifiConfiguration);
            startScan();
        }
        return netWorkId;
    }


    /**
     * 通过netWorkId来连接一个已经保存好的Wifi网络
     *
     * @param netWorkId
     */
    public boolean connetionConfiguration(int netWorkId) {
        if (configurationNetWorkIdCheck(netWorkId) && mWifiManager != null) {
            if(D) Log.d("wxwx","---------connetionConfiguration enableNetwork -------");
            return mWifiManager.enableNetwork(netWorkId, true);
        }else
        {
            if(D) Log.d("wxwx","---------connetionConfiguration  return false-----------------");
            return  false;
        }
    }

    /**
     * 检测尝试连接某个网络时，查看该网络是否已经在保存的队列中间
     *
     * @param netWorkId
     * @return
     */
    private boolean configurationNetWorkIdCheck(int netWorkId) {
        for (WifiConfiguration temp : wifiConfigurationList) {
            if (temp.networkId == netWorkId) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取当前手机所连接的wifi信息
     */
    public WifiInfo getCurrentWifiInfo() {
        return mWifiManager.getConnectionInfo();
    }
    /**
     * 添加一个网络并连接 传入参数：WIFI发生配置类WifiConfiguration
     */
    public boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        return mWifiManager.enableNetwork(wcgID, true);
    }

    /**
     * 创建WifiConfiguration对象 分为三种情况：1没有密码;2用wep加密;3用wpa加密
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public WifiConfiguration CreateWifiInfo(String SSID, String Password,
                                            int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) // WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            //config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            //config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }



    private WifiConfiguration createWifiConfiguration(String ssid, String password, WifiCipherType type) {
        WifiConfiguration newWifiConfiguration = new WifiConfiguration();
        newWifiConfiguration.allowedAuthAlgorithms.clear();
        newWifiConfiguration.allowedGroupCiphers.clear();
        newWifiConfiguration.allowedKeyManagement.clear();
        newWifiConfiguration.allowedPairwiseCiphers.clear();
        newWifiConfiguration.allowedProtocols.clear();
        newWifiConfiguration.SSID = "\"" + ssid + "\"";
        switch (type) {
            case NONE:
                newWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
            case IEEE8021XEAP:
                break;
            case WEP:
                break;
            case WPA:
                newWifiConfiguration.preSharedKey = "\"" + password + "\"";
                newWifiConfiguration.hiddenSSID = true;
                newWifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                newWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                newWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                newWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                newWifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                newWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                break;
            case WPA2:
                newWifiConfiguration.preSharedKey = "\"" + password + "\"";
                newWifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                newWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                newWifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                newWifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                newWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                newWifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                newWifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                newWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                break;
            default:
                return null;
        }
        return newWifiConfiguration;
    }



    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        if(existingConfigs!=null){
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }

        return null;
    }

    //判断网络连接是否可用
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null&&networkInfo.length>0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断WiFi是否打开
    public boolean isWiFi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getApp().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //判断移动数据是否打开
    public static boolean isMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
    //是否连接WIFI
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }

        return false ;
    }

    /**
     * 判断手机是否连接在Wifi上
     */
    public boolean isConnectWifi() {
        // 获取ConnectivityManager对象
        ConnectivityManager conMgr = (ConnectivityManager) MyApp.getApp().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        // 获取连接的方式为wifi
        NetworkInfo.State wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        if (info != null && info.isAvailable() && wifi == NetworkInfo.State.CONNECTED)
        {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取wifi的当前连接状态
     */
    public static NetworkInfo.State getWifiState() {
        // 获取ConnectivityManager对象
        ConnectivityManager conMgr = (ConnectivityManager) MyApp.getApp().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        // 获取连接的方式为wifi
        NetworkInfo.State wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return wifi;

    }




    //判断WLAN状态是否开启
    public boolean isWifiApOn() {
        Method method = null;
        int i = 0;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApState");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            i = (Integer) method.invoke(mWifiManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "wifi sharing state -> " + i);
        // 10---正在关闭；11---已关闭；12---正在开启；13---已开启
        return i == 13;
    }


    public int getWifiApState() {
        Method method = null;
        int i = 0;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApState");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            i = (Integer) method.invoke(mWifiManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "wifi sharing state -> " + i);
        // 10---正在关闭；11---已关闭；12---正在开启；13---已开启
        return i;
    }
    //设置WLAN状态
    public boolean setWifiApEnabled(boolean enabled) {
        Method method = null, configMethod = null;
        boolean result = false;
        if (mWifiManager == null) {
            Log.i(TAG, "mWifiManager is null  -> " + result);
            return result;
        }
        try {
            configMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) configMethod.invoke(mWifiManager);
            result = (boolean) method.invoke(mWifiManager, new Object[]{apConfig, enabled});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "------------setWifiApEnabled -> " + result);
        return result;
    }

    //获取WLAN ＳＳＩＤ
    public String getWifiApSSID() {
        Method method = null;
        String SSID = null;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
            SSID = apConfig.SSID;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "-------------getWifiApSSID -> " + SSID);
        return SSID;
    }

    //获取WLAN　密码
    public String getWifiApSharedKey() {
        Method method = null;
        String SharedKey = null;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
            SharedKey = apConfig.preSharedKey;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SharedKey;
    }
}
