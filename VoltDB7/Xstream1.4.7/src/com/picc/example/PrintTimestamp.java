package com.picc.example;

import com.picc.entity.EntityWithDate;
import com.picc.utils.SqlTimestampConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hunter on 2018/2/2.
 */
public class PrintTimestamp {
    public static void main(String[] args) {
        XStream xStream = createXstream();
        EntityWithDate entity = new EntityWithDate();
        //构造时间
        Date date = new Date();
        Date dateForSqlDate = new java.sql.Date(new Date().getTime());
        Date dateForTimestamp = new Timestamp(new Date().getTime());

        entity.setDate(date);
        entity.setDateForSqlDate(dateForSqlDate);
        entity.setDateForTimestamp(dateForTimestamp);

        String result = xStream.toXML(entity);
        System.out.println(result);
    }

    private static XStream createXstream() {
        XStream xStream = null;
        xStream = new XStream(new DomDriver()) {
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        try {
                            return definedIn != Object.class || realClass(fieldName) != null;
                        } catch (CannotResolveClassException cnrce) {
                            return false;
                        }
                    }
                };
            }
        };
        TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai");
        //yyyy-MM-dd HH:mm:ss.S  -- 格式可自行控制
        xStream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss.S",null,zone), XStream.PRIORITY_NORMAL);
        //这里一定要注意要引入自己编写的SqlTimestampConverter，而不是官方的。
        //这里一定要注意要引入自己编写的SqlTimestampConverter，而不是官方的。
        //这里一定要注意要引入自己编写的SqlTimestampConverter，而不是官方的。
        xStream.registerConverter(new SqlTimestampConverter(), XStream.PRIORITY_NORMAL);
        return xStream;
    }
}
