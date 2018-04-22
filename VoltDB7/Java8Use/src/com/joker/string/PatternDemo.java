package com.joker.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hunter on 2017/11/6.
 * 正则表达式 查找 提取 替换
 */
public class PatternDemo {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher("22bb33");
        while (matcher.find()) {
            System.out.println(matcher.toMatchResult()); //这是啥
            System.out.println(matcher.group());
            System.out.println(matcher.start());
            System.out.println(matcher.end());
        }
//        matcher.hasAnchoringBounds();
//        matcher.toMatchResult();
//        matcher.region();
    }

//    public static void theReplacement()
}
