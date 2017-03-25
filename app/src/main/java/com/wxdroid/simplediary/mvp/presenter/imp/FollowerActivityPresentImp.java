package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.mvp.model.FriendShipModel;
import com.wxdroid.simplediary.mvp.model.UserModel;
import com.wxdroid.simplediary.mvp.model.imp.FriendShipModelImp;
import com.wxdroid.simplediary.mvp.model.imp.UserModelImp;
import com.wxdroid.simplediary.mvp.presenter.FollowerActivityPresent;
import com.wxdroid.simplediary.mvp.view.FollowActivityView;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/16.
 */
public class FollowerActivityPresentImp implements FollowerActivityPresent {

    private UserModel mUserModel;
    private FriendShipModel friendShipModel;
    private FollowActivityView mFollowActivityView;

    public FollowerActivityPresentImp(FollowActivityView followActivityView) {
        this.mFollowActivityView = followActivityView;
        this.mUserModel = new UserModelImp();
        this.friendShipModel = new FriendShipModelImp();
    }

    @Override
    public void pullToRefreshData(long uid, Context context) {
        mFollowActivityView.showLoadingIcon();
        mUserModel.followers(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFollowActivityView.hideLoadingIcon();
            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFollowActivityView.hideLoadingIcon();
                mFollowActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.hideLoadingIcon();
                mFollowActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(long uid, Context context) {
        mUserModel.followersNextPage(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFollowActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFollowActivityView.hideFooterView();
                mFollowActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void user_destroy(final User user, final Context context, final ImageView follwerIcon, final TextView follwerText) {
        friendShipModel.user_destroy(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }
        }, true);
    }

    @Override
    public void user_create(final User user, final Context context, final ImageView follwerIcon, final TextView follwerText) {
        friendShipModel.user_create(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }
        }, true);
    }
}
