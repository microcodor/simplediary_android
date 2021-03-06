/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wxdroid.simplediary.ui.common.login;

import android.text.TextUtils;

public interface Constants {

    String JUHE_APP_KEY = "2d7c1b2cace27ab7fd43e85f8dbcd677";



    public static final String APP_KEY = "2405640785";//"211160679";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";//http://oauth.weico.cc";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";

    public static final String AppSecret = "c528c890f58d34b9e75dcca10b2601bf";//"1e6e33db08f9192306c4afa0a61ad56c";
    public static final String PackageName = "com.wxdroid.simplediary";

    public static final String FileDir = "/simplediary/";

    public static final String IMAGE_PATH="images/";


    public static final int  DEFAULT_TIMER = 15;
    String DEFAULT_TIMER_TYPE = "实时";//默认实时发送

    public static final int[] DEFAULT_TIMER_ARR = new int[]{15, 30, 45, 60};


    public static final String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + Constants.APP_KEY
            + "&response_type=token&redirect_uri=" + Constants.REDIRECT_URL
            + "&key_hash=" + Constants.AppSecret + (TextUtils.isEmpty(Constants.PackageName) ? "" : "&packagename=" + Constants.PackageName)
            + "&display=mobile" + "&scope=" + Constants.SCOPE;

    //图片形状
    public static final String IMGSIZE_HORIZONTAL = "1"; //水平方向的长方形尺寸
    public static final String IMGSIZE_VERTICAL = "2";//竖直方向的长方形尺寸
    public static final String IMGSIZE_SQUARE = "3";//正方形尺寸

    //首页分组
    public static final long GROUP_TYPE_ALL = 0;//全部微博
    public static final long GROUP_TYPE_FRIENDS_CIRCLE = 1;//好友圈


    //评论页分组
    public static final int GROUP_COMMENT_TYPE_ALL = 0x12;//全部评论
    public static final int GROUP_COMMENT_TYPE_FRIENDS = 0x13;//关注的人
    public static final int GROUP_COMMENT_TYPE_BYME = 0x14;//我发出的

    //@我的分组
    public static final int GROUP_RETWEET_TYPE_ALL = 0x15;//所有微博
    public static final int GROUP_RETWEET_TYPE_FRIENDS = 0x16;//关注人的微博
    public static final int GROUP_RETWEET_TYPE_ORIGINWEIBO = 0x17;//原创微博
    public static final int GROUP_RETWEET_TYPE_ALLCOMMENT = 0x18;//所有评论
    public static final int GROUP_RETWEET_TYPE_FRIEDNSCOMMENT = 0x19;//关注人的评论

    //我的微博
    public static final int GROUP_MYWEIBO_TYPE_ALL = 0;
    public static final int GROUP_MYWEIBO_TYPE_ORIGIN = 1;
    public static final int GROUP_MYWEIBO_TYPE_PICWEIBO = 2;

    //删除微博的类型
    public static final String DELETE_WEIBO_TYPE1 = "全部微博";

    public static final String DELETE_WEIBO_TYPE2 = "我的全部微博";
    public static final String DELETE_WEIBO_TYPE3 = "我的原创微博";
    public static final String DELETE_WEIBO_TYPE4 = "我的图片微博";
    public static final String DELETE_WEIBO_TYPE5 = "好友圈";
    public static final String DELETE_WEIBO_TYPE6 = "我的收藏";

    public static String[] TYPE_CLASSES = new String[]{"搞笑", "萌宠", "情感", "娱乐", "漫画", "尤物"};
    String[] SENDSTATUS_ARRAY = new String[]{"等待发送", "发送失败", "发送成功"};

    int PAGE_SIZE=20;

    int VIDEO_TYPE = 2;

    String[] TASK_TABS = new String[]{"互粉大厅", "转发大厅", "每日签到", "我的金币", "金币提现", "一元夺宝"};


}
