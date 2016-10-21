package com.artursworld.nccn.controller.util;

import com.artursworld.nccn.controller.config.App;


public class Strings {

    /**
     * Get the string by resource id
     * @param R_ID the resource id
     * @return the string by resource id
     */
    public static String getStringByRId(int R_ID) {
        return App.getAppContext().getResources().getString(R_ID);
    }

}

