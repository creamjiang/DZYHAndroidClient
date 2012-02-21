package com.dld.android.net;

public class HttpConfig implements NetConfig {
    private boolean gzip = true;
    private int retry = 3;
    private int timeout;

    public HttpConfig(int paramInt) {
        this.timeout = (paramInt * 1000);
    }

    public HttpConfig(int paramInt1, int paramInt2) {
        this.timeout = (paramInt1 * 1000);
        this.retry = paramInt2;
    }

    public HttpConfig(int paramInt1, int paramInt2, boolean paramBoolean) {
        this.timeout = (paramInt1 * 1000);
        this.retry = paramInt2;
        this.gzip = paramBoolean;
    }

    public HttpConfig(int paramInt, boolean paramBoolean) {
        this.timeout = (paramInt * 1000);
        this.gzip = paramBoolean;
    }

    public String getContentType() {
        return "";
    }

    public String getRequestMethod() {
        return "POST";
    }

    public int getRetry() {
        return this.retry;
    }

    public int getTimeOut() {
        return this.timeout;
    }

    public boolean isGzip() {
        return this.gzip;
    }
}
