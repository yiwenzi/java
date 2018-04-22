package com.joker.date;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by hunter on 2017/10/16.
 * 后续：
 *  Duration
 *  加减
 *  转换
 *  向下兼容
 *  http://mp.weixin.qq.com/s/PpDK4ZIk_DeqSglnJ6fKyg
 *  http://www.importnew.com/14857.html
 *
 */
public class Java8Date {
    public static void main(String[] args) {
        System.out.println("=================localDate===================");
        //localDate();
        System.out.println("=================localTime===================");
        //localTime();
        System.out.println("==============localDateTime==================");
        //localDateTime();
        System.out.println("============Instant====================");
        //instantShow();
        System.out.println("============DateParseFormat==========");
        dateFormatShow();
    }

    private static void dateFormatShow() {
        LocalDate date = LocalDate.now();
        System.out.println("Default format of LocalDate = " + date);

        System.out.println(date.format(DateTimeFormatter.ofPattern("d::MMM::uuuu")));
        System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    private static void instantShow() {
        Instant timestamp = Instant.now();
        System.out.println("Current timestamp = " + timestamp);

        Duration thirthDay = Duration.ofDays(30);
        System.out.println(thirthDay);

        Instant timestamp2 = Instant.now();
        Duration duration = Duration.between(timestamp, timestamp2);
        System.out.println(duration);
    }

    private static void localDateTime() {
        LocalDateTime today = LocalDateTime.now();
        System.out.println("Current DateTime = " + today);

        today = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        System.out.println("Current DateTime = " + today);
    }

    public static void localDate(){
        LocalDate today = LocalDate.now();
        System.out.println("Current Date = " + today);

        LocalDate todayKolkata = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        System.out.println("Current Date in IST = " + todayKolkata);

        LocalDate shanghaiDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        System.out.println("ShanghaiDate: " + shanghaiDate);

        //Getting date from the base date i.e 01/01/1970
        LocalDate dateFromBase = LocalDate.ofEpochDay(365);
        System.out.println("365th day from base date= " + dateFromBase);

        LocalDate hundredDay2014 = LocalDate.ofYearDay(2014, 100);
        System.out.println("100th day of 2014=" + hundredDay2014);
    }

    public static void localTime(){
        LocalTime time = LocalTime.now();
        System.out.println("Current time: " + time);

        //Creating LocalTime by providing input arguments
        LocalTime specificTime = LocalTime.of(12,20,25,40);
        System.out.println("Specific Time of Day="+specificTime);
    }
}
