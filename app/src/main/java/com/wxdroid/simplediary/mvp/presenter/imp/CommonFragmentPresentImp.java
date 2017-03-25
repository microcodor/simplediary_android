package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.bean.SimpleDiscoverBean;
import com.wxdroid.simplediary.model.bean.TnGouGirlBean;
import com.wxdroid.simplediary.model.bean.ToutiaoBean;
import com.wxdroid.simplediary.mvp.presenter.CommonFragmentPresent;
import com.wxdroid.simplediary.mvp.view.CommonFragmentView;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.network.URLDefine;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.utils.DateUtils;
import com.wxdroid.simplediary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by jinchun on 2017/1/10.
 */

public class CommonFragmentPresentImp implements CommonFragmentPresent {
    private Context mContext;

    private CommonFragmentView commonView;

    public CommonFragmentPresentImp(Context context, CommonFragmentView commonFragmentView) {
        this.mContext = context;
        this.commonView = commonFragmentView;
    }

    @Override
    public void pullToRefreshData(int classifytype, int page) {
        LogUtil.d("initData-page:" + page);
        commonView.showLoadingIcon();
        if (classifytype == 3) {//美女图片
            getTngouList(classifytype, page, 20);

        } else if (classifytype == 5) {
            toutiaolist("yule", Constants.JUHE_APP_KEY);
        } else if (classifytype != 0) {
            requestDiscoverList(classifytype, page, 20);

        }
    }

    private void requestDiscoverList(final int classifytype, final int page, int rows) {
        NetWorksUtils.pagelist(classifytype, page, rows, new Observer<SimpleDiscoverBean>() {
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
            public void onNext(SimpleDiscoverBean simpleDiscoverBean) {
                if (simpleDiscoverBean != null) {
                    Log.d("pagelist-onNext", "code:" + simpleDiscoverBean.getCode() + ";");
                    if (simpleDiscoverBean.getCode() == 1) {
                        if (page < commonView.getVisibleMaxPage()) {
                            commonView.updateRecycleView(simpleDiscoverBean.getData(), "load");

                        }else if (page == commonView.getVisibleMaxPage()) {
                            commonView.updateRecycleView(simpleDiscoverBean.getData(), "clear");

                        } else {
                            commonView.localSaveMaxPage(classifytype, page);

                            commonView.updateRecycleView(simpleDiscoverBean.getData(), "refresh");
                        }


                    } else {
                        commonView.showToast("分类：" + classifytype + " 下暂无相关资源");
                    }
                }
            }
        });
    }

    /**
     * 天狗云美女图片列表
     */
    private void getTngouList(final int classifytype, final int page, int rows) {
        NetWorksUtils.tngougirllist(page, rows, new Observer<TnGouGirlBean>() {
            @Override
            public void onCompleted() {
                commonView.hideLoadingIcon();
                commonView.hideFooterView();
                LogUtil.d("tngougirllist-onNext-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                commonView.hideLoadingIcon();
                commonView.hideFooterView();
                LogUtil.d("tngougirllist-onNext-onError:" + e.getMessage());
            }

            @Override
            public void onNext(TnGouGirlBean tnGouGirlBean) {
                LogUtil.d("tngougirllist-onNext:" + tnGouGirlBean.isStatus());
                if (tnGouGirlBean.isStatus()) {
                    if (tnGouGirlBean.getTngou() != null && tnGouGirlBean.getTngou().size() > 0) {
                        List<SimpleDiscoverModel> list = switchTGGListToSDModelList(tnGouGirlBean.getTngou());
                        if (page < commonView.getVisibleMaxPage()) {
                            commonView.updateRecycleView(list, "load");

                        } else {
                            commonView.localSaveMaxPage(classifytype, page);

                            commonView.updateRecycleView(list, "refresh");
                        }
                    }
                }
            }
        });
    }

    private List<SimpleDiscoverModel> switchTGGListToSDModelList(List<TnGouGirlBean.TnGouGirlModel> tngou) {
        List<SimpleDiscoverModel> templist = new ArrayList<>();

        for (int i = 0; i < tngou.size(); i++) {
            SimpleDiscoverModel model = new SimpleDiscoverModel();
            switchTGGToSDModel(tngou.get(i), model);
            templist.add(model);
        }
        return templist;
    }

    private void switchTGGToSDModel(TnGouGirlBean.TnGouGirlModel tnGouGirlModel, SimpleDiscoverModel model) {
        if (tnGouGirlModel != null) {
            model.setId(tnGouGirlModel.getId());
            model.setClassifytype(3);
            if (!TextUtils.isEmpty(tnGouGirlModel.getImg())) {
                model.setType(1);
                model.setImageurls(URLDefine.TNGOU_IMAGE_HOST + tnGouGirlModel.getImg());
            } else {
                model.setType(0);
            }
            model.setContent(tnGouGirlModel.getTitle());
            model.setCreatetime(DateUtils.getDateTimeFromMillisecond(tnGouGirlModel.getTime()));

        }
    }

    /**
     * 聚合娱乐
     */
    private void toutiaolist(String type, String key) {
        NetWorksUtils.toutiaolist(type, key, new Observer<ToutiaoBean>() {
            @Override
            public void onCompleted() {
                commonView.hideLoadingIcon();
                commonView.hideFooterView();
                LogUtil.d("tngougirllist-onNext-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                commonView.hideLoadingIcon();
                commonView.hideFooterView();
                LogUtil.d("tngougirllist-onNext-onError:" + e.getMessage());
            }

            @Override
            public void onNext(ToutiaoBean toutiaoBean) {
                LogUtil.d("tngougirllist-onNext:" + toutiaoBean.getError_code());
                if (toutiaoBean != null) {
                    if (toutiaoBean.getError_code() == 0) {
                        if (toutiaoBean.getResult().getData() != null && toutiaoBean.getResult().getData().size() > 0) {
                            List<SimpleDiscoverModel> list = switchToutiaoListToSDModelList(toutiaoBean.getResult().getData());
                            commonView.updateRecycleView(list, "clear");
                        }
                    }
                }
            }
        });
    }

    private List<SimpleDiscoverModel> switchToutiaoListToSDModelList(List<ToutiaoBean.ResultBean.DataBean> list) {
        List<SimpleDiscoverModel> templist = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            SimpleDiscoverModel model = new SimpleDiscoverModel();
            switchToutiaoToSDModel(list.get(i), model);
            templist.add(model);
        }
        return templist;
    }

    private void switchToutiaoToSDModel(ToutiaoBean.ResultBean.DataBean dataBean, SimpleDiscoverModel model) {
        if (dataBean != null) {
            model.setId(1996632971);
            model.setClassifytype(5);
            if (!TextUtils.isEmpty(dataBean.getThumbnail_pic_s())) {
                model.setType(1);
                model.setImageurls(dataBean.getThumbnail_pic_s());
            } else {
                model.setType(0);
            }
            model.setContent(dataBean.getTitle());
            model.setCreatetime(dataBean.getDate());

        }
    }
}
