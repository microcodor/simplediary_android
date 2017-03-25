package com.wxdroid.simplediary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by jinchun on 2017/1/8.
 */

public class SimpleDiscoverModel implements Parcelable {
    private long id;
    private long userid;
    private String title;
    private String content;
    private int type;
    private String imageurls;
    private String picurls;
    private String videourls;
    private String videoaddress;
    private String createtime;
    private int classifytype;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserid() {
        return userid;
    }
    public void setUserid(long userid) {
        this.userid = userid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getImageurls() {
        return imageurls;
    }
    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }
    public String getPicurls() {
        return picurls;
    }
    public void setPicurls(String picurls) {
        this.picurls = picurls;
    }
    public String getVideourls() {
        return videourls;
    }
    public void setVideourls(String videourls) {
        this.videourls = videourls;
    }
    public String getCreatetime() {
        return createtime;
    }
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
    public int getClassifytype() {
        return classifytype;
    }
    public void setClassifytype(int classifytype) {
        this.classifytype = classifytype;
    }

    public String getVideoaddress() {
        return videoaddress;
    }

    public void setVideoaddress(String videoaddress) {
        this.videoaddress = videoaddress;
    }

    public SimpleDiscoverModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userid);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeInt(this.type);
        dest.writeString(this.imageurls);
        dest.writeString(this.picurls);
        dest.writeString(this.videourls);
        dest.writeString(this.videoaddress);
        dest.writeString(this.createtime);
        dest.writeInt(this.classifytype);
    }

    protected SimpleDiscoverModel(Parcel in) {
        this.id = in.readLong();
        this.userid = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.type = in.readInt();
        this.imageurls = in.readString();
        this.picurls = in.readString();
        this.videourls = in.readString();
        this.videoaddress = in.readString();
        this.createtime = in.readString();
        this.classifytype = in.readInt();
    }

    public static final Creator<SimpleDiscoverModel> CREATOR = new Creator<SimpleDiscoverModel>() {
        @Override
        public SimpleDiscoverModel createFromParcel(Parcel source) {
            return new SimpleDiscoverModel(source);
        }

        @Override
        public SimpleDiscoverModel[] newArray(int size) {
            return new SimpleDiscoverModel[size];
        }
    };
}
