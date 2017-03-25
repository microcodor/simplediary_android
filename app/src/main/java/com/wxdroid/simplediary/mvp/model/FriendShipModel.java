package com.wxdroid.simplediary.mvp.model;

import android.content.Context;

import com.wxdroid.simplediary.entity.User;

/**
 * Created by jinchun on 16/6/6.
 */
public interface FriendShipModel {

    interface OnRequestListener {
        void onSuccess();

        void onError(String error);
    }

    public void user_destroy(User user, Context context, OnRequestListener onRequestListener, boolean updateCache);

    public void user_create(User user, Context context, OnRequestListener onRequestListener, boolean updateCache);

}
