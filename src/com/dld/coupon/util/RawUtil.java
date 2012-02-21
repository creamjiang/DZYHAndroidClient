package com.dld.coupon.util;

import android.app.Activity;
import android.content.res.Resources;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RawUtil {
    public static String readText(int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder();
        try {
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(ActivityManager.getCurrent()
                            .getResources().openRawResource(paramInt), "utf-8"));
            while (true) {
                String str = localBufferedReader.readLine();
                if (str == null)
                    return localStringBuilder.toString();
                localStringBuilder.append(str + "\n");
            }
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }
}
