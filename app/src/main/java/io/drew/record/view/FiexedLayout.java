package io.drew.record.view;

/**
 * @Author: KKK
 * @CreateDate: 2020/4/30 1:41 AM
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.io.Serializable;

import io.drew.record.R;


public class FiexedLayout extends RelativeLayout implements Serializable {

    private int mDemoHeight = -1;
    private int mDemoWidth = -1;
    private String mStandard = "w";

    public FiexedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FiexedLayout, defStyle, 0);
        if(a!=null){
            mDemoHeight = a.getInteger(R.styleable.FiexedLayout_demoHeight,-1);
            mDemoWidth = a.getInteger(R.styleable.FiexedLayout_demoWidth,-1);
            mStandard = a.getString(R.styleable.FiexedLayout_standard);
        }
    }

    public FiexedLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FiexedLayout(Context context) {
        this(context,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        if(mStandard.length() > 0 && mDemoWidth != -1 && mDemoHeight != -1){
            if(mStandard.equals("w")){//以宽为标准
                int childWidthSize = getMeasuredWidth();
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * mDemoHeight / mDemoWidth, MeasureSpec.EXACTLY);
            }else if(mStandard.equals("h")){//以高为标准
                int childheightSize = getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childheightSize, MeasureSpec.EXACTLY);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childheightSize * mDemoWidth / mDemoHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
