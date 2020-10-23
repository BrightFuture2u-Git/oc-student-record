package io.drew.record.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.CommentedListInfo;
import io.drew.record.util.GlideUtils;


/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class CommentedAdapter extends BaseQuickAdapter<CommentedListInfo.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public CommentedAdapter(Context context, int layoutResId, List<CommentedListInfo.RecordsBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position, @NotNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommentedListInfo.RecordsBean recordsBean) {
        ImageView iv_head = baseViewHolder.getView(R.id.iv_head);
        ImageView img_article = baseViewHolder.getView(R.id.img_article);
        TextView dot = baseViewHolder.getView(R.id.dot);
        baseViewHolder.setText(R.id.tv_name, recordsBean.getNickname());
        baseViewHolder.setText(R.id.tv_time, recordsBean.getFormatTime());

        GlideUtils.loadImg(mContext,recordsBean.getHeadImage(),iv_head);
        baseViewHolder.setText(R.id.tv_content, recordsBean.getContent());
        baseViewHolder.setText(R.id.content_article, recordsBean.getArticleContent());
        if (recordsBean.getImageList() != null && recordsBean.getImageList().size() > 0) {
            GlideUtils.loadImg(mContext,recordsBean.getImageList().get(0),img_article);
            img_article.setVisibility(View.VISIBLE);
        } else {
            img_article.setVisibility(View.GONE);
        }
        if (recordsBean.getIsRead() == 1) {
            dot.setVisibility(View.INVISIBLE);
        } else {
            dot.setVisibility(View.VISIBLE);
        }
    }
}
