package com.wxdroid.simplediary.mvp.presenter;

import android.content.Context;

/**
 * Created by jinchun on 16/4/27.
 */
public interface HotWeiBoPresent {
    public void pullToRefreshData(Context context);

    public void requestMoreData(Context context);
    
}
