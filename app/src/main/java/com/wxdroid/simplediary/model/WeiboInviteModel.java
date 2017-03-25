package com.wxdroid.simplediary.model;

/**
 * 用户权限管理
 * Created by jinchun on 2017/1/5.
 */

public class WeiboInviteModel {
    private long weiboid;
    private int status;//0-没有权限；1-已开通权限；2-高级用户权限；3-管理员权限
    private String createtime;

    public long getWeiboid() {
        return weiboid;
    }

    public void setWeiboid(long weiboid) {
        this.weiboid = weiboid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
