package com.joker.client;

import org.voltdb.client.*;
import org.voltdb.client.exampleutils.PerfCounter;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by hunter on 2017/11/30.
 */
public class ClientConnection implements Closeable {
    private final PerfCounterMap statictics;
    public final Client client;
    //这些key是用来计算唯一性的?
    protected final String keyBase;
    protected final String key;
    /**
     * The number of active users on the connection.  Used and managed by the pool to determine when a
     * specific {@link Client} wrapper has reached capacity (and a new one should be created).
     */
    protected short users;
    /**
     * 虽然操作可能会在客户端超时，但从技术上讲，一旦提交到数据库集群，调用就不能被取消！
     */
    public long defaultAsyncTimeout = 60000;

    /**
     * Creates a new native client wrapper from the given parameters (internal use only)
     *
     * @param clientConnectionKeyBase
     * @param clientConnectionKey
     * @param servers
     * @param port
     * @param user
     * @param password
     * @param isHeavyWeight
     * @param maxOutstandingTxns
     */
    protected ClientConnection(String clientConnectionKeyBase, String clientConnectionKey, String servers[],
                               int port, String user, String password, boolean isHeavyWeight,
                               int maxOutstandingTxns) throws IOException {
        this.keyBase = clientConnectionKeyBase;
        this.key = clientConnectionKey;
        this.statictics = ClientConnectionPool.getStatistics(clientConnectionKeyBase);

        //create configuration
        final ClientConfig config = new ClientConfig(user, password);
        config.setHeavyweight(isHeavyWeight);
        //此选项为true，则即使只连上集群中的一个节点，该client也会在所有的节点中建立连接。
        config.setTopologyChangeAware(true);
        if (maxOutstandingTxns > 0)
            config.setMaxOutstandingTxns(maxOutstandingTxns);
        //create client
        final Client client = ClientFactory.createClient(config);
        //create ClientConnections
        for (String server : servers) {
            if (server.trim().length() > 0)
                //这个client不仅仅是代表连接到一个节点，它代表的是连接到所有的节点。
                client.createConnection(server.trim(), port);
        }
//        //自定义区
//        List<InetSocketAddress> result = client.getConnectedHostList();
//        System.out.println("Client连接的节点为：");
//        for (InetSocketAddress inetSocketAddress : result) {
//            System.out.println(inetSocketAddress.toString());
//        }
//
//        System.out.println("buildString" + client.getBuildString());
        this.client = client;
        this.users = 0;
    }

    /**
     * Used by the pool to indicate a new thread/user is using a specific connection,
     * helping the pool determine when new connections need to be created.
     *
     * @return
     */
    protected ClientConnection use() { //50的限制来由pool限制
        this.users++;
        return this;
    }

    protected void dispose() {
        this.users--;
        if (this.users == 0) {
            try {
                //Shutdown the client closing all network connections and release all memory resources
                //后期优化这里可以不释放
                this.client.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void close() throws IOException {
        ClientConnectionPool.dispose(this);
    }

    /**
     * Executes a procedure synchronously and returns the result to the caller
     *
     * @param procedure
     * @param parameters
     * @return
     * @throws Exception
     */
    public ClientResponse execute(String procedure, Object... parameters) throws Exception {
        long start = System.currentTimeMillis();
        try {
            ClientResponse response = this.client.callProcedure(procedure, parameters);
            statictics.update(procedure, response);
            return response;
        } catch (ProcCallException e) {
            statictics.update(procedure, System.currentTimeMillis() - start, false);
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public ClientResponseWithPartitionKey[] callAllPartitionProcedure(String procedureName, Object... params)
            throws IOException, NoConnectionsException, ProcCallException {
        return client.callAllPartitionProcedure(procedureName, params);
    }

    /**
     * 异步调用的监控记录
     */
    private static class TrackingCallback implements ProcedureCallback {
        private final ClientConnection owner;
        private final String procedure;
        private final ProcedureCallback userCallback;

        public TrackingCallback(ClientConnection owner, String procedure, ProcedureCallback userCallback) {
            this.owner = owner;
            this.procedure = procedure;
            this.userCallback = userCallback;
        }

        @Override
        public void clientCallback(ClientResponse clientResponse) throws Exception {
            this.owner.statictics.update(this.procedure, clientResponse);
            if(this.userCallback != null) {
                this.userCallback.clientCallback(clientResponse);
            }
        }
    }

    /**
     * 普通的异步调用，response的处理在callback里
     * @param callback
     * @param procedure
     * @param parameters
     * @return
     * @throws IOException
     */
    public boolean executeAsync(ProcedureCallback callback, String procedure, Object... parameters) throws IOException {
        return this.client.callProcedure(new TrackingCallback(this, procedure, callback), procedure, parameters);
    }

    /**
     *
     * @param procedure
     * @param parameters
     * @return
     */
    public Future<ClientResponse> executeAsync(String procedure,Object... parameters) throws IOException {
        final ExecutionFuture future = new ExecutionFuture(defaultAsyncTimeout);
        this.client.callProcedure(
                new TrackingCallback(this, procedure, new ProcedureCallback() {
                    final ExecutionFuture result;
                    {
                        this.result = future; //这是个匿名内部类，所有构造函数没有，直接使用代码块。
                    }
                    @Override
                    public void clientCallback(ClientResponse clientResponse) throws Exception {
                        future.set(clientResponse);
                    }
                }),procedure,parameters
        );
        return future;
    }

    public PerfCounterMap getStatistics() {
        return ClientConnectionPool.getStatistics(this);
    }

    public PerfCounter getStatistics(String procedure) {
        return ClientConnectionPool.getStatistics(this).get(procedure);
    }

    public PerfCounter getStatistics(String... procedures) {
        PerfCounterMap map = ClientConnectionPool.getStatistics(this);
        PerfCounter result = new PerfCounter(false);
        for (String procedure : procedures)
            result.merge(map.get(procedure));
        return result;
    }

    /**
     * Save statistics to a CSV file.
     *
     * @param file File path
     * @throws IOException
     */
    public void saveStatistics(String file) throws IOException {
        if (file != null && !file.trim().isEmpty()) {
            FileWriter fw = new FileWriter(file);
            fw.write(getStatistics().toRawString(','));
            fw.flush();
            fw.close();
        }
    }

    /**
     * Block the current thread until all queued stored procedure invocations have received responses
     * or there are no more connections to the cluster
     * @throws NoConnectionsException
     * @throws InterruptedException
     * @see Client#drain()
     */
    public void drain() throws NoConnectionsException, InterruptedException
    {
        client.drain();
    }

    /**
     * Blocks the current thread until there is no more backpressure or there are no more connections
     * to the database
     * @throws InterruptedException
     */
    public void backpressureBarrier() throws InterruptedException
    {
        client.backpressureBarrier();
    }

    /**
     * Synchronously invokes UpdateApplicationCatalog procedure. Blocks until a
     * result is available. A {@link ProcCallException} is thrown if the
     * response is anything other then success.
     *
     * @param catalogPath Path to the catalog jar file.
     * @param deploymentPath Path to the deployment file
     * @return array of VoltTable results
     * @throws IOException If the files cannot be serialized
     * @throws ProcCallException
     */
    public ClientResponse updateApplicationCatalog(File catalogPath, File deploymentPath)
            throws IOException, ProcCallException
    {
        return client.updateApplicationCatalog(catalogPath, deploymentPath);
    }
}
