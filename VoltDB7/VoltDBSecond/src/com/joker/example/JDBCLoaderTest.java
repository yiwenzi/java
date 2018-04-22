package com.joker.example;

import com.joker.export.JDBCLoader;

import java.io.IOException;

/**
 * Created by hunter on 2017/12/14.
 */
public class JDBCLoaderTest {
    public static void main(String[] args) throws IOException {
        /**
         * 一共31张表，挑选6张复制表，分区表分别挑选 1 2 3
         */
        String[] tables = {
                "PrpDplan", // 25785
                "PrpDriskClause", //22992
                "PrpDbankAccount",//145959
                "PrpDnewCode",//34815
                "SaAAgentContract",//133012
                "SaDFeeRange",//4071252  在这个库中不存在
                "PrpDcompany", // 108912
                "PrpDplanClauseKind",//167846
                "PrpDplanLimit",//108331
                "PrpDnewCodeRisk_codeType",//370109
                "SaAAgent_AgentCode", //63350  去掉这张表
                "SaDAccount", // 104285 少一条
                "SaDQualify", //44433
        };
        String data = "--jdbcurl=jdbc:informix-sqli://10.10.0.120:8001/dmsb:informixserver=data_dic_ids;NEWLOCALE=zh_CN,zh_CN;NEWCODESET=GBK,8859-1,819,Big5;IFX_USE_STRENC=true;";
        String dataUser = "dmsb";
        String sales = "jdbc:informix-sqli://10.10.0.120:8001/salesdbcs:informixserver=data_dic_ids;NEWLOCALE=zh_CN,zh_CN;NEWCODESET=GBK,8859-1,819,Big5;IFX_USE_STRENC=true;";
        String salesUser = "xgcs";
        int length = tables.length;
        for (int i = 0; i < length; i++) {
            String target = tables[i];
            String source = target;
            if(source.contains("_")) {
                source = source.split("_")[0];
            }
            String[] paramsInformix = new String[] {target,
                    "--jdbcdriver=com.informix.jdbc.IfxDriver",
                    "--servers=10.10.0.159",
                    data,
                    "--jdbctable=" + source,
                    "--jdbcuser=" + dataUser,
                    "--jdbcpassword=" + dataUser,
                    "--port=11212"};
            JDBCLoader.main(paramsInformix);
        }

        String[] paramsVoltdb = new String[] {"SADACCOUNT2",
                "--jdbcdriver=org.voltdb.jdbc.Driver",
                "--servers=10.10.0.160",
                "--jdbcurl=jdbc:voltdb://10.10.0.160:11212",
                "--jdbctable=SADACCOUNT",
                "--jdbcuser=",
                "--jdbcpassword=",
                "--port=11212"};
    }
}
