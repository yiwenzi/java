package com.joker.thread;

/**
 * Created by hunter on 2017/11/8.
 */
public abstract class IntGenerator {
    private volatile boolean canceled = false;
    public abstract int next();
    public void cancel() {canceled = true;}
    public boolean isCanceled() { return canceled; }
}
