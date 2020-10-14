package io.drew.record.fragments_pad;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lihang.ShadowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.adapters.BgAdapter;
import io.drew.record.model.MessageEvent;
import io.drew.record.util.ShotUtils;
import io.drew.record.util.SpaceItemDecoration;

public class WorkDressFragment extends BaseDialogFragment {

    @BindView(R.id.iv_container)
    protected ImageView iv_container;
    @BindView(R.id.iv_center)
    protected ImageView iv_center;
    @BindView(R.id.relay_img)
    protected RelativeLayout relay_img;
    @BindView(R.id.tv_clear)
    protected TextView tv_clear;
    @BindView(R.id.shadow)
    protected ShadowLayout shadow;
    @BindView(R.id.recycleView_border)
    protected RecyclerView recycleView_border;
    @BindView(R.id.recycleView_bg)
    protected RecyclerView recycleView_bg;

    @BindView(R.id.bgshadow)
    protected ShadowLayout bgshadow;
    @BindView(R.id.line_content)
    protected LinearLayout line_content;

    private FrameLayout.LayoutParams params;


    private int current_border_index = -1;
    private int current_bg_index = -1;
    private List<Integer> borders = Arrays.asList
            (
                    R.drawable.border_work_white,
                    R.drawable.border_work_wood,
                    R.drawable.border_work_gold,
                    R.drawable.border_work_blue,
                    R.drawable.border_work_black,
                    R.drawable.border_work_pink
            );
    private List<Integer> bgs = Arrays.asList
            (
                    R.drawable.bg_work_oriange,
                    R.drawable.bg_work_white,
                    R.drawable.bg_work_blue,
                    R.drawable.bg_work_gray,
                    R.drawable.bg_work_green,
                    R.drawable.bg_work_pink,
                    R.drawable.bg_work_violet,
                    R.drawable.bg_work_drew
            );

    private String path;
    private int type = 3;
    private boolean cleared = true;
    private BgAdapter borderAdapter;
    private BgAdapter bgAdapter;

    public WorkDressFragment(String path) {
        this.path = path;
    }

    public WorkDressFragment(String path, int type) {
        this.path = path;
        this.type = type;
    }

    @Override
    protected int getScreenType() {
        return type;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_work_dress;
    }

    @Override
    protected void initData() {
        params = (FrameLayout.LayoutParams) iv_center.getLayoutParams();
    }

