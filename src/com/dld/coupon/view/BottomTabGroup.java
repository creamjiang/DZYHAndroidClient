package com.dld.coupon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.android.view.SegmentControl;

public class BottomTabGroup extends SegmentControl implements
        DragViewGroupNavigation {
    public BottomTabGroup(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void onChange(int paramInt1, int paramInt2, int paramInt3) {
        changeFocus(paramInt1);
        LogUtils.log("Notify Change To", paramInt1 + " ");
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        return super.onInterceptTouchEvent(paramMotionEvent);
    }
}
