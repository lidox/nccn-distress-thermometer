package com.artursworld.nccn.controller.util;

import android.util.Log;

import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;


public class Bits {

    /**
     * Get an byte array by binary string
     * @param binaryString the string representing a byte
     * @return an byte array
     */
    public static byte[] getByteByString(String binaryString){
        Iterable iterable = Splitter.fixedLength(8).split(binaryString);
        byte[] ret = new byte[Iterables.size(iterable) ];
        Iterator iterator = iterable.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Integer byteAsInt = Integer.parseInt(iterator.next().toString(), 2);
            ret[i] = byteAsInt.byteValue();
            i++;
        }
        return ret;
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
