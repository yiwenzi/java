package com.joker.thread;

/**
 * Created by hunter on 2017/11/9.
 */
public class SynchronizedEventGenerator extends IntGenerator {
    private int currentValue = 0;
    @Override
    public synchronized int next() {
        ++currentValue;
        Thread.yield();
        ++currentValue;
        return currentValue;
    }

    public static void main(String[] args) {
        EventChecker.test(new SynchronizedEventGenerator());
    }
}
