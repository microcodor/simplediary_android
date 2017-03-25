package com.wxdroid.simplediary.mvp.model;

import android.content.Context;

import com.wxdroid.simplediary.entity.Group;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/14.
 */
public interface GroupListModel {

    interface OnGroupListFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Group> groupslist);

        void onError(String error);
    }


    public void groupsOnlyOnce(Context context, OnGroupListFinishedListener onDataFinishedListener);

    public void cacheLoad(Context context, OnGroupListFinishedListener onGroupListFinishedListener);

    public void cacheSave(Context context, String response);


}
