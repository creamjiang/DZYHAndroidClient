package com.dld.android.net;

import android.content.Context;

import com.dld.coupon.activity.ActivityManager;

public abstract class Callback {
    public boolean custom = false;

    void exception(Exception paramException) {
        onException(paramException);
    }

    public Context getContext() {
        return ActivityManager.getCurrent();
    }

    public Param getHeader() {
        return null;
    }

    public byte[] getPostParam() {
        return null;
    }

    public abstract String getUrl();

    public abstract void onException(Exception paramException);

    public abstract void onRecieve(byte[] paramArrayOfByte);

    void recieve(byte[] paramArrayOfByte) {
        onRecieve(paramArrayOfByte);
    }
}
