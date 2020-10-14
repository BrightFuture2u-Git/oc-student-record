package io.drew.record.util;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.m7.imkfsdk.utils.DensityUtil;

import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/22 4:24 PM
 */
public class GlideUtils {

    /**
     * 加载圆形图片
     *
     * @param context
     * @param path
     * @param target
     * @param placeholder
     */
    public static void loadCircleImg(Context context, String path, ImageView target, int placeholder) {
        if (canLoad(context)) {
            Glide.with(context)
                    .load(path)
                    .placeholder(placeholder)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(target);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param path
     * @param target
     * @param dp
     */
    public static void loadRoundCornerImg(Context context, String path, ImageView target, float dp) {
        if (canLoad(context)) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.drawable.ic_placeholder_mywork)
                    .transform(new CenterCrop(), new RoundedCorners(DensityUtil.dip2px(dp)))
                    .into(target);
        }
    }

    /**
     * 普通加载图片
     *
     * @param context
     * @param path
     * @param target
     */
    public static void loadImg(Context context, String path, ImageView target) {
        if (canLoad(context)) {
            Glide.with(context).load(path).placeholder(R.drawable.ic_placeholder_mywork).into(target);
        }
    }

    /**
     * 加载宝宝头像
     *
     * @param context
     * @param path
     * @param target
     */
    public static void loadBabyHead(Context context, String path, ImageView target) {
        if (canLoad(context))
        Glide.with(context).load(path).placeholder(R.drawable.head_cat).into(target);
    }

    /**
     * 加载本地宝宝头像
     *
     * @param context
     * @param target
     */
    public static void loadBabyHead(Context context, ImageView target) {
        if (canLoad(context))
        Glide.with(context).load(R.drawable.head_cat).into(target);
    }

    /**
     * 加载用户头像
     *
     * @param context
     * @param path
     * @param target
     */
    public static void loadUserHead(Context context, String path, ImageView target) {
        if (canLoad(context))
        Glide.with(context).load(path).placeholder(R.drawable.head_user_placeholder).into(target);
    }


    /**
     * 加载缩略图
     *
     * @param context
     * @param url
     * @param target
     */
    public static void loadThumbnail_20(Context context, String url, ImageView target) {
        if (canLoad(context))
        loadImg(context, url + "?x-oss-process=image/quality,q_20", target);
    }

    public static void loadThumbnail_50(Context context, String url, ImageView target, int placeholder) {
        if (canLoad(context))
        Glide.with(context)
                .load(url + "?x-oss-process=image/quality,q_50")
                .placeholder(placeholder)
                .into(target);
    }

    /**
     * 是否可以加载图片
     *
     * @param context
     * @return
     */
    public static boolean canLoad(Context context) {
        if (context != null) {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                if (mActivity.isFinishing() || mActivity.isDestroyed()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
