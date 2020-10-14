package io.drew.record.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.drew.record.util.SoftKeyBoardListener;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/20 11:18 AM
 */
public class CommentDialog extends Dialog {
    public CommentDialog(@NonNull Activity context) {
        super(context);
    }

    public CommentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CommentDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isShowing() && shouldCloseOnTouch(getContext(), event)) {
            SoftKeyBoardListener.hideInput((Activity) getContext());
        }
        return super.onTouchEvent(event);
    }

    public boolean shouldCloseOnTouch(Context context, MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(context, event) && getWindow().peekDecorView() != null;
    }

    //判断下边界
    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }
}
