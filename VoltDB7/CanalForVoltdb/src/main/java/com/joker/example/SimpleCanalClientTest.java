package com.joker.example;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.voltdb.client.exampleutils.ClientConnection;
import org.voltdb.client.exampleutils.ClientConnectionPool;

import java.net.InetSocketAddress;

/**
 * Created by hunter on 2018/3/20.
 */
public class SimpleCanalClientTest  extends AbstractCanalClientTest {

    public SimpleCanalClientTest(String destination){
        super(destination);
    }

    public static void main(String args[]) throws Exception {
        // 根据ip，直接创建链接，无HA的功能
        String destination = "example";
        //String ip = AddressUtils.getHostIp();
        String ip = "10.10.68.43";
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, 11111),
                destination,
                "",
                "");

        //Voltdb连接
        String voltdbIp = "10.10.0.159";
        int port = 11212;
        ClientConnection connection = ClientConnectionPool.get(voltdbIp, port);
        final SimpleCanalClientTest clientTest = new SimpleCanalClientTest(destination);
        clientTest.setConnector(connector);
        clientTest.setConnection(connection);
        clientTest.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    logger.info("## stop the canal client");
                    clientTest.stop();
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
