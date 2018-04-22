package com.joker.others;

/**
 * Created by hunter on 2017/12/6.
 */
public class DynamicArray {
    public static void main(String[] args) {
        int[] ints = {1, 2, 3, 4};
        long[] longs = {1,2,3,4};
        Integer[] integers = {1, 2, 3, 4};
        String[] array = new String[]{"1", "2", "3"};
        Other.main(array);
        System.out.println(array.getClass().getName());
        System.out.println(ints.getClass());
        System.out.println(integers.getClass());
        System.out.println(longs.getClass());
    }
}

class Other {
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.print(arg + " ");
        }
    }
}
