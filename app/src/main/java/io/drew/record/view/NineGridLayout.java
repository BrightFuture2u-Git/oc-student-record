package io.drew.record.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import io.drew.record.R;
import io.drew.record.util.AppUtil;

public abstract class NineGridLayout extends ViewGroup {

    private static final float DEFUALT_SPACING = 3f;
    private static final int MAX_COUNT = 4;

    protected Context mContext;
    private float mSpacing = DEFUALT_SPACING;
    private int mColumns;
    private int mRows;
    private int mTotalWidth;
    private int mSingleWidth;

    private boolean mIsFirst = true;
    private List<String> mUrlList = new ArrayList<>();

    private int w_110;
    private int w_73;

    public NineGridLayout(Context context) {
        super(context);
        init(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
        mSpacing = typedArray.getDimension(R.styleable.NineGridLayout_sapcing, DEFUALT_SPACING);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        w_110 = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_110);
        w_73 = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_73);
        if (getListSize(mUrlList) == 0) {
            setVisibility(GONE);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTotalWidth = right - left;
        if (AppUtil.isPad(mContext)) {
            if (mUrlList.size() == 2) {
                mSingleWidth = (int) ((mTotalWidth - mSpacing) / 2);
                if (mSingleWidth > w_110) {
                    mSingleWidth = w_110;
                }
            } else {
                mSingleWidth = (int) ((mTotalWidth - mSpacing * (4 - 1)) / 4);
                if (mSingleWidth > w_73) {
                    mSingleWidth = w_73;
                }
            }
        } else {
            if (mUrlList.size() == 2) {
                mSingleWidth = (int) ((mTotalWidth - mSpacing) / 2);
            } else {
                mSingleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
            }
        }

        if (mIsFirst) {
            notifyDataSetChanged();
            mIsFirst = false;
        }

    }

    /**
     * 设置间隔
     *
     * @param spacing
     */
    public void setSpacing(float spacing) {
        mSpacing = spacing;
    }


    public void setUrlList(List<String> urlList) {
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);

        mUrlList.clear();
        mUrlList.addAll(urlList);

        if (!mIsFirst) {
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        post(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    private void refresh() {
        removeAllViews();
        int size = getListSize(mUrlList);
        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }

        if (size == 1) {
            String url = mUrlList.get(0);
            ImageView imageView = createImageView(0, url);
            //避免在ListView中一张图未加载成功时，布局高度受其他item影响
            LayoutParams params = getLayoutParams();
            params.height = mSingleWidth;
            setLayoutParams(params);
            imageView.layout(0, 0, mSingleWidth, mSingleWidth);

            boolean isShowDefualt = displayOneImage(imageView, url, mTotalWidth);
            if (isShowDefualt) {
                layoutImageView(imageView, 0, url);
            } else {
                addView(imageView);
            }
            return;
        }

        generateChildrenLayout(size);
        layoutParams();

        for (int i = 0; i < size; i++) {
            String url = mUrlList.get(i);
            ImageView imageView = createImageView(i, url);
            layoutImageView(imageView, i, url);
        }
    }

    private void layoutParams() {
        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        if (mUrlList.size() == 2) {
            params.height = mSingleWidth;
        } else {
            params.height = (int) (mSingleWidth * mRows + mSpacing * (mRows - 1));
        }
        setLayoutParams(params);
    }

    private ImageView createImageView(final int i, final String url) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage(i, url, mUrlList);
            }
        });
        return imageView;
    }

    /**
     * @param imageView
     * @param url
     */
    private void layoutImageView(ImageView imageView, int i, String url) {
        int singleWidth = 0;

        if (AppUtil.isPad(mContext)) {
            if (mUrlList.size() == 2) {
                singleWidth = (int) ((mTotalWidth - mSpacing) / 2);
                if (singleWidth > w_110) {
                    singleWidth = w_110;
                }
            } else {
                singleWidth = (int) ((mTotalWidth - mSpacing * (4 - 1)) / 4);
                if (singleWidth > w_73) {
                    singleWidth = w_73;
                }
            }
        } else {
            if (mUrlList.size() == 2) {
                singleWidth = (int) ((mTotalWidth - mSpacing) / 2);
            } else {
                singleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
            }
        }

        int singleHeight = singleWidth;

        int[] position = findPosition(i);
        int left = (int) ((singleWidth + mSpacing) * position[1]);
        int top = (int) ((singleHeight + mSpacing) * position[0]);
        int right = left + singleWidth;
        int bottom = top + singleHeight;

        imageView.layout(left, top, right, bottom);

        addView(imageView);
        displayImage(imageView, url);
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 根据图片个数确定行列数量
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (AppUtil.isPad(mContext)) {
            mRows = 1;
            mColumns = length;
            return;
        }
        if (length <= 3) {
            mRows = 1;
            mColumns = length;
        } else if (length == 4) {
            mRows = 2;
            mColumns = 2;
        }

    }

    protected void setOneImageLayoutParams(ImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LayoutParams(width, height));
        imageView.layout(0, 0, width, height);

        LayoutParams params = getLayoutParams();
//        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    private int getListSize(List<String> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    /**
     * @param imageView
     * @param url
     * @param parentWidth 父控件宽度
     * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
     */
    protected abstract boolean displayOneImage(ImageView imageView, String url, int parentWidth);

    protected abstract void displayImage(ImageView imageView, String url);

    protected abstract void onClickImage(int position, String url, List<String> urlList);
}