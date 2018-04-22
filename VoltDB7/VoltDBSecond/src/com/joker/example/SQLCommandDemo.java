package com.joker.example;

import org.voltdb.utils.SQLCommand;

/**
 * Created by hunter on 2018/2/7.
 */
public class SQLCommandDemo {
    public static void main(String[] args) {
        String[] array = {"--port=11212", "--servers=10.10.0.160"};
//                "--query=select * from prpdrisk limit 10;"};
        SQLCommand.main(array);
    }
}
