package com.joker.concurrency.example.threadlocal;

/**
 * Created by hunter on 2018/4/9.
 */
public class RequestHolder {
    private final static ThreadLocal<Long> requestHolder = new ThreadLocal<>();

    public static void add(Long id) {
        requestHolder.set(id);
    }

    //通过线程id去取
    public static Long getId() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
    public static void main(String[] args) {

    }
}
