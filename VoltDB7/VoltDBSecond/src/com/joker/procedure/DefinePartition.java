package com.joker.procedure;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

/**
 * Created by hunter on 2017/11/22.
 */
public class DefinePartition extends VoltProcedure {
    public final SQLStmt stmt = new SQLStmt("select plancode from PRPDPLANENGAGE where areacode  = ?");

    public VoltTable[] run(String partitionKey, String areacode) {
        voltQueueSQL(stmt, areacode);
        return voltExecuteSQL(true);
    }
}
