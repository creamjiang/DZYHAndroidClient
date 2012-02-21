package com.dld.coupon.view;

import android.view.View;

import com.dld.protocol.json.BankCoupon;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.JsonBean;

import java.util.List;

public abstract class BankCouponDownLoadAdapter extends JsonBeanDownLoadAdapter {
    public BankCouponDownLoadAdapter(DownloadListView paramDownloadListView,
            BankCoupon paramBankCoupon) {
        super(paramDownloadListView, paramBankCoupon);
    }

    public View getView(int paramInt) {
        return bankDetailToView((BankCouponDetail) this.bean.getDetails().get(
                paramInt));
    }
}
