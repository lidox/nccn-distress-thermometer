package com.artursworld.nccn.controller.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

    public static String getGermanDateByDateString(String creationDate) {
        try {
            Date form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(creationDate);
            SimpleDateFormat germanDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            creationDate = germanDateFormat.format(form);
        }
        catch (Exception e){
            Log.e(Dates.class.getSimpleName(), "could not parse date to german representation");
        }
        return creationDate;
    }

    public static String getGermanDateByDate(Date date) {
        String result = null;
        try {
            SimpleDateFormat germanDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            result = germanDateFormat.format(date);
        }
        catch (Exception e){
            Log.e(Dates.class.getSimpleName(), "could not parse date to german representation");
        }
        return result;
    }

    public static long getLongByDate(Date date){
        if(date!= null)
            return date.getTime();
        return -1;
    }

    public static Date getDateByNumber(long number){
        return new Date(number);
    }
}
