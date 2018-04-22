package com.joker.string;

import com.joker.util.BinaryFile;

import java.io.IOException;
import java.util.Formatter;
import java.util.regex.Pattern;

/**
 * Created by hunter on 2017/11/6.
 */
public class FormatterDemo {
    private double total = 0;
    private Formatter f = new Formatter(System.out);
    public static String PATH = "D:\\workspace\\jetbrains\\java\\VoltDB7\\out\\production\\Java8Use\\com\\joker\\string";
    public void printTitle() {
        f.format("%-15s %5s %10s\n", "Item", "Qty", "Price");
        f.format("%-15s %5s %10s\n", "----", "---", "-----");
    }

    public static void main(String[] args) throws IOException {
        System.out.println(format(BinaryFile.read(PATH + "\\FormatterDemo.class")));
    }

    /**
     * 将二进制以十六进制格式化输出
     * @param data
     * @return
     */
    public static String format(byte[] data) {
        StringBuilder result = new StringBuilder();
        int n = 0;
        for (byte b : data) {
            if(n % 16 == 0) //控制每行的头
                result.append(String.format("%05X: ", n));
            result.append(String.format("%02X ", b));
            n++;
            if(n % 16 == 0 ) result.append("\n"); //控制每行的尾，但是首先n++
        }
        result.append("\n");
        return result.toString();
    }
}
