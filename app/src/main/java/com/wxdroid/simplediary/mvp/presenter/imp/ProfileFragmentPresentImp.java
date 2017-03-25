package com.wxdroid.simplediary.mvp.presenter.imp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.model.ResultModel;
import com.wxdroid.simplediary.mvp.model.UserModel;
import com.wxdroid.simplediary.mvp.model.imp.UserModelImp;
import com.wxdroid.simplediary.mvp.presenter.ProfileFragmentPresent;
import com.wxdroid.simplediary.mvp.view.ProfileFragmentView;
import com.wxdroid.simplediary.network.NetWorksUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;

/**
 * Created by jinchun on 16/5/16.
 */
public class ProfileFragmentPresentImp implements ProfileFragmentPresent {

    private UserModel mUserModel;
    private ProfileFragmentView mProfileFragmentView;

    public ProfileFragmentPresentImp(ProfileFragmentView profileFragmentView) {
        this.mProfileFragmentView = profileFragmentView;
        this.mUserModel = new UserModelImp();
    }

    @Override
    public void refreshUserDetail(long uid, Context context, boolean loadIcon) {
        if (loadIcon) {
            mProfileFragmentView.showProgressDialog();
        }
        mUserModel.show(uid, context, new UserModel.OnUserDetailRequestFinish() {
            @Override
            public void onComplete(User user) {
                mProfileFragmentView.showScrollView();
                mProfileFragmentView.hideProgressDialog();
                mProfileFragmentView.setUserDetail(user);
                updateuser(user);
            }

            @Override
            public void onError(String error) {
                mProfileFragmentView.showScrollView();
                mProfileFragmentView.hideProgressDialog();
            }
        });
    }
    /**
     * 同步更新用户信息
     * */
    private void updateuser(User user){
//        Map<String, String> map = new HashMap<>();
//        map.put("uid", user.id);
//        map.put("screen_name", user.screen_name);
//        map.put("description", user.description);
//        map.put("url", user.url);
//        map.put("avatar_large", user.avatar_large);
//        map.put("province", ""+user.province);
//        map.put("city", ""+user.city);
//        map.put("location", user.location);
//        map.put("profile_url", user.profile_url);
//        map.put("gender", user.gender);
//        map.put("verified_reason", user.verified_reason);
//        map.put("", "");
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(user));
        NetWorksUtils.updateuser(requestBody, new Observer<ResultModel>() {
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
            public void onNext(ResultModel weiboLoginModel) {
                Log.d("onNext", "" + weiboLoginModel.getCode() + ";" + weiboLoginModel.getInfo());
//                if (weiboLoginModel!=null){
//                    if (weiboLoginModel.getCode()==1){
//                        Intent intent = new Intent();
//                        intent.setClass(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        ToastUtils.showLongToast(LoginActivity.this,weiboLoginModel.getInfo()+"");
//                    }
//                }

            }
        });
    }
}
