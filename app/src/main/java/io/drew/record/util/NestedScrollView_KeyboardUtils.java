package io.drew.record.util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.core.widget.NestedScrollView;

/**
 * @Author: KKK
 * @CreateDate: 2020/4/30 2:14 PM
 */

public class NestedScrollView_KeyboardUtils {
    private static final String TAG = "NestedScrollView_KeyboardUtils";

    /**
     * @param activity 上下文
     * @param viewId   NestedScrollView_ID
     */
    public static void assistActivity(Activity activity, int viewId) {
        new NestedScrollView_KeyboardUtils(activity, viewId);
    }

    private View mChildOfContent;
    /**android.support.v4包中,新版的ScrollView**/
    private NestedScrollView mScrollView;

    private NestedScrollView_KeyboardUtils(Activity activity, int viewId) {
        //拿到当前XML文件的根布局
        FrameLayout content = activity
                .findViewById(android.R.id.content);
        //监听当前View的状态,进行通知回调,即"软键盘弹出""
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        possiblyResizeChildOfContent();
                    }
                });
        mScrollView = content.findViewById(viewId);
    }

    private void possiblyResizeChildOfContent() {
        int contentHeight = mChildOfContent.getRootView().getHeight();
        int curDisplayHeight = computeUsableHeight();
        if (contentHeight - curDisplayHeight > contentHeight / 4) {
            Log.e("kkk", "possiblyResizeChildOfContent: 1");
            mScrollView.scrollTo(0, 600);
            //                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } else {
            Log.e("kkk", "possiblyResizeChildOfContent: 2");
        }
    }

    /**
     * 软键盘弹出后,获取屏幕可显示区域高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        //这行代码能够获取到去除标题栏和被软键盘挡住的部分,所剩下的矩形区域
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        //r.top : 标题栏的高度
        //屏幕高度-r.bottom : 软键盘的高度
        //可用高度(全屏模式) : rect.bottom
        //可用高度(非全屏模式) : rect.bottom - rect.top
        return r.height();
    }
}

