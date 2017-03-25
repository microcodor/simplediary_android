package com.wxdroid.simplediary.model.bean;

import com.wxdroid.simplediary.model.BaseModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;

import java.util.List;

/**
 * 定时微博查询结果集结构体
 * Created by jinchun on 2017/1/21.
 */

public class WeiboArticleBean  extends BaseModel {
    private List<WeiboArticleModel> data;

    public List<WeiboArticleModel> getData() {
        return data;
    }

    public void setData(List<WeiboArticleModel> data) {
        this.data = data;
    }
}
