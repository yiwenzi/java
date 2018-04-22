package com.joker.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hunter on 2017/11/8.
 */
public class EventChecker implements Runnable {
    private IntGenerator generator;
    // 非static的final变量可以在构造函数中赋值
    private final int id;

    public EventChecker(IntGenerator generator, int id) {
        this.generator = generator;
        this.id = id;
    }

    @Override
    public void run() {
        // 如果是偶数
        while (!generator.isCanceled()) { // canceled的状态还没转过来，所以会出现多于一次的情况（实在是太有意思了）
            int val = generator.next();
            if((val & 1) == 1) { //说明是奇数
                System.out.println(val + " not event!");
                System.out.println(generator.toString());
                generator.cancel();
            }
        }
    }
    public static void test(IntGenerator generator, int count) {
        System.out.println("Press Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < count; i++) {
            exec.execute(new EventChecker(generator,i));
        }
        exec.shutdown();
    }
    public static void test(IntGenerator generator) {
        test(generator, 10);
    }
}
