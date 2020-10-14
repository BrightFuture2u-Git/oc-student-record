package io.drew.record.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.drew.record.interfaces.OnImgClickListener;
import io.drew.record.util.GlideUtils;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/13 6:09 PM
 */
public class EditNineGridLayout extends NineGridLayout {

    private OnImgClickListener listener;

    public EditNineGridLayout(Context context) {
        super(context);
    }

    public EditNineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
    @Override
    protected boolean displayOneImage(ImageView imageView, String url, int parentWidth) {

        return true;
        // true 代表按照九宫格默认大小显示(此时不要调用setOneImageLayoutParams)；false 代表按照自定义宽高显示。
    }

    @Override
    protected void displayImage(ImageView imageView, String url) {
        if (isNumeric(url)) {
            Glide.with(mContext)
                    .load(Integer.parseInt(url))
                    .into(imageView);
        } else {
            GlideUtils.loadImg(mContext,url,imageView);
        }

    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public void setOnImgClickListener(OnImgClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        if (listener != null) listener.onImgClick(position, url, urlList);
    }
}
