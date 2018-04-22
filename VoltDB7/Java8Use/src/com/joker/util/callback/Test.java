package com.joker.util.callback;

/**
 * Created by hunter on 2017/10/27.
 */
public class Test {
    public static void main(String[] args) {
        Li li = new Li();

        Wang wang = new Wang(li);

        wang.askQuestion("1 + 1 = ?");
    }
}
