package com.joker.cache;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by hunter on 2018/1/5.
 * 线程安全
 */
public class Memoizer3<A,V> implements Computable<A,V> {
    private final Map<A,Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if(f == null) {
            Callable<V> eval = () -> c.compute(arg);
            FutureTask<V> ft = new FutureTask<>(eval);
            f = ft;
            cache.put(arg,ft);
            ft.run();
        }

        try {
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
