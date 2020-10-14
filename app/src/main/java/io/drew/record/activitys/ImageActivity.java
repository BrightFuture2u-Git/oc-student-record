package io.drew.record.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;
import io.drew.record.fragments.ImageFragment;
import io.drew.record.view.ViewPagerFixed;

/**
 * 原图查看
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    protected ViewPagerFixed viewpager;
    @BindView(R.id.tv_index)
    protected TextView tv_index;
    private List<ImageFragment> fragments;
    private List<String> imageList;
    private int index;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        imageList = (List<String>) getIntent().getSerializableExtra("data");
        index = getIntent().getIntExtra("index", 0);
    }

    @Override
    protected void initView() {
        hideActionBar();

        fragments = new ArrayList<ImageFragment>();
        for (String s : imageList) {
            fragments.add(ImageFragment.newInstance(s));
        }
        if (fragments.size() > 1) {
            tv_index.setVisibility(View.VISIBLE);
        } else {
            tv_index.setVisibility(View.INVISIBLE);
        }
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_index.setText(position + 1 + "/" + fragments.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(index);
        tv_index.setText(index + 1 + "/" + fragments.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
