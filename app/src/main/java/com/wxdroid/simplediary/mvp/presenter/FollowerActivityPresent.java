package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdroid.simplediary.entity.User;

/**
 * Created by jinchun on 16/5/16.
 */
public interface FollowerActivityPresent {
    public void pullToRefreshData(long uid, Context context);

    public void requestMoreData(long uid, Context context);

    public void user_destroy(User user, Context context, ImageView follwerIcon, TextView follwerText);

    public void user_create(User user, Context context, ImageView follwerIcon, TextView follwerText);
}
