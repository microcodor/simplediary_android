package com.wxdroid.simplediary.ui.login.fragment.task;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.fragment.message.comment.CommentActivity;
import com.wxdroid.simplediary.ui.login.fragment.message.mention.MentionActivity;
import com.wxdroid.simplediary.ui.unlogin.activity.WebViewActivity;
import com.wxdroid.simplediary.utils.ToastUtil;
import com.wxdroid.simplediary.webview.BrowserActivity;
import com.wxdroid.simplediary.widget.adapter.BaseAdapterHelper;
import com.wxdroid.simplediary.widget.adapter.BaseQuickAdapter;
import com.wxdroid.simplediary.widget.adapter.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinchun on 16/12/26.
 */
public class TaskMainFragment extends Fragment {
    private Activity mActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private QuickAdapter<String> mQuickAdapter;
    private List<String> tablist = new ArrayList<>();


    public TaskMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.taskmainfragment_layout, container, false);
        mActivity = getActivity();
        mContext = getContext();
        initRefreshLayout();
        initView();
        setUpListener();
        return mView;
    }

    private void setUpListener() {


    }


    private void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.message_pulltorefresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void initView(){
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.grid_recyclerView);
        initData();

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mQuickAdapter = new QuickAdapter<String>(getActivity(), R.layout.item_task_main, tablist) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.getTextView(R.id.task_tab_text).setText(item);
            }
        };
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                //intent.putExtra("url","https://api.weibo.com/2/oauth2/authorize?client_id=121960206&response_type=token&display=js&transport=html5&referer=http://hufen.wxdroid.com/");
                                        //https://api.weibo.com/2/oauth2/authorize?client_id=121960206&response_type=token&display=js&transport=html5&referer=http://hufen.wxdroid.com/
                intent.putExtra("url","http://hufen.wxdroid.com/");
                intent.setClass(getActivity(),BrowserActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mQuickAdapter);

    }

    private void initData(){
        for (String str: Constants.TASK_TABS) {
            tablist.add(str);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }


}
