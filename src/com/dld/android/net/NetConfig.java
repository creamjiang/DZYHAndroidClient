package com.dld.android.net;

public abstract interface NetConfig {
    public abstract String getContentType();

    public abstract String getRequestMethod();

    public abstract int getTimeOut();
}
