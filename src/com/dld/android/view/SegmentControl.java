package com.dld.android.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.dld.android.util.LogUtils;

import java.util.ArrayList;

public class SegmentControl extends LinearLayout {
    private SegmentControlAdapter adapter;
    private ArrayList<View> childs = new ArrayList();
    private View focused;

    public SegmentControl(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void changeFocus(int paramInt) {
        changeFocus((View) this.childs.get(paramInt), this.focused);
    }

    protected void changeFocus(View paramView1, View paramView2) {
        if ((this.adapter != null) && (!this.adapter.disable()))
            if (paramView2 != null) {
                if (paramView1 != this.focused) {
                    paramView2.setClickable(true);
                    this.adapter.onBlur(paramView2,
                            this.childs.indexOf(paramView2));
                    this.adapter.onFocus(paramView1,
                            this.childs.indexOf(paramView1));
                }
            } else {
                this.focused = paramView1;
                this.focused.setClickable(false);
            }
    }

    public SegmentControlAdapter getAdapter() {
        return this.adapter;
    }

    public int getFocusIndex() {
        return this.childs.indexOf(this.focused);
    }

    public void notifyDataChanged() {
        setAdapter(this.adapter);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        int j = (int) paramMotionEvent.getX();
        int i = (int) paramMotionEvent.getY();
        int k = getChildCount();
        int m = 0;
        while (m < k) {
            View localView = getChildAt(m);
            if (!new Rect(localView.getLeft(), localView.getTop(),
                    localView.getRight(), localView.getBottom()).contains(j, i)) {
                m++;
                continue;
            }
            if (localView != this.focused)
                changeFocus(localView, this.focused);
            else
                this.adapter.onBack();
        }
        return true;
    }

    public void setAdapter(SegmentControlAdapter paramSegmentControlAdapter) {
        this.adapter = paramSegmentControlAdapter;
        int i = paramSegmentControlAdapter.getDefaultFocusIndex();
        int j = paramSegmentControlAdapter.getCount();
        ArrayList contensChild = new ArrayList();
        for (int k = 0; k < j; k++) {
            LogUtils.log("SegmentControl", "contensChild.add(getChildAt(i)"
                    + getChildAt(k));
            contensChild.add(getChildAt(k));
        }
        for (int k = 0; k < j; k++) {
            View localView = paramSegmentControlAdapter.getView(k);
            if (!contensChild.contains(localView))
                addView(localView);
            this.childs.add(localView);
            localView.setFocusable(false);
            if (k != i)
                continue;
            changeFocus(localView, this.focused);
        }
    }
}
