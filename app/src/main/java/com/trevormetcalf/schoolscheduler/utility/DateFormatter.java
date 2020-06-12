package com.trevormetcalf.schoolscheduler.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    This class provides a simple date formatter for converting between String and Date types.
 */

public class DateFormatter {
    private static final String TAG = "DateFormatter";

    private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public static String formatDate(Date date) {
        if (date == null){
            return "";
        }
        return format.format(date);
    }

    public static Date toDate(String date){
        if (date == null) {
            return null;
        }
        try {
            return format.parse(date);
        } catch (ParseException e) {
            Log.d(TAG, "toDate: " + e.getMessage());
        }
        return null;

    }

}
