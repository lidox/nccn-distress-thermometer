package com.artursworld.nccn.controller.util;


import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Security {

    private static String CLASS_NAME = Security.class.getSimpleName();
    private static final byte[] key = new byte[]{'Y', 'O', 'O', '-', 'W', 'O', 'N', 'T', '-', 'F', 'I', 'N', 'D', '-', 'O', 'U'};

    /**
     * Encrypts a string using AES 128 Bit
     *
     * @param stringToEncrypt the string to encrypt
     * @return the encrypted string
     * See: http://stackoverflow.com/questions/22607791/aes-encryption-using-java-and-decryption-using-javascript
     */
    public static String encrypt(String stringToEncrypt) {
        try {
            Key key = new SecretKeySpec(Security.key, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(stringToEncrypt.getBytes());
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            return encryptedValue;
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not encrypt: '" + stringToEncrypt + "'");
        }
        return "-";
    }

    public static String decrypt(String encrypted) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Security.key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String getMD5ByString(String plainInputText) {
        String hashtext = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plainInputText.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Could not convert plain text '" + plainInputText + "' to  MD5-Hash " + e.getLocalizedMessage());
        }
        return hashtext;
    }


}
