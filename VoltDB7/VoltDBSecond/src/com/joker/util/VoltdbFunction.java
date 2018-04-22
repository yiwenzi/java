package com.joker.util;

/**
 * Created by hunter on 2017/12/24.
 */
public class VoltdbFunction {
    public String rightTrim(String source) {
        int len = source.length();
        int st = 0;
        char[] val = source.toCharArray();

        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return (len < source.length()) ? source.substring(st, len) : source;
    }
}
