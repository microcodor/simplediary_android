package com.wxdroid.simplediary.mvp.model;

import android.content.Context;

import com.wxdroid.simplediary.entity.Comment;
import com.wxdroid.simplediary.entity.Status;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/15.
 */
public interface MentionModel {

    interface OnMentionFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> mentionlist);

        void onError(String error);
    }

    interface OnCommentFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Comment> commentlist);

        void onError(String error);
    }

    public void mentions(int groupType, Context context, OnMentionFinishedListener onDataFinishedListener);

    public void mentionsNextPage(int groupType, Context context, OnMentionFinishedListener onDataFinishedListener);

    public void cacheSave(int groupType, Context context, String response);

    public void cacheLoad(int groupType, Context context, OnMentionFinishedListener onDataFinishedListener);

    public void cacheLoad(int groupType, Context context, OnCommentFinishedListener onCommentFinishedListener);

    public void commentMentions(int groupType, Context context, OnCommentFinishedListener onCommentFinishedListener);

    public void commentMentionsNextPage(int groupType, Context context, OnCommentFinishedListener onCommentFinishedListener);

}
