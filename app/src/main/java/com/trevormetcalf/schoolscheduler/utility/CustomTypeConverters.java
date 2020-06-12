package com.trevormetcalf.schoolscheduler.utility;

import androidx.room.TypeConverter;

import java.util.Date;

/*
    This class is utilized by the SchoolDatabase class to convert date types and long values.
    Allows certain date types to be stored in SQLite database.
 */

public class CustomTypeConverters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
