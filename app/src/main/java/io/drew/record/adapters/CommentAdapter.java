package io.drew.record.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.CommentsInfo;
import io.drew.record.util.GlideUtils;


/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class CommentAdapter extends BaseQuickAdapter<CommentsInfo.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CommentAdapter(Context context, int layoutResId, List<CommentsInfo.RecordsBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position, @NotNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommentsInfo.RecordsBean recordsBean) {
        ImageView iv_head = baseViewHolder.getView(R.id.iv_head);
        ImageView iv_like = baseViewHolder.getView(R.id.iv_like);
        TextView tv_like = baseViewHolder.getView(R.id.tv_like);
        baseViewHolder.setText(R.id.tv_name, recordsBean.getNickname());
        baseViewHolder.setText(R.id.tv_time, recordsBean.getFormatTime());
        GlideUtils.loadImg(mContext,recordsBean.getHeadImage(),iv_head);
        baseViewHolder.setText(R.id.tv_content, recordsBean.getContent());
        baseViewHolder.setText(R.id.tv_like, String.valueOf(recordsBean.getLikeNum()));
        iv_like.setImageResource(recordsBean.getIsLiked() == 1 ? R.drawable.ic_article_liked : R.drawable.ic_article_like);
    }
}
