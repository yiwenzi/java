package com.joker.concurrency.example.syncContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hunter on 2018/4/9.
 * 遍历集合时进行删除操作
 */
public class ArrayListDelete {
    // java.util.ConcurrentModificationException
    public static void delete1(List<Integer> list) {
        for (Integer i : list) {
            if(i.equals(3))
                list.remove(i);
        }
    }

    public static void delete2(List<Integer> list) {
        Iterator<Integer> itr = list.iterator();
        if(itr.hasNext()) {
            Integer i = itr.next();
            if (i.equals(3)) {
                itr.remove();
            }
        }
    }

    public static void delete3(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(1)) {
                list.remove(i);
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        delete3(list);
    }
}
