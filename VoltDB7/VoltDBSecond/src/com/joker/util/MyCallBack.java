package com.joker.util;

import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcedureCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hunter on 2017/11/10.
 */
public class MyCallBack implements ProcedureCallback {
    @Override
    public void clientCallback(ClientResponse response) throws Exception {
        Map<Integer, String> map = new HashMap<>();
        if(response.getStatus() != ClientResponse.SUCCESS) {
            System.err.println(response.getStatusString());
        } else {
            VoltTable table = response.getResults()[0];
            while (table.advanceRow()) {
                map.put((int) table.getLong(0), table.getString(1));
            }

            //测试voltdb类型
            final int colCount = table.getColumnCount();
            int rowCount = 1;
            table.resetRowPosition();
            while (table.advanceRow()){
                System.out.printf("--- Row %d ---\n", rowCount++);
                for (int i = 0; i < colCount; i++) {
                    System.out.printf("%s: ", table.getColumnType(i));
                    switch (table.getColumnType(i)) {
                        case TINYINT: case SMALLINT: case BIGINT: case INTEGER:
                            System.out.printf("%d\n", table.getLong(i));
                            break;
                        case STRING:
                            System.out.printf("%s\n", table.getString(i));
                            break;
                        case DECIMAL:
                            System.out.printf("%f\n", table.getDecimalAsBigDecimal(i));
                            break;
                        case FLOAT:
                            System.out.printf("%f\n", table.getDouble(i));
                            break;
                    }
                }
            }
        }
        if(map.size() > 0){
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                System.out.println("id : " + entry.getKey() + ", key: " + entry.getValue());
            }
        }
    }
}
