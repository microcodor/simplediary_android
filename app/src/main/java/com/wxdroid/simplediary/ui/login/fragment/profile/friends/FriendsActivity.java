package com.wxdroid.simplediary.ui.login.fragment.profile.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.api.StatusesAPI;
import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.mvp.presenter.FriendActivityPresent;
import com.wxdroid.simplediary.mvp.presenter.imp.FriendActivityPresentImp;
import com.wxdroid.simplediary.mvp.view.FriendActivityView;
import com.wxdroid.simplediary.ui.common.BaseActivity;
import com.wxdroid.simplediary.ui.common.FillContent;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wxdroid.simplediary.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wxdroid.simplediary.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wxdroid.simplediary.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/1.
 */
public class FriendsActivity extends BaseActivity implements FriendActivityView {

    public FriendsAdapter mAdapter;
    private ArrayList<User> mDatas;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public StatusesAPI mStatusesAPI;
    public boolean mRefrshAllData;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private FriendActivityPresent mFriendActivityPresent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends_layout);
        mContext = this;
        mFriendActivityPresent = new FriendActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mFriendActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
            }
        });
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFriendActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
            }
        });
    }


    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new FriendsAdapter(mDatas, mContext) {
            @Override
            public void friendLayoutClick(User user, int position, ImageView friendIcon, TextView friendText) {
                friendIcon.setImageResource(R.drawable.bga_refresh_loading02);
                friendText.setText("");
                if (user.following) {
                    mFriendActivityPresent.user_destroy(user, mContext, friendIcon, friendText);
                } else {
                    mFriendActivityPresent.user_create(user, mContext, friendIcon, friendText);
                }
            }
        };
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

    }


    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mFriendActivityPresent.requestMoreData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
            }
        }
    };

    public void onArrorClick(View view) {
        finish();
    }

    @Override
    public void updateListView(ArrayList<User> userlist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = userlist;
        mAdapter.setData(userlist);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(FriendsActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
    public void updateRealtionShip(Context context,User user, ImageView icon, TextView text) {
        FillContent.updateRealtionShip(context,user, icon, text);
    }


}
