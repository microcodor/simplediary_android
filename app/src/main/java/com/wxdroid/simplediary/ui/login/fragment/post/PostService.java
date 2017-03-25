package com.wxdroid.simplediary.ui.login.fragment.post;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.StringUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.SimpleDiaryApplication;
import com.wxdroid.simplediary.api.CommentsAPI;
import com.wxdroid.simplediary.api.GifUploadAPI;
import com.wxdroid.simplediary.api.StatusesAPI;
import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.model.ResultModel;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;
import com.wxdroid.simplediary.network.NetWorksUtils;
import com.wxdroid.simplediary.network.URLDefine;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.activity.MainActivity;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.CommentReplyBean;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.WeiBoCommentBean;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.WeiBoCreateBean;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.SharedPreferencesUtil;
import com.wxdroid.simplediary.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import rx.Observer;


/**
 * Created by jinchun on 16/5/8.
 */
public class PostService extends Service {


    private Context mContext;
    private NotificationManager mSendNotifity;
    private StatusesAPI mStatusesAPI;

    public String mPostType;

    public static final String POST_SERVICE_REPOST_STATUS = "转发微博";
    public static final String POST_SERVICE_CREATE_WEIBO = "发微博";
    public static final String POST_SERVICE_COMMENT_STATUS = "评论微博";
    public static final String POST_SERVICE_REPLY_COMMENT = "回复评论";

    //简日记特殊功能
    public static final String POST_SERVICE_TIMER_SEND = "定时发送";
    public static final String POST_SERVICE_CLONE_WEIBO = "克隆微博";
    public static final String POST_SERVICE_CLONE_SIMPLEDISCOVER = "克隆精选";
    public static final String POST_SERVICE_TIMER_RECORD_REPEAT = "编辑重发";


    /**
     * 微博发送成功
     */
    private static final int SEND_STATUS_SUCCESS = 1;
    /**
     * 微博发送失败
     */
    private static final int SEND_STATUS_ERROR = 2;
    /**
     * 微博发送中
     */
    private static final int SEND_STATUS_SEND = 3;

    private Oauth2AccessToken accessToken;

    private String defaulttimertype;

    private WeiBoCreateBean repeatTimerBean;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        accessToken = AccessTokenKeeper.readAccessToken(mContext);
        if (accessToken == null || !accessToken.isSessionValid()) {
            ToastUtil.showShort(mContext, "授权失效，请重新授权");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, accessToken);
        if (intent != null) {
            mPostType = intent.getStringExtra("postType");
        }
        showSendNotifiy();
        defaulttimertype = (String)SharedPreferencesUtil.get(mContext, "defaulttimertype", Constants.DEFAULT_TIMER_TYPE);

