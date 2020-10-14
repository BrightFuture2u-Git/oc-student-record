package io.drew.record.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.RoundedImageView;

import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/22 11:31 AM
 */
public class ResizableRoundImageView extends RoundedImageView {

    private int width_model;
    private int height_model;
    private int mask_coner;
    private boolean mask;

    public ResizableRoundImageView(Context context) {
        super(context);
    }

    public ResizableRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ResizableRoundImageView);
        if (typedArray != null) {
            width_model = typedArray.getInteger(R.styleable.ResizableRoundImageView_width_model, 0);
            height_model = typedArray.getInteger(R.styleable.ResizableRoundImageView_height_model, 0);
            mask_coner = typedArray.getDimensionPixelOffset(R.styleable.ResizableRoundImageView_mask_coner, 0);
            mask = typedArray.getBoolean(R.styleable.ResizableRoundImageView_mask_coner, false);
            typedArray.recycle();
        }
//        mask_coner = getResources().getDimensionPixelOffset(R.dimen.dp_8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度计算而得
            int height = (int) Math.ceil((float) width * (float) height_model / (float) width_model);
//            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void showMask(boolean show) {
        mask = show;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //半透明蒙层
        if (mask) {
            Paint _debugInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //设置无锯齿 也可以使用setAntiAlias(true)
            _debugInfoPaint.setColor(getResources().getColor(R.color.transparent_50));//设置画笔颜色
            _debugInfoPaint.setStrokeWidth(1.5f);//设置线宽
            _debugInfoPaint.setStyle(Paint.Style.FILL);//设置样式：FILL表示颜色填充整个；STROKE表示空心
            canvas.drawRoundRect(canvas.getClipBounds().left, canvas.getClipBounds().top, canvas.getClipBounds().right, canvas.getClipBounds().bottom,
                    mask_coner, mask_coner, _debugInfoPaint);
        }
    }
}
