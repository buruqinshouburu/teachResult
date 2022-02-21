package com.qzkj.teachingresult.Result;

public class Result {
    //响应码
    private int code;
    private String msg;


    public Result(int code) {
        this.code = code;
    }
    public Result(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
