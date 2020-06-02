package com.es.shop.enums;

/**
 * @Description:
 * @CreateTime: 2020-05-26
 * @Version:v1.0
 */
public enum ResultEnum {
    SUCCESS(200, "success"),
    FAIL(500, "fail");
    private int code;

    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
    }}
