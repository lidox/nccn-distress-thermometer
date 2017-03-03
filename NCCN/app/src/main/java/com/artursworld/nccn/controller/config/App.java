package com.artursworld.nccn.controller.config;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.artursworld.nccn.model.persistence.strictmode.StrictModeApplication;

/**
 * Dependencies:
 * 1. Add manifest:
 * <application
 *    android:name="com.artursworld.playground.App"
 */
public class App extends Application {

    private static Context ctx = null;
    private StrictModeApplication strictMode = null;

    @Override
    public void onCreate() {
        Log.i("App", "running applicaiton");
        super.onCreate();
        ctx = this.getApplicationContext();
        strictMode = new StrictModeApplication();
    }

    public static Context getAppContext(){
        return ctx;
    }
}

