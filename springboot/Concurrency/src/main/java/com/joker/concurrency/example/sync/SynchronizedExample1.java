package com.joker.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hunter on 2018/4/8.
 */
@Slf4j
public class SynchronizedExample1 {
    //修饰代码块 作用于调用对象
    public void test1(int j) {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                log.info("test1 {} - {}", j, i);
            }
        }
    }
    //修饰方法
    public synchronized void test2() {
        for (int i = 0; i < 10; i++) {
            log.info("test2 {}", i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample1 example = new SynchronizedExample1();
        SynchronizedExample1 example2 = new SynchronizedExample1();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> example.test1(1));
        service.execute(() -> example2.test1(2));
        service.shutdown();
    }
}
