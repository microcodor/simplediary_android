package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;

import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.entity.User;

/**
 * Created by xiangflight on 2016/4/22.
 */
public interface WeiBoArrowPresent {

    public void weibo_destroy(long id, Context context, int position, String weiboGroup);

    public void user_destroy(User user, Context context);

    public void user_create(User user, Context context);

    public void createFavorite(Status status, Context context);

    public void cancalFavorite(int position, Status status, Context context, boolean deleteAnimation);

    //public void cancalFavorite(int position, Status status, Context context);

}
