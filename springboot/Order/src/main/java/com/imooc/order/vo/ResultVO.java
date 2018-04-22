package com.imooc.order.vo;

import lombok.Data;

/**
 * Created by hunter on 2018/4/17.
 */
@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;
}
