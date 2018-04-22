package com.joker.data;

import java.math.BigDecimal;

/**
 * Created by hunter on 2017/11/28.
 */
public class BigDecimalDemo {
    public static void main(String[] args) {
        double d = 111231.5585;
        //BigDecimal number = new BigDecimal("111231.5585");
        BigDecimal number = new BigDecimal(d);
        //BigDecimal result  = number.setScale(3, BigDecimal.ROUND_DOWN);
        BigDecimal result =  getCorrectScale(number, 0);
        System.out.println(number);
        System.out.println(number.doubleValue());
        System.out.println(result);
        System.out.println(result.doubleValue());
        System.out.println(Thread.currentThread().getName());
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
            }).start();
        }
    }

    public static BigDecimal getCorrectScale(BigDecimal number, Integer scale) {
        number = number.setScale(scale, BigDecimal.ROUND_DOWN);
        return number;
    }
}
