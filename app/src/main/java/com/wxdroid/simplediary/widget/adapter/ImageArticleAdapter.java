package com.wxdroid.simplediary.widget.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wxdroid.simplediary.R;
import com.wxdroid.simplediary.model.SimpleDiscoverModel;
import com.wxdroid.simplediary.model.WeiboArticleModel;
import com.wxdroid.simplediary.ui.common.FillContent;
import com.wxdroid.simplediary.utils.ScreenUtil;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by jinchun on 16/1/3.
 */
public class ImageArticleAdapter extends RecyclerView.Adapter<ImageArticleAdapter.ViewHolder> {
    private ArrayList<String> mData;
    private Context mContext;
    private WeiboArticleModel simpleDiscoverModel;
    /**
     * 用于加载微博列表图片的配置，进行安全压缩，尽可能的展示图片细节
     */
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
    private final ImageSize mSingleImageSize;
    private final ImageSize mDoubleImgSize;
    private final ImageSize mThreeImgSize;

    public ImageArticleAdapter(WeiboArticleModel model, ArrayList<String> imagelist, Context context) {
        this.simpleDiscoverModel = model;
        this.mData = imagelist;
        this.mContext = context;
        mSingleImageSize = new ImageSize(ScreenUtil.getScreenWidth(context), (int) (ScreenUtil.getScreenWidth(context) * 0.7));
        mDoubleImgSize = new ImageSize(ScreenUtil.getScreenWidth(context) / 2, ScreenUtil.getScreenWidth(context) / 2);
        mThreeImgSize = new ImageSize(ScreenUtil.getScreenWidth(context) / 3,ScreenUtil.getScreenWidth(context) / 3);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainfragment_weiboitem_imageitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        //设置加载中的图片样式
        setImgSize(mData, mContext, viewHolder.norImg, viewHolder.longImg, viewHolder.gifImg);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FillContent.fillDiscoverImageList(mContext, mData, options, position, holder.longImg, holder.norImg, holder.gifImg, holder.imageLabel,mSingleImageSize,mDoubleImgSize,mThreeImgSize,true);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(ArrayList<String> data) {
        this.mData = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SubsamplingScaleImageView longImg;
        public ImageView norImg;
        public GifImageView gifImg;
        public ImageView imageLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            longImg = (SubsamplingScaleImageView) itemView.findViewById(R.id.longImg);
            norImg = (ImageView) itemView.findViewById(R.id.norImg);
            gifImg = (GifImageView) itemView.findViewById(R.id.gifView);
            imageLabel = (ImageView) itemView.findViewById(R.id.imageType);
        }
    }

    /**
     * 根据图片的数量，设置不同的尺寸
     *
     * @param datas
     * @param context
     * @param norImg
     * @param longImg
     * @param gifImg
     */
    private static void setImgSize(ArrayList<String> datas, Context context, ImageView norImg, SubsamplingScaleImageView longImg, GifImageView gifImg) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        if (datas.size() == 1) {
            setSingleImgSize(context, norImg, longImg, gifImg);
        } else if (datas.size() == 2 || datas.size() == 4) {
            setDoubleImgSize(context, norImg, longImg, gifImg);
        } else if (datas.size() == 3 || datas.size() >= 5) {
            setThreeImgSize(context, norImg, longImg, gifImg);
        }
    }

    private static void setDoubleImgSize(Context context, ImageView norImg, SubsamplingScaleImageView longImg, GifImageView gifImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        FrameLayout.LayoutParams longImgLayout = (FrameLayout.LayoutParams) longImg.getLayoutParams();
        FrameLayout.LayoutParams gifImgLayout = (FrameLayout.LayoutParams) gifImg.getLayoutParams();
        longImgLayout.width = ScreenUtil.getScreenWidth(context) / 2;
        norImgLayout.width = ScreenUtil.getScreenWidth(context) / 2;
        gifImgLayout.width = ScreenUtil.getScreenWidth(context) / 2;
        norImgLayout.height = ScreenUtil.getScreenWidth(context) / 2;
        longImgLayout.height = ScreenUtil.getScreenWidth(context) / 2;
        gifImgLayout.height = ScreenUtil.getScreenWidth(context) / 2;
    }

    private static void setSingleImgSize(Context context, ImageView norImg, SubsamplingScaleImageView longImg, GifImageView gifImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        FrameLayout.LayoutParams longImgLayout = (FrameLayout.LayoutParams) longImg.getLayoutParams();
        FrameLayout.LayoutParams gifImgLayout = (FrameLayout.LayoutParams) gifImg.getLayoutParams();
        longImgLayout.width = ScreenUtil.getScreenWidth(context);
        norImgLayout.width = ScreenUtil.getScreenWidth(context);
        gifImgLayout.width = ScreenUtil.getScreenWidth(context);
        norImgLayout.height = (int) (ScreenUtil.getScreenWidth(context) * 0.7);
        longImgLayout.height = (int) (ScreenUtil.getScreenWidth(context) * 0.7);
        gifImgLayout.height = (int) (ScreenUtil.getScreenWidth(context) * 0.7);
    }

    private static void setThreeImgSize(Context context, ImageView norImg, SubsamplingScaleImageView longImg, GifImageView gifImg) {
        FrameLayout.LayoutParams norImgLayout = (FrameLayout.LayoutParams) norImg.getLayoutParams();
        FrameLayout.LayoutParams longImgLayout = (FrameLayout.LayoutParams) longImg.getLayoutParams();
        FrameLayout.LayoutParams gifImgLayout = (FrameLayout.LayoutParams) gifImg.getLayoutParams();
        longImgLayout.width = ScreenUtil.getScreenWidth(context) / 3;
        norImgLayout.width = ScreenUtil.getScreenWidth(context) / 3;
        gifImgLayout.width = ScreenUtil.getScreenWidth(context) / 3;
        norImgLayout.height = ScreenUtil.getScreenWidth(context) / 3;
        longImgLayout.height = ScreenUtil.getScreenWidth(context) / 3;
        gifImgLayout.height = ScreenUtil.getScreenWidth(context) / 3;
    }

}
