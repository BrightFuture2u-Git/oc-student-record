package io.drew.record.fragments_pad;

import android.view.View;

import butterknife.OnClick;
import io.drew.record.R;
import io.drew.record.base.BaseFragment;

public class SuccessFragment extends BaseFragment {


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_success;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.btn_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getParentFragmentManager().popBackStack();
                break;
        }
    }
}
