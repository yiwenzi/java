package com.joker.util;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hunter on 2017/11/29.
 */
public class ConnectHelper {
    // 50 threads share the same socket
    private static final int THREADS_PER_CLIENT = 50;

    //存现在正在使用的连接 -- 个人觉得没意义
    private static HashMap<Long,ClientConnection> clientMapping = new HashMap<>();
    //可以使用的连接 -- 也没有意义
    private static ClientConnection activeConnection = null;
    //存放所有的初始化连接
    private static List<ClientConnection> clientConnectionPool = new ArrayList<>();

    //每个节点上的连接数
    private static final int clientCount = 15;

    public synchronized static Client createConnection(Long threadId, String servers, String user,
                                                       String psw, int rateLimit) throws InterruptedException {
        if(clientConnectionPool.size() == 0) {
            initClientConnectionPool(servers, user, psw,rateLimit);
        }
        ClientConnection conn = clientMapping.get(threadId);
        //在map里存在这个线程的ID
        if(conn != null && conn.m_client != null) {
            return conn.m_client;
        }
        return null;
    }

    private static void initClientConnectionPool(String servers, String user, String psw, int rateLimit) throws InterruptedException {
        ClientConfig config = new ClientConfig(user,psw);
        config.setHeavyweight(true);
        config.setMaxTransactionsPerSecond(rateLimit);
        config.setTopologyChangeAware(true);
        for (int i = 0; i < clientCount; i++) {
            //使创建的连接均匀存放在list中
            connect(config, servers);
        }
    }

    private static void connectToOneServerWithRetry(final Client client, String server) {
        int sleep = 100;
        while (true) {
            try {
                client.createConnection(server);
                break;
            } catch (IOException e) {
                System.err.printf("Connection failed - retrying in %d second(s).\n", sleep / 1000);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if(sleep < 8000) {
                    sleep += sleep;
                }
            }
        }
    }

    private static void connect(ClientConfig config,String servers) throws InterruptedException {
        String[] serverArray = servers.split(",");
        final CountDownLatch connections = new CountDownLatch(serverArray.length);
        for (final String server : serverArray) {
            new Thread(() -> {
                //创建连接
                Client client = ClientFactory.createClient(config);
                //将连接指定到节点
                connectToOneServerWithRetry(client, server);
                //将创建好的连接放到池子中
                ClientConnection clientConnection = new ClientConnection(client);
                clientConnectionPool.add(clientConnection);

                connections.countDown();
            }).start();
        }
        //block until all have connected
        connections.await();
    }

    public static void disconnect(Long clientId) {
        ClientConnection connection = clientMapping.get(clientId);
        clientMapping.remove(clientId);
        if(connection != null) {
            connection.disconnect();
        }
    }
    private static class ClientConnection {
        private Client m_client;
        //每50个线程共用一个client,也就是一个socket
        private AtomicInteger m_connectionCount;

        ClientConnection(Client client) {
            this.m_client = client;
            this.m_connectionCount = new AtomicInteger(1);
        }
        void connect() {
            //这里为什么没接到返回结果呢？返回的是自身？
            m_connectionCount.incrementAndGet();
        }
        void disconnect() {
            int count = m_connectionCount.decrementAndGet();
            //断开连接
            if(count >= 0 && m_client != null && !clientConnectionPool.contains(this)) {

            }
        }
    }
}
