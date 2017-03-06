package com.artursworld.nccn.controller.util;

import java.util.Random;


public class Generator {

    /**
     * Generates a random number between low (inclusive) and high (inclusive)
     *
     * @param low  the lowest param
     * @param high the highest param
     * @return a random number number between low (inclusive) and high (inclusive). if low <= high.
     * Otherwise returns -1
     */
    public static int getRandomInRange(int low, int high) {
        if (low <= high) {
            Random r = new Random();
            return r.nextInt(high + 1 - low) + low;
        }
        return -1;
    }
}
