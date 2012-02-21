package com.tencent.weibo.beans;

public class Account {
    private String fansnum = "";
    private String head = "";
    private String idolnum = "";
    private String isvip = "";
    private String name = "";
    private String nick = "";
    private String sex = "";
    private String tweetnum = "";

    public String getFansnum() {
        return this.fansnum;
    }

    public String getHead() {
        return this.head;
    }

    public String getIdolnum() {
        return this.idolnum;
    }

    public String getIsvip() {
        return this.isvip;
    }

    public String getName() {
        return this.name;
    }

    public String getNick() {
        return this.nick;
    }

    public String getSex() {
        return this.sex;
    }

    public String getTweetnum() {
        return this.tweetnum;
    }

    public void setFansnum(String paramString) {
        this.fansnum = paramString;
    }

    public void setHead(String paramString) {
        this.head = paramString;
    }

    public void setIdolnum(String paramString) {
        this.idolnum = paramString;
    }

    public void setIsvip(String paramString) {
        this.isvip = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setNick(String paramString) {
        this.nick = paramString;
    }

    public void setSex(String paramString) {
        this.sex = paramString;
    }

    public void setTweetnum(String paramString) {
        this.tweetnum = paramString;
    }

    public String toString() {
        return "{name:\"" + this.name + "\", nick:\"" + this.nick
                + "\", head:\"" + this.head + "\", isvip:\"" + this.isvip
                + "\", sex:\"" + this.sex + "\", fansnum:\"" + this.fansnum
                + "\", idolnum:\"" + this.idolnum + "\", tweetnum:\""
                + this.tweetnum + "\"}";
    }
}