    @Override
    protected void initView() {
        initActionBar("装饰作品");
        setActionBarRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_clear.setVisibility(View.INVISIBLE);
                if (cleared) {
                    MessageEvent messageEvent = new MessageEvent(ConfigConstant.EB_WORK_DRESSED);
                    messageEvent.setMessage(path);
                    EventBus.getDefault().post(messageEvent);
                    dismiss();
                } else {
                    ShotUtils.viewShot(relay_img, new ShotUtils.ShotCallback() {
                        @Override
                        public void onShotComplete(String savePath, Bitmap bitmap) {
                            MessageEvent messageEvent = new MessageEvent(ConfigConstant.EB_WORK_DRESSED);
                            messageEvent.setMessage(savePath);
                            EventBus.getDefault().post(messageEvent);
                            dismiss();
                        }
                    });
                }


            }
        });
        setActionBarRightColor("#6A48F5");

        LinearLayoutManager linearLayoutManager_border = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager_bg = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recycleView_border.setLayoutManager(linearLayoutManager_border);
        recycleView_bg.setLayoutManager(linearLayoutManager_bg);
        borderAdapter = new BgAdapter(mContext, R.layout.item_work_dress_border, borders);
        bgAdapter = new BgAdapter(mContext, R.layout.item_work_dress_bg, bgs);
        recycleView_border.setAdapter(borderAdapter);
        recycleView_bg.setAdapter(bgAdapter);
        recycleView_border.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_5)));
        recycleView_bg.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_5)));

        borderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                checkBorder(position, true);
            }
        });
        bgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                checkBg(position, true);
            }
        });

        if (type == 2) {
            int margin = getContext().getResources().getDimensionPixelOffset(R.dimen.dp_43);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) relay_img.getLayoutParams();
            lp.setMargins(margin,
                    0,
                    margin,
                    0);

            if (bgshadow != null) {
                bgshadow.setShowShadow(false);
            }
            if (line_content != null) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) line_content.getLayoutParams();
                params.leftMargin = 0;
            }

        }
        iv_container.post(new Runnable() {
            @Override
            public void run() {
                showOriginalImg();
            }
        });
    }

    /**
     * 原图预览
     */
    private void showOriginalImg() {
        Glide.with(WorkDressFragment.this)
                .asBitmap()
                .load(path)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int width_new;
                        int height_new;
                        if (width > height) {
                            width_new = iv_container.getWidth() * 100 / 100;
                            height_new = (int) Math.ceil((float) width_new * (float) height / (float) width);
                        } else {
                            height_new = iv_container.getWidth() * 100 / 100;
                            width_new = (int) Math.ceil((float) height_new * (float) width / (float) height);
                        }
                        params.height = height_new;
                        params.width = width_new;
                        iv_center.setImageBitmap(bitmap);
                        iv_center.setBackground(null);
                        iv_container.setImageResource(0);
                    }
                });
        shadow.setShowShadow(false);
        tv_clear.setVisibility(View.INVISIBLE);
    }

    /**
     * 装扮后预览
     */
    private void showDressedImg() {
        Glide.with(WorkDressFragment.this)
                .asBitmap()
                .load(path)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int width_new;
                        int height_new;
                        if (width > height) {
                            width_new = iv_container.getWidth() * 70 / 100;
                            height_new = (int) Math.ceil((float) width_new * (float) height / (float) width);
                        } else {
                            height_new = iv_container.getWidth() * 70 / 100;
                            width_new = (int) Math.ceil((float) height_new * (float) width / (float) height);
                        }
                        params.height = height_new;
                        params.width = width_new;
                        iv_center.setImageBitmap(bitmap);
//                        iv_center.setBackground(null);
                    }
                });
        shadow.setShowShadow(true);
//        if (current_border_index < 0) {
//            current_border_index = 0;
//            checkBorder(current_border_index, false);
//        }
//        if (current_bg_index < 0) {
//            current_bg_index = 0;
//            checkBg(current_bg_index, false);
//        }
        defaultConfig();
        iv_center.setBackground(getResources().getDrawable(borders.get(current_border_index)));
        iv_container.setImageResource(bgs.get(current_bg_index));
        tv_clear.setVisibility(View.VISIBLE);
    }

    private void defaultConfig() {
        if (current_border_index < 0 && current_bg_index >= 0) {
            switch (current_bg_index) {
                case 0:
                    current_border_index = 1;
                    break;
                case 1:
                    current_border_index = 2;
                    break;
                case 2:
                case 4:
                    current_border_index = 0;
                    break;
                case 3:
                    current_border_index = 4;
                    break;
                case 5:
                case 7:
                    current_border_index = 5;
                    break;
                case 6:
                    current_border_index = 3;
                    break;
            }
            checkBorder(current_border_index, false);
        } else if (current_border_index >= 0 && current_bg_index < 0) {
            switch (current_border_index) {
                case 0:
                    current_bg_index = 2;
                    break;
                case 1:
                    current_bg_index = 0;
                    break;
                case 2:
                    current_bg_index = 1;
                    break;
                case 3:
                    current_bg_index = 6;
                    break;
                case 4:
                    current_bg_index = 3;
                    break;
                case 5:
                    current_bg_index = 7;
                    break;
            }
            checkBg(current_bg_index, false);
        }
    }

    private void checkBg(int index, boolean needShow) {
        cleared = false;
        current_bg_index = index;
        bgAdapter.check(index);
        if (needShow)
            showDressedImg();
    }

    private void checkBorder(int index, boolean needShow) {
        cleared = false;
        current_border_index = index;
        borderAdapter.check(index);
        if (needShow)
            showDressedImg();
    }

    private void clear() {
        cleared = true;
        current_bg_index = -1;
        current_border_index = -1;
        borderAdapter.check(-1);
        bgAdapter.check(-1);
        showOriginalImg();
    }

    @OnClick({R.id.tv_clear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                clear();
                break;
        }
    }

}
