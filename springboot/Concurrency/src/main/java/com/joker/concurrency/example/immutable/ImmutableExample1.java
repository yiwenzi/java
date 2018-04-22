package com.joker.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.joker.concurrency.annotaions.NotThreadSafe;

import java.util.Map;

/**
 * Created by hunter on 2018/4/9.
 */
@NotThreadSafe
public class ImmutableExample1 {
    private final static Integer a = 1;
    private final static String b = "2";
    private static final Map<Integer, Integer> maps = Maps.newHashMap();

    static {
        maps.put(1,2);
        maps.put(3,4);
        maps.put(5,6);
    }

    public static void main(String[] args) {
        //a = 2;
        //b = "123";
       // maps = Maps.newHashMap();
        maps.put(1, 4);
    }
}
