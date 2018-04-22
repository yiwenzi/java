package com.joker.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by hunter on 2017/12/12.
 */
public class JdbcDemo {
    public static void main(String[] args) {
        String driver = "org.voltdb.jdbc.Driver";
        String url = "jdbc:voltdb://10.10.56.22:11212,10.10.56.23:11212";
        String sql = "select name from joker";
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url);
            Statement query = conn.createStatement();
            ResultSet results = query.executeQuery(sql);
            while (results.next()){
                System.out.println("Name is: " + results.getString(1));
            }
            //调用存储过程
            query.close();
            results.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
