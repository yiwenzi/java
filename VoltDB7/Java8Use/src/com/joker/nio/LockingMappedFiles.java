package com.joker.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by hunter on 2017/11/3.
 */
public class LockingMappedFiles {
    static final int LENGTH = 0x8FFFFFF; // 128 MB
    static FileChannel fc;
    public static final String PATH = "C:\\Users\\hunter\\Desktop\\Data2.txt";
    public static void main(String[] args) throws Exception {
        fc = new RandomAccessFile(PATH, "rw").getChannel();
        MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, LENGTH);
        for(int i = 0; i < LENGTH; i++)
            out.put((byte)'x');
        new LockAndModify(out, 0, LENGTH / 3);
        new LockAndModify(out, LENGTH/2, LENGTH/2 + LENGTH/4);
    }

    private static class LockAndModify extends Thread {
        private ByteBuffer buff;
        private int start, end;

        LockAndModify(ByteBuffer mbb, int start, int end) {
            this.start = start;
            this.end = end;
            //这里的顺序不能动，position的值不能大于limit,所以不能先设置position
            mbb.limit(end);
            mbb.position(start);
            buff = mbb.slice();
            start();
        }
        @Override
        public void run() {
            try {
                //获得通道的锁，不能获得缓冲区的锁
                FileLock fl = fc.lock(start, end, false);
                System.out.println("Locked: " + start + " to " + end);
                while (buff.position() < buff.limit() - 1) {
                    buff.put((byte) (buff.get() + 1));
                }
                fl.release();
                System.out.println("Released: " + start + " to " + end);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
