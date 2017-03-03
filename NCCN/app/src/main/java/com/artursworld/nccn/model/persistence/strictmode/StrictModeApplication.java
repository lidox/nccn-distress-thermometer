package com.artursworld.nccn.model.persistence.strictmode;

import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.compat.BuildConfig;
import android.util.Log;


public class StrictModeApplication {

    public StrictModeApplication() {

        if (BuildConfig.DEBUG) {
            Log.i("StrictModeApplication", " onCreate");
            enableStrictMode();
        }

    }

    private void enableStrictMode() {
        if (Build.VERSION.SDK_INT >= 9) {
            // strict mode is available from API 9
            strictModeConfigurations();
        }

        if (Build.VERSION.SDK_INT >= 16) {
            // from API 16 on the strict mode must be enabled like this when used in Application class
            new Handler().postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    strictModeConfigurations();
                }
            });
        }

    }

    private void strictModeConfigurations() {

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        );
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build()
        );
    }
}