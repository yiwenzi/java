package com.joker.example;


import com.joker.export.JDBCLoader;

import java.io.IOException;

/**
 * Created by hunter on 2018/1/30.
 * voltdb的jdbc模块游标有问题
 */
public class Voltdb4VoltdbByJDBC {
    public static void main(String[] args) throws IOException, InterruptedException {
        String target = "saaagentcontracthis2";
        String source = "saaagentcontracthis";
        String servers = "--servers=10.10.0.159";
        String url = "--jdbcurl=jdbc:voltdb://10.10.0.160:11212";
        String[] params = new String[] {target,
                "--jdbcdriver=org.voltdb.jdbc.Driver",
                servers,
                url,
                "--jdbctable=" + source,
                "--jdbcuser=",
                "--jdbcpassword=",
                "--port=11212"};
        long start = System.currentTimeMillis();
        JDBCLoader.main(params);
        long end = System.currentTimeMillis();
        long useTime = (end - start) / 1000;
        System.out.println("table: " + target + "导入完成，耗时: " + useTime + "s");
    }
}
