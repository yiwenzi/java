package com.imooc.security.core.properties;

/**
 * Created by hunter on 2018/2/13.
 */
public class SmsCodeProperties {


    private int length = 6;

    private int expireInt = 60;

    //由逗号隔开的url
    private String url;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireInt() {
        return expireInt;
    }

    public void setExpireInt(int expireInt) {
        this.expireInt = expireInt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
