package com.joker.example;

import au.com.bytecode.opencsv_voltpatches.CSVWriter;
import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.utils.BulkLoaderErrorHandler;
import org.voltdb.utils.CSVDataLoader;
import org.voltdb.utils.RowWithMetaData;
import org.voltdb.utils.SusceptibleRunnable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by hunter on 2017/12/22.
 */
public class PartitionReader extends SusceptibleRunnable {
    private final CSVDataLoader m_loader;
    private final BulkLoaderErrorHandler m_errHandler;
    private final Client m_client;
    private final Long m_limitCount;
    static AtomicLong m_totalRowCount = new AtomicLong(0);

    public PartitionReader(CSVDataLoader loader, BulkLoaderErrorHandler errorHandler, Client client) throws Exception {
        m_loader = loader;
        m_errHandler = errorHandler;
        ClientConnection connection = ClientConnectionPool.get("10.10.0.159", 11212);
        m_client = connection.client;
        m_limitCount = 30000L;
    }

    @Override
    public void susceptibleRun() throws Exception {
        ClientResponse response = null;
        VoltTable procInfo = null;
        //测试多少的数据是不需要分页的
        Long rowCount = 0L;
        String tableName = "SADFEERANGEHISTORY";
        String oderBy = "id";
        response = m_client.callProcedure("@AdHoc","select count(*) from " + tableName);
        if (response.getStatus() != response.SUCCESS) {
            System.out.println(response.getStatusString());
            System.exit(1);
        }
        rowCount = response.getResults()[0].asScalarLong();
        if(rowCount <= m_limitCount) {
            selectWithNoLimit(tableName);
        } else {
            selectWithLimit(tableName,oderBy);
        }
        m_loader.close();
    }

    public void selectWithNoLimit(String tableName) throws IOException, ProcCallException, InterruptedException {
        ClientResponse response = null;
        VoltTable procInfo = null;
        String sql = String.format("select * from %s;", tableName);
        response = m_client.callProcedure("@AdHoc",sql);
        if (response.getStatus() != response.SUCCESS) {
            System.out.println(response.getStatusString());
            System.exit(1);
        }
        procInfo = response.getResults()[0];
        insertData(procInfo);
    }

    public void selectWithLimit(String tableName,String orderBy) throws IOException, ProcCallException, InterruptedException {
        ClientResponse response = null;
        VoltTable procInfo = null;

        long offset = 0;
        long limit = 10000; // 改成10000,将数据分成3份或4份导入的效果比较好
        boolean allDone = false;
        String sql = String.format("select * from %s order by %s limit %d offset ?;",
                tableName,orderBy,limit);
        while (!allDone) {
            response = m_client.callProcedure("@AdHoc",sql.replace("?",offset + ""));
            if (response.getStatus() != response.SUCCESS) {
                System.out.println(response.getStatusString());
                System.exit(1);
            }
            procInfo = response.getResults()[0];
            if(procInfo.getRowCount() < 1) {
                allDone = true;
            } else {
                insertData(procInfo);
            }
            offset += limit;
        }
    }
    public void insertData(VoltTable procInfo) throws InterruptedException {
        RowWithMetaData lineData = null;
        Object[] data = null;
        String[] stringValues = null;

        int columnCount = 0;

        columnCount = procInfo.getColumnCount();

        StringWriter sw = new StringWriter(16384);
        PrintWriter pw = new PrintWriter(sw,true);
        CSVWriter csw = new CSVWriter(pw);
        StringBuffer sb = sw.getBuffer();

        stringValues = new String[columnCount];
        while (procInfo.advanceRow()) {
            long rowNum = m_totalRowCount.incrementAndGet();
            data = new Object[columnCount];
            lineData = new RowWithMetaData(new String[1], rowNum);
            for (int i = 0; i < columnCount; i++) {
                VoltType type = procInfo.getColumnType(i);
                data[i] = procInfo.get(i, type);
                stringValues[i] = (data[i] == null ? "NULL" : data[i].toString());
            }
            csw.writeNext(stringValues);
            ((String[]) lineData.rawLine)[0] = sb.toString();
            sb.setLength(0);
            m_loader.insertRow(lineData, data);
        }
    }

    @Deprecated
    public void test1() throws Exception {
        ClientResponse response = null;
        VoltTable procInfo = null;
        /**
         * 分区表就一个分区一个分区的获取
         * 复制表就利用limit offset 来获取
         * 问题是，这点该如何判断
         * 如果总数小于11万根本不需要limit
         */
        String tableName = "PRPDPLANLIMIT";
        String orderBy = "plancode";
        boolean is_MP = true; // 默认为多分区
        procInfo = m_client.callProcedure("@SystemCatalog", "COLUMNS").getResults()[0];
        while (procInfo.advanceRow()) {
            String table = procInfo.getString("TABLE_NAME");
            if (tableName.equalsIgnoreCase(table)) {
                String remarks = procInfo.getString("REMARKS");
                if (remarks != null && remarks.equalsIgnoreCase("PARTITION_COLUMN")) {
                    is_MP = false;
                    break;
                }
            }
        }
    }
}
