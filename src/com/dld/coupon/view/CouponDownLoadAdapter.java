package com.dld.coupon.view;

import android.view.View;

import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.JsonBean;

import java.util.List;

public abstract class CouponDownLoadAdapter extends JsonBeanDownLoadAdapter {
    protected boolean isStore;
    protected boolean search;

    public CouponDownLoadAdapter(boolean paramBoolean,
            DownloadListView paramDownloadListView, Coupon paramCoupon) {
        this(paramBoolean, paramDownloadListView, paramCoupon, false);
    }

    public CouponDownLoadAdapter(boolean paramBoolean1,
            DownloadListView paramDownloadListView, Coupon paramCoupon,
            boolean paramBoolean2) {
        super(paramDownloadListView, paramCoupon);
        this.isStore = paramBoolean1;
        this.search = paramBoolean2;
    }

    public View getView(int paramInt) {
        return couponDetailToView(
                (CouponDetail) this.bean.getDetails().get(paramInt),
                this.isStore, this.search);
    }
}
