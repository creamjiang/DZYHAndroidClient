package com.dld.protocol.json;

import com.dld.coupon.util.StringUtils;

public class User {
    public String birthday;
    public String cards;
    public String cityname;
    public String customername;
    public String email;
    public int gender;
    public String id;
    public String idcard;
    public String intrests;
    public String lastLoginTime;
    public String level;
    public Integer mark;
    public String mobile;
    public String msn;
    public String password;
    public String profile;
    public String qqcode;
    public String realname;
    public String registerDate;
    public String username;
    public String weibo;

    public User() {
    }

    public User(String paramString1, String paramString2, String paramString3,
            String paramString4, String paramString5, int paramInt,
            String paramString6, String paramString7, String paramString8,
            String paramString9, String paramString10, String paramString11,
            String paramString12) {
        this.id = paramString1;
        this.email = paramString2;
        this.mobile = paramString3;
        this.username = paramString4;
        this.realname = paramString5;
        this.gender = paramInt;
        this.qqcode = paramString6;
        this.msn = paramString7;
        this.weibo = paramString8;
        this.idcard = paramString9;
        this.cityname = paramString10;
        this.lastLoginTime = paramString11;
        this.registerDate = paramString12;
    }

    public int getDetailPercent() {
        int i = 0;
        if (!StringUtils.isEmpty(this.customername))
            i = 0 + 2;
        if (!StringUtils.isEmpty(this.profile))
            i += 2;
        if (!StringUtils.isEmpty(this.mobile))
            i += 2;
        if (!StringUtils.isEmpty(this.cityname))
            i++;
        if (!StringUtils.isEmpty(this.birthday))
            i++;
        if (!StringUtils.isEmpty(this.intrests))
            i++;
        if (!StringUtils.isEmpty(this.cards))
            i++;
        return i;
    }

    public String toString() {
        return "User [\nid=" + this.id + "\n" + "email=" + this.email + "\n"
                + "mobile=" + this.mobile + "\n" + "username=" + this.username
                + "\n" + "customername=" + this.customername + "\n"
                + "password=" + this.password + "\n" + "realname="
                + this.realname + "\n" + "gender=" + this.gender + "\n"
                + "qqcode=" + this.qqcode + "\n" + "msn=" + this.msn + "\n"
                + "weibo=" + this.weibo + "\n" + "idcard=" + this.idcard + "\n"
                + "mark=" + this.mark + "\n" + "level=" + this.level + "\n"
                + "cityname=" + this.cityname + "\n" + "lastLoginTime="
                + this.lastLoginTime + "\n" + "registerDate="
                + this.registerDate + "\n" + "birthday=" + this.birthday + "\n"
                + "intrests=" + this.intrests + "\n" + "cards=" + this.cards
                + "\n" + "profile=" + this.profile + "]";
    }
}
