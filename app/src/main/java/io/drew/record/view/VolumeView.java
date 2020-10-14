package io.drew.record.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * 音量展示控件
 */
public class VolumeView extends View {

    private Paint paint = new Paint();
    private int width_box;
    private int height_box;
    private int current_num = -1;
    private int max = 6;
    //    private int max=10;
    private boolean enable = false;//是否静音

    public VolumeView(Context context) {
        super(context);
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }

    public void setNum(int num) {
        current_num = num;
        invalidate();
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawRect(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        width_box = canvasWidth / (max * 2 + 1);
        height_box = canvasHeight / 3;

        if (enable) {//静音
            for (int i = 0; i < max; i++) {
                paint.setColor(Color.parseColor("#4dffffff"));
                canvas.drawRoundRect(2 * (i + 1) * width_box - width_box, height_box, 2 * (i + 1) * width_box, height_box * 2, 10, 10, paint);
            }
            paint.setColor(Color.parseColor("#F64945"));
            paint.setStrokeWidth((float) 5.0);
            canvas.drawLine(width_box, height_box, 2 * (6 + 1) * width_box-width_box, height_box * 2, paint);
        } else {
            for (int i = 0; i < max; i++) {
                if (i <= current_num) {
//                paint.setColor(Color.parseColor("#8088FF"));
                    paint.setColor(Color.parseColor("#F19422"));
                } else {
                    paint.setColor(Color.parseColor("#FFFFFF"));
//                paint.setColor(Color.parseColor("#36228B"));
                }
//            canvas.drawRect(2 * (i + 1) * width_box - width_box, height_box, 2 * (i + 1) * width_box, height_box * 2, paint);
                canvas.drawRoundRect(2 * (i + 1) * width_box - width_box, height_box, 2 * (i + 1) * width_box, height_box * 2, 10, 10, paint);
            }

        }


    }
}
