package com.joker.cache;

/**
 * Created by hunter on 2018/1/5.
 */
public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
