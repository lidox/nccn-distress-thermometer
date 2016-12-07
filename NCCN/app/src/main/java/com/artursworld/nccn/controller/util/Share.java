package com.artursworld.nccn.controller.util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.artursworld.nccn.controller.config.App;

import java.util.Set;

public class Share {

    private static String CLASS_NAME = Share.class.getSimpleName();

    /**
     * Reads value by key from shared preferences
     *
     * @param key the key to check
     * @return the value which stands for the key
     */
    public static String getStringByKey(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String restoredText = prefs.getString(key, null);
        String logMessage = "get global value by key(key=" + key + ", value=" + restoredText + ")";
        Log.i(CLASS_NAME, logMessage);
        return restoredText;
    }

    /**
     * Reads value from shared preferences by resourceId
     *
     * @param keyId the resource key ID
     * @return the value which stands for the key
     */
    public static String getStringByKey(int keyId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String key = App.getAppContext().getResources().getString(keyId);
        String restoredText = prefs.getString(key, null);
        String logMessage = "get global value by keyId(=" + key + ", value=" + restoredText + ")";
        Log.i(CLASS_NAME, logMessage);
        return restoredText;
    }

    /**
     * Reads value from shared preferences by resourceId
     *
     * @param keyId the resource key ID
     * @return the value which stands for the key
     */
    public static Set<String> getStringSetByKey(int keyId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String key = App.getAppContext().getResources().getString(keyId);
        Set<String> restoredText = prefs.getStringSet(key, null);
        String logMessage = "get global value by keyId(=" + key + ", value=" + restoredText + ")";
        Log.i(CLASS_NAME, logMessage);
        return restoredText;
    }

    /**
     * Writes value into shared preferences
     *
     * @param key
     * @param value
     */
    public static void putString(final String key, final String value) {
        AsyncTask a = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... unusedParams) {
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
                if (key != null && value != null) {
                    prefs.putString(key, value);
                    prefs.commit();
                    prefs.apply();
                    String logMessage = "set global value(key=" + key + ", value=" + value + ")";
                    Log.i(CLASS_NAME, logMessage);
                }
                return null;
            }
        }.execute();

        try {
            a.get();
        }
        catch (Exception e){
            String logMessage = "could not set global value(key=" + key + ", value=" + value + ") ";
            Log.e(CLASS_NAME, logMessage + e.getLocalizedMessage());
        }
    }

    /**
     * Writes value into shared preferences
     *
     * @param key
     * @param value
     */
    public static void putStringSet(final String key, final Set<String> value) {
        AsyncTask a = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... unusedParams) {
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
                if (key != null && value != null) {
                    prefs.putStringSet(key, value);
                    prefs.commit();
                    prefs.apply();
                    String logMessage = "set global value(key=" + key + ", value=" + value + ")";
                    Log.i(CLASS_NAME, logMessage);
                }
                return null;
            }
        }.execute();

        try {
            a.get();
        }
        catch (Exception e){
            String logMessage = "could not set global value(key=" + key + ", value=" + value + ") ";
            Log.e(CLASS_NAME, logMessage + e.getLocalizedMessage());
        }
    }
}
