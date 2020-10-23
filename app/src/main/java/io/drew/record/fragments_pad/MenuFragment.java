package io.drew.record.fragments_pad;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.MessageCount;
import q.rorbin.badgeview.QBadgeView;

/**
 * 动态，消息，收藏
 */
public class MenuFragment extends BaseFragment {

    @BindView(R.id.line_myarticles)
    protected LinearLayout line_myarticles;
    @BindView(R.id.line_mymsg)
    protected LinearLayout line_mymsg;
    @BindView(R.id.line_mycollection)
    protected LinearLayout line_mycollection;
    @BindView(R.id.tv_msg)
    protected TextView tv_msg;
    @BindView(R.id.btn_add_article)
    protected TextView btn_add_article;

    private MessageCount messageCount;
    private QBadgeView badge;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_menu;
    }

    @Override
    protected void initData() {
        if (EduApplication.instance.authInfo!=null){
            getMessageData();
        }
    }

    private void getMessageData() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class)
                .getNewMessageCount().enqueue(new BaseCallback<>(messageCount -> {
            if (messageCount != null) {
                this.messageCount = messageCount;
                if (messageCount.getLikeNum() + messageCount.getCommentNum() > 0) {
                    badge.setBadgeNumber(-1);
                } else {
                    badge.hide(false);
                }
            }
        }, throwable -> {
            MyLog.e("新消息总数获取失败" + throwable.getMessage());
        }));
    }

    @Override
    protected void initView() {
        badge = new QBadgeView(getActivity());
        badge.setBadgePadding(4, true);//设置大小
        badge.setBadgeBackgroundColor(Color.parseColor("#F24724"));
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.bindTarget(tv_msg);
    }

    @OnClick({R.id.line_myarticles, R.id.line_mymsg, R.id.line_mycollection, R.id.btn_add_article})
    public void onClick(View v) {
        if (EduApplication.instance.authInfo==null){
            goLogin();
            return;
        }
        switch (v.getId()) {
            case R.id.line_myarticles:
                new MyArticlesFragment().show(getParentFragmentManager(), "myArticles");
                break;
            case R.id.line_mymsg:
                new MyMessageFragment(messageCount).show(getParentFragmentManager(), "myMsg");
                break;
            case R.id.line_mycollection:
                new MyCollectionsFragment().show(getParentFragmentManager(), "myCollection");
                break;
            case R.id.btn_add_article:
                new ArticleEditFragment().show(getParentFragmentManager(), "addArticle");
                break;
        }
    }
}
