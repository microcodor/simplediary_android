package com.wxdroid.simplediary.ui.unlogin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.widget.LoginoutButton;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.SimpleDiaryApplication;
import com.wxdroid.simplediary.model.ResultModel;
import com.wxdroid.simplediary.mvp.model.TokenListModel;
import com.wxdroid.simplediary.mvp.model.imp.TokenListModelImp;
import com.wxdroid.simplediary.mvp.view.LoginActivityView;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.activity.MainActivity;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * 登录页
 * Created by jinchun on 2017/1/2.
 */

public class LoginActivity extends Activity implements LoginActivityView,View.OnClickListener{
    private LoginoutButton mLoginoutBtnSilver;
    private TextView mTokenView;
    private LinearLayout kefu_layout;
    private ImageView erhaji_logo;
    private Button login_copy_btn;

    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    private AuthInfo mAuthInfo;
    private boolean mComeFromAccoutActivity;
    private TokenListModel mTokenListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);
        mTokenListModel = new TokenListModelImp();
        mTokenView = (TextView) findViewById(R.id.result);
        kefu_layout = (LinearLayout) findViewById(R.id.kefu_layout);
        erhaji_logo = (ImageView)findViewById(R.id.erhaji_logo);
        login_copy_btn = (Button) findViewById(R.id.login_copy_btn);
        login_copy_btn.setOnClickListener(this);

        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        // 登陆按钮（样式二：银灰色）
        mLoginoutBtnSilver = (LoginoutButton) findViewById(R.id.login_btn);
        mLoginoutBtnSilver.setWeiboAuthInfo(mAuthInfo, mLoginListener);
        mLoginoutBtnSilver.setLogoutListener(mLogoutListener);
        // 由于 LoginLogouButton 并不保存 Token 信息，因此，如果您想在初次
        // 进入该界面时就想让该按钮显示"注销"，请放开以下代码
        setupToken();
        mLoginoutBtnSilver.setLogoutInfo(accessToken, mLogoutListener);
    }
    Oauth2AccessToken accessToken;
    private void setupToken(){
         accessToken =AccessTokenKeeper.readAccessToken(this);
        if (accessToken != null
                && accessToken.isSessionValid()) {
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                    new java.util.Date(accessToken.getExpiresTime()));
            String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
            if (SimpleDiaryApplication.getInstance().getmWeiboInviteModel()!=null
                    &&SimpleDiaryApplication.getInstance().getmWeiboInviteModel().getStatus()==0){
                mTokenView.setText(String.format(format, accessToken.getUid(), date)+"\n该账号暂无权限，请联系管理员\n关注微博客服，获取邀请码");
            }else {
                mTokenView.setText(String.format(format, accessToken.getUid(), date));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.login_btn:
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                break;
            case R.id.login_copy_btn:
                copyToClipboard();
                break;
        }
    }
    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLoginoutBtnSilver!=null) {
            mLoginoutBtnSilver.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("fisrtstart", true);
        if (mComeFromAccoutActivity) {
            intent.putExtra("comeFromAccoutActivity", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        finish();
    }
    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            LogUtil.d("AuthListener","onComplete");
            accessToken = Oauth2AccessToken.parseAccessToken(values);
            mTokenListModel.addToken(LoginActivity.this,
                    accessToken.getToken(), String.valueOf(accessToken.getExpiresTime()), accessToken.getRefreshToken(), accessToken.getUid());
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                mTokenView.setText(String.format(format, accessToken.getUid(), date));

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
                Map<String, String> map = new HashMap<>();
                map.put("uid",""+accessToken.getUid());
                map.put("accesstoken",accessToken.getToken());
                updateoauth();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.d("AuthListener","onWeiboException:"+e.getMessage());

            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            LogUtil.d("AuthListener","onCancel");
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(LoginActivity.this);
                        mTokenView.setText(R.string.weibosdk_demo_logout_success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mTokenView.setText(R.string.weibosdk_demo_logout_failed);
        }
    }

    /**
     * 更新授权
     * */
    private void updateoauth(){
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(accessToken.getExpiresTime()));
        Map<String, String> map = new HashMap<>();
        map.put("uid",""+accessToken.getUid());
        map.put("accesstoken",""+accessToken.getToken());
        map.put("expirein", ""+accessToken.getExpiresTime());
        map.put("refreshToken", ""+accessToken.getRefreshToken());
        map.put("validtime",date);
        NetWorksUtils.updateoauth(map, new Observer<ResultModel>() {
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
            public void onNext(ResultModel resultModel) {
                if (resultModel!=null){
                    Log.d("onNext", "" + resultModel.getCode() + ";" + resultModel.getInfo());
                    if (resultModel.getCode()==1){
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        //ToastUtil.showShort(LoginActivity.this,resultModel.getInfo()+"");
                        mTokenView.setText(resultModel.getInfo()+"\n关注微博客服：那些年的逗比萌宠\n获取邀请码");
                    }
                }

            }
        });
    }
    private void copyToClipboard(){
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(getResources().getString(R.string.login_developer));
        ToastUtil.showLong(this, "复制开发者微博名称成功，请到微博关注并获取开通权限");
    }
}
