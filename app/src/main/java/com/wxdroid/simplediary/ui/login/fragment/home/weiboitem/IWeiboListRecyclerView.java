package com.wxdroid.simplediary.ui.login.fragment.home.weiboitem;

/**
 * Created by jinchun on 16/4/27.
 */
public interface IWeiboListRecyclerView {


    public abstract void initRecyclerView();

    public abstract void firstLoadData();

    public abstract void pullToRefreshData(boolean firstHttpGet);

    public abstract void requestMoreData();

    public abstract void loadMoreData(String response);

    public abstract void updateList();

    public abstract void getlatestWeiBo();

    public abstract void refreshAllData();


}
