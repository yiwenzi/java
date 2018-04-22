package com.joker.thread;

/**
 * Created by hunter on 2017/11/13.
 */
import java.util.concurrent.TimeUnit;

import static com.joker.util.Print.*;


class SleepBlocked implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            print("InterruptingException");
        }
        print("Exiting SleepBlocked.run()");
    }
}
public class Interrupting {
    public static void main(String[] args) {

    }
}
