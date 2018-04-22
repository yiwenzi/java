package com.joker.io;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by hunter on 2017/10/30.
 * StringReader : 字符串比较大，字符到字节流的转换，
 * StringWriter ： 写入的字符串比较大，将字符串一次性写入到writer里
 */
public class StringReaderDemo {
    public static void main(String[] args) {
        String target = "<bargainDate>2017-10-21 16:00:00.0 UTC</bargainDate>\n" +
                "<phoneNumber>0755-86290708</phoneNumber>\n" +
                "<bargainDate>2018-12-13 16:00:00.0 UTC</bargainDate>\n" +
                "<phoneNumber>02087355173</phoneNumber>\n" +
                "<bargainDate>2018-12-13 16:00:00.0 UTC</bargainDate>\n" +
                "<phoneNumber>60965700</phoneNumber>\n" +
                "<faxNumber></faxNumber>\n" +
                "<bargainDate>2017-11-28 16:00:00.0 UTC</bargainDate>\n";
        StringReader reader = new StringReader(target);
        StringWriter writer = new StringWriter(100);
        writer.toString();
       /* CharArrayReader reader2 = new CharArrayReader();
        reader2.close();*/
    }
}
