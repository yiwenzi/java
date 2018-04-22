package com.joker.example;

import com.joker.client.ClientConnection;
import com.joker.client.ClientConnectionPool;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Created by hunter on 2018/2/8.
 */
public class DecimalDemo {

    /**
     * The scale of decimals in Volt
     */
    public static final int kDefaultScale = 12;
    /**
     * The precision of decimals in Volt
     */
    public static final int kDefaultPrecision = 38;

    private static final MathContext context = new MathContext( kDefaultPrecision );
    public static void main(String[] args) throws Exception {
        //voltDB();
        decimalDemo();
    }

    public static void decimalDemo() {
        final String DECIMAL_FORMAT = "%01.12f";
        byte decimalBytes[] = {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                4, -116, 39, 57, 80, 80};
        final BigDecimal bd = new BigDecimal(
                new BigInteger(decimalBytes),
                kDefaultScale,
                context).stripTrailingZeros();
//        System.out.println(bd.unscaledValue());
        System.out.println(bd);
        System.out.println(bd.scale());
        System.out.println(bd.doubleValue());
        String valueStr = String.format(DECIMAL_FORMAT, bd.doubleValue());
        System.out.println(valueStr);
//        System.out.println(bd.toPlainString());
//        BigDecimal result2 = bd.stripTrailingZeros();
//        System.out.println(result2);
    }

    public static void voltDB() throws Exception {
        String servers = "10.10.0.159";
        ClientConnection connection = ClientConnectionPool.get(servers, 11212);
        ClientResponse response = null;
        VoltTable table = null;
        String sql = "select id from joker;";
        response = connection.client.callProcedure("@AdHoc", sql);

        if(response.getStatus() != ClientResponse.SUCCESS) {
            System.out.println(response.getStatusString());
            System.exit(1);
        }

        table = response.getResults()[0];
        while (table.advanceRow()) {
            BigDecimal result = table.getDecimalAsBigDecimal(0);
            System.out.println(result);
        }
        connection.close();
    }
}
