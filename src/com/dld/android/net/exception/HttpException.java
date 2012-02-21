package com.dld.android.net.exception;

public class HttpException extends Exception {
    private static final long serialVersionUID = -4130367639103179707L;
    private int mStatusCode;

    public HttpException(int paramInt) {
        super(String.valueOf(paramInt));
        this.mStatusCode = paramInt;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }
}
