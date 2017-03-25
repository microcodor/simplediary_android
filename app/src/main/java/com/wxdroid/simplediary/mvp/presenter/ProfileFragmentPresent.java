package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;

/**
 * Created by jinchun on 16/5/16.
 */
public interface ProfileFragmentPresent {
    public void refreshUserDetail(long uid, Context context,boolean loadicon);
}
