package com.wxdroid.simplediary.ui.login.fragment.post.idea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.api.UsersAPI;
import com.wxdroid.simplediary.entity.Comment;
import com.wxdroid.simplediary.entity.Status;
import com.wxdroid.simplediary.entity.User;
import com.wxdroid.simplediary.model.DateModel;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;
import com.wxdroid.simplediary.ui.common.BaseActivity;
import com.wxdroid.simplediary.ui.common.FillContent;
import com.wxdroid.simplediary.ui.common.login.AccessTokenKeeper;
import com.wxdroid.simplediary.ui.common.login.Constants;
import com.wxdroid.simplediary.ui.login.activity.MainActivity;
import com.wxdroid.simplediary.ui.login.fragment.post.PostService;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.CommentReplyBean;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.WeiBoCommentBean;
import com.wxdroid.simplediary.ui.login.fragment.post.bean.WeiBoCreateBean;
import com.wxdroid.simplediary.ui.login.fragment.post.idea.imagelist.ImgCloneListAdapter;
import com.wxdroid.simplediary.ui.login.fragment.post.idea.imagelist.ImgListAdapter;
import com.wxdroid.simplediary.ui.login.fragment.post.picselect.activity.AlbumActivity;
import com.wxdroid.simplediary.ui.login.fragment.post.picselect.bean.AlbumFolderInfo;
import com.wxdroid.simplediary.ui.login.fragment.post.picselect.bean.ImageInfo;
import com.wxdroid.simplediary.utils.DateUtils;
import com.wxdroid.simplediary.utils.DensityUtil;
import com.wxdroid.simplediary.utils.KeyBoardUtil;
import com.wxdroid.simplediary.utils.LogUtil;
import com.wxdroid.simplediary.utils.SharedPreferencesUtil;
import com.wxdroid.simplediary.utils.ToastUtil;
import com.wxdroid.simplediary.widget.emojitextview.WeiBoContentTextUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * 微博发送内容页
 * Created by jinchun on 16/5/2.
 */
