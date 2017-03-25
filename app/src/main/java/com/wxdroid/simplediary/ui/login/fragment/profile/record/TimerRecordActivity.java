package com.wxdroid.simplediary.ui.login.fragment.profile.record;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.model.SimpleClassifyModel;
import com.wxdroid.simplediary.mvp.presenter.UserActivityPresent;
import com.wxdroid.simplediary.mvp.presenter.imp.UserActivityPresentImp;
import com.wxdroid.simplediary.mvp.view.UserActivityView;
import com.wxdroid.simplediary.ui.common.BaseActivity;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.fragment.discovery.CommonFragment;
import com.wxdroid.simplediary.ui.login.fragment.home.userdetail.UserActivity;
import com.wxdroid.simplediary.ui.login.fragment.home.userdetail.adapter.UserPhotoAdapter;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.wxdroid.simplediary.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.wxdroid.simplediary.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.wxdroid.simplediary.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 定时记录
 * Created by jinchun on 16/4/27.
 */
public class TimerRecordActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.profilefragment_timerrecord_layout);
        initSmartTabLayout();

    }
    private void initSmartTabLayout(){
        //FragmentPagerItems pages = new FragmentPagerItems(getContext());
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for (String str: Constants.SENDSTATUS_ARRAY){
            Bundle bundle = new Bundle();
            bundle.putString("sendStatus", str);
//            FragmentPagerItem fragmentPagerItem = new FragmentPagerItem(model.getClassifyname(),0,CommonFragment.class.toString(),bundle);
//            //pages.add(FragmentPagerItem.of(model.getClassifyname(), CommonFragment.class));
//            pages.add(fragmentPagerItem);
            creator.add(str, TimerRecordFragment.class, bundle);
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), creator.create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }
    public void onArrorClick(View view) {
        finish();
    }

}
