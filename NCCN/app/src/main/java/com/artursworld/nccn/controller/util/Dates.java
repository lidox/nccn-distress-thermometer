package com.artursworld.nccn.controller.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

    private static final String GERMAN_DATE_PATTERN = "dd.MM.yyyy HH:mm";

    /**
     * Transforms a date string to the german date string representation
     *
     * @param givenDate the date to transform
     * @return a german string representation of the given date
     */
    public static String getGermanDateByDateString(String givenDate) {
        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(givenDate);
            givenDate = getGenericDateAsStringRepresentation(date, GERMAN_DATE_PATTERN);

        } catch (Exception e) {
            Log.e(Dates.class.getSimpleName(), "could not parse date to german representation");
        }
        return givenDate;
    }

    /**
     * Transforms a date to the german date string representation
     *
     * @param date the date to transform
     * @return a german string representation of the given date
     */
    public static String getGermanDateByDate(Date date) {
        return getGenericDateAsStringRepresentation(date, GERMAN_DATE_PATTERN);
    }

    /**
     * Transforms a date into a string representation which can be used in file names
     *
     * @param date the date to trasform
     * @return a string representation which can be used in file names
     */
    public static String getFileNameDate(Date date) {
        return getGenericDateAsStringRepresentation(date, "yyyy-MM-dd-HH-mm");
    }


    /**
     * Transforms a date into a string representation using a pattern
     *
     * @param date    the date to transform
     * @param pattern the pattern to use for transformation
     * @return a string representation using given pattern
     */
    @Nullable
    private static String getGenericDateAsStringRepresentation(Date date, String pattern) {
        String result = null;
        try {
            result = new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            Log.e(Dates.class.getSimpleName(), "Could not parse date! " + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Transforms a date into a number representation
     *
     * @param date the date to transform
     * @return a number representation of the given date
     */
    public static long getLongByDate(Date date) {
        if (date != null)
            return date.getTime();
        return -1;
    }

    /*
    public static Date getDateByNumber(long number) {
        return new Date(number);
    }
    */
}
