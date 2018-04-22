package com.joker.cache;

import java.math.BigInteger;

/**
 * Created by hunter on 2018/1/5.
 */
public class ExpensiveFunction implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        //在经过很长时间的计算后
        return new BigInteger(arg);
    }
}
