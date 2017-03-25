package com.wxdroid.simplediary.mvp.view;

import com.wxdroid.simplediary.entity.User;

/**
 * Created by jinchun on 16/5/16.
 */
public interface ProfileFragmentView {

    public void setUserDetail(User user);

    public void showScrollView();

    public void hideScrollView();

    public void showProgressDialog();

    public void hideProgressDialog();

}
