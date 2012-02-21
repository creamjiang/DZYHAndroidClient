package com.dld.protocol.json;

public class AllResultDetail extends CouponDetail {
    public boolean hasBank;
    public boolean hasCoupon;
    public boolean hasGroup;
    public boolean onlyShop;

    public boolean isOnlyShop() {
        return this.onlyShop;
    }
}
