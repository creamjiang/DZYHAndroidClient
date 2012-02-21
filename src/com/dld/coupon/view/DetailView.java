package com.dld.coupon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dld.protocol.json.CouponDetail;

public abstract class DetailView extends LinearLayout {
    public DetailView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public abstract boolean isEmpty();

    public abstract boolean isReady();

    public abstract void onCreate(CouponDetail paramCouponDetail,
            Showable paramShowable, CouponDetailView.Listener paramListener,
            int paramInt, boolean paramBoolean);

    public abstract void onDestroy();
}
