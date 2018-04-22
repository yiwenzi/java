package com.joker.concurrency.example.immutable;

import com.google.common.collect.Maps;
import com.joker.concurrency.annotaions.NotThreadSafe;
import com.joker.concurrency.annotaions.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hunter on 2018/4/9.
 */
@Slf4j
@ThreadSafe
public class ImmutableExample2 {
    private static  Map<Integer, Integer> maps = Maps.newHashMap();

    static {
        maps.put(1,2);
        maps.put(3,4);
        maps.put(5,6);
        //去看看源码
        maps = Collections.unmodifiableMap(maps);
    }

    public static void main(String[] args) {
        //a = 2;
        //b = "123";
       // maps = Maps.newHashMap();
        maps.put(1, 4);//运行时异常
        log.info("{}",maps.get(1));
    }
}
