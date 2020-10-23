package io.drew.record.activitys;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.drew.record.R;
import io.drew.record.adapters.FragmentAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.fragments.CommentTabFragment;
import io.drew.record.fragments.LikeTabFragment;
import io.drew.record.service.bean.response.MessageCount;

public class MyMessageActivity extends BaseActivity {

    @BindView(R.id.tab)
    protected SlidingTabLayout tab_layout;
    @BindView(R.id.viewPager)
    protected ViewPager viewPager;

    private MessageCount messageCount;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private String[] titles = {"评论", "赞"};

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {

        messageCount = (MessageCount) getIntent().getSerializableExtra("messageCount");

        if (messageCount == null)
            finish();
        fragmentList = new ArrayList<>();

        fragmentList.add(new CommentTabFragment());
        fragmentList.add(new LikeTabFragment());
    }

    @Override
    protected void initView() {
        initActionBar("我的消息", true);
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(fragmentAdapter);
        tab_layout.setViewPager(viewPager, titles);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tab_layout.hideMsg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (messageCount != null && messageCount.getCommentNum() > 0) {
            tab_layout.showMsg(0, messageCount.getCommentNum() + 1);
            tab_layout.setMsgMargin(0, 75, 0);
        }
        if (messageCount != null && messageCount.getLikeNum() > 0) {
            tab_layout.showMsg(1, messageCount.getLikeNum() + 1);
            tab_layout.setMsgMargin(1, 85, 0);
        }

    }
}
