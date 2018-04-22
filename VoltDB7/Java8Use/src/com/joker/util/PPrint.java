package com.joker.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by hunter on 2017/11/1.
 * Pretty-printer for collections
 */
public class PPrint {
     public static String pformat(Collection<?> c) {
        if(c.size() == 0 ) return "[]";
        StringBuilder result = new StringBuilder("[");
        boolean flag = (c.size() != 1);
        for (Object element : c) {
            if(flag) {
                result.append("\n ");
            }
            result.append(element);
        }
        if(flag)
            result.append("\n");
        result.append("]");
        return result.toString();
    }

    public static void pprint(Collection<?> c) {
        System.out.println(pformat(c));
    }
    public static void pprint(Object[] c) {
        System.out.println(pformat(Arrays.asList(c)));
    }
}
