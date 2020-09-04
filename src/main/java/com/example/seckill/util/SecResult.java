package com.example.seckill.util;

public class SecResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SecResult() {
    }

    public SecResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public SecResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static SecResult ok(Object data) {
        return new SecResult(data);
    }

    public static SecResult ok() {
        return new SecResult(null);
    }

    public static SecResult build(Integer status, String msg, Object data) {
        return new SecResult(status, msg, data);
    }

}
