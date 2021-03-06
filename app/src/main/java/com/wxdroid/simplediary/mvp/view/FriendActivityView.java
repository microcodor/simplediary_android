package com.wxdroid.simplediary.mvp.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdroid.simplediary.entity.User;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/16.
 */
public interface FriendActivityView {
    public void updateListView(ArrayList<User> userlist);

    /**
     * 显示loading动画
     */
    public void showLoadingIcon();

    /**
     * 隐藏loadding动画
     */
    public void hideLoadingIcon();

    /**
     * 显示正在加载的FooterView
     */
    public void showLoadFooterView();

    /**
     * 隐藏FooterView
     */
    public void hideFooterView();

    /**
     * 显示FooterView，提示没有任何内容了
     */
    public void showEndFooterView();

    /**
     * 显示FooterView，提示没有网络
     */
    public void showErrorFooterView();

    public void updateRealtionShip(Context context, User user, ImageView icon, TextView text);
}
