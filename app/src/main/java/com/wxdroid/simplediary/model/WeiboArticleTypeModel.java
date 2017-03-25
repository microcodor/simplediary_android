package com.wxdroid.simplediary.model;

import java.io.Serializable;

/**
 * 微博消息内容
 * Created by jinchun on 2016/12/31.
 */

public class WeiboArticleTypeModel implements Serializable {
    private int typeId;
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
