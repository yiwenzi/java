package com.joker.data;

/**
 * Created by hunter on 2017/10/27.
 * 1. 了解内存结果
 * 2. 了解二进制的表示
 * 3. 按位操作
 * 4. 包装类
 */
public class AboutInteger {
    public static void main(String[] args) {
        System.out.println("二进制7: " + Integer.toBinaryString(7));
        System.out.println("二进制-7: " + Integer.toBinaryString(-7));
        //无符号表示
        System.out.println(Byte.toUnsignedInt((byte)-2));
        //得到十进制的值
       // System.out.println(Integer.parseInt("0xFFFFFFFB", 16));
        //System.out.println(Short.parseShort("11111110", 2));
    }
}
