package com.imooc.security.core.validate.code;

import com.imooc.security.core.ImageCodeGenerator;
import com.imooc.security.core.ValidateCodeGenerator;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hunter on 2018/2/13.
 * 让接口的实现可配置
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;
    //方法的名字就是bean的名字
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator") //该注解十分有用
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    /**
     * 接口的实现类可配置，若在容器内发现了用户自定义的实现类，则不使用默认的类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class) //该注解十分有用
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
