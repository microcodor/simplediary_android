package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;

/**
 * Created by jinchun on 16/5/15.
 */
public interface CommentActivityPresent {
    public void pullToRefreshData(int groupId, Context context);

    public void requestMoreData(int groupId, Context context);

}
