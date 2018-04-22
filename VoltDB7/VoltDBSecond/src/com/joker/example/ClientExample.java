package com.joker.example;

import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import org.voltdb.*;
import org.voltdb.VoltType;
import org.voltdb.client.ClientResponse;

/**
 * Created by hunter on 2017/12/5.
 */
public class ClientExample {
    public static void main(String[] args) throws Exception {
        //String servers = "10.10.56.16,10.10.56.17,10.10.56.18,10.10.56.19";
        String servers = "10.10.56.22";
        ClientConnection connection = ClientConnectionPool.get(servers, 11212);
//        VoltTable[] result = connection.execute("@SystemInformation", "OVERVIEW").getResults();
//        //VoltTable[] result = client.callProcedure("@SystemInformation", "DEPLOYMENT").getResults();
//        System.out.println("Information about the database cluster:");
//        for (VoltTable node : result) {
//            //System.out.println(node.toString());
//        }
        String sql = "select * from joker";
        ClientResponse result = connection.execute("@AdHoc", sql);
        VoltTable table = result.getResults()[0];
        while (table.advanceRow()){
            VoltType type = table.getColumnType(0);
            int no = (int) table.get(0,type);
            type = table.getColumnType(1);
            String name = (String) table.get(1,type);
            System.out.println("no: " + no + ", name: " + name);
            System.out.println("now index is :" + table.getActiveRowIndex());
            System.out.println(table.getBuffer().toString());

            VoltTable.ColumnInfo[] columnInfos = table.getTableSchema();
            System.out.println(columnInfos);
            System.out.println(columnInfos);
        }
        connection.close();
        //connection.getStatistics();
    }
}
