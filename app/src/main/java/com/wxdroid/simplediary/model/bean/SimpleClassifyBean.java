package com.wxdroid.simplediary.model.bean;

import com.wxdroid.simplediary.model.BaseModel;
import com.wxdroid.simplediary.model.SimpleClassifyModel;

import java.util.List;

/**
 * Created by jinchun on 2017/1/8.
 */

public class SimpleClassifyBean extends BaseModel {
    List<SimpleClassifyModel> data;

    public List<SimpleClassifyModel> getData() {
        return data;
    }

    public void setData(List<SimpleClassifyModel> data) {
        this.data = data;
    }
}
