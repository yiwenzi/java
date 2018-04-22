package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hunter on 2018/3/29.
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    //返回值必须为void
    @Pointcut("execution(public * com.example.demo.controller.GirlController.*(..))")
    public void log() {

    }
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //url
        logger.info("url = {}", request.getRequestURL());
        //method
        logger.info("method = {}", request.getMethod());
        //client ip
        logger.info("ip = {}", request.getRemoteAddr());
        //类方法
        logger.info("class_method = {}", joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        //参数
        logger.info("args = {}", joinPoint.getArgs());

    }
    @After("log()")
    public void doAfter() {
        logger.info("2222");
    }

    //获取返回结果
    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturning(Object object) {
        logger.info("response = {}",object);
    }
}
