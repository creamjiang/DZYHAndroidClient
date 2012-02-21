package com.dld.android.view;

import android.view.View;

public abstract class SegmentControlAdapter {
    public boolean disable() {
        return false;
    }

    public abstract int getCount();

    public abstract int getDefaultFocusIndex();

    public abstract View getView(int paramInt);

    public abstract void onBack();

    public abstract void onBlur(View paramView, int paramInt);

    public abstract void onFocus(View paramView, int paramInt);
}
