package com.joker.util;

import org.voltdb.client.*;

import java.io.IOException;

/**
 * Created by hunter on 2017/11/10.
 */
public class ClientConn {
    public static void main(String[] args) {
        Client client = getClient();
        boolean response = false;
        try {
            //new MyCallBack()调用后就将控制权交给了主线程
            //存储过程中发生的错误也会传递到异步调用里。
            response = client.callProcedure(new MyCallBack(), "@GetPartitionKeys", "VARCHAR");
            client.callAllPartitionProcedure("DefinePartition","00000000");
        } catch (IOException | ProcCallException e) {
            e.printStackTrace();
        } finally {
            if(client != null) {
                try {
                    //暂停主进程，直到所有的异步调用都成功。
                    client.drain();
                    if(response) System.out.println("执行成功");
                    client.close();
                } catch (NoConnectionsException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static Client getClient() {
        Client client = null;
        ClientStatusListenerExt mylistener = new ClientStatusListenerExt(){
            @Override
            public void connectionLost(String hostname, int port, int connectionsLeft, DisconnectCause cause) {
                super.connectionLost(hostname, port, connectionsLeft, cause);
            }

            @Override
            public void connectionCreated(String hostname, int port, AutoConnectionStatus status) {
                super.connectionCreated(hostname, port, status);
            }

            @Override
            public void backpressure(boolean status) {
                super.backpressure(status);
            }

            @Override
            public void uncaughtException(ProcedureCallback callback, ClientResponse r, Throwable e) {
                super.uncaughtException(callback, r, e);
            }

            @Override
            public void lateProcedureResponse(ClientResponse r, String hostname, int port) {
                super.lateProcedureResponse(r, hostname, port);
            }
        };
        ClientConfig config = new ClientConfig("","");
        config.setTopologyChangeAware(true);
        client = ClientFactory.createClient(config);
        try {
            client.createConnection("10.10.56.16:11212");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return client;
    }
}
