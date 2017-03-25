package com.wxdroid.simplediary.ui.login.fragment.profile.setting.admin;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.api.UsersAPI;
import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.model.ResultModel;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.ui.common.BaseActivity;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * 管理类
 * Created by jinchun on 2017/1/5.
 */

public class AdminActivity extends BaseActivity {
    private static final String TAG = "AdminActivity";
    private EditText admin_screen_name;
    private TextView admin_error_text;
    private Button admin_jihuo_btn;

    private UsersAPI mUsersAPI;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);
        mContext = this;
        admin_screen_name = (EditText) findViewById(R.id.admin_screen_name);
        admin_error_text = (TextView) findViewById(R.id.admin_error_text);
        admin_jihuo_btn = (Button) findViewById(R.id.admin_jihuo_btn);

        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));

        admin_jihuo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String screenName = admin_screen_name.getText().toString();
                if (TextUtils.isEmpty(screenName)){
                    admin_error_text.setText("请输入正确的微博昵称");
                    return;
                }
                getUserByScreenName(screenName.trim());
            }
        });


    }

    public void exit(View View) {
        if (getApplicationContext() != null) {
            finish();
        }
    }
    /**
     * 根据昵称获取用户信息
     */
    private void getUserByScreenName(String screenName) {
        long uid = Long.parseLong(AccessTokenKeeper.readAccessToken(this).getUid());
        mUsersAPI.show(screenName, new RequestListener() {
            @Override
            public void onComplete(String response) {
                User user = User.parse(response);
                if (user != null) {
                    String uid  = user.id;
                    setupinvite(user.id,1);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                LogUtil.e(TAG,"onWeiboException:"+info.toString());
                ToastUtil.showShort(mContext, info.toString());
            }
        });
    }
    /**
     * 激活用户
     * */
    private void setupinvite(String uid,int status){
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("status",status+"");
        NetWorksUtils.setupinvite(map, new Observer<ResultModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG,"onError:"+e.getLocalizedMessage());
                ToastUtil.showShort(mContext,""+e.getMessage());
            }

            @Override
            public void onNext(ResultModel resultModel) {
                if (resultModel!=null) {
                    if (resultModel.getCode() == 1) {
                        admin_error_text.setText("用户激活成功！");
                    } else {
                        admin_error_text.setText("" + resultModel.getInfo());
                    }
                }else {
                    admin_error_text.setText("服务器异常！");
                }
            }
        });
    }
}
