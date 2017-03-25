package com.wxdroid.simplediary.model;

import com.wxdroid.simplediary.utils.DateUtils;

import java.util.Date;

/**
 * Created by jinchun on 2017/1/11.
 */

public class DateModel {
    private String mYear;
    private String mMonth;
    private String mDay;
    private String mHour;
    private String mMinute;


    public void setDate(String newtimer){
        Date date = DateUtils.parseDate(newtimer,DateUtils.yyyyMMddHHmmss);
        mYear = intToStr(DateUtils.getYear(date));
        mMonth = intToStr(DateUtils.getMonth(date));
        mDay = intToStr(DateUtils.getDay(date));
        mHour =  intToStr(DateUtils.getHour(date));
        mMinute =  intToStr(DateUtils.getMinute(date));
    }

    public String getAllDate(){
        return mYear+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute+":00";
    }

    public String getYearMonthDay(){
        return mYear+"年"+(mMonth)+"月"+mDay+"日";
    }

    public String getHourMinute(){
        return mHour+":"+mMinute;
    }


    public static String intToStr(int i){
        if (i<10){
            return "0"+String.valueOf(i);
        }else {
            return ""+i;
        }
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public String getmHour() {
        return mHour;
    }

    public void setmHour(String mHour) {
        this.mHour = mHour;
    }

    public String getmMinute() {
        return mMinute;
    }

    public void setmMinute(String mMinute) {
        this.mMinute = mMinute;
    }
}
