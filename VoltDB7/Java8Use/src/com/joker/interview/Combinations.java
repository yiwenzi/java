package com.joker.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hunter on 2018/3/28.
 */
public class Combinations {

    public void combinations(List<Integer> selected, List<Integer> data, int n) {

        if(n == 0) {
            // output all selected elements
            for (Integer i : selected) {
                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println();
            return;
        }
        if(data.isEmpty()) {
            return;
        }
        //选择
        selected.add(data.get(0));
        combinations(selected,data.subList(1, data.size()), n - 1);
        //不选择
        selected.remove(selected.size() - 1);
        combinations(selected, data.subList(1, data.size()), n);
    }
    public static void main(String[] args) {
        Combinations combinations = new Combinations();
        combinations.combinations(new ArrayList<>(),Arrays.asList(1, 2, 3, 4), 2);
    }
}
