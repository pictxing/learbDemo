package com.superfuns.healthcode.network.entity;


@SuppressWarnings("unused")
public class HttpResult<T> {
    /**
     * 错误码
     */
    private int code=0;
    /**
     * 错误信息
     */
    private String msg;

    private String status;
    /**
     * 消息响应的主体
     */
    private T data;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
