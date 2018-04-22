package com.joker.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.voltdb.client.exampleutils.ClientConnection;
import org.voltdb.client.exampleutils.ClientConnectionPool;

import java.net.InetSocketAddress;

/**
 * Created by hunter on 2018/3/22.
 */
public class SimpleCanalClient extends AbstractCanalClient {
    public SimpleCanalClient(String destination, CanalConnector connector,ClientConnection connection){
        super(destination,connector,connection);
    }
    public static void main(String[] args) throws Exception {
        // canal 根据ip，直接创建链接，无HA的功能
        String destination = "example";
        //String ip = AddressUtils.getHostIp();
        String ip = "10.10.68.43";
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, 11111),
                destination,
                "",
                "");
        //voltdb
        String voltdbIp = "10.10.0.159";
        int port = 11212;
        ClientConnection connection = ClientConnectionPool.get(voltdbIp, port);

        final SimpleCanalClient canalClient = new SimpleCanalClient(destination,connector,connection);
        canalClient.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    logger.info("## stop the canal client");
                    canalClient.stop();
                    logger.info("## stop the voltdb client");
                    connection.close();
                } catch (Throwable e) {
                    logger.warn("##something goes wrong when stopping canal:", e);
                } finally {
                    logger.info("## canal client is down.");
                    logger.info("## voltdb client is down.");
                }
            }

        });
    }
}
