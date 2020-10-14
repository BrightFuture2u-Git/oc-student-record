package io.drew.record.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.binioter.guideview.Component;

import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/24 10:58 AM
 */
public class DrewGuide implements Component {
    private int type;
    private View target;
    private Activity mActivity;
    private int XOffset;

    public DrewGuide(int type, Activity activity, View target, int XOffset) {
        this.target = target;
        this.mActivity = activity;
        this.XOffset = XOffset;
        this.type = type;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        RelativeLayout ll = null;
        switch (type) {
            case 1:
                ll = (RelativeLayout) inflater.inflate(R.layout.guide_enter, null);
                break;
            case 2:
                ll = (RelativeLayout) inflater.inflate(R.layout.guide_change_baby_home, null);
                break;
            case 3:
                ll = (RelativeLayout) inflater.inflate(R.layout.guide_change_baby_mine, null);
                break;
            case 4:
                ll = (RelativeLayout) inflater.inflate(R.layout.guide_change_baby_home_pad, null);
                break;
            case 5:
                ll = (RelativeLayout) inflater.inflate(R.layout.guide_enter_pad, null);
                break;
        }
        return ll;
    }

    @Override
    public int getAnchor() {
        if (type == 4) {
            return Component.ANCHOR_RIGHT;
        } else {
            return Component.ANCHOR_BOTTOM;
        }
    }

    @Override
    public int getFitPosition() {
        if (type==5){
            return Component.FIT_START;
        }
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return XOffset;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
