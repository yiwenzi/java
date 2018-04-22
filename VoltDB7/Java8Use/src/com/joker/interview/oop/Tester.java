package com.joker.interview.oop;

/**
 * Created by hunter on 2018/3/9.
 */
public class Tester {
    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        for (Integer value : list) {
            System.out.println(value);
        }
    }
}
