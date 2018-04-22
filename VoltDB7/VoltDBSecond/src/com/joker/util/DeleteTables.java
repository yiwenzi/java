package com.joker.util;

import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

/**
 * Created by hunter on 2017/12/20.
 * 多分区执行删除操作
 */
public class DeleteTables {
    public static void main(String[] args) throws Exception {
        String servers = "10.10.56.16";
        ClientConnection connection = ClientConnectionPool.get(servers, 11212);;


        ClientResponse response = null;
        VoltTable table = null;

        String sql = "delete from SADFEERANGE2";
        Long start = System.currentTimeMillis();
//        connection.callAllPartitionProcedure("DeleteTable");
//        String[] partitionKeys = GetPartitionKey.getPartitionKeys();
//        int length = partitionKeys.length;
//        Long start = System.currentTimeMillis();
//        for (int i = 0; i < length; i++) {
//            response = connection.execute("@AdHocSpForTest",sql,partitionKeys[i]);
//            if (response.getStatus() != ClientResponse.SUCCESS) {
//                System.err.println(response.getStatusString());
//                System.exit(1);
//            }
//        }
        connection.execute("@AdHoc",sql);
        Long end = System.currentTimeMillis();
        System.out.println("delete successful, usetime is: " + (end - start));
    }
}
