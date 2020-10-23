package io.drew.record.fragments_pad;

import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import io.drew.record.R;
import io.drew.record.base.BaseFragment;
import io.drew.record.model.MessageEvent;


/**
 * 创作分享
 */
public class GalleryFragmentPad extends BaseFragment {

    @BindView(R.id.main)
    protected RelativeLayout mainContainer;
    @BindView(R.id.second)
    protected RelativeLayout secondContainer;
    private FragmentManager fragmentManager;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_gallery_main;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main, new TabGalleryFragmentPad());
        transaction.add(R.id.second, new MenuFragment());
        transaction.commit();
    }

}
