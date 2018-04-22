package com.joker.thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by hunter on 2017/10/29.
 */
public class CallAndFuture {
    public static void main(String[] args) {
        //() -> new Random().nextInt(10);
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt(10);
            }
        };
        //它有两个构造函数，第二个好像可以接受result.
        FutureTask<Integer> future = new FutureTask<Integer>(callable);
        new Thread(future).start();

        try {
            Thread.sleep(5000);
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
