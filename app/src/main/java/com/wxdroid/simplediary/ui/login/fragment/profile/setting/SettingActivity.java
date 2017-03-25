package com.wxdroid.simplediary.ui.login.fragment.profile.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxdroid.simplediary.SimpleDiaryApplication;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.mvp.presenter.SettingActivityPresent;
import com.wxdroid.simplediary.mvp.presenter.imp.SettingActivityPresentImp;
import com.wxdroid.simplediary.mvp.view.SettingActivityView;
import com.wxdroid.simplediary.ui.common.BaseActivity;
import com.wxdroid.simplediary.ui.login.fragment.profile.setting.accoutlist.AccoutActivity;
import com.wxdroid.simplediary.ui.login.fragment.profile.setting.admin.AdminActivity;
import com.wxdroid.simplediary.utils.SharedPreferencesUtil;
import com.wxdroid.simplediary.utils.ToastUtil;

import org.w3c.dom.Text;

/**
 * Created by jinchun on 2016/1/7.
 */
public class SettingActivity extends BaseActivity implements SettingActivityView {

    private Context mContext;
    private RelativeLayout mExitLayout;
    private ImageView mBackImageView;
    private RelativeLayout mClearCache;
    private SettingActivityPresent mSettingActivityPresent;
    private RelativeLayout mAccountLayout;
    private CheckBox mCheckBox;

    private RelativeLayout mAdminLayout;

    private RelativeLayout mFankuilayout;
    private RelativeLayout mAboutlayout;

    private TextView version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        mContext = this;
        mSettingActivityPresent = new SettingActivityPresentImp(this);
        mExitLayout = (RelativeLayout) findViewById(R.id.exitLayout);
        mBackImageView = (ImageView) findViewById(R.id.toolbar_back);
        mClearCache = (RelativeLayout) findViewById(R.id.clearCache_layout);
        mAccountLayout = (RelativeLayout) findViewById(R.id.accoutlayout);
        mCheckBox = (CheckBox) findViewById(R.id.nightMode_cb);

        mAdminLayout = (RelativeLayout) findViewById(R.id.admin_layout);
        mFankuilayout = (RelativeLayout) findViewById(R.id.fankui_layout);
        mAboutlayout = (RelativeLayout) findViewById(R.id.about_layout);

        version_name = (TextView) findViewById(R.id.version_name);
        version_name.setText(getVersion(this));

        initView();
        setUpListener();
    }

    private void initView() {
        boolean isNightMode = (boolean) SharedPreferencesUtil.get(this, "setNightMode", false);
        mCheckBox.setChecked(isNightMode);

        if (SimpleDiaryApplication.getInstance().getmWeiboInviteModel()!=null
                &&SimpleDiaryApplication.getInstance().getmWeiboInviteModel().getStatus()==3){
            mAdminLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setUpListener() {
        mExitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要退出微博？")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //mSettingActivityPresent.logout(mContext);
                                ((SimpleDiaryApplication) getApplication()).finishAll();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingActivityPresent.clearCache(mContext);
            }
        });
        mAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AccoutActivity.class);
                startActivity(intent);
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.put(mContext, "setNightMode", true);
                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    SharedPreferencesUtil.put(mContext, "setNightMode", false);
                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                ((SimpleDiaryApplication) mContext.getApplicationContext()).recreateAll();
            }
        });
        mAdminLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        mFankuilayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showLong(SettingActivity.this, "反馈意见请私信微博客服：@那些年的逗比萌宠");
            }
        });
        mAboutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastUtil.showShort(SettingActivity.this, "暂无新版本！");
            }
        });
    }
    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "未知";
        }
    }

}
