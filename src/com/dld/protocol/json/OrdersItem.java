package com.dld.protocol.json;

import com.dld.coupon.activity.R;

public class OrdersItem {
    public String amount;
    public String date;
    public String id;
    public String orderNo;
    public String product;
    public String quantity;
    public String seller;
    public String stat;

    public boolean equals(Object paramObject) {
        boolean bool = false;
        try {
            bool = ((OrdersItem) paramObject).id.equals(this.id);
            bool = bool;
            return bool;
        } catch (Exception localException) {
            // while (true)
            bool = false;
            return bool;
        }
    }

    public int getColor() {
        int i;
        if (!this.stat.equals("已退款"))
            i = -9727206;
        else
            i = -950784;
        return i;
    }

    public int getImage() {
        int i;
        if (!this.stat.equals("购买成功")) {
            if (!this.stat.equals("支付成功购买中"))
                i = R.drawable.order_state_1;
            else
                i = R.drawable.order_state_3;
        } else
            i = R.drawable.order_state_2;
        return i;
    }

    public String getState() {
        String str;
        if (!this.stat.equals("支付成功购买中")) {
            if (!this.stat.equals("购买被取消"))
                str = this.stat;
            else
                str = "取消购买";
        } else
            str = "购买中";
        return str;
    }
}
