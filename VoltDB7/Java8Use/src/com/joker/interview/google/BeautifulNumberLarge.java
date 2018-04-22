package com.joker.interview.google;

import java.io.InputStreamReader;
import java.util.LongSummaryStatistics;
import java.util.Scanner;

/**
 * Created by hunter on 2018/3/11.
 */
public class BeautifulNumberLarge {
    public static void main(String[] args) {
        Scanner in = new Scanner(new InputStreamReader(System.in));
        int cases = in.nextInt();
        for (int i = 1; i <= cases; i++) {
            long n = in.nextLong();
            System.out.println("Case #" + i + ": " + beautiful(n));
        }
    }

    private static long beautiful(long n) {
       for(int bits = 64; bits >=2 ; bits--) {
           long radix = getRadix(n, bits);
           if(radix != -1) {
               return radix;
           }
       }
        //should not reach here
        //turn n - 1; 返回这个是不对的。总会返回11
        throw new IllegalStateException("Should not reach here");
    }

    /**
     * Gets radix so that n is 111...1(bits 1 in total) in that radix
     * @return the radix. -1 if there's no such radix
     */
    private static long getRadix(long n, int bits) {
        long minRadix = 2;
        long maxRadix = n;
        while (minRadix < maxRadix) {
            //二分法查找
            long m = minRadix + (maxRadix - minRadix) / 2; //避免越界
            long t = convert(m, bits);
            if(t == n) {
                return m;
            } else if(t < n) {
                minRadix = m + 1;
            } else {
                maxRadix = m;
            }
        }
        return -1;
    }

    /**
     * returns the value of 111...1(bits 1 in total) in radix
     */
    private static long convert(long radix, int bits) {
        long component = 1;
        long sum = 0;
        for (int i = 0; i < bits; i++) {
            if(Long.MAX_VALUE - sum < component) { //溢出
                sum = Long.MAX_VALUE;
            } else {
                sum += component;
            }
            if(Long.MAX_VALUE / component < radix) {
                component = Long.MAX_VALUE;
            } else {
                component *= radix;
            }
        }
        return sum;
    }

}
