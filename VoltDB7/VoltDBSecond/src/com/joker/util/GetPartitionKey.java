package com.joker.util;

import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import java.security.SecureRandom;

/**
 * Created by hunter on 2017/12/20.
 */
public class GetPartitionKey {
    private static String[] array;
    private static ClientConnection connection;
    private static String servers = "10.10.56.16";

    private static ClientResponse response = null;
    private static VoltTable table = null;

    public static String getPartitionKey() {
        SecureRandom random = null;
        try {
            array = getPartitionKeys();
            int length = array.length;
            random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(array.toString().getBytes());
            int index = random.nextInt(length);
            return array[index];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "123";
    }

    public static String[] getPartitionKeys() {
        try {
            if (array == null) {
                synchronized (GetPartitionKey.class) {
                    if (array == null) { // 双重检测
                        connection = ClientConnectionPool.get(servers, 11212);
                        response = connection.execute("@GetPartitionKeys", "VARCHAR");
                        if (response.getStatus() != ClientResponse.SUCCESS) {
                            System.err.println(response.getStatusString());
                            return null;
                        }
                        table = response.getResults()[0];
                        int size = table.getRowCount();
                        array = new String[size];
                        while (table.advanceRow()) {
                            int index = table.getActiveRowIndex();
                            array[index] = table.getString(1);
                        }
                        connection.close();
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
}
