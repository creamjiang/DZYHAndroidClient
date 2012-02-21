package com.dld.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class Device {
    private static Device instance;
    private DisplayMetrics dm;
    private TelephonyManager telephonyManager;

    public static Device getInstance() {
        if (instance == null)
            instance = new Device();
        return instance;
    }

    public DisplayMetrics getDisplayMetrics(Context paramContext) {
        if (this.dm == null)
            this.dm = paramContext.getApplicationContext().getResources()
                    .getDisplayMetrics();
        return this.dm;
    }

    public String getNetType(Context paramContext) {
        String localObject;
        try {
            NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                    .getSystemService("connectivity")).getActiveNetworkInfo();
            if (localNetworkInfo == null) {
                localObject = null;
            } else {
                localObject = null;
                String str = localNetworkInfo.getTypeName();
                if (("wifi".equalsIgnoreCase(str))
                        && (localNetworkInfo.isAvailable())) {
                    localObject = str;
                } else if (("mobile".equalsIgnoreCase(str))
                        && (localNetworkInfo.isAvailable())) {
                    str = localNetworkInfo.getExtraInfo();
                    localObject = str;
                }
            }
        } catch (Exception localException) {
            localObject = null;
        }
        return localObject;
    }

    public TelephonyManager getTelephonyManager(Context paramContext) {
        if (this.telephonyManager == null)
            this.telephonyManager = ((TelephonyManager) paramContext
                    .getSystemService("phone"));
        return this.telephonyManager;
    }
}

/*
 * Location: G:\DEV\MobileDev\反编译\classes_dex2jar\ Qualified Name:
 * com.dld.android.util.Device JD-Core Version: 0.6.0
 */