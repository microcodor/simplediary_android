package com.wxdroid.simplediary.ui.login.fragment.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.bean.SimpleDiscoverBean;
import com.wxdroid.simplediary.model.bean.TnGouGirlBean;
import com.wxdroid.simplediary.mvp.presenter.CommonFragmentPresent;
import com.wxdroid.simplediary.mvp.presenter.imp.CommonFragmentPresentImp;
import com.wxdroid.simplediary.mvp.view.CommonFragmentView;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.network.URLDefine;
import com.wxdroid.simplediary.ui.common.FillContent;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.fragment.discovery.player.PlayActivity;
import com.wxdroid.simplediary.ui.login.fragment.post.PostService;
import com.wxdroid.simplediary.ui.login.fragment.post.idea.IdeaActivity;
import com.wxdroid.simplediary.utils.DateUtils;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.SharedPreferencesUtil;
import com.wxdroid.simplediary.utils.ToastUtil;
import com.wxdroid.simplediary.widget.adapter.BaseAdapterHelper;
import com.wxdroid.simplediary.widget.adapter.BaseQuickAdapter;
import com.wxdroid.simplediary.widget.adapter.QuickAdapter;
import com.wxdroid.simplediary.widget.emojitextview.EmojiTextView;
import com.wxdroid.simplediary.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wxdroid.simplediary.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wxdroid.simplediary.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wxdroid.simplediary.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * 首页二级子页面
 * Created by jinchun on 2016/11/30.
 */

public class CommonFragment extends Fragment implements CommonFragmentView {
    private static String TAG = "CommonFragment";
    private CommonFragmentPresent mCommonPresent;

    private View view;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private QuickAdapter<SimpleDiscoverModel> mQuickAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private List<SimpleDiscoverModel> simpleDiscoverModelList = new ArrayList<>();
    private int classifytype = 0;
    private String classifyname;

