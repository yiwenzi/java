package com.joker.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.voltdb.client.exampleutils.ClientConnection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hunter on 2018/3/22.
 */
public class AbstractCanalClient {
    protected final static Logger logger = LoggerFactory.getLogger(AbstractCanalClient.class);

    protected Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("parse events has an error", e);
        }
    };
    protected Thread thread = null;
    protected volatile boolean running = false;
    protected static final String SEP = SystemUtils.LINE_SEPARATOR;

    //存放jdk中Types的数字类型
    protected static final Integer[] array = {-6, 5, 4, -5, 6, 8, 2, 3, 7};
    protected static final Set<Integer> typeCollection= new HashSet<>(Arrays.asList(array));

    //构造时需要的属性
    protected String destination;
    protected CanalConnector connector;
    protected ClientConnection connection;

    public AbstractCanalClient(String destination){
        this(destination, null, null);
    }

    public AbstractCanalClient(String destination, CanalConnector connector) {
        this(destination, connector, null);
    }

    public AbstractCanalClient(String destination, CanalConnector connector,ClientConnection connection){
        this.destination = destination;
        this.connector = connector;
        this.connection = connection;
    }

    public void start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(new Runnable() {

            public void run() {
                process();
            }
        });

        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        running = true;
    }

    protected void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }

        MDC.remove("destination");
    }

    protected void process() {
        int batchSize = 5 * 1024;
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe();
                while (running) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        // try {
                        // Thread.sleep(1000);
                        // } catch (InterruptedException e) {
                        // }
                    } else {
                        //printSummary(message, batchId, size);
                        processEntry(message.getEntries());
                    }

                    connector.ack(batchId); // 提交确认
                    // connector.rollback(batchId); // 处理失败, 回滚数据
                }
            } catch (Exception e) {
                logger.error("process error!", e);
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    protected void processEntry(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                CanalEntry.RowChange rowChange = null;
                try {
                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }

                CanalEntry.EventType eventType = rowChange.getEventType();

                String sql = "";
                if (eventType == CanalEntry.EventType.TRUNCATE ) {
                    //logger.info(" sql ----> " + rowChange.getSql() + SEP);
                    sql = rowChange.getSql();
                    System.out.println("这是批量删除操作");
                    System.out.println(sql);
                    try {
                        connection.execute("@AdHoc",sql);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                String tableName = entry.getHeader().getTableName();

                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    //sql = "";
                    if (eventType == CanalEntry.EventType.DELETE) {
                        //printColumn(rowData.getBeforeColumnsList());
                        System.out.println("这是删除操作");
                        System.out.println(getDeleteSql(rowData,tableName));
                        sql = getDeleteSql(rowData,tableName);
                    } else if (eventType == CanalEntry.EventType.INSERT) {
                        printColumn(rowData.getAfterColumnsList());
                        System.out.println("这是插入操作");
                        System.out.println(getInsertSql(rowData, tableName));
                        sql = getInsertSql(rowData, tableName);
                    } else {
                        //printColumn(rowData.getBeforeColumnsList());
                        // printColumn(rowData.getAfterColumnsList());
                        System.out.println("这是更新操作");
                        System.out.println(getUpdateSql(rowData,tableName));
                        sql = getUpdateSql(rowData,tableName);
                    }
                    try {
                        connection.execute("@AdHoc",sql);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //
    /**
     * 拼接insert部分sql
     * @param rowData
     * @param tableName
     * @return
     * 是全量字段插入，还是会有单个字段插入的情况，考虑第二种吧
     * insert into TableName(a,b,c) values (a,b,c)
     */
    private String getInsertSql(CanalEntry.RowData rowData,String tableName) {
        StringBuilder insertPart = new StringBuilder("");
        StringBuilder valuesPart = new StringBuilder("");

        insertPart.append("insert into ").append(tableName).append("(");
        valuesPart.append(" values (");
        List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : columns) {
            insertPart.append(column.getName()).append(",");
            valuesPart.append("'").append(column.getValue()).append("',");
        }
        insertPart.deleteCharAt(insertPart.length()-1).append(")");
        valuesPart.deleteCharAt(valuesPart.length()-1).append(");");
        insertPart.append(valuesPart);
        return insertPart.toString();

    }

    /**
     * 获取删除sql
     * @param rowData
     * @param tableName
     * @return
     */
    private String getDeleteSql(CanalEntry.RowData rowData,String tableName) {
        StringBuilder result = new StringBuilder();
        result.append("delete from ").append(tableName).append(" where 1 = 1 ");
        List<CanalEntry.Column> columns = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : columns) {
            if(column.getIsKey()) { //这里应该考虑多主键问题和主键类型
                int javaSqlType = column.getSqlType();
                result.append(" and ").append(column.getName()).append(" = ");
                if(needQuote(javaSqlType)) {
                    result.append("'").append(column.getValue()).append("'");
                } else {
                    result.append(column.getValue());
                }
            }
        }
        result.append(";");
        return result.toString();
    }

    private String getUpdateSql(CanalEntry.RowData rowData,String tableName) {
        StringBuilder result = new StringBuilder();
        result.append("update ").append(tableName).append(" set ");
        List<CanalEntry.Column> columnsBefore = rowData.getBeforeColumnsList();
        List<CanalEntry.Column> columnsAfter = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : columnsAfter) {
            if(column.getUpdated()) {
                int javaSqlType = column.getSqlType();
                result.append(column.getName()).append(" = ");
                if(needQuote(javaSqlType)) {
                    result.append("'").append(column.getValue()).append("',");
                } else {
                    result.append(column.getValue()).append(",");
                }
            }
        }
        result.deleteCharAt(result.length()-1).append(" where 1 = 1 ");
        for (CanalEntry.Column column : columnsBefore) {
            if(column.getIsKey()) {
                int javaSqlType = column.getSqlType();
                result.append(" and ").append(column.getName()).append(" = ");
                if(needQuote(javaSqlType)) {
                    result.append("'").append(column.getValue()).append("'");
                } else {
                    result.append(column.getValue());
                }
            }
        }
        result.append(";");
        return result.toString();
    }

    /**
     * 该类型的值是否需要加引号
     * @param javaSqlType
     * @return false 则为数字类型，不需要添加引号,true则需要添加引号
     */
    private boolean needQuote(int javaSqlType) {
        return !typeCollection.contains(javaSqlType);
    }
    private void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            StringBuilder builder = new StringBuilder();
            builder.append(column.getName() + " : " + column.getValue());
            builder.append("    type=" + column.getMysqlType());
            builder.append("    type2=" + column.getSqlType());
            if (column.getUpdated()) {
                builder.append("    update=" + column.getUpdated());
            }
            if(column.getIsKey()) {
                builder.append("    primary=" + column.getIsKey());
            }
            builder.append(SEP);
            logger.info(builder.toString());
        }
    }

}
