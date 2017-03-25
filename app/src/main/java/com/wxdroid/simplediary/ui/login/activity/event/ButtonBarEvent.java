package com.wxdroid.simplediary.ui.login.activity.event;

/**
 * Created by jinchun on 16/8/9.
 */
public class ButtonBarEvent {

    public static final int SHOW_BAR = 1;
    public static final int HIDE_BAR = 2;

    private int mId;


    public ButtonBarEvent(int id) {
        this.mId = id;

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }


}
