package com.wxdroid.simplediary.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.SimpleDiaryApplication;
import com.wxdroid.simplediary.model.ResultModel;
import com.wxdroid.simplediary.model.bean.WeiboInviteBean;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.login.activity.MainActivity;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.WeiBoCreateBean;
import com.wxdroid.simplediary.ui.unlogin.activity.UnLoginActivity;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observer;

/**
 * Created by jinchun on 16/5/4.
 */
public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomeActivity";
    private Intent mStartIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        if (AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            verifyinvite();
        } else {
            mStartIntent = new Intent(WelcomeActivity.this, UnLoginActivity.class);
            toActivity();
        }
    }


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(mStartIntent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    private void verifyinvite(){
        NetWorksUtils.verifyinvite(AccessTokenKeeper.readAccessToken(this).getUid(),
                new Observer<WeiboInviteBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG,"verifyinvite:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG,"verifyinvite:"+e.getMessage());
                        ToastUtil.showShort(WelcomeActivity.this,""+e.getMessage());
                    }

                    @Override
                    public void onNext(WeiboInviteBean resultModel) {
                        LogUtil.d(TAG,"verifyinvite:"+resultModel.getCode()+"info:"+resultModel.getInfo());
                        if (resultModel!=null) {
                            SimpleDiaryApplication.getInstance().setmWeiboInviteModel(resultModel.getData());
                            if (resultModel.getCode() == 1) {
                                mStartIntent = new Intent(WelcomeActivity.this, MainActivity.class);

                            } else {
                                //ToastUtil.showShort(WelcomeActivity.this,""+resultModel.getInfo());
                                mStartIntent = new Intent(WelcomeActivity.this, UnLoginActivity.class);
                            }
                            toActivity();
                        }
                    }
                });
    }
    private void toActivity(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(Message.obtain());
            }
        }, 500);
    }
}
