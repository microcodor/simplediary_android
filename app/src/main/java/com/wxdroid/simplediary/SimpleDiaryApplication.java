/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.wxdroid.simplediary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.wxdroid.simplediary.model.WeiboInviteModel;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.SharedPreferencesUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class SimpleDiaryApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static SimpleDiaryApplication instance;

    private List<Activity> mActivityList = new LinkedList<Activity>();

    public static SimpleDiaryApplication getInstance() {
        return instance;
    }

    public static WeiboInviteModel mWeiboInviteModel;

    public static ExecutorService mExecutorService;

    public static ExecutorService getExecutorService(){
        if (mExecutorService==null){
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        return mExecutorService;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCache(new WeakMemoryCache());
        config.memoryCacheSize(20 * 1024 * 1024);//设置内存缓存的最大字节数为 App 最大可用内存的 1/8。
        config.diskCacheSize(200 * 1024 * 1024); // 200 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
        this.instance = this;
        initImageLoader(getApplicationContext());
        registerActivityLifecycleCallbacks(this);
        initCrashReport();
        initX5Browser();
        //使用亮色(light)主题，不使用夜间模式
        boolean setNightMode = (boolean) SharedPreferencesUtil.get(this, "setNightMode", false);
        LogUtil.d("setNightMode = " + setNightMode);
        if (setNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    private void initCrashReport() {
        CrashReport.initCrashReport(getApplicationContext(), "c9f56d565d", false);
    }

    public void initX5Browser() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.e("app", " onCoreInitFinished");
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("app", "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("app", "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("app", "onDownloadProgress:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }




    public void finishAll() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public void recreateAll() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.recreate();
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.d("OnCreate = " + activity.getLocalClassName());
        mActivityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.d("OnDestroyed = " + activity.getLocalClassName());
        mActivityList.remove(activity);
    }

    public void setmWeiboInviteModel(WeiboInviteModel model) {
        this.mWeiboInviteModel = model;
    }

    public WeiboInviteModel getmWeiboInviteModel() {
        return mWeiboInviteModel;
    }
}