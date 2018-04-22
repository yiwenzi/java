package com.joker.cache;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by hunter on 2018/1/5.
 * 线程安全
 */
public class Memoizer4<A,V> implements Computable<A,V> {
    private final Map<A,Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer4(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if(f == null) {
                Callable<V> eval = () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, ft);
                if( f == null) {
                    f = ft;
                    ft.run();
                }
            }

            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
