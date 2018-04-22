package com.joker.thread;

/**
 * Created by hunter on 2017/11/8.
 */
public class EventGenerator extends IntGenerator {
    private int currentEventValue = 0;
    @Override
    public int next() {//为了保证是偶数，这里加了两次，但是因为多线程，可能会出问题，在141325的时候出了问题
        ++currentEventValue;
        ++currentEventValue;
        return currentEventValue;
    }

    public static void main(String[] args) {
        EventChecker.test(new EventGenerator());
    }
}