        switch (mPostType) {
            case POST_SERVICE_CREATE_WEIBO://此处
                WeiBoCreateBean createWeiBo = intent.getParcelableExtra("weiBoCreateBean");
                if (createWeiBo.selectImgList == null || createWeiBo.selectImgList.size() == 0) {
                    sendTextContent(createWeiBo);
                } else {
                    sendImgTextContent(createWeiBo);
                }
                break;
            case POST_SERVICE_REPOST_STATUS:
                WeiBoCreateBean repostBean = intent.getParcelableExtra("weiBoCreateBean");
                repost(repostBean);
                break;
            case POST_SERVICE_REPLY_COMMENT:
                CommentReplyBean commentReplyBean = intent.getParcelableExtra("commentReplyBean");
                replyComment(commentReplyBean);
                break;
            case POST_SERVICE_COMMENT_STATUS:
                WeiBoCommentBean weiBoCommentBean = intent.getParcelableExtra("weiBoCommentBean");
                commentWeiBo(weiBoCommentBean);
                break;
            case POST_SERVICE_TIMER_SEND:
                WeiBoCreateBean timerWeiBo = intent.getParcelableExtra("weiboTimerBean");
                if (!TextUtils.isEmpty(defaulttimertype)&&defaulttimertype.equals(mContext.getResources().getString(R.string.idea_timer_text))) {//定时
                    if (timerWeiBo.selectImgList == null || timerWeiBo.selectImgList.size() == 0) {
                        timerSendTextContent(timerWeiBo);
                    } else {
                        timerSendImgTextContent(timerWeiBo);
                    }
                } else {//实时
                    if (timerWeiBo.selectImgList == null || timerWeiBo.selectImgList.size() == 0) {
                        sendTextContent(timerWeiBo);
                    } else {
                        sendImgTextContent(timerWeiBo);
                    }
                }

                break;
            case POST_SERVICE_CLONE_WEIBO://weiBoCloneBean
                WeiBoCreateBean cloneWeiBo = intent.getParcelableExtra("weiBoCloneBean");
                setupTimerCloneSendWeibo(cloneWeiBo);
                break;
            case POST_SERVICE_CLONE_SIMPLEDISCOVER://simpleDiscoverCloneBean
                WeiBoCreateBean cloneSimpleDiscover = intent.getParcelableExtra("simpleDiscoverCloneBean");
                setupTimerCloneSendSimpleDiscover(cloneSimpleDiscover);
                break;
            case POST_SERVICE_TIMER_RECORD_REPEAT://
                repeatTimerBean = intent.getParcelableExtra("repeatTimerBean");
                setupRepeatSendWeiboArticleModel(repeatTimerBean);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * clone 微博
     */
    private void setupTimerCloneSendWeibo(WeiBoCreateBean cloneWeiBo) {
        if (!TextUtils.isEmpty(defaulttimertype)&&defaulttimertype.equals(mContext.getResources().getString(R.string.idea_current_text))) {//实时发送
            if (cloneWeiBo.status != null) {
                if (cloneWeiBo.status.retweeted_status != null) {
                    cloneSendWeibo(cloneWeiBo.content, cloneWeiBo.status.retweeted_status);
                } else {
                    cloneSendWeibo(cloneWeiBo.content, cloneWeiBo.status);
                }
            }
        } else {//定时发送
            cloneTimerSend(setupCloneWeiBoCreateBeanToMap(cloneWeiBo));
        }
    }

    private void setupTimerCloneSendSimpleDiscover(WeiBoCreateBean cloneSimpleDiscover) {
        if (!TextUtils.isEmpty(defaulttimertype)&&defaulttimertype.equals(mContext.getResources().getString(R.string.idea_current_text))) {//实时发送
            if (cloneSimpleDiscover != null) {
                if (cloneSimpleDiscover.simpleDiscoverModel!=null) {
                    cloneSendSimpleDiscover(cloneSimpleDiscover.content, cloneSimpleDiscover.simpleDiscoverModel);
                }
            }
        } else {//定时发送
            cloneTimerSend(setupCloneSimpleDiscoverCreateBeanToMap(cloneSimpleDiscover));
        }
    }

    private void setupRepeatSendWeiboArticleModel(WeiBoCreateBean repeatTimerBean) {
        if (!TextUtils.isEmpty(defaulttimertype)&&defaulttimertype.equals(mContext.getResources().getString(R.string.idea_current_text))) {//实时发送
            if (repeatTimerBean != null) {
                repeatSendWeiboArticleModel(repeatTimerBean.content, repeatTimerBean.weiboArticleModel);
            }
        } else {//定时发送
            repeatTimerSend(setupRepeatTimerCreateBeanToMap(repeatTimerBean,0));
        }
    }
    /**
     * 定时文本微博
     */
    private void timerSendTextContent(WeiBoCreateBean timerWeiBo) {
        timerSend(timerWeiBo, null);
    }

    /**
     * 创建微博定时存储到私有服务器
     */
    private void timerSend(WeiBoCreateBean timerWeiBo, String imageUrl) {
        NetWorksUtils.timersend(setupWeiBoCreateBeanToMap(timerWeiBo, imageUrl), new Observer<ResultModel>() {
            @Override
            public void onCompleted() {
                LogUtil.d("PostService", "timerSendTextContent:onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("PostService", "timerSendTextContent:" + e.getMessage());
                onRequestError(e.getMessage(), "定时发送失败");
            }

            @Override
            public void onNext(ResultModel resultModel) {
                if (resultModel.getCode() == 1) {
                    onRequestComplete();
                } else {
                    ToastUtil.showShort(mContext, "" + resultModel.getInfo());
                }
            }
        });
    }

    /**
     * 定时图片微博
     */
    private void timerSendImgTextContent(final WeiBoCreateBean weiBoCreateBean) {
        if (weiBoCreateBean.content.isEmpty() && weiBoCreateBean.status == null) {
            weiBoCreateBean.content = "分享图片";
        } else if (weiBoCreateBean.content.isEmpty() && weiBoCreateBean.status != null) {
            weiBoCreateBean.content = "转发微博";
        }
        //本地展示图片的同时，上传图片，获取7牛token
        getToken(weiBoCreateBean);
    }

    private void getToken(final WeiBoCreateBean weiBoCreateBean) {
        NetWorksUtils.getqiniutoken(AccessTokenKeeper.readAccessToken(mContext).getUid(),
                new Observer<ResultModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultModel resultModel) {
                        if (resultModel != null && resultModel.getCode() == 1) {
                            setupQiniu(weiBoCreateBean, resultModel.getData());
                        } else {
                            ToastUtil.showShort(mContext, "" + resultModel.getInfo());
                        }
                    }
                });
    }

