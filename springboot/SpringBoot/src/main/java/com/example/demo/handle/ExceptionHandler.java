package com.example.demo.handle;

import com.example.demo.domain.Result;
import com.example.demo.exception.GirlException;
import com.example.demo.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hunter on 2018/3/29.
 */
@ControllerAdvice
public class ExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handler(Exception e) {
        if(e instanceof GirlException) {
            GirlException exception = (GirlException)e;
            return ResultUtil.error(exception.getCode(),exception.getMessage());
        } else {
            logger.error("【系统异常】{}", e);
            return ResultUtil.error(-1,"未知错误");
        }
    }
}
