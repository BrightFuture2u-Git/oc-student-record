package io.drew.record.activitys_pad;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.moor.imkf.GetPeersListener;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.InitListener;
import com.moor.imkf.OnSessionBeginListener;
import com.moor.imkf.model.entity.Peer;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.WebViewActivity;
import io.drew.record.base.BaseActivity;
import io.drew.record.fragments_pad.GalleryFragmentPad;
import io.drew.record.fragments_pad.HomeFragmentPad;
import io.drew.record.fragments_pad.MineFragmentPad;
import io.drew.record.fragments_pad.MyRecordLecturesFragment;
import io.drew.record.fragments_pad.VerticalLoginFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.util.CustomerHelper;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.TagHelper;
import io.drew.record.util.WxShareUtil;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/28 12:05 AM
 */
public class HomeActivity_Pad extends BaseActivity {
    @BindView(R.id.container)
    protected RelativeLayout container;
    @BindView(R.id.tv_home)
    protected TextView tv_home;
    @BindView(R.id.tv_record)
    protected TextView tv_record;
    @BindView(R.id.tv_gallery)
    protected TextView tv_gallery;
    @BindView(R.id.tv_mine)
    protected TextView tv_mine;
    @BindView(R.id.line_changeBaby)
    public LinearLayout line_changeBaby;
    @BindView(R.id.iv_head)
    protected ImageView iv_head;
    @BindView(R.id.tv_name)
    protected TextView tv_name;

