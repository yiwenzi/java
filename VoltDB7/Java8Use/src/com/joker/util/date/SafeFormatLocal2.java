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
public class SafeFormatLocal2 {

    private static final String date_format = "yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat() {
        DateFormat format = threadLocal.get();
        if(format == null) {
            format = new SimpleDateFormat(date_format);
            threadLocal.set(format);
        }
        return format;
    }

    public static Date parse(String string) throws ParseException {
        return getDateFormat().parse(string);
    }

    public static String format(Date date) {
        return getDateFormat().format(date);
    }
}
