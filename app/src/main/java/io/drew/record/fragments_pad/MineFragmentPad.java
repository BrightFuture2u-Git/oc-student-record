package io.drew.record.fragments_pad;

import android.content.Intent;
import android.net.Uri;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.base.BaseFragment;
import io.drew.record.fragments.UserProfileFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.bean.response.MyRecordWorks;
import io.drew.record.util.MySharedPreferencesUtils;


public class MineFragmentPad extends BaseFragment {

    @BindView(R.id.mainContainer)
    protected RelativeLayout mainContainer;
    @BindView(R.id.secondContainer)
    protected RelativeLayout secondContainer;
    private FragmentManager fragmentManager;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine_pad;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
            case ConfigConstant.EB_PAD_CHANGE_TAB:
                String type = event.getMessage();
               if (type.equals("about")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new CommonWebViewFragment(ConfigConstant.url_about + "?phone=" + MySharedPreferencesUtils.get(context, ConfigConstant.SP_ACCOUNT, "")));
                    transaction.commit();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConfigConstant.url_about + "?phone=" + MySharedPreferencesUtils.get(context, ConfigConstant.SP_ACCOUNT, ""))));
                } else if (type.equals("setting")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new SettingsFragment());
                    transaction.commit();
                } else if (type.equals("edit")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new UserProfileFragment());
                    transaction.commit();
                } else if (type.equals("editNickName")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.secondContainer, new EditNickNameFragment());
                    transaction.addToBackStack("editNickName");
                    transaction.commit();
                } else if (type.equals("successWork")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.secondContainer, new SuccessFragment());
                    transaction.addToBackStack("successWork");
                    transaction.commit();
                } else if (type.equals("uploadMyRecordWork")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.secondContainer, new UploadMyRecordWorkFragment());
                    transaction.addToBackStack("uploadMyRecordWork");
                    transaction.commit();
                } else if (type.equals("editMyRecordWork")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.secondContainer, new UploadMyRecordWorkFragment((MyRecordWorks.RecordsBean) event.getObjectMessage()));
                    transaction.addToBackStack("editMyWork");
                    transaction.commit();
                }else if (type.equals("myRecordLectures")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new MyRecordLecturesFragment());
                    transaction.commit();
                } else if (type.equals("address")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new AddressListCustomFragment());
                    transaction.commit();
                } else if (type.equals("orders")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new MyOrdersFragment());
                    transaction.commit();
                } else if (type.equals("recordWorks")) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.secondContainer, new MyRecordWorksFragment());
                    transaction.commit();
                }
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainContainer, new MineFragmentMain());
        transaction.add(R.id.secondContainer, new MyRecordLecturesFragment());
        transaction.commit();
    }

}
