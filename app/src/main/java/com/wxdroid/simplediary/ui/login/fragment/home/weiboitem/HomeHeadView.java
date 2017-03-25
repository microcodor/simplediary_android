package com.wxdroid.simplediary.ui.login.fragment.home.weiboitem;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wxdroid.simplediary.R;

/**
 * Created by jinchun on 16/4/27.
 */
public class HomeHeadView extends RelativeLayout {

    public HomeHeadView(Context context) {
        super(context);
        init(context);
    }

    public HomeHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.headview_homefragment, this);
    }
}
