package com.wxdroid.simplediary.mvp.view;

import com.wxdroid.simplediary.entity.User;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/18.
 */
public interface AccoutActivityView {
    public void updateListView(ArrayList<User> userArrayList);

    public void showListView();

    public void hideListView();

    public void showProgressDialog();

    public void hideProgressDialog();

    public void setUpListener();

    public void initListView(ArrayList<User> userArrayList);

    public void finishItself();
}
