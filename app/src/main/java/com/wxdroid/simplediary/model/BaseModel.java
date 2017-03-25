package com.wxdroid.simplediary.model;

/**
 * Created by jinchun on 2016/11/29.
 */

public class BaseModel {
    /**
     * msg : success
     * status : 0
     */

    private int code;
    private String info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "status='" + code + '\'' +
                ", msg='" + info + '\'' +
                '}';
    }
}
