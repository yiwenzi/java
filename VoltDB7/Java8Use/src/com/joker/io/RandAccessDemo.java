package com.joker.io;

import java.io.*;

/**
 * Created by hunter on 2017/10/25.
 */
public class RandAccessDemo {
    public static final String FILENAME = "D:\\workspace\\jetbrains\\java\\VoltDB7\\Java8Use\\resource\\randomaccess.txt";

    public static void main(String[] args) throws IOException {
        //RandomAccessFile file  = new RandomAccessFile(FILENAME, "rw");
        insert(FILENAME, 33, "用java做爬虫怎么样!");
    }
    //利用RandomAccessFile来实现文件任意位置的插入
    public static void insert (String fileName, long points, String insertConnt) throws IOException {
        File tmp = File.createTempFile("tmp", null);
        tmp.deleteOnExit();//在HVM退出时删除
        //打开临时文件
        FileOutputStream tmpOut = new FileOutputStream(tmp);
        FileInputStream tmpIn = new FileInputStream(tmp);
        //打开目标文件
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        raf.seek(points);
        //将插入点之后的内容保存进临时文件
        byte[] buff = new byte[1024];
        int length = 0;
        while ((length = raf.read(buff)) != -1) {
            tmpOut.write(buff, 0, length);
        }
        //重置指针位置
        raf.seek(points);
        raf.write(insertConnt.getBytes());
        //将临时文件内容写回文件
        while ((length = tmpIn.read(buff)) != -1) {
            raf.write(buff, 0, length); //不是立即写入的，当raf本身的缓冲区满了或调用close方法才会写入
        }
        raf.close();
    }
}
