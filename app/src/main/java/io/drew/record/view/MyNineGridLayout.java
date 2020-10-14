package io.drew.record.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.m7.imkfsdk.utils.DensityUtil;

import java.io.Serializable;
import java.util.List;

import io.drew.record.R;
import io.drew.record.activitys.ImageActivity;
import io.drew.record.util.AppUtil;
import io.drew.record.util.GlideUtils;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/13 6:09 PM
 */
public class MyNineGridLayout extends NineGridLayout {

    public MyNineGridLayout(Context context) {
        super(context);
    }

    public MyNineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
    @Override
    protected boolean displayOneImage(ImageView imageView, String url, int parentWidth) {
        if (GlideUtils.canLoad(mContext)) {
            if (AppUtil.isPad(mContext)) {
                Glide.with(mContext)
                        .load(url + "?x-oss-process=image/quality,q_50")
                        .placeholder(R.drawable.ic_placeholder_mywork)
                        .transform(new CenterCrop(), new RoundedCorners(DensityUtil.dip2px(5f)))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageView == null) {
                                    return false;
                                }
                                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                int width = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_112);
                                setOneImageLayoutParams(imageView, width, width);
                                return false;
                            }
                        })
                        .into(imageView);
            } else {
                Glide.with(mContext)
                        .load(url + "?x-oss-process=image/quality,q_50")
                        .placeholder(R.drawable.ic_placeholder_mywork)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageView == null) {
                                    return false;
                                }
                                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                float scale = (float) parentWidth / (float) resource.getIntrinsicWidth();
                                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                                setOneImageLayoutParams(imageView, parentWidth, vh);
                                return false;
                            }
                        })
                        .into(imageView);
            }
        }
        return false;
        // true 代表按照九宫格默认大小显示(此时不要调用setOneImageLayoutParams)；false 代表按照自定义宽高显示。
    }

    @Override
    protected void displayImage(ImageView imageView, String url) {
        if (GlideUtils.canLoad(mContext)) {
            if (AppUtil.isPad(mContext)) {
                Glide.with(mContext)
                        .load(url + "?x-oss-process=image/quality,q_50")
                        .placeholder(R.drawable.ic_placeholder_mywork)
                        .transform(new CenterCrop(), new RoundedCorners(DensityUtil.dip2px(5f)))
                        .into(imageView);
            } else {
                Glide.with(mContext)
                        .load(url + "?x-oss-process=image/quality,q_50")
                        .placeholder(R.drawable.ic_placeholder_mywork)
                        .transform(new CenterCrop())
                        .into(imageView);
            }
        }

    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        Intent intent = new Intent();
        intent.setClass(mContext, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) urlList);
        bundle.putInt("index", position);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
