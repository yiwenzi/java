package com.joker.io;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hunter on 2017/11/1.
 * IO流的典型使用方法
 */
public class Summary {
    public static final String PATH = "D:\\workspace\\jetbrains\\java\\VoltDB7\\Java8Use\\src\\com\\joker\\io\\Summary.java";
    public static final String PATH2 = "C:\\Users\\hunter\\Desktop\\Summary.out";
    public static final String PATH3 = "C:\\Users\\hunter\\Desktop\\Data.txt";
    public static final String PATH4 = "C:\\Users\\hunter\\Desktop\\table2.txt";
    public static void main(String[] args) throws IOException {
        //memoryInput();
        //formattedMemoryInput();
        //basicOutput();
        //storingAndRecoveringFile();
        //changIO();
        //getHashString(PATH4);
        getArray(PATH4);
    }
    public static void getArray(String fileNamme) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileNamme));
        String s;
        while ((s = in.readLine()) != null) {
            System.out.println("\"" + s.toLowerCase().trim() + "\",");
        }
        in.close();
    }
    //流跟字符串是不一样的
    public static void streamAndString() throws IOException {
        BufferedReader input = new BufferedReader(new StringReader("Sir Robin of joker 123"));
        String name = input.readLine();
    }
    public static void getHashString(String fileNamme) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileNamme));
        Set<String> result = new HashSet<>();
        String s;
        while ((s = in.readLine()) != null) {
            result.add(s.trim().toLowerCase());
        }
        in.close();
        int count = 0;
        for (String table : result) {
            System.out.println(table);
            count++;
        }
        System.out.println(count);
    }
    //缓冲输入文件
    public static String read(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null) {
            sb.append(s).append("\n");
        }
        in.close();
        return sb.toString();
    }
    //从内存输入
    public static void memoryInput() throws IOException {
        StringReader in = new StringReader(read(PATH));
        int c;
        while ((c = in.read()) != -1) {
            System.out.println((char)c);
        }
    }
    //格式化内存输入
    public static void formattedMemoryInput() throws IOException {
        try {
            DataInputStream in = new DataInputStream(
                    new ByteArrayInputStream(read(PATH).getBytes()));
            while (true) {
                System.out.println((char)in.readByte());
            }
        } catch (EOFException e) {//好好的利用Exception,使用异常进行流程控制，被认为是对
            //异常特性的错误使用。
            System.err.println("End of stream");
        }
    }
    //基本的文件输出
    public static void basicOutput() throws IOException {
        BufferedReader in = new BufferedReader(
                new StringReader(read(PATH)));
        //new PrintWriter(String fileName) 省去了每次创建out时创建装饰器的部分
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(PATH2)));
        int lineNumber = 1;
        String s;
        while ((s = in.readLine()) != null) {
            out.println(lineNumber++ + ": " + s);
        }
        //若没有close，那么缓冲区内容不会被刷新清空。
        out.close();
        System.out.println(read(PATH2));
    }
    //存储和恢复数据（平台无关化）
    public static void storingAndRecoveringFile() throws IOException {
        DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(PATH3)));
        out.writeDouble(3.14159);
        out.writeUTF("That was pi");
        out.writeDouble(1.41413);
        out.writeUTF("Square root of 2");
        out.close();

        DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(PATH3)));
        System.out.println(in.readDouble());
        System.out.println(in.readUTF());
        System.out.println(in.readDouble());
        System.out.println(in.readUTF());
    }
    //标准IO重定向
    public static void changIO() throws IOException {
        PrintStream console = System.out;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(PATH));
        PrintStream out = new PrintStream(
                new BufferedOutputStream(new FileOutputStream(PATH3)));
        System.setIn(in);
        System.setErr(out);
        System.setOut(out);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = br.readLine()) != null) {
            System.out.println(s);
        }
        br.close();
        out.close();
        in.close();
        System.setOut(console);
        System.setIn(System.in);
    }
}
