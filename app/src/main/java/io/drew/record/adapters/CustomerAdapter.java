package io.drew.record.adapters;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseDelegateMultiAdapter;
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.m7.imkfsdk.utils.DateUtil;
import com.moor.imkf.model.entity.FromToMessage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.util.GlideUtils;

/**
 * @Author: KKK
 * @CreateDate: 2020/7/8 11:16 AM
 */
public class CustomerAdapter extends BaseDelegateMultiAdapter<FromToMessage, BaseViewHolder> {

    public CustomerAdapter(@Nullable List<FromToMessage> data) {
        super(data);
        setMultiTypeDelegate(new BaseMultiTypeDelegate<FromToMessage>() {
            @Override
            public int getItemType(@NotNull List<? extends FromToMessage> list, int i) {
                switch (list.get(i).userType) {
                    case "0":
                        return 1;
                    case "1":
                        return 2;
                    default:
                        break;
                }
                return 0;
            }
        });

        getMultiTypeDelegate()
                .addItemType(1, R.layout.item_message_send)
                .addItemType(2, R.layout.item_message_receive);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable FromToMessage item) {
        ImageView head = helper.getView(R.id.chatting_avatar_iv);
        helper.setText(R.id.chatting_time_tv, DateUtil.getDateString(item.when, 1));
        switch (helper.getItemViewType()) {
            case 1://我发送出去的
                GlideUtils.loadCircleImg(getContext(),EduApplication.instance.authInfo.getUser().getHeadImage(),head,R.drawable.head_user_placeholder);
                helper.setText(R.id.chat_content_tv, item.message);
                break;
            case 2://客服发给我的
                if (!TextUtils.isEmpty(item.im_icon)) {
                    GlideUtils.loadCircleImg(getContext(),item.im_icon,head,R.drawable.ic_customer);
                }
                helper.setText(R.id.tv_content, item.message);
                break;
        }
    }
}