public class IdeaActivity extends BaseActivity implements ImgListAdapter.OnFooterViewClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private UsersAPI mUsersAPI;
    private Context mContext;
    private TextView mCancal;
    private TextView mUserName;
    private TextView mSendButton;
    private TextView publicbutton;
    private ImageView picture;
    private ImageView mentionbutton;
    private ImageView trendbutton;
    private ImageView emoticonbutton;
    private ImageView more_button;
    private EditText mEditText;
    private TextView mLimitTextView;
    private TextView mInputType;
    private LinearLayout mRepostlayout;
    private ImageView repostImg;
    private TextView repostName;
    private TextView repostContent;
    private RecyclerView mRecyclerView;
    private ImageView mBlankspace;
    private LinearLayout mIdea_linearLayout;

    private ArrayList<AlbumFolderInfo> mFolderList = new ArrayList<AlbumFolderInfo>();
    private ArrayList<ImageInfo> mSelectImgList = new ArrayList<ImageInfo>();
    private Status mStatus;
    private Comment mComment;
    private String mIdeaType;
    private String sendType;

    private SimpleDiscoverModel simpleDiscoverModel;

    private WeiboArticleModel weiboArticleModel;


    //定时发送设置
    private TextView timertype_spinner;


    private TextView location_btn;
    private LinearLayout timer_layout;
    private TextView date_btn;
    private TextView time_btn;
    private TextView timer_jiange;

    private LinearLayout bottom_cz_layout;
    private RelativeLayout root_layout;

    private int keyboarkHeight = 0;


    /**
     * 最多输入140个字符
     */
    private static final int TEXT_LIMIT = 140;

    /**
     * 在只剩下10个字可以输入的时候，做提醒
     */
    private static final int TEXT_REMIND = 10;

    private String newtimer = null;
    private int defaultTimerjiange = Constants.DEFAULT_TIMER;
    private String defaulttimertype;//实时发送

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_idea_layout);
        mContext = this;
        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mInputType = (TextView) findViewById(R.id.inputType);
        mCancal = (TextView) findViewById(R.id.idea_cancal);
        mUserName = (TextView) findViewById(R.id.idea_username);
        mSendButton = (TextView) findViewById(R.id.idea_send);
        publicbutton = (TextView) findViewById(R.id.publicbutton);
        picture = (ImageView) findViewById(R.id.picture);
        mentionbutton = (ImageView) findViewById(R.id.mentionbutton);
        trendbutton = (ImageView) findViewById(R.id.trendbutton);
        emoticonbutton = (ImageView) findViewById(R.id.emoticonbutton);
        more_button = (ImageView) findViewById(R.id.more_button);
        mEditText = (EditText) findViewById(R.id.idea_content);
        mLimitTextView = (TextView) findViewById(R.id.limitTextView);
        mRepostlayout = (LinearLayout) findViewById(R.id.repost_layout);
        repostImg = (ImageView) findViewById(R.id.repost_img);
        repostName = (TextView) findViewById(R.id.repost_name);
        repostContent = (TextView) findViewById(R.id.repost_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.ImgList);
        mBlankspace = (ImageView) findViewById(R.id.blankspace);
        mIdea_linearLayout = (LinearLayout) findViewById(R.id.idea_linearLayout);

        timertype_spinner = (TextView) findViewById(R.id.timertype_spinner);

        location_btn = (TextView) findViewById(R.id.location_btn);
        timer_layout = (LinearLayout) findViewById(R.id.timer_layout);
        date_btn = (TextView) findViewById(R.id.date_btn);
        time_btn = (TextView) findViewById(R.id.time_btn);
        timer_jiange = (TextView) findViewById(R.id.timer_jiange);

        mIdeaType = getIntent().getStringExtra("ideaType");
        mStatus = getIntent().getParcelableExtra("status");
        mComment = getIntent().getParcelableExtra("comment");

        sendType = getIntent().getStringExtra("sendType");

        simpleDiscoverModel = getIntent().getParcelableExtra("simplediscover");
        weiboArticleModel = getIntent().getParcelableExtra("weiboArticleModel");

        refreshUserName();
        initContent();
        setUpListener();
        mEditText.setTag(false);
        if (getIntent().getBooleanExtra("startAlumbAcitivity", false) == true) {
            Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
            intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
            startActivityForResult(intent, 0);
        }
        if (sendType!=null&&sendType.equals("timersend")) {
            inittimer();
        }else {
            timertype_spinner.setText( Constants.DEFAULT_TIMER_TYPE);
            timertype_spinner.setBackgroundColor(getResources().getColor(R.color.gray));
        }


        mEditText.post(new Runnable() {
            @Override
            public void run() {
                setLimitTextColor(mLimitTextView, mEditText.getText().toString());
                mEditText.requestFocus();
            }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePickerDialog!=null&&!datePickerDialog.isAdded()) {
                    datePickerDialog.show(getFragmentManager(), "datePicker");
                }
            }
        });
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timePickerDialog!=null&&!timePickerDialog.isAdded()) {
                    timePickerDialog.show(getFragmentManager(), "timePicker");
                }
            }
        });
        timer_jiange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimerJiangeDialog();
            }
        });

        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        bottom_cz_layout = (LinearLayout) findViewById(R.id.bottom_cz_layout);

        mEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                IdeaActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                //int screenHeight =  IdeaActivity.this.getWindow().getDecorView().getRootView().getHeight();
                int rootHeight = root_layout.getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = rootHeight - r.bottom;
                Log.d("Keyboard Size", "Size: " + heightDifference);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (heightDifference>0){
                    if (heightDifference!=keyboarkHeight) {
                        keyboarkHeight = heightDifference;
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        int dpheight = (int) DensityUtil.px2dp(IdeaActivity.this, heightDifference);
                        params.bottomMargin = 0;
                        Log.d("Keyboard Size", "dpheight: " + dpheight);
                        //bottom_cz_layout.setLayoutParams(params);
                        root_layout.removeView(bottom_cz_layout);
                        root_layout.addView(bottom_cz_layout, params);
                    }
                }else {
                    if (heightDifference!=keyboarkHeight) {
                        keyboarkHeight = heightDifference;
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        root_layout.removeView(bottom_cz_layout);
                        root_layout.addView(bottom_cz_layout, params);
                    }
                    //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

                }
            }

        });
    }

    /**
     * 实时、定时 UI切换
     * */
    private void switchTimerTypeUI(boolean isTimer){
        if (isTimer){
            location_btn.setVisibility(View.GONE);
            timer_layout.setVisibility(View.VISIBLE);
        }else {
            location_btn.setVisibility(View.VISIBLE);
            timer_layout.setVisibility(View.GONE);
        }
    }

    private DateModel mDateModel = new DateModel();
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;


    private void inittimer(){
        defaulttimertype = (String)SharedPreferencesUtil.get(mContext,"defaulttimertype", Constants.DEFAULT_TIMER_TYPE);
        mInputType.setText(defaulttimertype+"发送");
        if (!TextUtils.isEmpty(defaulttimertype)&&defaulttimertype.equals(Constants.DEFAULT_TIMER_TYPE)){
            switchTimerTypeUI(false);
        }else {
            switchTimerTypeUI(true);
        }

        /**
         * 0-实时发送
         * 1-定时发送
         *
         * */
        timertype_spinner.setText(defaulttimertype);
        timertype_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = timertype_spinner.getText().toString();
                if (!TextUtils.isEmpty(text)&&text.equals(Constants.DEFAULT_TIMER_TYPE)){
                    timertype_spinner.setText(getResources().getString(R.string.idea_timer_text));
                    defaulttimertype = getResources().getString(R.string.idea_timer_text);
                    timer_layout.setVisibility(View.VISIBLE);
                    location_btn.setVisibility(View.GONE);
                }else {
                    timertype_spinner.setText(getResources().getString(R.string.idea_current_text));
                    defaulttimertype = getResources().getString(R.string.idea_current_text);
                    timer_layout.setVisibility(View.GONE);
                    location_btn.setVisibility(View.VISIBLE);
                }
                SharedPreferencesUtil.put(mContext,"defaulttimertype", defaulttimertype);
                mInputType.setText(defaulttimertype+"发送");
                ToastUtil.showShort(IdeaActivity.this, "切换"+defaulttimertype+"类型成功");
            }
        });

        defaultTimerjiange = (int)SharedPreferencesUtil.get(mContext,"defaulttimer", Constants.DEFAULT_TIMER);
        timer_jiange.setText("间隔："+defaultTimerjiange+"分");
        String preSendTime = (String)SharedPreferencesUtil.get(mContext,"newtimer", DateUtils.getDateTimeFromMillisecond(new Date().getTime()));
        LogUtil.d("newtimer-preSendTime获取："+preSendTime);
        if (TextUtils.isEmpty(preSendTime)){
            newtimer = DateUtils.getSendTimer(new Date(), defaultTimerjiange);
        }else {
            newtimer =DateUtils.getSendTimer(DateUtils.parseDate(preSendTime,DateUtils.yyyyMMddHHmmss), defaultTimerjiange);
        }
        mDateModel.setDate(newtimer);

        date_btn.setText(mDateModel.getYearMonthDay());
        time_btn.setText(mDateModel.getHourMinute());

        datePickerDialog = DatePickerDialog.newInstance(this,
                Integer.valueOf(mDateModel.getmYear()),
                Integer.valueOf(mDateModel.getmMonth())-1,
                Integer.valueOf(mDateModel.getmDay()));
        datePickerDialog.setYearRange(
                Integer.valueOf(mDateModel.getmYear()),
                Integer.valueOf(mDateModel.getmYear())+1);

        timePickerDialog = TimePickerDialog.newInstance(this,
                Integer.valueOf(mDateModel.getmHour()),
                Integer.valueOf(mDateModel.getmMinute()), true);
    }

    private int getDefaultTimerIndex(int defaultTimerjiange){
        int index = -1;
        for (int i=0;i<Constants.DEFAULT_TIMER_ARR.length;i++){
            if (Constants.DEFAULT_TIMER_ARR[i]==defaultTimerjiange){
                index = i;
                break;
            }
        }
        return index;
    }

    private AlertDialog alert;
    /**
     * 显示设置时间间隔dialog
     */
    private void initTimerJiangeDialog(){
        if (alert==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("设置间隔时间");
            String[] arr = new String[Constants.DEFAULT_TIMER_ARR.length];
            for (int i=0;i<Constants.DEFAULT_TIMER_ARR.length;i++){
                arr[i] = Constants.DEFAULT_TIMER_ARR[i]+"";
            }

            builder.setSingleChoiceItems(arr, getDefaultTimerIndex(defaultTimerjiange), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    timer_jiange.setText("间隔：" + Constants.DEFAULT_TIMER_ARR[i] + "分");
                    SharedPreferencesUtil.put(IdeaActivity.this, "defaulttimer", Constants.DEFAULT_TIMER_ARR[i]);
                    //客户点击后，对话框记得消失
                    dialogInterface.dismiss();
                }

            });
            alert = builder.create();
        }
        alert.show();
    }

    /**
     * 填充内容，
     * 1. 转发的内容是转发微博，
     * 2. 转发的内容是原创微博，
     */
    private void initContent() {
        switch (mIdeaType) {
            case PostService.POST_SERVICE_CREATE_WEIBO:
                break;
            case PostService.POST_SERVICE_REPOST_STATUS:
                //填充转发的内容
                repostWeiBo();
                break;
            case PostService.POST_SERVICE_COMMENT_STATUS:
                break;
            case PostService.POST_SERVICE_REPLY_COMMENT:
                break;
            case PostService.POST_SERVICE_TIMER_SEND:
                break;
            case PostService.POST_SERVICE_CLONE_WEIBO:
                cloneWeiBo();
                break;
            case PostService.POST_SERVICE_CLONE_SIMPLEDISCOVER:
                cloneSimpleDiscoverModel();
                break;
            case PostService.POST_SERVICE_TIMER_RECORD_REPEAT:
                repeatWeiboArticleModel();
                break;
        }
    }


    /**
     * 填充转发的内容
     */
    private void repostWeiBo() {

        if (mStatus == null) {
            return;
        }
        mRepostlayout.setVisibility(View.VISIBLE);
        mEditText.setHint("说说分享的心得");

        //1. 转发的内容是转发微博
        if (mStatus.retweeted_status != null) {
            mEditText.setText(WeiBoContentTextUtil.getWeiBoContent("//@" + mStatus.user.name + ":" + mStatus.text, mContext, mEditText));
            FillContent.fillMentionCenterContent(mStatus.retweeted_status, repostImg, repostName, repostContent);
            mEditText.setSelection(0);
        }
        //2. 转发的内容是原创微博
        else if (mStatus.retweeted_status == null) {
            FillContent.fillMentionCenterContent(mStatus, repostImg, repostName, repostContent);
        }
        mEditText.setSelection(mEditText.getText().toString().length());
        changeSendButtonBg();
    }
    /**
     * 填充克隆的微博
     * */
    private void cloneWeiBo(){
        if (mStatus == null) {
            return;
        }
        if (mStatus.retweeted_status==null) {
            mEditText.setText("" + mStatus.text);
            if (mStatus.pic_urls!=null&&mStatus.pic_urls.size()>0){
                initCloneImgList(mStatus.pic_urls);
            }else {
                setupStatusImages(mStatus);
            }
        }else {
            mEditText.setText("" + mStatus.retweeted_status.text);
            if (mStatus.retweeted_status.pic_urls!=null&&mStatus.retweeted_status.pic_urls.size()>0){
                initCloneImgList(mStatus.retweeted_status.pic_urls);
            }else {
                setupStatusImages(mStatus.retweeted_status);
            }
        }
        changeCloneSendButtonBg();
    }
    private void setupStatusImages(Status status){
        if (status!=null) {
            String imageurl = "";
            if (!TextUtils.isEmpty(status.original_pic)){
                imageurl = status.original_pic;
            }else if (!TextUtils.isEmpty(status.bmiddle_pic)){
                imageurl = status.bmiddle_pic;
            }else if (!TextUtils.isEmpty(status.thumbnail_pic)){
                imageurl = status.thumbnail_pic;
            }else {
                return;
            }
            Status.PicUrlsBean picUrlsBean = new Status.PicUrlsBean();
            picUrlsBean.thumbnail_pic = imageurl;
            ArrayList<Status.PicUrlsBean> arrayList = new ArrayList<>();
            arrayList.add(picUrlsBean);
            initCloneImgList(arrayList);
        }
    }

    /**
     * 填充自有精选素材
     * */
    private void cloneSimpleDiscoverModel(){
        if (simpleDiscoverModel==null){
            return;
        }
        mEditText.setText("" + simpleDiscoverModel.getContent());
        initCloneDiscoverImgList(simpleDiscoverModel);
        changeCloneSendButtonBg();
    }

    /**
     * 填充重发微博内容
     * */
    private void repeatWeiboArticleModel(){
        if (weiboArticleModel==null){
            return;
        }
        mEditText.setText("" + weiboArticleModel.getContent());
        initRepeatModelImgList(weiboArticleModel);
        changeCloneSendButtonBg();
    }

    /**
     * 刷新顶部的名字
     */
    private void refreshUserName() {
        long uid = Long.parseLong(AccessTokenKeeper.readAccessToken(this).getUid());
        mUsersAPI.show(uid, new RequestListener() {
            @Override
            public void onComplete(String response) {
                User user = User.parse(response);
                if (user != null) {
                    mUserName.setText(user.name);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                ToastUtil.showShort(mContext, info.toString());
            }
        });
    }

    /**
     * 设置监听事件
     */
    private void setUpListener() {
        mCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
                intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
                startActivityForResult(intent, 0);
            }
        });
        mentionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.getText().insert(mEditText.getSelectionStart(), "@");
            }
        });
        trendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.getText().insert(mEditText.getSelectionStart(), "##");
                mEditText.setSelection(mEditText.getSelectionStart() - 1);
            }
        });
        emoticonbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发此功能...");
            }
        });
        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "正在开发此功能...");
            }
        });
        mEditText.addTextChangedListener(watcher);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送自有精选素材判断
                if (mIdeaType.equals(PostService.POST_SERVICE_CLONE_SIMPLEDISCOVER)){
                    if (TextUtils.isEmpty(simpleDiscoverModel.getImageurls())&&(mEditText.getText().toString().isEmpty() || mEditText.getText().toString().length() == 0)){
                        ToastUtil.showShort(mContext, "发送的内容不能为空");
                        return;
                    }
                }else {
                    //在发微博状态下，如果发的微博没有图片，且也没有文本内容，识别为空
                    if (!isRetweetWeiBoState() && mStatus == null && mSelectImgList.size() == 0 && (mEditText.getText().toString().isEmpty() || mEditText.getText().toString().length() == 0)) {
                        ToastUtil.showShort(mContext, "发送的内容不能为空");
                        return;
                    }
                }

                if (calculateWeiboLength(mEditText.getText().toString()) > TEXT_LIMIT) {
                    ToastUtil.showShort(mContext, "文本超出限制" + TEXT_LIMIT + "个字！请做调整");
                    return;
                }
                if (simpleDiscoverModel!=null&&simpleDiscoverModel.getType()==2){
                    if (calculateWeiboLength(mEditText.getText().toString()+simpleDiscoverModel.getVideourls()) > TEXT_LIMIT){
                        ToastUtil.showShort(mContext, "视频文本超出限制" + (TEXT_LIMIT-20) + "个字！请做调整");
                        return;
                    }
                }

                if (mSelectImgList.size() > 1) {
                    ToastUtil.showShort(mContext, "由于新浪的限制，第三方微博客户端只允许上传一张图，请做调整");
                    return;
                }
                Intent intent = new Intent(mContext, PostService.class);
                Bundle bundle = new Bundle();

                switch (mIdeaType) {
                    case PostService.POST_SERVICE_CREATE_WEIBO:
                        WeiBoCreateBean weiboBean = new WeiBoCreateBean(mEditText.getText().toString(), mSelectImgList);
                        intent.putExtra("postType", PostService.POST_SERVICE_CREATE_WEIBO);
                        bundle.putParcelable("weiBoCreateBean", weiboBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_REPOST_STATUS:
                        WeiBoCreateBean repostBean = new WeiBoCreateBean(mEditText.getText().toString(), mSelectImgList, mStatus);
                        intent.putExtra("postType", PostService.POST_SERVICE_REPOST_STATUS);
                        bundle.putParcelable("weiBoCreateBean", repostBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_COMMENT_STATUS:
                        intent.putExtra("postType", PostService.POST_SERVICE_COMMENT_STATUS);
                        WeiBoCommentBean weiBoCommentBean = new WeiBoCommentBean(mEditText.getText().toString(), mStatus);
                        bundle.putParcelable("weiBoCommentBean", weiBoCommentBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_REPLY_COMMENT:
                        intent.putExtra("postType", PostService.POST_SERVICE_REPLY_COMMENT);
                        CommentReplyBean commentReplyBean = new CommentReplyBean(mEditText.getText().toString(), mComment);
                        bundle.putParcelable("commentReplyBean", commentReplyBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_TIMER_SEND://定时发送微博
                        WeiBoCreateBean weiboTimerBean = new WeiBoCreateBean(mEditText.getText().toString(), mSelectImgList,getTime());
                        intent.putExtra("postType", PostService.POST_SERVICE_TIMER_SEND);
                        bundle.putParcelable("weiboTimerBean", weiboTimerBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_CLONE_WEIBO://克隆微博定时
                        WeiBoCreateBean cloneBean = new WeiBoCreateBean(mEditText.getText().toString(), mStatus,getTime());
                        intent.putExtra("postType", PostService.POST_SERVICE_CLONE_WEIBO);
                        bundle.putParcelable("weiBoCloneBean", cloneBean);
                        intent.putExtras(bundle);
                        break;
                    case PostService.POST_SERVICE_CLONE_SIMPLEDISCOVER://克隆自由素材定时
                        WeiBoCreateBean cloneSdBean = new WeiBoCreateBean(mEditText.getText().toString(), simpleDiscoverModel,getTime());
                        intent.putExtra("postType", PostService.POST_SERVICE_CLONE_SIMPLEDISCOVER);
                        bundle.putParcelable("simpleDiscoverCloneBean", cloneSdBean);
                        intent.putExtras(bundle);
                        break;
                    //weiboArticleModel
                    case PostService.POST_SERVICE_TIMER_RECORD_REPEAT://重发
                        WeiBoCreateBean repeatTimerBean = new WeiBoCreateBean(mEditText.getText().toString(), weiboArticleModel,getTime());
                        intent.putExtra("postType", PostService.POST_SERVICE_TIMER_RECORD_REPEAT);
                        bundle.putParcelable("repeatTimerBean", repeatTimerBean);
                        intent.putExtras(bundle);
                        break;
                }
                startService(intent);
                finish();
            }
        });
        mSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pressSendButton();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    changeSendButtonBg();
                }
                return false;
            }
        });

        mBlankspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.openKeybord(mEditText, mContext);
            }
        });

    }

    private String getTime(){
        String time = DateUtils.getDateTimeFromMillisecond(new Date().getTime());
        if (mDateModel!=null){
            time = mDateModel.getAllDate();
        }
        if (!defaulttimertype.equals(Constants.DEFAULT_TIMER_TYPE)) {
            LogUtil.d("newtimer-存储之前：" + time);
            SharedPreferencesUtil.put(mContext, "newtimer", time);
            LogUtil.d("newtimer-存储之后：" + SharedPreferencesUtil.get(mContext, "newtimer", null));
        }
        return time;
    }

    /**
     * 根据输入的文本数量，决定发送按钮的背景 克隆使用
     */
    private void changeCloneSendButtonBg() {
        //如果有文本，或者有图片，或者是处于转发微博状态
        if (mEditText.getText().toString().length() > 0 || (isRetweetWeiBoState())) {
            mEditText.setSelection(mEditText.getText().toString().length());
            highlightSendButton();
        } else {
            sendNormal();
        }
    }

    /**
     * 根据输入的文本数量，决定发送按钮的背景
     */
    private void changeSendButtonBg() {
        //如果有文本，或者有图片，或者是处于转发微博状态
        if (mEditText.getText().toString().length() > 0 || mSelectImgList.size() > 0 || (isRetweetWeiBoState())) {
            highlightSendButton();
        } else {
            sendNormal();
        }
    }

    private void pressSendButton() {
        mSendButton.setBackgroundResource(R.drawable.compose_send_corners_highlight_press_bg);
        mSendButton.setTextColor(Color.parseColor("#ebeef3"));
    }

    private void highlightSendButton() {
        mSendButton.setBackgroundResource(R.drawable.compose_send_corners_highlight_bg);
        mSendButton.setTextColor(Color.parseColor("#fbffff"));
        mSendButton.setEnabled(true);
    }

    private void sendNormal() {
        mSendButton.setBackgroundResource(R.drawable.compose_send_corners_bg);
        mSendButton.setTextColor(Color.parseColor("#b3b3b3"));
        mSendButton.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    mSelectImgList = data.getParcelableArrayListExtra("selectImgList");
                    initImgList();
                    changeSendButtonBg();
                }
                break;
        }
    }


    public void initImgList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        ImgListAdapter imgListAdapter = new ImgListAdapter(mSelectImgList, mContext);
        imgListAdapter.setOnFooterViewClickListener(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(imgListAdapter);
    }

    public void initCloneImgList(ArrayList<Status.PicUrlsBean> picUrlsList) {
        mRecyclerView.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        ImgCloneListAdapter imgListAdapter = new ImgCloneListAdapter(picUrlsList, mContext);
        //imgListAdapter.setOnFooterViewClickListener(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(imgListAdapter);
    }
    /**
     * 展示克隆发现里的精选素材图片列表
     * */
    public void initCloneDiscoverImgList(SimpleDiscoverModel model) {
        mRecyclerView.setVisibility(View.VISIBLE);
        FillContent.fillDiscoverImgList(model, mContext, mRecyclerView);
    }

    /**
     * 展示重发里的图片列表
     * */
    public void initRepeatModelImgList(WeiboArticleModel model) {
        mRecyclerView.setVisibility(View.VISIBLE);
        FillContent.fillWeiboArticleImgList(model, mContext, mRecyclerView);
    }

    @Override
    public void OnFooterViewClick() {
        Intent intent = new Intent(IdeaActivity.this, AlbumActivity.class);
        intent.putParcelableArrayListExtra("selectedImglist", mSelectImgList);
        startActivityForResult(intent, 0);
    }

    private TextWatcher watcher = new TextWatcher() {
        private CharSequence inputString;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputString = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changeSendButtonBg();
            setLimitTextColor(mLimitTextView, inputString.toString());
        }
    };


    /**
     * 计算微博文本的长度，统计是否超过140个字，其中中文和全角的符号算1个字符，英文字符和半角字符算半个字符
     *
     * @param c
     * @return 微博的长度，结果四舍五入
     */
    public long calculateWeiboLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    public void setLimitTextColor(TextView limitTextView, String content) {
        long length = calculateWeiboLength(content);
        if (length > TEXT_LIMIT) {
            long outOfNum = length - TEXT_LIMIT;
            limitTextView.setTextColor(Color.parseColor("#e03f22"));
            limitTextView.setText("-" + outOfNum + "");
        } else if (length == TEXT_LIMIT) {
            limitTextView.setText(0 + "");
            limitTextView.setTextColor(Color.parseColor("#929292"));
        } else if (TEXT_LIMIT - length <= TEXT_REMIND) {
            limitTextView.setText(TEXT_LIMIT - length + "");
            limitTextView.setTextColor(Color.parseColor("#929292"));
        } else {
            limitTextView.setText("");
        }
    }

    /**
     * 判断此页是处于转发微博还是发微博状态
     *
     * @return
     */
    public boolean isRetweetWeiBoState() {
        if (mStatus != null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        if (mDateModel==null){
            mDateModel = new DateModel();
        }
        mDateModel.setmYear(DateModel.intToStr(year));
        mDateModel.setmMonth(DateModel.intToStr(monthOfYear+1));
        mDateModel.setmDay(DateModel.intToStr(dayOfMonth));

        date_btn.setText(mDateModel.getYearMonthDay());
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if (mDateModel==null){
            mDateModel = new DateModel();
        }
        mDateModel.setmHour(DateModel.intToStr(hourOfDay));
        mDateModel.setmMinute(DateModel.intToStr(minute));

        time_btn.setText(mDateModel.getHourMinute());
    }
}
