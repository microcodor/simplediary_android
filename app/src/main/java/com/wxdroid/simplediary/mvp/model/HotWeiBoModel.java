package com.wxdroid.simplediary.mvp.model;

import android.content.Context;

import com.wxdroid.simplediary.entity.Status;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/16.
 */
public interface HotWeiBoModel {

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);

    }


    public void getHotWeiBo(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getHotWeiBoNextPage(Context context, OnDataFinishedListener onDataFinishedListener);


}
