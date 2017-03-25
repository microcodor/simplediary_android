package com.wxdroid.simplediary.ui.unlogin.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxdroid.simplediary.R;

/**
 * Created by jinchun on 15/12/26.
 */
public class ProfileFragment extends BaseFragment {
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.unlogin_profilefragment_layout, container, false);
        return mView;
    }


}
