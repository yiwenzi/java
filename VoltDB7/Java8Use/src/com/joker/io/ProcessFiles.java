package com.joker.io;

import java.io.File;
import java.io.IOException;

/**
 * Created by hunter on 2017/11/2.
 */
public class ProcessFiles {
    //计算符合条件的文件大小
    public long length = 0L;

    public interface Strategy {
        void process(File file);
    }

    private Strategy strategy;
    private String ext;

    public ProcessFiles(Strategy strategy, String ext) {
        this.strategy = strategy;
        this.ext = ext;
    }

    public void start(String[] args) {
        try {
            if(args.length == 0) {
                processDirectoryTree(new File("."));
            } else {
                for (String arg : args) {
                    File fileArg = new File(arg);
                    if(fileArg.isDirectory())
                        processDirectoryTree(fileArg);
                    else {
                        if(!arg.endsWith("." + ext))
                            arg += "." + ext;
                        strategy.process(new File(arg).getCanonicalFile());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //这个方法里调用file的方法是什么
    public void processDirectoryTree(File root) throws IOException {
        for (File file : Directory.walk(
                root.getAbsolutePath(), ".*\\." + ext)) {
            length += file.length();
            strategy.process(file.getCanonicalFile());
        }
    }

    public static void main(String[] args) {
        ProcessFiles files = new ProcessFiles(new Strategy() {
            @Override
            public void process(File file) {
                //System.out.println(file);
            }
        },"java");
        files.start(args);
        System.out.println(files.length/1024/1024);
    }
}
