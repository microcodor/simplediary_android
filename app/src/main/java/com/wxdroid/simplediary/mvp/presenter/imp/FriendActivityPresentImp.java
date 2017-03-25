package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.mvp.model.FriendShipModel;
import com.wxdroid.simplediary.mvp.model.UserModel;
import com.wxdroid.simplediary.mvp.model.imp.FriendShipModelImp;
import com.wxdroid.simplediary.mvp.model.imp.UserModelImp;
import com.wxdroid.simplediary.mvp.presenter.FriendActivityPresent;
import com.wxdroid.simplediary.mvp.view.FriendActivityView;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/16.
 */
public class FriendActivityPresentImp implements FriendActivityPresent {

    private UserModel mUserModel;
    private FriendShipModel friendShipModel;
    private FriendActivityView mFriendActivityView;

    public FriendActivityPresentImp(FriendActivityView friendActivityView) {
        this.mFriendActivityView = friendActivityView;
        this.mUserModel = new UserModelImp();
        this.friendShipModel = new FriendShipModelImp();
    }

    @Override
    public void pullToRefreshData(long uid, Context context) {
        mFriendActivityView.showLoadingIcon();
        mUserModel.friends(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFriendActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFriendActivityView.hideLoadingIcon();
                mFriendActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.hideLoadingIcon();
                mFriendActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(long uid, Context context) {
        mUserModel.friendsNextPage(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFriendActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFriendActivityView.hideFooterView();
                mFriendActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void user_destroy(final User user, final Context context, final ImageView friendIcon, final TextView friendText) {
        friendShipModel.user_destroy(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
                //mFriendActivityView.disFocusSuccess(friendIcon, friendText);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
            }
        }, false);
    }

    @Override
    public void user_create(final User user, final Context context, final ImageView friendIcon, final TextView friendText) {
        friendShipModel.user_create(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
            }
        }, false);
    }

}
