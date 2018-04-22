package com.joker.io;

import com.joker.util.PPrint;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hunter on 2017/11/1.
 * 找出所有的目录和文件
 */
public final class Directory {
    public static File[] local(File dir, final String regex) {
        return dir.listFiles(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(new File(name).getName()).matches();
            }
        });
    }

    public static File[] local(String path, final String regex) {
        return local(new File(path), regex);
    }
    //定义了内部类
    public static class TreeInfo implements Iterable<File> {
        public List<File> files = new ArrayList<>();
        public List<File> dirs = new ArrayList<>();

        public Iterator<File> iterator() {
            return files.iterator();
        }

        void addAll(TreeInfo other) {
            files.addAll(other.files);
            dirs.addAll(other.dirs);
        }

        public String toString() {
            return "dirs: " + PPrint.pformat(dirs) +
                    "\n\nfiles: " + PPrint.pformat(files);
        }
    }
    //使用内部类,默认是private
    static TreeInfo recurseDirs(File startDir, String regex) {
        TreeInfo result = new TreeInfo();
        File[] files = startDir.listFiles();
        for (File file : files) {
            if(file.isDirectory()) {
                result.dirs.add(file);
                result.addAll(recurseDirs(file, regex));
            } else {
                if(file.getName().matches(regex))
                    result.files.add(file);
            }
        }
        return result;
    }
    //使上一个方法更有普遍性,同时也避免客户端直接调用涉及具体逻辑的代码
    public static TreeInfo walk(File start, String regex) {
        return recurseDirs(start, regex);
    }
    public static TreeInfo walk(String start, String regex) {
        return recurseDirs(new File(start), regex);
    }
    public static TreeInfo walk(File start) {
        return recurseDirs(start, ".*");
    }
    public static TreeInfo walk(String start) {
        return recurseDirs(new File(start), ".*");
    }

    public static void main(String[] args) {
        if(args.length == 0)
            System.out.println(walk("."));
        else
            for(String arg : args)
                System.out.println(walk(arg));
    }
}
