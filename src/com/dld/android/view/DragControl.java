package com.dld.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class DragControl extends DragViewGroup {
    public DragControl(Context paramContext) {
        super(paramContext);
        this.touchSlop /= 3.0F;
    }

    public DragControl(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.touchSlop /= 3.0F;
    }

    protected void dispatchDraw(Canvas paramCanvas) {
        long l = getDrawingTime();
        int i = getChildCount();
        for (int j = 0; j < i; j++)
            drawChild(paramCanvas, getChildAt(j), l);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        int j = getChildCount();
        for (int i = 0; i < j; i++) {
            View localView = getChildAt(i);
            localView.measure(localView.getWidth(), paramInt2);
        }
    }
}
