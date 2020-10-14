package io.drew.record.activitys;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;

public class SuccessActivity extends BaseActivity {


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_success;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        hideActionBar();
    }

    @OnClick({R.id.btn_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                startActivity(new Intent(SuccessActivity.this, HomeActivity.class));
                break;
        }
    }
}
