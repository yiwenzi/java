package com.joker.date;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hunter on 2017/10/26.
 */
public class DateFormatDemo {
    public static void main(String[] args) {
        Locale locale = Locale.CHINA;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat dateFormat2 = DateFormat.getDateInstance(DateFormat.LONG, locale);
        DateFormat dateFormat3 = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        DateFormat dateFormat4 = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        System.out.println(dateFormat.format(new Date()));
        System.out.println(dateFormat2.format(new Date()));
        System.out.println(dateFormat3.format(new Date()));
        System.out.println(dateFormat4.format(new Date()));
    }
}
