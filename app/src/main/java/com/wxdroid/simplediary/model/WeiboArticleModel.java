package com.wxdroid.simplediary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 微博内容model
 * Created by jinchun on 2017/1/3.
 */

public class WeiboArticleModel implements  Parcelable {
    private long id;
    private long weibouserid;
    private String accesstoken;
    private String content;
    private String imageurls;
    private String picurls;
    private String latitude;
    private String longitude;
    private String createtime;
    private String sendtime;
    private int sendstatus;
    private int msgtype;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWeibouserid() {
        return weibouserid;
    }

    public void setWeibouserid(long weibouserid) {
        this.weibouserid = weibouserid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageurls() {
        return imageurls;
    }

    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public int getSendstatus() {
        return sendstatus;
    }

    public void setSendstatus(int sendstatus) {
        this.sendstatus = sendstatus;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getPicurls() {
        return picurls;
    }

    public void setPicurls(String picurls) {
        this.picurls = picurls;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.weibouserid);
        dest.writeString(this.accesstoken);
        dest.writeString(this.content);
        dest.writeString(this.imageurls);
        dest.writeString(this.picurls);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.createtime);
        dest.writeString(this.sendtime);
        dest.writeInt(this.sendstatus);
        dest.writeInt(this.msgtype);
    }

    public WeiboArticleModel() {
    }

    protected WeiboArticleModel(Parcel in) {
        this.id = in.readLong();
        this.weibouserid = in.readLong();
        this.accesstoken = in.readString();
        this.content = in.readString();
        this.imageurls = in.readString();
        this.picurls = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.createtime = in.readString();
        this.sendtime = in.readString();
        this.sendstatus = in.readInt();
        this.msgtype = in.readInt();
    }

    public static final Parcelable.Creator<WeiboArticleModel> CREATOR = new Parcelable.Creator<WeiboArticleModel>() {
        @Override
        public WeiboArticleModel createFromParcel(Parcel source) {
            return new WeiboArticleModel(source);
        }

        @Override
        public WeiboArticleModel[] newArray(int size) {
            return new WeiboArticleModel[size];
        }
    };
}
