package com.joker.sysprocedure;

import com.joker.util.ClientConn;
import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ProcCallException;

import java.io.IOException;

/**
 * Created by hunter on 2017/11/22.
 */
public class SystemCatalogPro {
    public static void main(String[] args) {
        Client client = ClientConn.getClient();
        try {
            VoltTable[] result = client.callProcedure("@Statistics", "TABLE").getResults();
            System.out.println("Information about the database cluster:");
            for (VoltTable node : result) {
                System.out.println(node.toString());
            }
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
