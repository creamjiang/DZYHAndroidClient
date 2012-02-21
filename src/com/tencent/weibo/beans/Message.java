package com.tencent.weibo.beans;

public class Message {
    private int Errcode = 0;
    private Data data = new Data();
    private String msg = "";
    private int ret = 0;

    public Data getData() {
        return this.data;
    }

    public int getErrcode() {
        return this.Errcode;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getRet() {
        return this.ret;
    }

    public void setData(Data paramData) {
        this.data = paramData;
    }

    public void setErrcode(int paramInt) {
        this.Errcode = paramInt;
    }

    public void setMsg(String paramString) {
        this.msg = paramString;
    }

    public void setRet(int paramInt) {
        this.ret = paramInt;
    }
}
