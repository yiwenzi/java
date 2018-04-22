package com.joker.concurrency.example.concurrent;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by hunter on 2018/4/9.
 */
@Slf4j
public class CopyOnWriteExample {
    //请求总数
    public static int clientTotal = 5000;
    //同时并发执行的线程数
    public static int threadTotal = 200;

    public static List<Integer> list = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        //线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        //信号量
        final Semaphore semaphore = new Semaphore(threadTotal);
        //计数器闭锁
        final CountDownLatch latch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            final int number = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add(number);
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception: " + e);
                }
                latch.countDown();
            });
        }
        //去看方法的注释
        latch.await();
        executorService.shutdown();
        log.info("size:{}",list.size());
    }

    private static void add(int i) {
        list.add(i);
    }
}
