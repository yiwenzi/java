package com.joker.concurrency.example.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.joker.concurrency.annotaions.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hunter on 2018/4/9.
 */
@Slf4j
@ThreadSafe
public class ImmutableExample3 {
    private static final ImmutableList list = ImmutableList.of(1, 2, 3);
    private static final ImmutableSet set = ImmutableSet.copyOf(list);
    private static final ImmutableMap<Integer, Integer> maps =
            ImmutableMap.<Integer,Integer>builder().put(1, 2).put(2, 3).build();

    public static void main(String[] args) {
       list.add(4);
        set.add(4);
    }
}
