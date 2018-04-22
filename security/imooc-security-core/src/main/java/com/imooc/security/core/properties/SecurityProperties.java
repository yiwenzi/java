package com.imooc.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hunter on 2018/2/6.
 * 这里存的是默认配置，可在配置文件中进行更改
 */

@ConfigurationProperties(prefix = "imooc.security") //读取配置文件中以imooc.security开头的属性
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties(); //imooc.security.browser的属性会被读到这里
    private ValidateCodeProperties code = new ValidateCodeProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }
}