    private FragmentTransaction mFragmentTransaction;//fragment事务
    private FragmentManager mFragmentManager;//fragment管理者
    private HomeFragmentPad homeFragment;
    private MyRecordLecturesFragment recordFragment;
    private GalleryFragmentPad galleryFragment;
    private MineFragmentPad mineFragment;
    public int currentTab = -1;
    public SharedPreferences sharedPreferences;
    public Peer peer;
    public boolean init7mSuccess = false;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver localReceiver;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        sharedPreferences = getSharedPreferences("serviceNotice", Context.MODE_PRIVATE);
        localReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("new_message_customer")) {
//                    homeFragment.getUnReadCount();
                }
            }
        };
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("new_message_customer");
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        WxShareUtil.getInstance().regToWx(this);
        CustomerHelper.getInstance(this);
        initCustomer();//初始化客服
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getIntExtra("code", 0) == 401) {
                new VerticalLoginFragment().show(getSupportFragmentManager(), "login");
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        if (event.getCode() == ConfigConstant.EB_FIND_RECORD_COURSE) {
            changeTab(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    public void initCustomer() {
        new Thread() {
            @Override
            public void run() {
                IMChatManager.getInstance().setOnInitListener(new InitListener() {
                    @Override
                    public void oninitSuccess() {
                        MyLog.d("kkk--MainActivity", "customer初始化成功");
                        init7mSuccess = true;
//                        homeFragment.getUnReadCount();
                        IMChatManager.getInstance().getPeers(new GetPeersListener() {
                            @Override
                            public void onSuccess(List<Peer> peers) {
                                for (Peer peer : peers) {
                                    MyLog.d("技能组" + peer.getId() + "/" + peer.getName() + "/" + peer.getStatus());
                                }
                                if (peers != null && peers.size() > 0) {
                                    peer = peers.get(0);
                                    IMChatManager.getInstance().beginSession(peer.getId(), new OnSessionBeginListener() {
                                        @Override
                                        public void onSuccess() {
                                            MyLog.d("会话开始成功");
                                        }

                                        @Override
                                        public void onFailed() {
                                            MyLog.d("会话开始失败");
                                        }
                                    });
                                } else {
                                    MyLog.d("没有找到合适的技能组");
                                }

                            }

                            @Override
                            public void onFailed() {
                                MyLog.e("技能组获取失败");
                            }
                        });
                    }

                    @Override
                    public void onInitFailed() {
                        Log.d("kkk--MainActivity", "sdk初始化失败, 请填写正确的accessid");
                    }
                });
                if (EduApplication.instance.authInfo != null && EduApplication.instance.authInfo.getUser() != null) {
                    Log.d("kkk", "7M-----sdk开始初始化");
                    IMChatManager.getInstance().init(getApplicationContext(),
                            "m7.KEFU_NEW_MSG",
                            ConfigConstant.CUSTOMER_7MOOR,
                            EduApplication.instance.authInfo.getUser().getNickname(),
                            EduApplication.instance.authInfo.getUser().getId());
                } else {
                    Log.d("kkk", "7M-----sdk不开始初始化");
                }

            }
        }.start();
    }

    @Override
    protected void initView() {
        hideActionBar();
        mFragmentManager = getSupportFragmentManager();
        changeTab(0);

        fresh();

        if (!sharedPreferences.getBoolean(ConfigConstant.SP_ALLOW_SERVICE, false)) {
            showServiceNoticeDialog();
        }
    }

    @OnClick({R.id.tv_home, R.id.tv_record, R.id.tv_gallery, R.id.tv_mine, R.id.line_changeBaby})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                changeTab(0);
                TagHelper.getInstance().submitEvent("btn_v", "home_bottom");
                break;
            case R.id.tv_record:
                changeTab(1);
                break;
            case R.id.tv_gallery:
                changeTab(2);
                break;
            case R.id.tv_mine:
                changeTab(3);
                break;
            case R.id.line_changeBaby:
                if (isLogin()) {
//                    showKidChangeDialog(EduApplication.instance.authInfo.getUser().getStudentList());
                } else {
                    goLogin();
                }
                break;
        }
    }

    public void changeTab(int index) {
        if (index == currentTab) return;
        switch (currentTab) {
            case 0:
                tv_home.setSelected(false);
                tv_home.setTextColor(Color.parseColor("#333333"));
                break;
            case 1:
                tv_record.setSelected(false);
                tv_record.setTextColor(Color.parseColor("#333333"));
                break;
            case 2:
                tv_gallery.setSelected(false);
                tv_gallery.setTextColor(Color.parseColor("#333333"));
                break;
            case 3:
                tv_mine.setSelected(false);
                tv_mine.setTextColor(Color.parseColor("#333333"));
                break;
        }
        currentTab = index;
        mFragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(mFragmentTransaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragmentPad();
                    mFragmentTransaction.add(R.id.container, homeFragment);
                } else {
                    mFragmentTransaction.show(homeFragment);
                }
                tv_home.setSelected(true);
                tv_home.setTextColor(Color.parseColor("#6A48F5"));
                break;
            case 1:
                if (recordFragment == null) {
                    recordFragment = new MyRecordLecturesFragment();
                    mFragmentTransaction.add(R.id.container, recordFragment);
                } else {
                    mFragmentTransaction.show(recordFragment);
                }
                tv_record.setSelected(true);
                tv_record.setTextColor(Color.parseColor("#6A48F5"));
                break;
            case 2:
                if (galleryFragment == null) {
                    galleryFragment = new GalleryFragmentPad();
                    mFragmentTransaction.add(R.id.container, galleryFragment);
                } else {
                    mFragmentTransaction.show(galleryFragment);
                }
                tv_gallery.setSelected(true);
                tv_gallery.setTextColor(Color.parseColor("#6A48F5"));
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragmentPad();
                    mFragmentTransaction.add(R.id.container, mineFragment);
                } else {
                    mFragmentTransaction.show(mineFragment);
                }
                tv_mine.setSelected(true);
                tv_mine.setTextColor(Color.parseColor("#6A48F5"));
                break;
        }
        mFragmentTransaction.commit();
    }

    public void fresh() {
        if (isLogin()) {
            GlideUtils.loadBabyHead(this, EduApplication.instance.authInfo.getUser().getHeadImage(), iv_head);
            iv_head.setImageResource(R.drawable.head_cat);
            tv_name.setText(EduApplication.instance.authInfo.getUser().getNickname());
        } else {
            iv_head.setImageResource(R.drawable.head_cat);
            tv_name.setText("请登录");
        }
    }

    /**
     * 用来隐藏fragment的方法
     *
     * @param fragmentTransaction
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (recordFragment != null) {
            fragmentTransaction.hide(recordFragment);
        }
        if (galleryFragment != null) {
            fragmentTransaction.hide(galleryFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
    }


    private void showServiceNoticeDialog() {
        Dialog addDialog = new Dialog(this, R.style.mdialog);
        View content = LayoutInflater.from(this).inflate(R.layout.dialog_service, null);
        Button btn_sure = content.findViewById(R.id.btn_sure);
        TextView rule_content_txt = content.findViewById(R.id.rule_content_txt);
        TextView tv_refuse = content.findViewById(R.id.tv_refuse);
        SpannableString spanString = new SpannableString(getString(R.string.user_rule_content_new2));
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("url", ConfigConstant.url_user_agreement);
                intent.setClass(HomeActivity_Pad.this, WebViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#ff5281fa"));
                ds.setUnderlineText(false);

            }
        }, 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spanString2 = new SpannableString(getString(R.string.user_rule_content_new4));
        spanString2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("url", ConfigConstant.url_privacy);
                intent.setClass(HomeActivity_Pad.this, WebViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#ff5281fa"));
                ds.setUnderlineText(false);

            }
        }, 0, spanString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        rule_content_txt.setText(getString(R.string.user_rule_content_new1));
        rule_content_txt.append(spanString);
        rule_content_txt.append(getString(R.string.user_rule_content_new3));
        rule_content_txt.append(spanString2);
        rule_content_txt.append(getString(R.string.user_rule_content_new5));
        rule_content_txt.setMovementMethod(LinkMovementMethod.getInstance());
        addDialog.setContentView(content);
        addDialog.setCancelable(false);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putBoolean(ConfigConstant.SP_ALLOW_SERVICE, true);
                editor.apply();
                addDialog.dismiss();
            }
        });
        tv_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
                finish();
            }
        });
        Window dialogWindow = addDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = getResources().getDimensionPixelOffset(R.dimen.dp_160);
        lp.height = getResources().getDimensionPixelOffset(R.dimen.dp_200);
        dialogWindow.setAttributes(lp);
        addDialog.show();
    }

}
