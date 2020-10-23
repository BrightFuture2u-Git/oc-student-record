package io.drew.record.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.service.bean.response.Articles;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.MySharedPreferencesUtils;
import io.drew.record.view.MyNineGridLayout;


/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class ArticlesAdapter extends BaseQuickAdapter<Articles.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    public String loacalUserId;

    public ArticlesAdapter(Context context, int layoutResId, List<Articles.RecordsBean> data) {
        super(layoutResId, data);
        mContext = context;
        loacalUserId = (String) MySharedPreferencesUtils.get(mContext, ConfigConstant.SP_USER_ID, "");
    }

    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position, @NotNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.setIsRecyclable(false);//取消复用
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Articles.RecordsBean recordsBean) {
        ImageView iv_head = baseViewHolder.getView(R.id.iv_head);
        TextView tv_like = baseViewHolder.getView(R.id.tv_like);
        ImageView iv_delect = baseViewHolder.getView(R.id.iv_delect);
        ImageView iv_collection = baseViewHolder.getView(R.id.iv_collection);
        ImageView iv_share = baseViewHolder.getView(R.id.iv_share);
        baseViewHolder.setText(R.id.tv_name, recordsBean.getNickname());
        baseViewHolder.setText(R.id.tv_time, recordsBean.getFormatTime());

        GlideUtils.loadImg(mContext,recordsBean.getHeadImage(),iv_head);
        baseViewHolder.setText(R.id.tv_content, recordsBean.getContent());
        baseViewHolder.setText(R.id.tv_comment, String.valueOf(recordsBean.getCommentNum()));
        baseViewHolder.setText(R.id.tv_like, String.valueOf(recordsBean.getLikeNum()));

        baseViewHolder.setImageResource(R.id.iv_collection, recordsBean.getIsCollected() == 1 ? R.drawable.ic_item_collectioned : R.drawable.ic_item_collection);
        Drawable dw_tv_like = mContext.getResources().getDrawable(recordsBean.getIsLiked() == 1 ? R.drawable.ic_article_liked : R.drawable.ic_article_like);
        tv_like.setCompoundDrawablesWithIntrinsicBounds(dw_tv_like, null, null, null);
        if (isMyself(recordsBean)) {
            iv_delect.setVisibility(View.VISIBLE);
            if (recordsBean.getImageList() != null && recordsBean.getImageList().size() > 0) {
                iv_share.setVisibility(View.VISIBLE);
            } else {
                iv_share.setVisibility(View.GONE);
            }
            iv_collection.setVisibility(View.GONE);
        } else {
            iv_delect.setVisibility(View.GONE);
            iv_share.setVisibility(View.GONE);
            iv_collection.setVisibility(View.VISIBLE);
        }

        MyNineGridLayout layout_nine_grid = baseViewHolder.getView(R.id.layout_nine_grid);
        layout_nine_grid.setSpacing(5);
        layout_nine_grid.setUrlList(recordsBean.getImageList());
    }

    private boolean isMyself(Articles.RecordsBean recordsBean) {
        return recordsBean.getUserId().equals(loacalUserId);
    }
}
