package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;

import com.wxdroid.simplediary.entity.Group;
import com.wxdroid.simplediary.mvp.model.GroupListModel;
import com.wxdroid.simplediary.mvp.model.imp.GroupListModelImp;
import com.wxdroid.simplediary.mvp.presenter.GroupListPresenter;
import com.wxdroid.simplediary.mvp.view.GroupPopWindowView;
import com.wxdroid.simplediary.mvp.view.HomeFragmentView;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/14.
 */
public class GroupListPresenterImp implements GroupListPresenter {

    private GroupPopWindowView mGroupPopView;
    private GroupListModel mGroupListModel;
    private HomeFragmentView mHomeFragmentView;

    public GroupListPresenterImp(GroupPopWindowView groupPopView) {
        this.mGroupPopView = groupPopView;
        this.mGroupListModel = new GroupListModelImp();
    }

    @Override
    public void updateGroupList(final Context context) {
        mGroupListModel.groupsOnlyOnce(context, new GroupListModel.OnGroupListFinishedListener() {
            @Override
            public void noMoreDate() {
                
            }

            @Override
            public void onDataFinish(ArrayList<Group> groupslist) {
                mGroupPopView.updateListView(groupslist);
            }

            @Override
            public void onError(String error) {
                mGroupPopView.showErrorMessage(error);
            }

        });
    }


}
