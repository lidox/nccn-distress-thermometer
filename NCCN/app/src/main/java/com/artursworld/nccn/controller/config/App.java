package com.artursworld.nccn.controller.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Dependencies:
 * 1. Add manifest:
 * <application
 *    android:name="com.artursworld.playground.App"
 */
public class App extends Application {

    private static Context ctx = null;

    @Override
    public void onCreate() {
        Log.i("App", "running applicaiton");
        super.onCreate();
        ctx = this.getApplicationContext();
    }

    public static Context getAppContext(){
        return ctx;
    }
}

