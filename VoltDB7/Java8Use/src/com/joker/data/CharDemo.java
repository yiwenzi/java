package com.joker.data;

import java.io.UnsupportedEncodingException;

/**
 * Created by hunter on 2017/10/24.
 */
public class CharDemo {
    public static void main(String[] args) {
        String str = "严";
        System.out.println(str.length());
        System.out.println(str.codePointCount(0, str.length()));
        System.out.println(str.codePointAt(0));
        System.out.println(str.charAt(0));
        char c = '虫';
        char c2 = '严';
        try {
            byte[] strBytes = str.getBytes("utf-16");
            byte[] cBytes = charToByte(c);
            byte[] cBytes2 = charToByte(c2);
            System.out.println("strByte大小: " + strBytes.length);
            System.out.println("cByte大小: " + cBytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String test = "joker";
        String test2 = "卜佳珺";
        int i = test.codePointAt(0);
        char ic = test.charAt(0);
        System.out.println(i + " " + ic);
        //返回true
        System.out.println(i == ic);
        int i2 = test2.codePointAt(1);
        char ic2 = test2.charAt(1);
        System.out.println(i2 + " " + ic2);
        System.out.println("  ");
    }
    //计算char的unicode表示,unicode表示跟字节是不一样的
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
}
