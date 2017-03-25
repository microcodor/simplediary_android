package com.wxdroid.simplediary.model;

import java.io.Serializable;

/**
 * Created by jinchun on 2017/1/8.
 */

public class SimpleClassifyModel implements Serializable {
    private  int id;
    private  String classifyname;
    private  String createtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassifyname() {
        return classifyname;
    }

    public void setClassifyname(String classifyname) {
        this.classifyname = classifyname;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
