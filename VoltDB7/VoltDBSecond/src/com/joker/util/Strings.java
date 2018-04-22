package com.joker.util;

/**
 * Created by hunter on 2017/12/24.
 */
public class Strings {
    public static String ringhtTrim(String source) {
        //这里还是没有注意null值
        int len = source.length();
        int st = 0;
        char[] val = source.toCharArray();    /* avoid getfield opcode */

        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return (len < source.length()) ? source.substring(st, len) : source;
    }
}
