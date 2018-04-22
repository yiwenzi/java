package com.joker.io;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by hunter on 2017/10/24.
 */
public class MultiDown {
    public static final String FILENAME = "D:\\workspace\\jetbrains\\java\\VoltDB7\\Java8Use\\resource\\randomfile.txt";
    public static void main(String[] args) throws IOException {
        //首先创建文件
            /*RandomAccessFile randomAccessFile = new RandomAccessFile(FILENAME, "rw");
            randomAccessFile.setLength(80);
            randomAccessFile.close();*/

        // 所要写入的文件内容
        char c = '哈'; //它为什么可以存储中文
        System.out.println(c);
        for (int i = 0; i < 6; i++) {
            String str = "第" + (i+1) + "个字符串";
            System.out.println("di".getBytes().length + " " +
                    "第".getBytes().length + " " +
                    "1".getBytes().length + " " +
                    "123".getBytes().length);
            //System.out.println(str.getBytes().length); //一个汉字三个字节
            new FileWriteThread(16 * 2, str.getBytes()).start();
        }
        System.out.println("写入完成");
        RandomAccessFile randomAccessFile = new RandomAccessFile(FILENAME, "rw");
        //randomAccessFile.setLength(80);
        System.out.println(randomAccessFile.length());
        randomAccessFile.close();
    }
    static class FileWriteThread extends Thread {
        private int skip;
        private byte[] content;

        public FileWriteThread(int skip, byte[] content){
            this.skip = skip;
            this.content = content;
        }
        @Override
        public void run() {
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(FILENAME, "rw");
                randomAccessFile.seek(skip);
                randomAccessFile.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
