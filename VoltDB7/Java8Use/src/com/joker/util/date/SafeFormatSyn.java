package com.joker.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hunter on 2017/10/26.
 */
public class SafeFormatSyn {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date) {
        synchronized (format) {
            return format.format(date);
        }
    }

    public static Date parse(String string) throws ParseException {
        synchronized (format) {
            return format.parse(string);
        }
    }
}
