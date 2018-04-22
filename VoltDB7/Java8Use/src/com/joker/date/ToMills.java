package com.joker.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hunter on 2017/11/2.
 */
public class ToMills {
    public static void main(String[] args) {
        String date = "2013-01-04 11:32:22.0";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            System.out.println(format.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = new Date(1357270342000L);
        System.out.println();
    }
}
