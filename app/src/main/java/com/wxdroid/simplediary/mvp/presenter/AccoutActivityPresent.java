package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;

/**
 * Created by jinchun on 16/5/18.
 */
public interface AccoutActivityPresent {

    public void logoutCurrentAccout(Context context);

    public void logout(Context context, String uid);

    public void switchAccout(Context context, String uid);

    public void obtainUserListDetail(Context context);

}
