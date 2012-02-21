package com.tencent.weibo.beans;

public class Data {
    private String id = "";
    private int timeStamp = 0;

    public String getId() {
        return this.id;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }

    public void setId(String paramString) {
        this.id = paramString;
    }

    public void setTimeStamp(int paramInt) {
        this.timeStamp = paramInt;
    }
}