    /**
     * 七牛上传
     */
    private void setupQiniu(final WeiBoCreateBean weiBoCreateBean, String token) {
        Configuration config = new Configuration.Builder().zone(Zone.zone0).build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        if (weiBoCreateBean != null
                && weiBoCreateBean.selectImgList != null
                && weiBoCreateBean.selectImgList.size() > 0) {
            File imageFile = weiBoCreateBean.selectImgList.get(0).getImageFile();
            if (imageFile.exists()) {
                String fileName = imageFile.getName();
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String key = "simplediary/images/" + AccessTokenKeeper.readAccessToken(mContext).getUid() + "_" + System.currentTimeMillis() + "." + prefix;
                //String qiniutoken = "wl7S9Q83nYIGrQRm1HpnxwRruTW__ck1D2s57nIQ:LIIPEGI72L9zAnHOfBvm7VIZr68=:eyJzY29wZSI6ImFzc2V0cyIsImRlYWRsaW5lIjoxNDgzNTAzMDQ0fQ==";
                uploadManager.put(imageFile,
                        key,
                        token, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                try {
                                    if (info.isOK()) {
                                        LogUtil.d("setupQiniu-info:complete");
                                        LogUtil.d("setupQiniu-info:" + info.toString());
                                        LogUtil.d("setupQiniu:" + response.toString());
                                        if (response.has("key")) {
                                            String imageUrl = URLDefine.IMAGE_SERVER + response.getString("key");
                                            timerSend(weiBoCreateBean, imageUrl);
                                        }
                                    } else {
                                        ToastUtil.showShort(mContext, "" + info.error);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, null);
            }
        }

    }

    /**
     * 消息类型：0-文本；1-单图片；2-视频；3-多图片
     */
    private HashMap<String, String> setupWeiBoCreateBeanToMap(WeiBoCreateBean bean, String imageUrl) {
        HashMap<String, String> map = new HashMap<>();
        if (bean != null && accessToken != null && accessToken.isSessionValid()) {
            map.put("weibouserid", "" + accessToken.getUid());
            map.put("accesstoken", accessToken.getToken());
            int msgType = 0;
            if (!TextUtils.isEmpty(imageUrl)) {
                msgType = 1;
                map.put("imageurls", imageUrl);
            }
            map.put("msgtype", msgType + "");
            if (bean.content.isEmpty() && bean.status == null) {
                bean.content = "分享图片";
            }
//            } else if (bean.content.isEmpty() && bean.status != null) {
//                bean.content = "转发微博";
//            }
            try {
                map.put("content", URLEncoder.encode(bean.content, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("latitude", "");
            map.put("longitude", "");
            map.put("sendtime", bean.sendTime);

        }
        return map;
    }

    /**
     * 重发，修改weiboArticleModel
     * */
    private void repeatTimerSend(HashMap<String, String> map){
        NetWorksUtils.updatearticle(map, new Observer<ResultModel>() {
            @Override
            public void onCompleted() {
                LogUtil.d("PostService", "repeatTimerSend:onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("PostService", "repeatTimerSend:" + e.getMessage());
                onRequestError(e.getMessage(), "重发失败");
            }

            @Override
            public void onNext(ResultModel resultModel) {
                if (resultModel.getCode() == 1) {
                    onRequestComplete();
                } else {
                    ToastUtil.showShort(mContext, "" + resultModel.getInfo());
                }
            }
        });
    }

    /**
     * clone微博服务器存储接口
     **/
    private void cloneTimerSend(HashMap<String, String> map) {
        NetWorksUtils.timersend(map, new Observer<ResultModel>() {
            @Override
            public void onCompleted() {
                LogUtil.d("PostService", "timerSendTextContent:onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("PostService", "timerSendTextContent:" + e.getMessage());
                onRequestError(e.getMessage(), "定时发送失败");
            }

            @Override
            public void onNext(ResultModel resultModel) {
                if (resultModel.getCode() == 1) {
                    onRequestComplete();
                } else {
                    ToastUtil.showShort(mContext, "" + resultModel.getInfo());
                }
            }
        });
    }


    /**
     * clone微博服务器存储数据封装
     * msgType 消息类型：0-文本；1-单图片；2-视频；3-多图片
     */
    private HashMap<String, String> setupCloneWeiBoCreateBeanToMap(WeiBoCreateBean bean) {
        HashMap<String, String> map = new HashMap<>();
        if (bean != null && accessToken != null && accessToken.isSessionValid()) {
            map.put("weibouserid", "" + accessToken.getUid());
            map.put("accesstoken", accessToken.getToken());
            int msgType = 0;
            if (bean.content.isEmpty()) {
                bean.content = "分享图片";
            }
            if (bean.status != null) {
                if (bean.status.retweeted_status != null) {
                    if (bean.status.retweeted_status.pic_urls != null && bean.status.retweeted_status.pic_urls.size() > 1) {//clone 多图微博
                        msgType = 3;
                        map.put("imageurls", bean.status.retweeted_status.original_pic);
                        map.put("picurls", new Gson().toJson(bean.status.retweeted_status.pic_urls));
                    } else if (!TextUtils.isEmpty(bean.status.retweeted_status.original_pic)) {//clone 单图微博
                        msgType = 1;
                        map.put("imageurls", bean.status.retweeted_status.original_pic);
                    }
                } else {
                    if (bean.status.pic_urls != null && bean.status.pic_urls.size() > 1) {//clone 多图微博
                        msgType = 3;
                        map.put("imageurls", bean.status.original_pic);
                        map.put("picurls", new Gson().toJson(bean.status.pic_urls));
                    } else if (!TextUtils.isEmpty(bean.status.original_pic)) {//clone 单图微博
                        msgType = 1;
                        map.put("imageurls", bean.status.original_pic);
                    }
                }
            }
            map.put("msgtype", msgType + "");
            try {
                map.put("content", URLEncoder.encode(bean.content, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            map.put("latitude", "");
            map.put("longitude", "");
            map.put("sendtime", bean.sendTime);
        }
        return map;
    }

    private HashMap<String, String> setupCloneSimpleDiscoverCreateBeanToMap(WeiBoCreateBean bean) {
        HashMap<String, String> map = new HashMap<>();
        if (bean != null && accessToken != null && accessToken.isSessionValid()) {
            map.put("weibouserid", "" + accessToken.getUid());
            map.put("accesstoken", accessToken.getToken());
            int msgType = bean.simpleDiscoverModel.getType();
            String content = bean.content;
            if (content.isEmpty()) {
                content = "分享";
            }
            if (bean.simpleDiscoverModel != null && !TextUtils.isEmpty(bean.simpleDiscoverModel.getImageurls())) {
                map.put("imageurls", bean.simpleDiscoverModel.getImageurls());
            }

            map.put("msgtype", msgType + "");

            if (bean.simpleDiscoverModel != null &&!StringUtils.isNullOrEmpty(bean.simpleDiscoverModel.getVideourls())
                    && bean.simpleDiscoverModel.getType()==Constants.VIDEO_TYPE){
                content = content+" "+bean.simpleDiscoverModel.getVideourls();
            }
            map.put("content", content);
//            try {
//                map.put("content", URLEncoder.encode(bean.content, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            map.put("latitude", "");
            map.put("longitude", "");
            map.put("sendtime", bean.sendTime);
        }
        return map;
    }

    /**
     * 封装修改的定时记录
     *
     * */
    private HashMap<String, String> setupRepeatTimerCreateBeanToMap(WeiBoCreateBean bean, int sendstatus) {
        HashMap<String, String> map = new HashMap<>();
        if (bean != null && accessToken != null && accessToken.isSessionValid()) {
            map.put("weibouserid", "" + accessToken.getUid());
            map.put("accesstoken", accessToken.getToken());
            int msgType = 0;
            if (bean.content.isEmpty()) {
                bean.content = "分享图片";
            }
            if (bean.weiboArticleModel != null) {
                map.put("id",bean.weiboArticleModel.getId()+"");
                if (!StringUtils.isNullOrEmpty(bean.weiboArticleModel.getImageurls())) {
                    msgType = 1;
                    map.put("imageurls", bean.weiboArticleModel.getImageurls());
                }else {
                    map.put("imageurls", "");
                }
                if (!StringUtils.isNullOrEmpty(bean.weiboArticleModel.getPicurls())){
                    msgType = 3;
                    map.put("picurls", bean.weiboArticleModel.getPicurls());
                }else {
                    map.put("picurls", "");
                }

            }
            map.put("msgtype", msgType + "");
            map.put("content", bean.content);
//            try {
//                map.put("content", URLEncoder.encode(bean.content, "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            map.put("latitude", "");
            map.put("longitude", "");
            map.put("sendstatus", sendstatus+"");
            map.put("sendtime", bean.sendTime);
        }
        return map;
    }

    /**
     * 发送图文微博
     */
    private void sendImgTextContent(final WeiBoCreateBean weiBoCreateBean) {
        if (weiBoCreateBean.content.isEmpty() && weiBoCreateBean.status == null) {
            weiBoCreateBean.content = "分享图片";
        } else if (weiBoCreateBean.content.isEmpty() && weiBoCreateBean.status != null) {
            weiBoCreateBean.content = "转发微博";
        }
        final String localpath = weiBoCreateBean.selectImgList.get(0).getImageFile().getAbsolutePath();
        LogUtil.d("sendImgTextContent-localpath:" + localpath);
        if (!TextUtils.isEmpty(localpath)) {
            SimpleDiaryApplication.getExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    GifUploadAPI.uploadMultiFile(weiBoCreateBean.content, localpath, "0", "0", AccessTokenKeeper.readAccessToken(mContext).getToken(),
                            new RequestListener() {
                                @Override
                                public void onComplete(String s) {
                                    onRequestComplete();
                                }

                                @Override
                                public void onWeiboException(WeiboException e) {
                                    onRequestError(e, "发送失败");
                                }
                            });
                }
            });

        }


        DisplayImageOptions imageItemOptions;

        if (new File(weiBoCreateBean.selectImgList.get(0).getImageFile().getAbsolutePath()).length() > 5 * 1024 * 1024) {
            imageItemOptions = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .imageScaleType(ImageScaleType.NONE)
                    .considerExifParams(true)
                    .build();
        } else {
            imageItemOptions = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .considerExifParams(true)
                    .build();
        }
//        ImageLoader.getInstance().loadImage("file:///" + weiBoCreateBean.selectImgList.get(0).getImageFile().getAbsolutePath(), imageItemOptions, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                mStatusesAPI.upload(weiBoCreateBean.content, loadedImage, "0", "0", new RequestListener() {
//                    @Override
//                    public void onComplete(String s) {
//                        onRequestComplete();
//                    }
//
//                    @Override
//                    public void onWeiboException(WeiboException e) {
//                        onRequestError(e, "发送失败");
//                    }
//                });
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                ToastUtil.showShort(mContext, "本地找不到此图片");
//                ToastUtil.showShort(mContext, failReason.getCause().getMessage());
//            }
//        });
    }

    /**
     * 发送图文微博，网络图片
     */
    private void sendInernetImgTextContent(String content, final String imageUrl) {
        if (TextUtils.isEmpty(content)) {
            content = "分享图片";
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            String prefix = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
            if (!TextUtils.isEmpty(prefix) && prefix.toLowerCase().equals("gif")) {
                /**
                 * 网络图片存储到本地，通过流的上传方式发送微博
                 * */
                final String finalContent = content;
                SimpleDiaryApplication.getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        String localpath = GifUploadAPI.synchronizedGet(imageUrl);
                        if (!TextUtils.isEmpty(localpath)) {
                            GifUploadAPI.uploadMultiFile(finalContent, localpath, "0", "0", AccessTokenKeeper.readAccessToken(mContext).getToken(),
                                    new RequestListener() {
                                        @Override
                                        public void onComplete(String s) {
                                            onRequestComplete();
                                            updateSendStatus(1);
                                        }

                                        @Override
                                        public void onWeiboException(WeiboException e) {
                                            onRequestError(e, "发送失败");
                                            updateSendStatus(2);
                                        }
                                    });
                        }
                    }
                });


            } else {
                apiUploadUrlText(content, imageUrl, null);
            }
        }
    }

    /**
     * 重发若选择实时发送时
     * */
    private void updateSendStatus(final int sendstatus){
        if (mPostType!=null&&mPostType.equals(PostService.POST_SERVICE_TIMER_RECORD_REPEAT)
                &&repeatTimerBean!=null){
            SimpleDiaryApplication.getExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    repeatTimerSend(setupRepeatTimerCreateBeanToMap(repeatTimerBean,sendstatus));
                }
            });

        }
    }

    /**
     * 发送纯文字的微博
     */
    private void sendTextContent(WeiBoCreateBean weiBoCreateBean) {
        apiUpdateText(weiBoCreateBean.content.toString());
    }

    /**
     * 文本发送API
     */
    private void apiUpdateText(String content) {
        mStatusesAPI.update(content, "0.0", "0.0", new RequestListener() {
            @Override
            public void onComplete(String s) {
                onRequestComplete();
                updateSendStatus(1);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onRequestError(e, "发送失败");
                updateSendStatus(2);
            }
        });
    }

    /**
     * 图片URL、多图PID发送API
     */
    private void apiUploadUrlText(String content, String imageUrl, String pic_id) {
        mStatusesAPI.uploadUrlText(content, imageUrl, pic_id, "0.0", "0.0", new RequestListener() {
            @Override
            public void onComplete(String s) {
                onRequestComplete();
                updateSendStatus(1);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                LogUtil.d("apiUploadUrlText-onWeiboException" + e.getMessage());
                onRequestError(e, "发送失败");
                updateSendStatus(2);
            }
        });
    }

    /**
     * clone 实时发送一条微博
     */
    private void cloneSendWeibo(String content, Status status) {
        if (status.pic_urls != null && status.pic_urls.size() > 1) {//clone 多图微博
            apiUploadUrlText(content, status.original_pic, setupPicIdStr(status.pic_urls));
        } else if (!TextUtils.isEmpty(status.original_pic)) {//clone 单图微博
            //apiUploadUrlText(content, status.original_pic,null);
            sendInernetImgTextContent(content, status.original_pic);
        } else if (!TextUtils.isEmpty(content)) {//clone 文本微博
            apiUpdateText(content);
        } else {
            ToastUtil.showShort(mContext, "暂不支持的微博类型");
        }
    }

    /**
     * clone 实时发送一条微博(自有素材使用)
     */
    private void cloneSendSimpleDiscover(String content, SimpleDiscoverModel model) {
        if (!TextUtils.isEmpty(model.getImageurls())&&model.getType()!=Constants.VIDEO_TYPE) {//clone 单图微博
            //apiUploadUrlText(content, model.getImageurls(),null);
            sendInernetImgTextContent(content, model.getImageurls());
        } else if (!TextUtils.isEmpty(content)) {//clone 文本微博
            if (model.getType()==Constants.VIDEO_TYPE){
                content = content+" "+model.getVideourls();
            }
            apiUpdateText(content);
        } else {
            ToastUtil.showShort(mContext, "暂不支持的微博发送类型");
        }
    }

    /**
     * 重发
     * */
    private void repeatSendWeiboArticleModel(String content, WeiboArticleModel model) {
        if (model.getMsgtype()==3&&!StringUtils.isNullOrEmpty(model.getPicurls())) {//repeat 多图微博
            apiUploadUrlText(content, model.getImageurls(), setupPicIdStr(model.getPicurls()));
        } else if (model.getMsgtype()==1&&!TextUtils.isEmpty(model.getImageurls())) {//repeat 单图微博
            sendInernetImgTextContent(content, model.getImageurls());
        } else if (!TextUtils.isEmpty(content)) {//clone 文本微博
            apiUpdateText(content);
        } else {
            ToastUtil.showShort(mContext, "暂不支持的微博发送类型");
        }
    }

    /**
     * 提取多图PID
     **/
    private String setupPicIdStr(ArrayList<Status.PicUrlsBean> pic_urls) {
        String pic_ids = "";
        for (int i = 0; i < pic_urls.size(); i++) {
            String url = pic_urls.get(i).thumbnail_pic;
            String picId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            if (i == 0) {
                pic_ids += picId;
            } else {
                pic_ids += "," + picId;
            }
        }
        LogUtil.d("timerCloneSendCommonContent", "pic_ids=" + pic_ids);
        return pic_ids;
    }
    /**
     * 通过json串提取多图PID
     **/
    private String setupPicIdStr(String pic_urls) {
        String pic_ids = "";
        try {
            JSONArray jsonArray = new JSONArray(pic_urls);
            if (jsonArray!=null&&jsonArray.length()>0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("thumbnail_pic")) {
                        String url = jsonObject.getString("thumbnail_pic");
                        String picId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
                        if (i == 0) {
                            pic_ids += picId;
                        } else {
                            pic_ids += "," + picId;
                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("setupPicIdStr", "pic_ids=" + pic_ids);
        return pic_ids;
    }
    /**
     * 转发一条微博
     */
    private void repost(WeiBoCreateBean weiBoCreateBean) {
        mStatusesAPI.repost(Long.valueOf(weiBoCreateBean.status.id), weiBoCreateBean.content.toString(), 0, new RequestListener() {
            @Override
            public void onComplete(String s) {
                onRequestComplete();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onRequestError(e, "转发失败");
            }
        });
    }

    /**
     * 评论一条微博
     *
     * @param weiBoCommentBean
     */
    public void commentWeiBo(WeiBoCommentBean weiBoCommentBean) {
        CommentsAPI commentsAPI = new CommentsAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(mContext));
        commentsAPI.create(weiBoCommentBean.content, Long.valueOf(weiBoCommentBean.status.id), false, new RequestListener() {
            @Override
            public void onComplete(String s) {
                onRequestComplete();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onRequestError(e, "评论失败");

            }
        });
    }

    /**
     * 回复一条评论
     *
     * @param commentReplyBean
     */
    public void replyComment(CommentReplyBean commentReplyBean) {
        CommentsAPI commentsAPI = new CommentsAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(mContext));
        commentsAPI.reply(Long.valueOf(commentReplyBean.comment.id), Long.valueOf(commentReplyBean.comment.status.id), commentReplyBean.content, false, false,
                new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        onRequestComplete();
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        onRequestError(e, "回复评论失败");
                    }
                });
    }

