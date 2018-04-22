package com.imooc.security.core.validate.code.sms;

/**
 * Created by hunter on 2018/2/13.
 */
public interface SmsCodeSender {
    void send(String mobile, String code);
}
