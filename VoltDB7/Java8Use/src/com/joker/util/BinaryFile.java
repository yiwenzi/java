package com.joker.util;

import java.io.*;

/**
 * Created by hunter on 2017/11/6.
 */
public class BinaryFile {
    public static byte[] read(File file) throws IOException {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = null;
        try {
            bytes = new byte[bf.available()];
            bf.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bf.close();
        }
        return bytes;
    }
    public static byte[] read(String file) throws IOException {
        return read(new File(file).getAbsoluteFile());
    }
}
