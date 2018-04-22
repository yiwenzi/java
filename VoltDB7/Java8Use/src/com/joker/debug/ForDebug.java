package com.joker.debug;

/**
 * Created by hunter on 2017/11/15.
 */
public class ForDebug {
    public static Integer result = 0;
    public static void main(String[] args) {
        int count = 10;
        for (int i = 0; i < count; i++) {
            result = i * i;
            System.out.println("I am: " + result);
            //assert result != 1 : "It's time to stop";
        }
    }
}
