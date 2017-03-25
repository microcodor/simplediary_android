package com.wxdroid.simplediary.mvp.view;

import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;

import java.util.List;

/**
 * Created by jinchun on 16/5/14.
 */
public interface TimerRecordFragmentView {
    /**
     * 刷新数据集
     * @param loadType  "refresh" 下拉刷新；"load" 上拉加载
     */
     void updateRecycleView(List<WeiboArticleModel> modelslist, String loadType);

    /**
     * 维护的各个分类下的已刷新的最新页数
     * */
    void localSaveMaxPage(int visibleMaxPage);

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

}
