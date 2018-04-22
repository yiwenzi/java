package com.joker.example;

import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import com.joker.export.JDBCLoader;
import org.voltdb.client.ClientImpl;
import org.voltdb.client.VoltBulkLoader.VoltBulkLoader;
import org.voltdb.utils.CSVBulkDataLoader;
import org.voltdb.utils.CSVDataLoader;

/**
 * Created by hunter on 2017/12/22.
 */
public class GetDataFromPartition {
    public static void main(String[] args) throws Exception {
        //没有数据的表
        String target = "SADFEERANGEHISTORY";
        //获取连接
        String servers = "10.10.0.160";
        final JDBCLoader errHandler = new JDBCLoader();
        errHandler.launchErrorFlushProcessor();
        ClientConnection connection = ClientConnectionPool.get(servers, 11212);

        //JDBCLoader.configuration();

        final CSVDataLoader dataLoader = new CSVBulkDataLoader((ClientImpl) connection.client,
                target, 200, false, errHandler);
        PartitionReader reader = new PartitionReader(dataLoader, errHandler, connection.client);
        Thread readerThread = new Thread(reader);
        readerThread.setName("JDBCSourceReader");
        readerThread.setDaemon(true);

        long insertTimeStart = System.currentTimeMillis();
        //Wait for reader to finish.
        readerThread.start();
        readerThread.join();

        connection.close();

        errHandler.waitForErrorFlushComplete();

        long insertTimeEnd = System.currentTimeMillis();
        System.out.println("耗时: " + (insertTimeEnd - insertTimeStart) / 1000);
    }
}
