package com.joker.interview.google;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by hunter on 2018/3/11.
 */
public class BeautifulNumber {
    public static void main(String[] args) {
//        Scanner in = new Scanner(new InputStreamReader(System.in));
//        int cases = in.nextInt();
//        for (int i = 1; i <= cases; i++) {
//            int n = in.nextInt();
//            System.out.println("Case #" + i + ": " + beautiful(n));
//        }
        System.out.println(toBinary(8));

    }

    private static String  toBinary(int i) {
        StringBuffer sb = new StringBuffer();
        while (i != 0){
            sb.append(i % 2);
            i /= 2;
        }
        return sb.reverse().toString();
    }

    private static int beautiful(int n) {
        for(int radix = 2; radix < n; radix++){
            if(isBeautiful(n,radix)) {
                return radix;
            }
        }
        return n - 1; // n在n-1的进制下总会返回11
    }

    private static boolean isBeautiful(int n, int radix) {
        while (n > 0) {
            int bit = n % radix;
            if(bit != 1) {
                return false;
            }
            n /= radix;
        }
        return true;
    }
}
