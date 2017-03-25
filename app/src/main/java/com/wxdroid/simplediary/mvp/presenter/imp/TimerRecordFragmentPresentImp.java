package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;
import android.util.Log;

import com.qiniu.android.utils.StringUtils;
import com.wxdroid.simplediary.model.bean.WeiboArticleBean;
import com.wxdroid.simplediary.mvp.presenter.TimerRecordFragmentPresent;
import com.wxdroid.simplediary.mvp.view.TimerRecordFragmentView;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.utils.LogUtil;

import rx.Observer;

/**
 * Created by jinchun on 2017/1/10.
 */

public class TimerRecordFragmentPresentImp implements TimerRecordFragmentPresent {
    private Context mContext;

    private TimerRecordFragmentView commonView;

    public TimerRecordFragmentPresentImp(Context context, TimerRecordFragmentView commonFragmentView) {
        this.mContext = context;
        this.commonView = commonFragmentView;
    }

    @Override
    public void pullToRefreshData(String uid, String sendstatus, int page,int size) {
        LogUtil.d("initData-page:" + page);
        commonView.showLoadingIcon();
        int status = -1;
        if (!StringUtils.isNullOrEmpty(sendstatus)){
            if (sendstatus.equals(Constants.SENDSTATUS_ARRAY[0])){
                status= 0;
            }else if (sendstatus.equals(Constants.SENDSTATUS_ARRAY[1])){
                status= 2;
            }else if (sendstatus.equals(Constants.SENDSTATUS_ARRAY[2])){
                status= 1;
            }
        }
        requestTimerList(uid,status, page, 20);
    }

    private void requestTimerList(String uid, int sendstatus, final int page, int rows) {
        NetWorksUtils.timerlist(uid, sendstatus, page, rows, new Observer<WeiboArticleBean>() {
            @Override
            public void onCompleted() {
                commonView.hideLoadingIcon();
                commonView.hideFooterView();
                LogUtil.d("pagelist-onNext-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                commonView.hideLoadingIcon();
                commonView.hideFooterView();
                Log.e("pagelist-onNext-MAIN3", e.getLocalizedMessage() + "--" + e.getMessage());
            }

            @Override
            public void onNext(WeiboArticleBean weiboArticleBean) {
                if (weiboArticleBean != null) {
                    Log.d("pagelist-onNext", "code:" + weiboArticleBean.getCode() + ";");
                    if (weiboArticleBean.getCode() == 1) {
                        if (page!=1) {
                            commonView.updateRecycleView(weiboArticleBean.getData(), "load");

                        } else {
                            commonView.localSaveMaxPage(page);
                            commonView.updateRecycleView(weiboArticleBean.getData(), "refresh");
                        }


                    } else {
                        commonView.showToast("暂无更多记录");
                    }
                }
            }
        });
    }
}
