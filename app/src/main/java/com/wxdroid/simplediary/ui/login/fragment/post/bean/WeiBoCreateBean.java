package com.wxdroid.simplediary.ui.login.fragment.post.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;
import com.wxdroid.simplediary.ui.login.fragment.post.picselect.bean.ImageInfo;

import java.util.ArrayList;

/**
 * Created by jinchun on 2016/6/27.
 */
public class WeiBoCreateBean implements Parcelable {
    /**
     * 要发送的图片列表
     */
    public ArrayList<ImageInfo> selectImgList;
    /**
     * 要发送的文本
     */
    public String content;
    /**
     * 要转发的微博
     */
    public Status status;

    public String sendTime;

    /**
     * 自由素材model
     * */
    public SimpleDiscoverModel simpleDiscoverModel;

    /**
     * 编辑重发
     * */
    public WeiboArticleModel weiboArticleModel;

    public WeiBoCreateBean(String content, ArrayList<ImageInfo> selectImgList, Status status) {
        this.selectImgList = selectImgList;
        this.content = content;
        this.status = status;

    }
    public WeiBoCreateBean(String content, ArrayList<ImageInfo> selectImgList, Status status,String sendTime) {
        this.selectImgList = selectImgList;
        this.content = content;
        this.status = status;
        this.sendTime = sendTime;

    }
    public WeiBoCreateBean(String content, Status status,String sendTime) {
        this.content = content;
        this.status = status;
        this.sendTime = sendTime;

    }

    public WeiBoCreateBean(String content, ArrayList<ImageInfo> selectImgList) {
        this.content = content;
        this.selectImgList = selectImgList;
    }
    public WeiBoCreateBean(String content, ArrayList<ImageInfo> selectImgList,String sendTime) {
        this.content = content;
        this.selectImgList = selectImgList;
        this.sendTime = sendTime;
    }

    public WeiBoCreateBean(String content, SimpleDiscoverModel model, String sendTime) {
        this.content = content;
        this.simpleDiscoverModel = model;
        this.sendTime = sendTime;

    }

    public WeiBoCreateBean(String content, WeiboArticleModel model, String sendTime) {
        this.content = content;
        this.weiboArticleModel = model;
        this.sendTime = sendTime;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.selectImgList);
        dest.writeString(this.content);
        dest.writeParcelable(this.status, flags);
        dest.writeString(this.sendTime);
        dest.writeParcelable(this.simpleDiscoverModel, flags);
        dest.writeParcelable(this.weiboArticleModel, flags);
    }

    protected WeiBoCreateBean(Parcel in) {
        this.selectImgList = in.createTypedArrayList(ImageInfo.CREATOR);
        this.content = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.sendTime = in.readString();
        this.simpleDiscoverModel = in.readParcelable(SimpleDiscoverModel.class.getClassLoader());
        this.weiboArticleModel = in.readParcelable(WeiboArticleModel.class.getClassLoader());
    }

    public static final Creator<WeiBoCreateBean> CREATOR = new Creator<WeiBoCreateBean>() {
        @Override
        public WeiBoCreateBean createFromParcel(Parcel source) {
            return new WeiBoCreateBean(source);
        }

        @Override
        public WeiBoCreateBean[] newArray(int size) {
            return new WeiBoCreateBean[size];
        }
    };
}
