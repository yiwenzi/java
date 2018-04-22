package com.joker.export;

import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import org.voltdb.client.VoltBulkLoader.VoltBulkLoader;

/**
 * Created by hunter on 2018/1/23.
 */
public class TryBulkDataLoader {
    public static void main(String[] args) throws Exception {
        //获取连接
        String servers = "10.10.0.159";
        String target = "SADFEERANGEHISTORY";
        final JDBCLoader errHandler = new JDBCLoader();
        errHandler.launchErrorFlushProcessor();
        ClientConnection connection = ClientConnectionPool.get(servers, 11212);
        VoltBulkLoader dataLoader = connection.client.getNewBulkLoader(target, 200, null);
        dataLoader.drain();
        dataLoader.close();
    }
}
