package com.svmuu.common.http;

/**
 * Created by Lincoln on 15/7/10.
 */
public class Response {

    public String data;
    public String message;
    public boolean status;
    public String code;

    @Override
    public String toString() {
        return "Response{" +
                "data='" + data + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", code='" + code + '\'' +
                '}';
    }
}
