package com.joker.io;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hunter on 2017/10/27.
 */
public class SearchAndWrite {
    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\hunter\\Desktop\\chen.txt";
        String path2 = "C:\\Users\\hunter\\Desktop\\chen2.txt";
        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File file = new File(path);
        File file2 = new File(path2);
        if(!file.exists()) {
            file.createNewFile();
        }
        if(!file2.exists()) {
            file2.createNewFile();
        }
        oneFileWithRandom(file);

       /* RandomAccessFile raf = new RandomAccessFile(file, "rws");
        System.out.println(raf.length());
        raf.close();*/
        /*String str = "";
        long length = 0;
        int count = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((str = br.readLine()) != null) {
           length += str.getBytes("UTF-8").length;
            count++;
        }
        System.out.println(length);
        br.close();
        System.out.println(count);*/
    }
    public static void oneFileWithRandom(File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "rws");
        //按行读入,会抹去末尾的换行符
        String str = "";
        long length = 0L;
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((str = br.readLine()) != null) {
            if(str.contains("UTC")) {
                //设定写入的位置
                raf.seek(length);
                str = getFinalStr(str);
                //这里实验写入的各种形式
                raf.write(str.getBytes("UTF-8"));
                //raf.writeBytes(str); 已废弃
                //raf.writeChars(str);
                //raf.writeUTF(str);
            }
            //readLine忽略了换行符号,这样的的话严格来说，readLine读的并不是一行完整的
            //文本，而是去掉了换行符;raf却没有忽略。所以这里每次都要加2,seek中的参数是
            //从0开始的，所以length本身就具有下一位的意思
            length += str.getBytes("UTF-8").length + 2;
        }
        br.close();
        raf.close();
    }
    public static void twoFile(File file, File file2) throws IOException {
        //按行读入,会抹去末尾的换行符
        String str = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
        while ((str = br.readLine()) != null) {
            if(str.contains("UTC")) {
                str = getFinalStr(str);
            }
            bw.write(str + "\n");
        }
        br.close();
        bw.close();
    }
    public static String getFinalStr(String target){
        StringBuilder sb = new StringBuilder();
        int position = target.indexOf("UTC");
        //得到日期的起始位置
        int dateStart = position - 22;
        //获得完整的时间字符串"2018-12-13 16:00:00"
        String s = target.substring(dateStart, position - 1);
        //拼接新字符串
        //拼接时间之前的字符串（包括时间字段）
        sb.append(target.substring(0,dateStart)).append(getCSTStr(s));
        //拼接时间之后的字符串

        sb.append(" CST").append(target.substring(position + 3));
        return sb.toString();
    }
    public static String getCSTStr(String dateString){
        String result = "";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(dateString);
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            result = format.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
