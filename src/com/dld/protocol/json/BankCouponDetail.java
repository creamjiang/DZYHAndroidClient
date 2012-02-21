package com.dld.protocol.json;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.R;

import java.util.HashMap;
import java.util.Map;

public class BankCouponDetail implements Detail {
    private static Map<String, Integer> bankImages;
    private static Map<String, String> bankNames = new HashMap();
    public String address;
    public String bank;
    public int dbId;
    public String discount;
    public int distance;
    public long end;
    public int id;
    public String introduction;
    public String landmark;
    public double lat;
    public double lng;
    public String name = "";
    public String tel;
    public String trade_name;

    static {
        bankNames.put("BOC", "中国银行");
        bankNames.put("ICBC", "工商银行");
        bankNames.put("CCB", "建设银行");
        bankNames.put("BOCOM", "交通银行");
        bankNames.put("CMB", "招商银行");
        bankNames.put("CEB", "光大银行");
        bankNames.put("CIB", "兴业银行");
        bankNames.put("CITIC", "中信银行");
        bankNames.put("CMBC", "民生银行");
        bankNames.put("BOB", "北京银行");
        bankNames.put("HXB", "华夏银行");
        bankNames.put("SDB", "深圳发展银行");
        bankNames.put("SPDB", "上海浦东发展银行");
        bankNames.put("GDB", "广东发展银行");
        bankImages = new HashMap();
        bankImages.put("BOC", Integer.valueOf(R.drawable.bank_boc));
        bankImages.put("ICBC", Integer.valueOf(R.drawable.bank_icbc));
        bankImages.put("CCB", Integer.valueOf(R.drawable.bank_ccb));
        bankImages.put("BOCOM", Integer.valueOf(R.drawable.bank_bocom));
        bankImages.put("CMB", Integer.valueOf(R.drawable.bank_cmb));
        bankImages.put("CEB", Integer.valueOf(R.drawable.bank_ceb));
        bankImages.put("CIB", Integer.valueOf(R.drawable.bank_cib));
        bankImages.put("CITIC", Integer.valueOf(R.drawable.bank_citic));
        bankImages.put("CMBC", Integer.valueOf(R.drawable.bank_cmbc));
        bankImages.put("BOB", Integer.valueOf(R.drawable.bank_bob));
        bankImages.put("HXB", Integer.valueOf(R.drawable.bank_hxb));
        bankImages.put("SDB", Integer.valueOf(R.drawable.bank_sdb));
        bankImages.put("SPDB", Integer.valueOf(R.drawable.bank_spdb));
        bankImages.put("GDB", Integer.valueOf(R.drawable.bank_gdb));
    }

    public boolean equals(Object paramObject) {
        boolean i = false;
        try {
            int k = ((BankCouponDetail) paramObject).id;
            int j = this.id;
            if (k == j)
                i = true;
            return i;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    public String getBank() {
        return (String) bankNames.get(this.bank);
    }

    public int getDBId() {
        return this.dbId;
    }

    public String getId() {
        return String.valueOf(this.id);
    }

    public int getImage() {
        Integer localInteger = (Integer) bankImages.get(this.bank);
        int i;
        if (localInteger != null)
            i = localInteger.intValue();
        else
            i = R.drawable.store;
        return i;
    }

    public void setDBId(int paramInt) {
        this.dbId = paramInt;
    }
}
