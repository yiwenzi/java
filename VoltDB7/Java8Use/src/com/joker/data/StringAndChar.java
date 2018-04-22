package com.joker.data;

/**
 * Created by hunter on 2017/10/27.
 */
public class StringAndChar {
    public static void main(String[] args) {
        String str = "今天";
        int length = str.length();
        char[] chars = new char[length];
        str.getChars(0, length, chars, 0);
        for (char aChar : chars) {
            System.out.println(Integer.toHexString(aChar));
            System.out.println((int)aChar);
        }
        //String string = "\\u20170\\u22825";
        //System.out.println(Integer.toHexString(20170));
        //System.out.println(Integer.toHexString(22825));
    }
}
