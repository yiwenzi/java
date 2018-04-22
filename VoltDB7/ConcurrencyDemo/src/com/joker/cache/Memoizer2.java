package com.joker.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hunter on 2018/1/5.
 * 线程安全并且多线程可以并发地使用它
 * 但是可能会导致计算得到相同的值
 */
public class Memoizer2<A,V> implements Computable<A,V> {
    private final Map<A,V> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer2(Computable<A, V> c) {
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
