package com.joker.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by hunter on 2017/11/1.
 */
public class DirList {
    public static void main(String[] args) {
        File path = new File(".");
        String[] list;
        if(args.length ==0) {
            list = path.list();
        } else {
            //匿名内部类可以当作方法的返回结果，也可以当作方法的参数
            //list = path.list(new DirFilter(args[0]));
            list = path.list(new FilenameFilter() {
                //省去了构造函数
                private Pattern pattern = Pattern.compile(args[0]);
                @Override
                public boolean accept(File dir, String name) {
                    return pattern.matcher(name).matches();
                }
            });
        }
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String s : list) {
            System.out.println(s);
        }
    }
    //匿名内部类当作方法的返回参数，返回的是new类名的对象
    public static FilenameFilter filter(String regex) {
        return new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        };
    }
}
//可以对目录进行筛选,目录也是一种文件路径。使用的是策略模式
class DirFilter implements FilenameFilter {
    private Pattern pattern;

    public DirFilter(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}