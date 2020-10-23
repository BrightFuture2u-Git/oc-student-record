package io.drew.record.fragments_pad;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.drew.record.R;
import io.drew.record.adapters.FragmentAdapter;
import io.drew.record.fragments.CommentTabFragment;
import io.drew.record.fragments.LikeTabFragment;
import io.drew.record.service.bean.response.MessageCount;

public class MyMessageFragment extends BaseDialogFragment {

    @BindView(R.id.slideTab)
    protected SlidingTabLayout slideTab;
    @BindView(R.id.viewPager_message)
    protected ViewPager viewPager_message;

    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private MessageCount messageCount;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private String[] titles = {"评论", "赞"};

    public MyMessageFragment(MessageCount messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_message;
    }

    @Override
    protected void initData() {
        if (messageCount == null)
            getParentFragmentManager().popBackStack();
        fragmentList = new ArrayList<>();

        fragmentList.add(new CommentTabFragment());
        fragmentList.add(new LikeTabFragment());
    }

    @Override
    protected void initView() {
        title.setText("我的消息");
        relay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragmentList, titles);
        viewPager_message.setAdapter(fragmentAdapter);
        slideTab.setViewPager(viewPager_message, titles);
        viewPager_message.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                slideTab.hideMsg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (messageCount != null && messageCount.getCommentNum() > 0) {
            slideTab.showMsg(0, messageCount.getCommentNum() + 1);
            slideTab.setMsgMargin(0, 75, 0);
        }
        if (messageCount != null && messageCount.getLikeNum() > 0) {
            slideTab.showMsg(1, messageCount.getLikeNum() + 1);
            slideTab.setMsgMargin(1, 85, 0);
        }

    }
}
