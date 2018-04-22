package com.joker.util;

import org.voltdb.client.*;

import java.io.IOException;

/**
 * Created by hunter on 2017/11/29.
 * 不要spring的容器管理
 */
public class VoltClient {
    private Client m_client;


    public void poolFactory() {
        String servers = "10.10.56.16:11212,10.10.56.17:11212,10.10.56.18:11212,10.10.56.19:11212";
        String user = "";
        String password = "";
        String strLimit = "";
        int ratelimit = strLimit != null ? Integer.parseInt(strLimit) : Integer.MAX_VALUE;
    }

    public ClientResponseWithPartitionKey[] callAllPartitionProcedure(String arg0, Object... arg1)
            throws IOException, NoConnectionsException, ProcCallException {
        // TODO Auto-generated method stub
        return m_client.callAllPartitionProcedure(arg0, arg1);
    }

    public boolean callAllPartitionProcedure(AllPartitionProcedureCallback arg0, String arg1, Object... arg2)
            throws IOException, NoConnectionsException, ProcCallException {
        // TODO Auto-generated method stub
        return m_client.callAllPartitionProcedure(arg0, arg1, arg2);
    }

    public ClientResponse callProcedure(String arg0, Object... arg1)
            throws IOException, NoConnectionsException, ProcCallException {
        // TODO Auto-generated method stub
        return m_client.callProcedure(arg0, arg1);
    }

    public boolean callProcedure(ProcedureCallback arg0, String arg1, Object... arg2)
            throws IOException, NoConnectionsException {
        // TODO Auto-generated method stub
        return m_client.callProcedure(arg0, arg1, arg2);
    }


    public ClientResponse callProcedureWithTimeout(int arg0, String arg1, Object... arg2)
            throws IOException, NoConnectionsException, ProcCallException {
        // TODO Auto-generated method stub
        return m_client.callProcedureWithTimeout(arg0, arg1, arg2);
    }

    public boolean callProcedureWithTimeout(ProcedureCallback arg0, int arg1, String arg2, Object... arg3)
            throws IOException, NoConnectionsException {
        // TODO Auto-generated method stub
        return m_client.callProcedureWithTimeout(arg0, arg1, arg2, arg3);
    }
}
