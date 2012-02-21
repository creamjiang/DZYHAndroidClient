package com.dld.android.view;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnTouchListenerImpl implements View.OnTouchListener {
    int drawable;
    View view;

    public OnTouchListenerImpl(View paramView) {
        this.view = paramView;
    }

    public OnTouchListenerImpl(View paramView, int paramInt) {
        this.view = paramView;
        this.drawable = paramInt;
    }

    public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
        int i = paramMotionEvent.getAction();
        if (i != 0) {
            if ((i == 1) || (i == 3))
                paramView.setBackgroundColor(0);
        } else if (this.drawable <= 0)
            paramView.setBackgroundColor(1346734848);
        else
            paramView.setBackgroundResource(this.drawable);
        return false;
    }
}
