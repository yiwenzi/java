package com.imooc.web.config;

import java.util.ArrayList;
import java.util.List;

import com.imooc.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.imooc.web.filter.TimeFilter;
//import com.imooc.web.interceptor.TimeInterceptor;

/**
 * Created by hunter on 2018/1/30.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @SuppressWarnings("unused")
    @Autowired
    private TimeInterceptor timeInterceptor;

    //配置异步处理

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(timeInterceptor);
    }

    //	@Bean
    public FilterRegistrationBean timeFilter() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        TimeFilter timeFilter = new TimeFilter();
        registrationBean.setFilter(timeFilter);
        //指定过滤器在哪些url上起作用
        List<String> urls = new ArrayList<>();
        urls.add("/*"); //所有的路径
        registrationBean.setUrlPatterns(urls);

        return registrationBean;

    }

}