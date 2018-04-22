package com.joker.interview;

/**
 * Created by hunter on 2018/3/30.
 */
public class BinarySearch {

    /**
     * 二分查找 [a,b) 最好使用开区间
     * @param arr
     * @param k
     * @return
     */
    public int binarySearch(int[] arr,int k ) {
        int a = 0;
        int b = arr.length;


        while (a < b ) {
            //这里的直接写法会越界
            //int m = (a + b) / 2;
            int m = a + (b - a) / 2;
            //a == b: m = a and m = b
            //b = a + 1 : m = a
            //b == a + 2 : m = a + 1
            //这里的赋值与最开始的区间有关
            if(k < arr[m]) {
                b = m;
            } else if(k > arr[m]) {
                a = m + 1;
            } else {
                return m;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        //多写几种测试用例，是几种，而不是几个
        int result = binarySearch.binarySearch(new int[]{1, 2, 10, 15, 100}, 15);
        System.out.println(result);
    }
}
