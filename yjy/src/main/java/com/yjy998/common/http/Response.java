package com.yjy998.common.http;

public class Response<T> {
    /**
     * 返回的数据
     */
    public String data;
    /**
     * 消息
     */
    public String message;
    /**
     * true 业务逻辑正确 false 反之
     */
    public boolean status;
    /**
     * code
     */
    public String code;


}
