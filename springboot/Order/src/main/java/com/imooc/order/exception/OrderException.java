package com.imooc.order.exception;

import com.imooc.order.enums.ResultEnum;

/**
 * Created by hunter on 2018/4/17.
 */
public class OrderException extends RuntimeException {

    private Integer code;

    public OrderException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public OrderException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}
