package com.dld.coupon.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneUtil {
    public static String getIMEI(Context paramContext) {
        return ((TelephonyManager) paramContext.getSystemService("phone"))
                .getDeviceId();
    }

    public static String getIMSI(Context paramContext) {
        return ((TelephonyManager) paramContext.getSystemService("phone"))
                .getSubscriberId();
    }
}
