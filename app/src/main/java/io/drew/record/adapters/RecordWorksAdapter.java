package io.drew.record.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.MyRecordWorks;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.TimeUtil;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class RecordWorksAdapter extends BaseQuickAdapter<MyRecordWorks.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public RecordWorksAdapter(Context context, int layoutResId, List<MyRecordWorks.RecordsBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder holder, int position, @NotNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyRecordWorks.RecordsBean recordsBean) {
        ImageView iv_img = baseViewHolder.getView(R.id.iv_img);
        ImageView iv_edit = baseViewHolder.getView(R.id.iv_edit);
        View line = baseViewHolder.getView(R.id.line);
        RelativeLayout relay_des = baseViewHolder.getView(R.id.relay_des);
        TextView tv_time_des = baseViewHolder.getView(R.id.tv_time_des);
        RelativeLayout relay_comment = baseViewHolder.getView(R.id.relay_comment);
        RelativeLayout relay_comment_empty = baseViewHolder.getView(R.id.relay_comment_empty);
        TextView tv_time_comment = baseViewHolder.getView(R.id.tv_time_comment);
        GifImageView gif_des = baseViewHolder.getView(R.id.gif_des);
        GifImageView gif_comment = baseViewHolder.getView(R.id.gif_comment);
        GifDrawable gif_des_drawable = (GifDrawable) gif_des.getDrawable();
        GifDrawable gif_comment_drawable = (GifDrawable) gif_comment.getDrawable();
        gif_des_drawable.stop();
        gif_comment_drawable.stop();

        GlideUtils.loadImg(mContext, recordsBean.getProductImage(), iv_img);
        baseViewHolder.setText(R.id.tv_workname, String.valueOf(recordsBean.getLectureName()));
        baseViewHolder.setText(R.id.tv_time, "创作时间：" + recordsBean.getProductTime());
        if (TextUtils.isEmpty(recordsBean.getProductVoice())) {
            relay_des.setVisibility(View.GONE);
        } else {
            relay_des.setVisibility(View.VISIBLE);
            tv_time_des.setText(TimeUtil.getVideoTime(1000 * Integer.parseInt(recordsBean.getProductVoiceSeconds())));
        }
        if (TextUtils.isEmpty(recordsBean.getReviewVoice())) {
            relay_comment.setVisibility(View.GONE);
            relay_comment_empty.setVisibility(View.VISIBLE);
            iv_edit.setVisibility(View.VISIBLE);
        } else {
            relay_comment.setVisibility(View.VISIBLE);
            relay_comment_empty.setVisibility(View.GONE);
            iv_edit.setVisibility(View.GONE);
            tv_time_comment.setText(TimeUtil.getVideoTime(1000 * Integer.parseInt(recordsBean.getReviewVoiceSeconds())));
        }
        if (baseViewHolder.getAdapterPosition() == getItemCount() - 2) {//emptyView占一个
            line.setVisibility(View.INVISIBLE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

    }

}
