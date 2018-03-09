package com.biyesheji.android.robot.socket;

import android.util.Log;

import com.biyesheji.android.robot.MyApp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class GsonList {

    public static void gsonList(String type, int id, String ask, String answer, String operatorType){
        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("para1",id);
        map.put("para2",ask);
        map.put("para3",answer);
        map.put("para4",operatorType);
        list.add(map);

        String ap = new Gson().toJson(list);
        list.clear();
        Log.d("wxwx","---- gsonList--------"+ap);
        ConnectToServer.socketClient.sendMsg(ap);
      //  MyApplication.mChatService.sendBTMessage(ap);
    }


    public static void gsonListViaBT(String type, int id, String ask, String answer, String operatorType){
        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("para1",id);
        map.put("para2",ask);
        map.put("para3",answer);
        map.put("para4",operatorType);
        list.add(map);

        String ap = new Gson().toJson(list);
        list.clear();
        Log.d("wxwx","---- gsonList--------"+ap);
        //   ConnectToServer.socketClient.sendMsg(ap);
        MyApp.mChatService.sendBTMessage(ap);
    }
}

