import org.voltdb.utils.JDBCLoader;

import java.io.IOException;

/**
 * Created by hunter on 2018/3/29.
 */
public class TestImport {
    public static void main(String[] args) throws IOException, InterruptedException {
        String data = "--jdbcurl=jdbc:informix-sqli://10.10.0.120:8001/dmsb:informixserver=data_dic_ids;NEWLOCALE=zh_CN,zh_CN;NEWCODESET=GBK,8859-1,819,Big5;IFX_USE_STRENC=true;";
        String dataUser = "dmsb";
        String sales = "--jdbcurl=jdbc:informix-sqli://10.10.0.120:8001/salesdbcs:informixserver=data_dic_ids;NEWLOCALE=zh_CN,zh_CN;NEWCODESET=GBK,8859-1,819,Big5;IFX_USE_STRENC=true;";
        String salesUser = "xgcs";
        String servers = "--servers=10.10.1.1,10.10.1.2,10.10.1.4,10.10.1.5";
        String[] tables = {
                "saaagent_agentcode",
                "prpdavitinscompany",
                "sadqualify",
                "prpdriskclausekind",
                "prpdaccountinfo",
                "prpdagent",
                "sadaccount",
                "prpdplanlimit",
                "saacontract",
                "prpdcompany",
                "prpdcompany_r",
                "prpdbankaccount",
                "prpdplanclausekind",
                "prpdrisklimit",
                "prpdrisklimit_riskcode",
                "prpdresource",
                "saaagentcontract_agentcode",
                "saaagentcontract_comcode",
                "saaagentcontract",
                "saacontracthis",
                "prpdnewcoderisk_codetype",
                "prpdnewcoderisk",
                "prpdriskitem",
                "prpdriskitem_riskcode",
                // "saaagentcontracthis",
                "saaagentcontracthis_ruleno",
                "sadfeerange_riskcode_notnull",
                "sadfeerange",
                "sadfeerangehistory"
        };
        int length = tables.length;
        for (int i = 0; i < length; i++) {
            String target = tables[i];
            String source = target;
            if(target.contains("_")) {
                source = target.split("_")[0];
            }
            String url = data;
            String user = dataUser;
            if(source.toLowerCase().equals("sadfeerange")) {
                url = sales;
                user = salesUser;
            }
            String[] paramsInformix = new String[] {target,
                    "--jdbcdriver=com.informix.jdbc.IfxDriver",
                    servers,
                    url,
                    "--jdbctable=" + source,
                    "--jdbcuser=" + user,
                    "--jdbcpassword=" + user,
                    "--port=11212"};
            long start = System.currentTimeMillis();
            JDBCLoader.main(paramsInformix);
            long end = System.currentTimeMillis();
            long useTime = (end - start) / 1000;
            System.out.println("table: " + target + "导入完成，耗时: " + useTime + "s");
        }
    }
}
