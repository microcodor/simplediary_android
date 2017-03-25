package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;

import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.mvp.model.HotWeiBoModel;
import com.wxdroid.simplediary.mvp.model.imp.HotWeiBoModelImp;
import com.wxdroid.simplediary.mvp.presenter.HotWeiBoPresent;
import com.wxdroid.simplediary.mvp.view.HotWeiBoActivityView;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/16.
 */
public class HotWeiBoPresentImp implements HotWeiBoPresent {

    private HotWeiBoModel mHotWeiBoModel;
    private HotWeiBoActivityView mHotWeiBoActivityView;

    public HotWeiBoPresentImp(HotWeiBoActivityView hotWeiBoActivityView) {
        this.mHotWeiBoActivityView = hotWeiBoActivityView;
        mHotWeiBoModel = new HotWeiBoModelImp();
    }

    @Override
    public void pullToRefreshData(Context context) {
        mHotWeiBoActivityView.showLoadingIcon();
        mHotWeiBoModel.getHotWeiBo(context, new HotWeiBoModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHotWeiBoActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<Status> list) {
                mHotWeiBoActivityView.hideLoadingIcon();
                mHotWeiBoActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mHotWeiBoActivityView.hideLoadingIcon();
                mHotWeiBoActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(Context context) {
        mHotWeiBoModel.getHotWeiBoNextPage(context, new HotWeiBoModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHotWeiBoActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<Status> list) {
                mHotWeiBoActivityView.hideFooterView();
                mHotWeiBoActivityView.updateListView(list);
            }

            @Override
            public void onError(String error) {
                mHotWeiBoActivityView.showErrorFooterView();
            }
        });
    }
}
