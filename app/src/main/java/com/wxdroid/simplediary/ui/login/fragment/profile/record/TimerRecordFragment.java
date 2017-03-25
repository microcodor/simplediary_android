package com.wxdroid.simplediary.ui.login.fragment.profile.record;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.qiniu.android.utils.StringUtils;
import com.sina.weibo.sdk.codestyle.ErrorCode;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.SimpleDiaryApplication;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;
import com.wxdroid.simplediary.model.bean.TnGouGirlBean;
import com.wxdroid.simplediary.mvp.presenter.CommonFragmentPresent;
import com.wxdroid.simplediary.mvp.presenter.TimerRecordFragmentPresent;
import com.wxdroid.simplediary.mvp.presenter.imp.CommonFragmentPresentImp;
import com.wxdroid.simplediary.mvp.presenter.imp.TimerRecordFragmentPresentImp;
import com.wxdroid.simplediary.mvp.view.CommonFragmentView;
import com.wxdroid.simplediary.mvp.view.TimerRecordFragmentView;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.network.URLDefine;
import com.wxdroid.simplediary.ui.common.FillContent;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.common.login.Constants;
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

public class TimerRecordFragment extends Fragment implements TimerRecordFragmentView {
    private static String TAG = "CommonFragment";
    private TimerRecordFragmentPresent mTimerRecordPresent;

    private View view;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private QuickAdapter<WeiboArticleModel> mQuickAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private List<WeiboArticleModel> weiboArticleModelList = new ArrayList<>();
    private String sendStatus;

    private int currentPage = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_common, container, false);
        mTimerRecordPresent = new TimerRecordFragmentPresentImp(getContext(),this);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            sendStatus = bundle.getString("sendStatus", Constants.SENDSTATUS_ARRAY[0]);
            LogUtil.d("sendStatus", "sendStatus:" + sendStatus);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                if (weiboArticleModelList != null && weiboArticleModelList.size() > 0) {
                    showLoadFooterView();
                }
                //initData(currentPage - 1);
                mTimerRecordPresent.pullToRefreshData(AccessTokenKeeper.readAccessToken(getContext()).getUid(), sendStatus, currentPage+1, Constants.PAGE_SIZE);
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
        mQuickAdapter = new QuickAdapter<WeiboArticleModel>(getActivity(), R.layout.item_timer_record, weiboArticleModelList) {
            @Override
            protected void convert(BaseAdapterHelper helper, final WeiboArticleModel item) {
                helper.getTextView(R.id.send_date_text).setText(""+item.getSendtime());
                ((EmojiTextView) helper.getTextView(R.id.weibo_Content)).setText(Html.fromHtml(item.getContent()));
                FillContent.fillWeiboArticleImgList(item, getContext(), (RecyclerView) helper.getView(R.id.discover_image));
                if (item.getSendstatus()>1){
                    helper.getTextView(R.id.error_text).setText("("+item.getSendstatus()+")"+ErrorCode.getErrorMsg(item.getSendstatus()));
                }else {
                    helper.getTextView(R.id.error_text).setVisibility(View.GONE);
                }
                helper.getView(R.id.bottombar_attitude).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ToastUtil.showShort(getContext(),""+item.getContent());
                        Intent intent = new Intent(getContext(), IdeaActivity.class);
                        intent.putExtra("ideaType", PostService.POST_SERVICE_TIMER_RECORD_REPEAT);
                        intent.putExtra("sendType", "timersend");
                        intent.putExtra("weiboArticleModel", item);
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

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //mTimerRecordPresent.pullToRefreshData(AccessTokenKeeper.readAccessToken(getContext()).getUid(), sendStatus, currentPage, Constants.PAGE_SIZE);
                listener.onRefresh();
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            //LogUtil.d("onRefresh-currentPage:" + currentPage);
            mTimerRecordPresent.pullToRefreshData(AccessTokenKeeper.readAccessToken(getContext()).getUid(), sendStatus, 1, Constants.PAGE_SIZE);
        }
    };


    @Override
    public void updateRecycleView(List<WeiboArticleModel> modelslist, String loadType) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        LogUtil.d("pagelist-onNext  ", "size:" + modelslist.size());
        if (loadType.equals("load")){
            weiboArticleModelList.addAll(modelslist);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
        }else {
            weiboArticleModelList.clear();
            weiboArticleModelList.addAll(modelslist);
            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void localSaveMaxPage(int visibleMaxPage) {
        this.currentPage = visibleMaxPage;
    }

    @Override
    public int getVisibleMaxPage() {
        return currentPage;
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
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, weiboArticleModelList.size(), LoadingFooter.State.Loading, null);
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
}
