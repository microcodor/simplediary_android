package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;

import com.wxdroid.simplediary.entity.Status;

/**
 * Created by jinchun on 16/6/26.
 */
public interface DetailActivityPresent {
    public void pullToRefreshData(int groupId, Status status, Context context);

    public void requestMoreData(int groupId, Status status, Context context);
}
