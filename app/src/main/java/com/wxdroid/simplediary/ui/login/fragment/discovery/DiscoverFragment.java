package com.wxdroid.simplediary.ui.login.fragment.discovery;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.api.GifUploadAPI;
import com.wxdroid.simplediary.model.SimpleClassifyModel;
import com.wxdroid.simplediary.model.WeiboArticleTypeModel;
import com.wxdroid.simplediary.model.bean.SimpleClassifyBean;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by jinchun on 15/12/26.
 */
public class DiscoverFragment extends Fragment {
    private View mView;
    private RelativeLayout mPublicWeibo;


    private List<SimpleClassifyModel> simpleClassifyModellList = new ArrayList<>();

    public DiscoverFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.discoverfragment_layout, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initClassifyTypeView(){
        //FragmentPagerItems pages = new FragmentPagerItems(getContext());
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(getActivity());
        for (SimpleClassifyModel model : simpleClassifyModellList) {
            Bundle bundle = new Bundle();
            bundle.putInt("classifytype",model.getId());
            bundle.putString("classifyname", model.getClassifyname());
//            FragmentPagerItem fragmentPagerItem = new FragmentPagerItem(model.getClassifyname(),0,CommonFragment.class.toString(),bundle);
//            //pages.add(FragmentPagerItem.of(model.getClassifyname(), CommonFragment.class));
//            pages.add(fragmentPagerItem);
            creator.add(model.getClassifyname(), CommonFragment.class, bundle);
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), creator.create());

        ViewPager viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) mView.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

    private void initData() {
        LogUtil.d("HomeFragment", "onCreateView-list:" + simpleClassifyModellList.size());
        simpleClassifyModellList.clear();
        getclassifies();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = "http://assets.microcodor.com/simplediary/images/6030419837_1483637933018.gif";
//                String localpath = GifUploadAPI.synchronizedGet(url);
//                if (!TextUtils.isEmpty(localpath)){
//                    GifUploadAPI.uploadMultiFile("友谊的小船说翻就翻~~~",localpath,"0", "0",
//                            AccessTokenKeeper.readAccessToken(getContext()).getToken());
//                }
//            }
//        }).start();
    }

    private void getclassifies(){
        NetWorksUtils.getclassifies(new Observer<SimpleClassifyBean>() {
            @Override
            public void onCompleted() {
                Log.d("onNext-onCompleted", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                //异常
                Log.e("onNext-MAIN3", e.getLocalizedMessage() + "--" + e.getMessage());
            }

            @Override
            public void onNext(SimpleClassifyBean simpleClassifyBean) {
                if (simpleClassifyBean!=null){
                    if (simpleClassifyBean.getCode()==1){
                        simpleClassifyModellList.addAll(simpleClassifyBean.getData());
                        initClassifyTypeView();
                    }else {
                        ToastUtil.showShort(getActivity(),""+simpleClassifyBean.getInfo());
                    }
                }
            }
        });
    }

}
