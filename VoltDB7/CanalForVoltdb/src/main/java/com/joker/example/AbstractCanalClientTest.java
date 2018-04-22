package com.joker.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.TransactionBegin;
import com.alibaba.otter.canal.protocol.CanalEntry.TransactionEnd;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import org.voltdb.client.exampleutils.ClientConnection;

/**
 * Created by hunter on 2018/3/20.
 */
public class AbstractCanalClientTest {

    protected final static Logger             logger             = LoggerFactory.getLogger(AbstractCanalClientTest.class);
    protected static final String             SEP                = SystemUtils.LINE_SEPARATOR;
    protected static final String             DATE_FORMAT        = "yyyy-MM-dd HH:mm:ss";
    protected volatile boolean                running            = false;
    protected Thread.UncaughtExceptionHandler handler            = new Thread.UncaughtExceptionHandler() {

        public void uncaughtException(Thread t, Throwable e) {
            logger.error("parse events has an error", e);
        }
    };
    protected Thread                          thread             = null;
    protected CanalConnector                  connector;
    protected ClientConnection                connection;
    protected static String                   context_format     = null;
    protected static String                   row_format         = null;
    protected static String                   transaction_format = null;
    protected String                          destination;

    static {
        context_format = SEP + "****************************************************" + SEP;
        context_format += "* Batch Id: [{}] ,count : [{}] , memsize : [{}] , Time : {}" + SEP;
        context_format += "* Start : [{}] " + SEP;
        context_format += "* End : [{}] " + SEP;
        context_format += "****************************************************" + SEP;

        row_format = SEP
                + "----------------> binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {}({}) , delay : {} ms"
                + SEP;

        transaction_format = SEP + "================> binlog[{}:{}] , executeTime : {}({}) , delay : {}ms" + SEP;

    }

    public AbstractCanalClientTest(String destination){
        this(destination, null);
    }

    public AbstractCanalClientTest(String destination, CanalConnector connector){
        this.destination = destination;
        this.connector = connector;
    }

    protected void start() {
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
                        printEntry(message.getEntries());
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

    private void printSummary(Message message, long batchId, int size) {
        long memsize = 0;
        for (Entry entry : message.getEntries()) {
            memsize += entry.getHeader().getEventLength();
        }

        String startPosition = null;
        String endPosition = null;
        if (!CollectionUtils.isEmpty(message.getEntries())) {
            startPosition = buildPositionForDump(message.getEntries().get(0));
            endPosition = buildPositionForDump(message.getEntries().get(message.getEntries().size() - 1));
        }

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        logger.info(context_format, new Object[] { batchId, size, memsize, format.format(new Date()), startPosition,
                endPosition });
    }

    protected String buildPositionForDump(Entry entry) {
        long time = entry.getHeader().getExecuteTime();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + ":"
                + entry.getHeader().getExecuteTime() + "(" + format.format(date) + ")";
    }

    protected void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            long executeTime = entry.getHeader().getExecuteTime();
            long delayTime = new Date().getTime() - executeTime;
            Date date = new Date(entry.getHeader().getExecuteTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            /*
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN) {
                    TransactionBegin begin = null;
                    try {
                        begin = TransactionBegin.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务头信息，执行的线程id，事务耗时
                    logger.info(transaction_format,
                            new Object[] { entry.getHeader().getLogfileName(),
                                    String.valueOf(entry.getHeader().getLogfileOffset()),
                                    String.valueOf(entry.getHeader().getExecuteTime()), simpleDateFormat.format(date),
                                    String.valueOf(delayTime) });
                    logger.info(" BEGIN ----> Thread id: {}", begin.getThreadId());
                } else if (entry.getEntryType() == EntryType.TRANSACTIONEND) {
                    TransactionEnd end = null;
                    try {
                        end = TransactionEnd.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务提交信息，事务id
                    logger.info("----------------\n");
                    logger.info(" END ----> transaction id: {}", end.getTransactionId());
                    logger.info(transaction_format,
                            new Object[] { entry.getHeader().getLogfileName(),
                                    String.valueOf(entry.getHeader().getLogfileOffset()),
                                    String.valueOf(entry.getHeader().getExecuteTime()), simpleDateFormat.format(date),
                                    String.valueOf(delayTime) });
                }

                continue;
            }
            */
            if (entry.getEntryType() == EntryType.ROWDATA) {
                RowChange rowChange = null;
                try {
                    rowChange = RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }

                EventType eventType = rowChange.getEventType();

//                logger.info(row_format,
//                        new Object[] { entry.getHeader().getLogfileName(),
//                                String.valueOf(entry.getHeader().getLogfileOffset()), entry.getHeader().getSchemaName(),
//                                entry.getHeader().getTableName(), eventType,
//                                String.valueOf(entry.getHeader().getExecuteTime()), simpleDateFormat.format(date),
//                                String.valueOf(delayTime) });

                String sql = "";
                if (eventType == EventType.TRUNCATE ) {
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

                for (RowData rowData : rowChange.getRowDatasList()) {
                    //sql = "";
                    if (eventType == EventType.DELETE) {
                        //printColumn(rowData.getBeforeColumnsList());
                        System.out.println("这是删除操作");
                        System.out.println(getDeleteSql(rowData,tableName));
                        sql = getDeleteSql(rowData,tableName);
                    } else if (eventType == EventType.INSERT) {
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
    protected String getInsertSql(RowData rowData,String tableName) {
        StringBuilder insertPart = new StringBuilder("");
        StringBuilder valuesPart = new StringBuilder("");

        insertPart.append("insert into ").append(tableName).append("(");
        valuesPart.append(" values (");
        List<Column> columns = rowData.getAfterColumnsList();
        for (Column column : columns) {
            String name = column.getName();
            String value = column.getValue();
            insertPart.append(name).append(",");
            valuesPart.append("'").append(value).append("',");
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
    protected String getDeleteSql(RowData rowData,String tableName) {
        StringBuilder result = new StringBuilder();
        result.append("delete from ").append(tableName).append(" where ");
        List<Column> columns = rowData.getBeforeColumnsList();
        for (Column column : columns) {
            String name = column.getName();
            String value = column.getValue();
            if(column.getIsKey()) { //这里应该考虑多主键问题和主键类型
                result.append(name).append(" = ").append(value).append(";");
                return result.toString();
            }
        }
        //不应该走到这里
        return "";
    }

    protected String getUpdateSql(RowData rowData,String tableName) {
        StringBuilder result = new StringBuilder();
        result.append("update ").append(tableName).append(" set ");
        List<Column> columnsBefore = rowData.getBeforeColumnsList();
        List<Column> columnsAfter = rowData.getAfterColumnsList();
        for (Column column : columnsAfter) {
            String name = column.getName();
            String value = column.getValue();
            result.append(name).append(" = ").append("'").append(value).append("'").append(",");
        }
        result.deleteCharAt(result.length()-1).append(" where ");
        for (Column column : columnsBefore) {
            if(column.getIsKey()) {
                result.append(column.getName()).append(" = ").append(column.getValue());
                return result.toString();
            }
        }
        //不应该走到这里
        return "";
    }

    protected void printColumn(List<Column> columns) {
        for (Column column : columns) {
            StringBuilder builder = new StringBuilder();
            builder.append(column.getName() + " : " + column.getValue());
            builder.append("    type=" + column.getSqlType());
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

    public void setConnector(CanalConnector connector) {
        this.connector = connector;
    }
    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }
}
