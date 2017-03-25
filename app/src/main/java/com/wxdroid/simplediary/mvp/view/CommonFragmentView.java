package com.wxdroid.simplediary.mvp.view;

import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinchun on 16/5/14.
 */
public interface CommonFragmentView {
    /**
     * 刷新数据集
     * @param loadType  "refresh" 下拉刷新；"load" 上拉加载
     */
    public void updateRecycleView(List<SimpleDiscoverModel> modelslist,String loadType);

    /**
     * 存储本地维护的各个分类下的已刷新的最新页数
     * */
    void localSaveMaxPage(int classifytype, int visibleMaxPage);

    int getVisibleMaxPage();


    void showToast(String info);



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


    /**
     * 滑动到顶部
     */
    public void scrollToTop(boolean refreshData);


    public void showRecyclerView();

    public void hideRecyclerView();

    public void showEmptyBackground(String text);

    public void hideEmptyBackground();

    public void popWindowsDestory();

}
