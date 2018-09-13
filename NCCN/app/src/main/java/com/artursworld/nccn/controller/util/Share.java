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
        //String logMessage = "get global value by key(key=" + key + ", value=" + restoredText + ")";
        //Log.i(CLASS_NAME, logMessage);
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
        //Log.i(CLASS_NAME, logMessage);
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
        //Log.i(CLASS_NAME, logMessage);
        return restoredText;
    }

    /**
     * Writes key-value pair into shared preferences
     *
     * @param key   the key
     * @param value the value of type string
     * @return True if the function (put key-value pair into shared preferances) was succesful). Otherwise false
     */
    public static boolean putString(final String key, final String value) {
        boolean isFunctionSuccesful = false;
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
        if (key != null && value != null) {
            prefs.putString(key, value);
            prefs.commit();
            prefs.apply();
            isFunctionSuccesful = true;
        }
        return isFunctionSuccesful;
    }

    /**
     * Writes value into shared preferences
     *
     * @param key
     * @param value
     */
    public static void putBoolean(final String key, final boolean value) {
        try {
            SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
            if (key != null) {
                prefs.putBoolean(key, value);
                prefs.commit();
                prefs.apply();
            }

        } catch (Exception e) {
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
        try {
            SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
            if (key != null && value != null) {
                prefs.putStringSet(key, value);
                prefs.commit();
                prefs.apply();
                //String logMessage = "set global value(key=" + key + ", value=" + value + ")";
                //Log.i(CLASS_NAME, logMessage);
            }
        } catch (Exception e) {
            String logMessage = "could not set global value(key=" + key + ", value=" + value + ") ";
            Log.e(CLASS_NAME, logMessage + e.getLocalizedMessage());
        }
    }

    public static boolean getBooleanByKey(int keyId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String key = App.getAppContext().getResources().getString(keyId);
        boolean retBool = prefs.getBoolean(key, false);
        return retBool;
    }
}
