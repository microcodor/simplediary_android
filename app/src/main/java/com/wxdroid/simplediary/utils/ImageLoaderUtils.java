package com.wxdroid.simplediary.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.wxdroid.simplediary.R;

/**
 * ImageLoader  配置
 * Created by jinchun on 2017/1/5.
 */

public class ImageLoaderUtils {

    public static  DisplayImageOptions getHeadOptions(){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.avator_default)
                .showImageForEmptyUri(R.drawable.avator_default)
                .showImageOnFail(R.drawable.avator_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(14671839, 1))
                .build();
    }
}
