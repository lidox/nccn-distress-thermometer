package com.artursworld.nccn.controller.util;


import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Security {

    private static String CLASS_NAME = Security.class.getSimpleName();

    public static String getMD5ByString(String plainInputText){
        String hashtext = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plainInputText.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            hashtext = bigInt.toString(16);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not convert plain text '"+plainInputText+"' to  MD5-Hash " + e.getLocalizedMessage());
        }
        return hashtext;
    }
}
