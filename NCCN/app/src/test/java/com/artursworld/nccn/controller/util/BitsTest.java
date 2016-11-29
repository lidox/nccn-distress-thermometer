package com.artursworld.nccn.controller.util;

import dalvik.annotation.TestTargetClass;

import static junit.framework.Assert.assertEquals;

public class BitsTest {

    public void testBits1(){
        String bits = "1111000";
        byte [] asByte = Bits.getByteByString(bits);
        String bitsAgain = Bits.getStringByByte(asByte);
        assertEquals(bits, bitsAgain);
    }
}
