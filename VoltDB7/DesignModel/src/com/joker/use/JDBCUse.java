package com.joker.use;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by hunter on 2017/10/19.
 */
public class JDBCUse {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/joker" ;
        String username = "root" ;
        String password = "joker";
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url,username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
