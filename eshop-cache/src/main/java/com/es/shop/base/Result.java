package com.es.shop.base;

import com.es.shop.enums.ResultEnum;

/**
 * @Description:
 * @CreateTime: 2020-05-26
 * @Version:v1.0
 */
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public static <T> Result<T> success(T t) {
        Result result = new Result<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(t);
        return result;
    }

    public static <T> Result<T> success() {
        Result result = new Result<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        return result;
    }

    public static <T> Result<T> fail() {
        Result result = new Result<>();
        result.setCode(ResultEnum.FAIL.getCode());
        result.setMsg(ResultEnum.FAIL.getMsg());
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
