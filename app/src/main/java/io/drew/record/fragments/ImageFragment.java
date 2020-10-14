package io.drew.record.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.record.R;
import io.drew.record.activitys.ImageActivity;
import io.drew.record.base.BaseFragment;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/19 9:54 AM
 */
public class ImageFragment extends BaseFragment {
    private ImageActivity imageActivity;
    private String imageUrl;
    @BindView(R.id.image)
    protected ImageView image;
    @BindView(R.id.loading)
    protected ProgressBar loading;


    public static ImageFragment newInstance(String imageUrl) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", imageUrl);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_viewpager;
    }

    @Override
    protected void initData() {
        imageActivity = (ImageActivity) context;
        Bundle bundle = getArguments();
        if (bundle != null) {
            imageUrl = bundle.getString("data");
        }
    }

    @Override
    protected void initView() {
        Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.picture_icon_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image);
    }


    @OnClick({R.id.image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image:
                imageActivity.finish();
                break;
        }
    }

}
