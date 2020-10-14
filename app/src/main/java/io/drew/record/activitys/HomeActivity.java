package io.drew.record.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.adapters.KidAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.fragments.MineFragment;
import io.drew.record.fragments.RecordFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.AppUtil;
import io.drew.record.util.CustomerHelper;
import io.drew.record.util.TagHelper;
import io.drew.record.util.WxShareUtil;


public class HomeActivity extends BaseActivity {

    public BottomNavigationView navView;
    public int lastIndex;
    private List<Fragment> mFragments;
    private List<Integer> ids;
    public SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        sharedPreferences = getSharedPreferences("serviceNotice", Context.MODE_PRIVATE);
        mFragments = new ArrayList<>();
        mFragments.add(new RecordFragment());
        mFragments.add(new MineFragment());

        ids = new ArrayList<>();
        ids.add(R.id.record);
        ids.add(R.id.mine);

        setFragmentPosition(0);

        WxShareUtil.getInstance().regToWx(this);
        CustomerHelper.getInstance(this);//初始化客服

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Override
    protected void receiveEvent(MessageEvent event) {
        if (event.getCode() == ConfigConstant.EB_FIND_RECORD_COURSE) {
            navView.setSelectedItemId(navView.getMenu().getItem(0).getItemId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {
        customStatusBar();
        hideActionBar();
        navView = findViewById(R.id.nav_view);
        //Label is shown on all navigation items
        navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //todo navView=null 待观察
        navView.setItemIconTintList(null);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.record:
                        customStatusBar();
                        setFragmentPosition(0);
                        TagHelper.getInstance().submitEvent("btn_v", "home_bottom");
                        break;
                    case R.id.mine:
                        setStatusBarFullTransparent();
                        setFragmentPosition(1);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
        clearToast(navView, ids);
        if (!sharedPreferences.getBoolean(ConfigConstant.SP_ALLOW_SERVICE, false)) {
            showServiceNoticeDialog();
        }
    }

    /**
     * * 清除长按时的toast
     * * @param bottomNavigationView 当前BottomNavigationView
     *
     * @param ids                  与配置文件中对应的所有id
     */
    public static void clearToast(BottomNavigationView bottomNavigationView, List ids) {
        ViewGroup bottomNavigationMenuView = (ViewGroup) bottomNavigationView.getChildAt(0);
        for (int position = 0; position < ids.size(); position++) {
            bottomNavigationMenuView.getChildAt(position).findViewById((Integer) ids.get(position)).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }

    public void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commitAllowingStateLoss();
            ft.add(R.id.ll_frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 用户协议
     */
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
                intent.setClass(HomeActivity.this, WebViewActivity.class);
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
                intent.setClass(HomeActivity.this, WebViewActivity.class);
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
        lp.width = getResources().getDimensionPixelOffset(R.dimen.dp_320);
        lp.height = getResources().getDimensionPixelOffset(R.dimen.dp_400);
        dialogWindow.setAttributes(lp);
        addDialog.show();
    }

    /**
     * 切换宝宝弹窗
     *
     * @param studentList
     */
    public void showKidChangeDialog(List<AuthInfo.UserBean.StudentListBean> studentList) {
        final int[] current = {EduApplication.instance.currentKid_index};
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_kid, null);
        GridView gridView = view.findViewById(R.id.gridView);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        KidAdapter kidAdapter = new KidAdapter(HomeActivity.this);
        kidAdapter.setData(studentList, current[0]);
        gridView.setAdapter(kidAdapter);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentList == null) {
                    dialog.dismiss();
                    return;
                }
                if (current[0] != EduApplication.instance.currentKid_index) {
                    EduApplication.instance.currentKid_index = current[0];
                    EduApplication.instance.currentKid = studentList.get(current[0]);
//                    MySharedPreferencesUtils.put(HomeActivity.this, ConfigConstant.SP_CURRENT_KID_INDEX, current[0]);
                    EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_KID_CHANGE));
                }
                dialog.dismiss();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (studentList == null || position == studentList.size()) {
                    Dialog addDialog = new Dialog(HomeActivity.this, R.style.mdialog);
                    View content = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_notice, null);
                    Button btn_sure = content.findViewById(R.id.btn_sure);
                    addDialog.setContentView(content);
                    btn_sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addDialog.dismiss();
                        }
                    });

                    Window dialogWindow = addDialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = getResources().getDimensionPixelOffset(R.dimen.dp_320);
                    lp.height = getResources().getDimensionPixelOffset(R.dimen.dp_240);
                    dialogWindow.setAttributes(lp);
                    addDialog.show();
                } else {
                    current[0] = position;
                    kidAdapter.checkItem(current[0]);
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = AppUtil.getScreenWidth(HomeActivity.this);
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
