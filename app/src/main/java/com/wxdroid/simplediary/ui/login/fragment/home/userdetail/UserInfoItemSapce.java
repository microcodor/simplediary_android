package com.wxdroid.simplediary.ui.login.fragment.home.userdetail;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jinchun on 16/4/18.
 */
public class UserInfoItemSapce extends RecyclerView.ItemDecoration {
    private int space;

    public UserInfoItemSapce(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 1) {
            outRect.set(0, space, 0, 0);
        }

    }
}
