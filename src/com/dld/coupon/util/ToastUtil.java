package com.dld.coupon.util;

import android.widget.Toast;

import com.dld.coupon.activity.ActivityManager;

public class ToastUtil {
    public static void showMsg(String paramString) {
        Toast.makeText(ActivityManager.getCurrent(), paramString, 0).show();
    }
}