    public void onRequestComplete() {
        mSendNotifity.cancel(SEND_STATUS_SEND);
        showSuccessNotifiy();
        final Message message = Message.obtain();
        message.what = SEND_STATUS_SEND;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(message);
            }
        }, 2000);
    }

    public void onRequestError(WeiboException e, String errorRemind) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        LogUtil.d("onRequestError:" + e.getMessage());
        if (e.getMessage().contains("repeat content")) {
            ToastUtil.showShort(PostService.this, errorRemind + "：请不要回复重复的内容");
        } else {
            ToastUtil.showShort(PostService.this, errorRemind);
        }
        mSendNotifity.cancel(SEND_STATUS_SEND);
        showErrorNotifiy();
        final Message message = Message.obtain();
        message.what = SEND_STATUS_ERROR;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(message);
            }
        }, 2000);
        Looper.loop();
    }

    public void onRequestError(String errorinfo, String errorRemind) {
        if (!TextUtils.isEmpty(errorinfo) && errorinfo.contains("repeat content")) {
            ToastUtil.showShort(PostService.this, errorRemind + "：请不要回复重复的内容");
        } else {
            ToastUtil.showShort(PostService.this, errorRemind);
        }
        mSendNotifity.cancel(SEND_STATUS_SEND);
        showErrorNotifiy();
        final Message message = Message.obtain();
        message.what = SEND_STATUS_ERROR;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(message);
            }
        }, 2000);
    }


    /**
     * 获取本地的图片,并且根据图片鞋带的信息纠正旋转方向
     *
     * @param absolutePath
     * @return
     */
    private Bitmap getLoacalBitmap(String absolutePath) {
        return null;
    }

    /**
     * 显示发送的notify
     */
    private void showSendNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_SEND, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.queue_icon_send);
        builder.setTicker(ticker);
        builder.setContentTitle(getApplicationContext().getResources().getString(R.string.app_name));
        builder.setContentText("正在发送");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setProgress(0, 0, true);
        Notification notification = builder.build();
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_SEND, notification);
    }

    /**
     * 发送成功的通知
     */
    private void showSuccessNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_SUCCESS, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.queue_icon_success);
        builder.setTicker(ticker);
        builder.setContentTitle(getApplicationContext().getResources().getString(R.string.app_name));
        builder.setContentText("发送成功");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_SUCCESS, notification);
    }

    /**
     * 发送失败的通知
     */
    private void showErrorNotifiy() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this, SEND_STATUS_ERROR, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String ticker = "您有一条新通知";
        builder.setContentIntent(rightPendIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo));
        builder.setSmallIcon(R.drawable.queue_icon_miss);
        builder.setTicker(ticker);
        builder.setContentTitle(getApplicationContext().getResources().getString(R.string.app_name));
        builder.setContentText("发送失败");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = builder.build();
        // 发送该通知
        mSendNotifity = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        mSendNotifity.notify(SEND_STATUS_ERROR, notification);
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSendNotifity.cancelAll();
            stopSelf();

        }
    };

}
