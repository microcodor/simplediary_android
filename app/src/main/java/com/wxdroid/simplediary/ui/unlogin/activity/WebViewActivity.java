package com.wxdroid.simplediary.ui.unlogin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.mvp.presenter.WebViewActivityPresent;
import com.wxdroid.simplediary.mvp.presenter.imp.WebViewActivityPresentImp;
import com.wxdroid.simplediary.mvp.view.WebViewActivityView;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.activity.MainActivity;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.webview.X5WebView;

/**
 * Created by jinchun on 16/5/12.
 */

public class WebViewActivity extends Activity implements WebViewActivityView {

    private Context mContext;
    private String sRedirectUri;
    private X5WebView mWeb;
    private ViewGroup mViewParent;

    private String mLoginURL;
    private WebViewActivityPresent mWebViewActivityPresent;
    private boolean mComeFromAccoutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.webview_layout);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);

        mLoginURL = getIntent().getStringExtra("url");
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);
        sRedirectUri = Constants.REDIRECT_URL;
//        if (!mComeFromAccoutActivity){
//            sRedirectUri = "http://hufen.wxdroid.com";
//        }
        mWeb = new X5WebView(this, null);

        mViewParent.addView(mWeb, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        //mWeb = (X5WebView) findViewById(R.id.webview);
        mWebViewActivityPresent = new WebViewActivityPresentImp(this);
        initWebView();
    }

    private void initWebView() {
        WebSettings webSetting = mWeb.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("shouldOverrideUrlLoading:"+url);
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // TODO Auto-generated method stub

                Log.e("should", "request.getUrl().toString() is " + request.getUrl().toString());

                return super.shouldInterceptRequest(view, request);
            }



            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        mWeb.loadUrl(mLoginURL);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
//        mWeb.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
//                    if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
//                        if (mWeb.canGoBack()) {
//                            mWeb.goBack();
//                        } else {
//                            if (!mComeFromAccoutActivity) {
//                                Intent intent = new Intent(WebViewActivity.this, UnLoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                finish();
//                            }
//
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.putExtra("fisrtstart", true);
        if (mComeFromAccoutActivity) {
            intent.putExtra("comeFromAccoutActivity", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        finish();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (isUrlRedirected(url)) {
//                view.stopLoading();
//                mWebViewActivityPresent.handleRedirectedUrl(mContext, url);
//            } else {
//                view.loadUrl(url);
//            }
//            LogUtil.d("shouldOverrideUrlLoading:"+url);
//            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && isUrlRedirected(url)) {
                view.stopLoading();
                mWebViewActivityPresent.handleRedirectedUrl(mContext, url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }


    public boolean isUrlRedirected(String url) {
        return url.startsWith(sRedirectUri);
    }


}
