package com.wxdroid.simplediary.model.bean;

import com.wxdroid.simplediary.model.BaseModel;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;

import java.util.List;

/**
 * Created by jinchun on 2017/1/8.
 */

public class SimpleDiscoverBean extends BaseModel {
    List<SimpleDiscoverModel> data;

    public List<SimpleDiscoverModel> getData() {
        return data;
    }

    public void setData(List<SimpleDiscoverModel> data) {
        this.data = data;
    }
}
