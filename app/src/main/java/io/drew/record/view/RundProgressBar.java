package io.drew.record.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/15 2:36 PM
 * 圆环进度条
 */
public class RundProgressBar extends View {
    private Paint paint;
    private int now = 0; //当前进度
    private int max = 100;//最大进度
    private Rect rect;
    private int rundwidth = 5;//圆弧宽度
    private int measuredWidth;


    public RundProgressBar(Context context) {
        this(context, null);
    }

    public RundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        initView();
    }

    private void initView() {
        paint = new Paint();//创建笔
        rect = new Rect();//创建矩形
    }

    /**
     * max 100
     *
     * @param currentProgress
     */
    public void setProgress(int currentProgress) {
        now = currentProgress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();//测量当前画布大小
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);//设置为空心圆
        paint.setStrokeWidth(rundwidth);
        paint.setColor(Color.WHITE);
        float x = measuredWidth / 2;
        float y = measuredWidth / 2;
        int rd = measuredWidth / 2 - rundwidth / 2;
        canvas.drawCircle(x, y, rd, paint);
        //绘制圆弧
        RectF rectF = new RectF(rundwidth / 2, rundwidth / 2, measuredWidth - rundwidth / 2, measuredWidth - rundwidth / 2);
        paint.setColor(Color.parseColor("#6A48F5"));
        canvas.drawArc(rectF, -90, now * 360 / max, false, paint);
//       //设置当前文字
//        String text=now*100/max+"%";
//        paint.setStrokeWidth(0);
//
//        Rect rect = new Rect();
//        paint.setTextSize(90);
//        paint.getTextBounds(text,0,text.length(),rect);
//        paint.setColor(Color.BLACK);
//        canvas.drawText(text,measuredWidth/2-rect.width()/2,measuredWidth/2+rect.height()/2,paint);
    }
}