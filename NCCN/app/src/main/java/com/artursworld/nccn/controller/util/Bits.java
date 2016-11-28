package com.artursworld.nccn.controller.util;

import android.util.Log;

import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.math.BigInteger;
import java.util.ArrayList;


public class Bits {

    /**
     * Get an byte array by string
     * @param byteString the string representing a byte
     * @return an byte array
     */
    public static byte[] getByteByString(String byteString){
        /*BigInteger bigInteger = new BigInteger(byteString, 2);
        return bigInteger.toByteArray();*/
        ArrayList<Integer> arrayList = new ArrayList<>();
        Iterable<String> result = Splitter.fixedLength(8).split(byteString);
        String[] parts = Iterables.toArray(result, String.class);
        for(String str : parts){
            arrayList.add(Integer.parseInt(str, 2));
        }


        byte[] ret = new byte[arrayList.size()];
        for(int i = 0; i < arrayList.size(); i++){
            ret[i] = arrayList.get(i).byteValue();
        }
        return ret;

        /*
        byte[] b = new byte[byteString.length()];
        for (int i=0; i<byteString.length(); i++) {
            b[i]= byteString.charAt(i)=='1' ? (byte)1 : (byte)0;
        }
        return b;
        */

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
