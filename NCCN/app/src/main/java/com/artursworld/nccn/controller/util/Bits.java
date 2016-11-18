package com.artursworld.nccn.controller.util;

import android.util.Log;

import com.artursworld.nccn.model.entity.HADSDQuestionnaire;

import java.math.BigInteger;


public class Bits {

    public static byte[] getByteByString(String byteString){
        if(byteString.length() % 8 == 1){
            Log.e(HADSDQuestionnaire.class.getSimpleName(), "the byteString % 8 = 0 must be fulfilled");
            return null;
        }

        return new BigInteger(byteString, 2).toByteArray();
    }

    public static String getStringByByte(byte[] bytes){
        StringBuilder ret  = new StringBuilder();
        if(bytes != null){
            for (byte b : bytes) {
                ret.append(Integer.toBinaryString(b & 255 | 256).substring(1));
            }
        }
        return ret.toString();
    }

    public static byte[] getNewByteByCheckBox(boolean checked, int index, byte[] answerByte) {
        int bit = checked ? 1 : 0;
        StringBuilder bits = new StringBuilder(Bits.getStringByByte(answerByte));
        bits.setCharAt( (bits.length() - index - 1), Character.forDigit(bit, 10));
        return  Bits.getByteByString(bits.toString());
    }


    public static byte[] getNewByteByRadioBtn(boolean checked, int index, byte[] answerByte) {
        int bit = checked ? 1 : 0;
        StringBuilder bits = new StringBuilder(Bits.getStringByByte(new byte[answerByte.length]));
        bits.setCharAt( (bits.length() - index - 1), Character.forDigit(bit, 10));
        return  Bits.getByteByString(bits.toString());
    }

}
