package com.joker.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hunter on 2017/10/26.
 * 将共享变量变为独享，线程独享肯定能比方法独享在并发环境中能减少不少创建对象的开销。
 * 如果对性能要求比较高的情况下，一般推荐使用这种方法。
 */
public class SafeFormatLocal {

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static Date parse(String string) throws ParseException {
        return threadLocal.get().parse(string);
    }

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }
}
