package com.joker.client;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hunter on 2017/11/30.
 * optimal TCP socket usage is attained when 50 threads share the same socket, sending
 * execution requests through it.
 */
public class ClientConnectionPool {
    //收集监控信息
    private static final ConcurrentHashMap<String, PerfCounterMap> statistics = new ConcurrentHashMap<>();
    private static final HashMap<String, ClientConnection> clientConnections = new HashMap<>();

    //私有的构造函数
    private ClientConnectionPool() {
    }

    public static ClientConnection get(String servers, int port) throws Exception {
        return get(servers.split(","), port, "", "", false, 0);
    }

    public static ClientConnection getWithRetry(String servers, int port) throws Exception {
        return getWithRetry(servers.split(","), port, "", "", false, 0);
    }

    public static ClientConnection get(String[] servers, int port) throws Exception {
        return get(servers, port, "", "", false, 0);
    }

    public static ClientConnection getWithRetry(String[] servers, int port) throws Exception {
        return getWithRetry(servers, port, "", "", false, 0);
    }

    public static ClientConnection getWithRetry(String servers, int port, String user, String password, boolean isHeavyWeight, int maxOutstandingTxns) throws Exception {
        return getWithRetry(servers.split(","), port, user, password, isHeavyWeight, maxOutstandingTxns);
    }

    public static ClientConnection get(String servers, int port, String user, String password, boolean isHeavyWeight, int maxOutstandingTxns) throws Exception {
        return get(servers.split(","), port, user, password, isHeavyWeight, maxOutstandingTxns);
    }

    /**
     * Gets a client connection to the given VoltDB server(s).
     *
     * @param servers
     * @param port
     * @param user
     * @param password
     * @param isHeavyWeight
     * @param maxOutstandingTxns
     * @return
     */
    public static ClientConnection get(String[] servers, int port, String user, String password, boolean isHeavyWeight,
                                       int maxOutstandingTxns) throws Exception {
        String clientConnectionKeyBase = getClientConnectionKeyBase(servers, port, user, password, isHeavyWeight, maxOutstandingTxns);
        String clientConnectionKey = clientConnectionKeyBase;

        synchronized (clientConnections) {
            if (!clientConnections.containsKey(clientConnectionKey)) {
                clientConnections.put(
                        clientConnectionKey,
                        //这里是因为不在一个包下？？？
                        new ClientConnection(
                                clientConnectionKeyBase,
                                clientConnectionKey,
                                servers,
                                port,
                                user,
                                password,
                                isHeavyWeight,
                                maxOutstandingTxns
                        )
                );
            }
            return clientConnections.get(clientConnectionKey).use();
        }
    }

    public static ClientConnection getWithRetry(String[] servers, int port, String user, String password, boolean isHeavyWeight,
                                                int maxOutstandingTxns) {
        ClientConnection con = null;
        System.out.println("Connecting to servers: ");
        for (String server : servers) {
            System.out.printf(" - %s:%d\n", server, port);
        }
        System.out.printf("Credentials:\n%s\nOptions:\n - Heavyweight: %s\n - MaxTxnQueue: %s\n"
                , user == "" ? " - None" : " - User: " + user + "\n - Password: ********"
                , isHeavyWeight ? "yes" : "no"
                , maxOutstandingTxns == 0 ? "(default)" : String.format("%,d", maxOutstandingTxns)
        );
        int sleep = 1000;
        while (true) {
            try {
                con = ClientConnectionPool.get(servers, port);
                break;
            } catch (Exception e) {
                System.err.printf("Connection failed - retrying in %d second(s).\n", sleep / 1000);
                try {
                    Thread.sleep(sleep);
                } catch (Exception tie) {
                }
                if (sleep < 8000)
                    sleep += sleep;
            }
        }
        System.out.println("Connected");
        return con;
    }

    public static void dispose(ClientConnection connection) {
        synchronized (clientConnections) {
            connection.dispose();
            if (connection.users == 0) {
                clientConnections.remove(connection.key);
            }
        }
    }

    public static PerfCounterMap getStatistics(ClientConnection connection) {
        return getStatistics(connection.keyBase);
    }

    public static PerfCounterMap getStatistics(String servers, int port) {
        return getStatistics(getClientConnectionKeyBase(servers.split(","), port, "", "", false, 0));
    }

    public static PerfCounterMap getStatistics(String[] servers, int port) {
        return getStatistics(getClientConnectionKeyBase(servers, port, "", "", false, 0));
    }

    public static PerfCounterMap getStatistics(String servers, int port, String user, String password, boolean isHeavyWeight, int maxOutstandingTxns) {
        return getStatistics(getClientConnectionKeyBase(servers.split(","), port, user, password, isHeavyWeight, maxOutstandingTxns));
    }

    public static PerfCounterMap getStatistics(String[] servers, int port, String user, String password, boolean isHeavyWeight, int maxOutstandingTxns) {
        return getStatistics(getClientConnectionKeyBase(servers, port, user, password, isHeavyWeight, maxOutstandingTxns));
    }

    /**
     * 主体的方法好像都不是public的
     */
    protected static PerfCounterMap getStatistics(String clientConnectionKeyBase) {
        if (!statistics.containsKey(clientConnectionKeyBase))
            statistics.put(clientConnectionKeyBase, new PerfCounterMap());
        return statistics.get(clientConnectionKeyBase);
    }

    /**
     * Generates a hash/key for a connection based on the given list of connection parameters
     *
     * @param servers
     * @param port
     * @param user
     * @param password
     * @param isHeavyWeight
     * @param maxOutstandingTxns
     * @return
     */
    private static String getClientConnectionKeyBase(String[] servers, int port, String user, String password, boolean isHeavyWeight,
                                                     int maxOutstandingTxns) {
        String clientConnectionKeyBase = user + ":" + password + "@";
        for (int i = 0; i < servers.length; i++) {
            clientConnectionKeyBase += servers[i].trim() + ",";
        }
        clientConnectionKeyBase += ":" + Integer.toString(port) + "{" + Boolean.toString(isHeavyWeight) +
                ":" + Integer.toString(maxOutstandingTxns) + "}";
        return clientConnectionKeyBase;
    }
}
