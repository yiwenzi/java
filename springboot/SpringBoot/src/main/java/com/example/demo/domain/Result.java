package com.example.demo.domain;

/**
 * http请求返回的最外层对象
 * Created by hunter on 2018/3/29.
 */
public class Result<T> {
    /** 返回码 **/
    private Integer code;
    /** 提示信息  **/
    private String msg;
    /** 具体内容 **/
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
