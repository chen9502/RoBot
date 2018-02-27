package com.biyesheji.android.robot;

import android.app.Application;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MyApp extends Application{
    private static MyApp app;

    public static MyApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
    }
}