    private int currentPage = 1;
    private int visibleMaxPage = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("classifytype", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_common, container, false);
        mCommonPresent = new CommonFragmentPresentImp(getContext(),this);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            classifytype = bundle.getInt("classifytype", 0);
            LogUtil.d("classifytype", "classifytype:" + classifytype);
            classifyname = bundle.getString("classifyname");
            TAG = TAG + "-" + classifytype;
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        visibleMaxPage = (int) SharedPreferencesUtil.get(getContext(), "visibleMaxPage_" + classifytype, 1);
        currentPage = visibleMaxPage;
        int position = FragmentPagerItem.getPosition(getArguments());
        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(String.valueOf(position));
        initRefresh();
    }


    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            LogUtil.d("上拉加载旧数据！currentPage:" + currentPage);
            if (currentPage != 1) {
                if (simpleDiscoverModelList != null && simpleDiscoverModelList.size() > 0) {
                    showLoadFooterView();
                }
                //initData(currentPage - 1);
                mCommonPresent.pullToRefreshData(classifytype,currentPage-1);
            } else {
                showLoadFooterView();
                showEndFooterView();
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");

    }

    private void initRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setOnRefreshListener(listener);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.content_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mQuickAdapter = new QuickAdapter<SimpleDiscoverModel>(getActivity(), R.layout.item_discover_common, simpleDiscoverModelList) {
            @Override
            protected void convert(BaseAdapterHelper helper, final SimpleDiscoverModel item) {
                ((EmojiTextView) helper.getTextView(R.id.weibo_Content)).setText(Html.fromHtml(item.getContent()));

                if (item.getClassifytype()==6){
                    helper.getView(R.id.discover_image).setVisibility(View.GONE);
                    FillContent.fillVideoImgList(item, getContext(),  helper.getView(R.id.video_cover_layout), helper.getImageView(R.id.video_cover_image));
                    helper.getView(R.id.play_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (classifyname!=null&&classifyname.equals("视频")){
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), PlayActivity.class);
                                intent.putExtra("item", item);
                                startActivity(intent);
                            }
                        }
                    });
                }else {
                    helper.getView(R.id.video_cover_layout).setVisibility(View.GONE);
                    helper.getView(R.id.discover_image).setVisibility(View.VISIBLE);
                    FillContent.fillDiscoverImgList(item, getContext(), (RecyclerView) helper.getView(R.id.discover_image));
                }
                helper.getView(R.id.bottombar_attitude).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ToastUtil.showShort(getContext(),""+item.getContent());
                        Intent intent = new Intent(getContext(), IdeaActivity.class);
                        intent.putExtra("ideaType", PostService.POST_SERVICE_CLONE_SIMPLEDISCOVER);
                        intent.putExtra("sendType", "timersend");
                        intent.putExtra("simplediscover", item);
                        context.startActivity(intent);
                    }
                });
            }
        };
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //ToastUtils.showToast(getActivity(),""+wpPostsModelList.get(position).getPost_title());
//                Intent intent = new Intent();
//                intent.putExtra("postId",simpleDiscoverModelList.get(position).getId());
//                intent.setClass(getActivity(), ArticleActivity.class);
//                startActivity(intent);

            }
        });

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mQuickAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        //RecyclerViewUtils.setHeaderView(mRecyclerView, new HomeHeadView(getContext()));
        /**
         * 加载网络文章数据
         * */
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });
// listener.onRefresh();//添加此方法解决，直接调用setRefreshing(true)无效的问题

        //showLoadingIcon();
        //initData(currentPage);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mCommonPresent.pullToRefreshData(classifytype,currentPage);
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            LogUtil.d("onRefresh-visibleMaxPage:" + visibleMaxPage);
            int page = 1;
            if (simpleDiscoverModelList!=null&&simpleDiscoverModelList.size()> Constants.PAGE_SIZE){
                page =visibleMaxPage+1;
            }
            //initData(visibleMaxPage + 1);
            mCommonPresent.pullToRefreshData(classifytype,page);
        }
    };


    @Override
    public void updateRecycleView(List<SimpleDiscoverModel> modelslist, String loadType) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        LogUtil.d("pagelist-onNext  ", "size:" + modelslist.size());
        LogUtil.d("loadType:"+loadType);
        if (loadType.equals("refresh")) {
            simpleDiscoverModelList.addAll(0, modelslist);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
        }else if (loadType.equals("load")){
            simpleDiscoverModelList.addAll(modelslist);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
        }else {
            simpleDiscoverModelList.clear();
            simpleDiscoverModelList.addAll(modelslist);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void localSaveMaxPage(int classifytype, int visibleMaxPage) {
        this.visibleMaxPage = visibleMaxPage;
        SharedPreferencesUtil.put(getContext(), "visibleMaxPage_" + classifytype, visibleMaxPage);
    }

    @Override
    public int getVisibleMaxPage() {
        return visibleMaxPage;
    }

    @Override
    public void showToast(String info) {
        ToastUtil.showShort(getContext(),""+info);
    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoadingIcon() {

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, simpleDiscoverModelList.size(), LoadingFooter.State.Loading, null);
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }

    @Override
    public void scrollToTop(boolean refreshData) {

    }

    @Override
    public void showRecyclerView() {

    }

    @Override
    public void hideRecyclerView() {

    }

    @Override
    public void showEmptyBackground(String text) {

    }

    @Override
    public void hideEmptyBackground() {

    }

    @Override
    public void popWindowsDestory() {

    }

    /**
     * 天狗云美女图片列表
     */
    private void getTngouList(int page) {
        NetWorksUtils.tngougirllist(page, 20, new Observer<TnGouGirlBean>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
                LogUtil.d("tngougirllist-onNext-onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                LogUtil.d("tngougirllist-onNext-onError:" + e.getMessage());
            }

            @Override
            public void onNext(TnGouGirlBean tnGouGirlBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                LogUtil.d("tngougirllist-onNext:" + tnGouGirlBean.isStatus());
                if (tnGouGirlBean.isStatus()) {
                    if (tnGouGirlBean.getTngou() != null && tnGouGirlBean.getTngou().size() > 0) {
                        switchTGGListToSDModelList(tnGouGirlBean.getTngou());
                    }
                }
            }
        });
    }

    private void switchTGGListToSDModelList(List<TnGouGirlBean.TnGouGirlModel> tngou) {
        List<SimpleDiscoverModel> templist = new ArrayList<>();

        for (int i = 0; i < tngou.size(); i++) {
            SimpleDiscoverModel model = new SimpleDiscoverModel();
            switchTGGToSDModel(tngou.get(i), model);
            templist.add(model);
        }
        if (templist != null && templist.size() > 0) {
            simpleDiscoverModelList.addAll(0, templist);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
        }
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
}
