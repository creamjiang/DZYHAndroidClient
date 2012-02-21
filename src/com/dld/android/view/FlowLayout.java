package com.dld.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class FlowLayout extends ViewGroup {
    private int line_height;
    private static boolean isAssertionsDisabled = false;

    static {
        boolean bool;
        if (FlowLayout.class.desiredAssertionStatus())
            bool = false;
        else
            bool = true;
        isAssertionsDisabled = bool;
    }

    public FlowLayout(Context paramContext) {
        super(paramContext);
    }

    public FlowLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
        boolean i;
        if (!(paramLayoutParams instanceof LayoutParams))
            i = false;
        else
            i = true;
        return i;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1);
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4) {
        int i = getChildCount();
        int i1 = paramInt3 - paramInt1;
        int j = getPaddingLeft();
        int i2 = getPaddingTop();
        for (int m = 0; m < i; m++) {
            View localView = getChildAt(m);
            if (localView.getVisibility() == 8)
                continue;
            int k = localView.getMeasuredWidth();
            int n = localView.getMeasuredHeight();
            LayoutParams localLayoutParams = (LayoutParams) localView
                    .getLayoutParams();
            if (j + k > i1) {
                j = getPaddingLeft();
                i2 += this.line_height;
            }
            localView.layout(j, i2, j + k, i2 + n);
            j += k + localLayoutParams.horizontal_spacing;
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        if ((isAssertionsDisabled)
                || (View.MeasureSpec.getMode(paramInt1) != 0)) {
            int k = View.MeasureSpec.getSize(paramInt1) - getPaddingLeft()
                    - getPaddingRight();
            int i2 = View.MeasureSpec.getSize(paramInt2) - getPaddingTop()
                    - getPaddingBottom();
            int m = getChildCount();
            int i = 0;
            int n = getPaddingLeft();
            int i3 = getPaddingTop();
            for (int j = 0; j < m; j++) {
                View localView = getChildAt(j);
                if (localView.getVisibility() == 8)
                    continue;
                LayoutParams localLayoutParams = (LayoutParams) localView
                        .getLayoutParams();
                localView.measure(
                        View.MeasureSpec.makeMeasureSpec(k, -2147483648),
                        View.MeasureSpec.makeMeasureSpec(i2, -2147483648));
                int i1 = localView.getMeasuredWidth();
                i = Math.max(i, localView.getMeasuredHeight()
                        + localLayoutParams.vertical_spacing);
                if (n + i1 > k) {
                    n = getPaddingLeft();
                    i3 += i;
                }
                n += i1 + localLayoutParams.horizontal_spacing;
            }
            this.line_height = i;
            if (View.MeasureSpec.getMode(paramInt2) == 0)
                i2 = i3 + i;
            setMeasuredDimension(k, i2);
            return;
        }
        throw new AssertionError();
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public final int horizontal_spacing;
        public final int vertical_spacing;

        public LayoutParams(int paramInt1, int paramInt2) {
            // super(0);
            super(paramInt1, paramInt2);
            this.horizontal_spacing = paramInt1;
            this.vertical_spacing = paramInt2;
        }
    }
}
