package com.joker.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hunter on 2018/1/5.
 * 线程安全
 * 但是每次只能一个线程能够执行compute，如果一个线程正在计算，则其他线程可能被阻塞很长时间。
 */
public class Memoizer1<A,V> implements Computable<A,V> {
    private final Map<A,V> cache = new HashMap<>();
    private final Computable<A,V> c;

    public Memoizer1(Computable<A,V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
