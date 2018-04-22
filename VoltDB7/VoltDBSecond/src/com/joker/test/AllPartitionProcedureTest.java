package com.joker.test;

import com.joker.util.ClientConn;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponseWithPartitionKey;
import org.voltdb.client.ProcCallException;

import java.io.IOException;

/**
 * Created by hunter on 2017/11/22.
 */
public class AllPartitionProcedureTest {
    public static void main(String[] args) {
        //客户端有问题
        Client client = ClientConn.getClient();
        try {
            ClientResponseWithPartitionKey[] results = client.callAllPartitionProcedure("DefinePartition","00000000");
        } catch (IOException | ProcCallException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
