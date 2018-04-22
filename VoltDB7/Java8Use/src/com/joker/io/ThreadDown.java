package com.joker.io;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hunter on 2017/10/25.
 */
public class ThreadDown {
    //声明下载路径
    public static final String PATH = "https://download.jetbrains.com/idea/ideaIU-2017.2.5.exe";
    //线程数
    public static int threadCount = 3;

    public static void main(String[] args) {
        try {
            String fileName = PATH.substring(PATH.lastIndexOf("/") + 1);
            System.out.println("将要下载的文件为: " + fileName);
            URL url = new URL(PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                            + "application/x-shockwave-flash, application/xaml+xml, "
                            + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                            + "application/x-ms-application, application/vnd.ms-excel, "
                            + "application/vnd.ms-powerpoint, application/msword, */*");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            //connection.connect();
            //获取下载文件大小
            int fileLength = connection.getContentLength();
            //仅仅计算大小
            connection.disconnect();

            //创建下载文件
            RandomAccessFile target = new RandomAccessFile("E:\\BaiduDownload\\" + fileName, "rwd");
            System.out.println("将要下载的大小为: " + fileLength);
            target.setLength(fileLength);
            target.close();
            //不一定是平均分
            int blockSize = fileLength / threadCount;
            for (int i = 1; i <= threadCount; i++) {
                //定义下载位置
                int startPos = (i - 1) * blockSize;
                int endPos = (i * blockSize) - 1;
                if(i == threadCount){
                    endPos = fileLength;
                }
                //启动线程
                new Thread(new DownLoadThread(i, startPos, endPos, PATH)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class DownLoadThread implements Runnable {
        private int threadId;
        private int startPos;
        private int endPos;
        private String path;

        public DownLoadThread(int threadId, int startPos, int endPos, String path) {
            super();
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(PATH);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty(
                        "Accept",
                        "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                                + "application/x-shockwave-flash, application/xaml+xml, "
                                + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                                + "application/x-ms-application, application/vnd.ms-excel, "
                                + "application/vnd.ms-powerpoint, application/msword, */*");
                connection.setRequestProperty("Accept-Language", "zh-CN");
                connection.setRequestProperty("Charset", "UTF-8");
                connection.setRequestProperty("Connection", "Keep-Alive");
                String fileName = PATH.substring(PATH.lastIndexOf("/") + 1);
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                File file = new File("E:\\BaiduDownload\\" + threadId + ".txt");
                //这里应该是断点续传的代码
                if(file.exists() && file.length() > 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String saveStartPos = br.readLine();
                    if(saveStartPos != null && saveStartPos.length() > 0) {
                        startPos = Integer.parseInt(saveStartPos);
                    }
                    br.close();
                }
                //设置分段下载的头信息。  Range:做分段数据请求用的。格式: Range bytes=0-1024  或者 bytes:0-1024
                connection.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
                RandomAccessFile raf = new RandomAccessFile("E:\\BaiduDownload\\" + fileName, "rwd");
                //设置下载位置
                raf.seek(startPos);
                System.out.println("线程" + threadId + ":" + startPos + "~~" + endPos);
                //if(connection.getResponseCode() == 206) {
                InputStream inputStream = connection.getInputStream();
                byte[] bytes = new byte[1024 * 1024 * 10];
                int length = -1;
                int newPos = startPos;
                while ((length = inputStream.read(bytes)) != 1) {
                    raf.write(bytes, 0, length);
                    //将下载标记写入记录文档(断点续传这里是有问题的)
                    RandomAccessFile rr = new RandomAccessFile(file, "rwd");
                    //这样就发生了覆盖
                    rr.seek(0);
                    String savePoint = String.valueOf(newPos += length);
                    rr.write(savePoint.getBytes("UTF-8"));
                    rr.close();
                }
                inputStream.close();
                raf.close();
                System.out.println("下载完成");
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
