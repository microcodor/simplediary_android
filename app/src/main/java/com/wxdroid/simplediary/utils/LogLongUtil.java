package com.wxdroid.simplediary.utils;

import android.util.Log;

/**
 * Created by jinchun on 2017/1/6.
 */

public class LogLongUtil {
    //可以全局控制是否打印log日志
    private static boolean isPrintLog = true;

    private static int LOG_MAXLENGTH = 2000;

    public static void show(String msg) {
        if (isPrintLog) {

            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e("LogLongUtil_" + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e("LogLongUtil_" + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }
}
