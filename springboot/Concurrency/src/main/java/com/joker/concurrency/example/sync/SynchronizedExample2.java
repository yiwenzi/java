package com.joker.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hunter on 2018/4/8.
 */
@Slf4j
public class SynchronizedExample2 {
    //修饰代码块 作用于调用对象
    public static void test1(int j) {
        synchronized (SynchronizedExample2.class) {
            for (int i = 0; i < 10; i++) {
                log.info("test1 {} - {}", j, i);
            }
        }
    }
    //修饰方法
    public static synchronized void test2() {
        for (int i = 0; i < 10; i++) {
            log.info("test2 {}", i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample2 example = new SynchronizedExample2();
        SynchronizedExample2 example2 = new SynchronizedExample2();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> example.test1(1));
        service.execute(() -> example2.test1(2));
        service.shutdown();
    }
}
