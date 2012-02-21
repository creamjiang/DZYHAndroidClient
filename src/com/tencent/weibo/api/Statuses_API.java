package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Statuses_API extends RequestAPI {
    public String area_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pos", paramString2));
        localArrayList.add(new QParameter("reqnum", paramString3));
        localArrayList.add(new QParameter("country", paramString4));
        localArrayList.add(new QParameter("province", paramString5));
        localArrayList.add(new QParameter("city", paramString6));
        return getResource("http://open.t.qq.com/api/statuses/area_timeline",
                localArrayList, paramOAuth);
    }

    public String broadcast_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        localArrayList.add(new QParameter("type", paramString6));
        localArrayList.add(new QParameter("contenttype", paramString7));
        localArrayList.add(new QParameter("accesslevel", paramString8));
        return getResource(
                "http://open.t.qq.com/api/statuses/broadcast_timeline",
                localArrayList, paramOAuth);
    }

    public String broadcast_timeline_ids(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        localArrayList.add(new QParameter("type", paramString6));
        localArrayList.add(new QParameter("contenttype", paramString7));
        localArrayList.add(new QParameter("accesslevel", paramString8));
        return getResource(
                "http://open.t.qq.com/api/statuses/broadcast_timeline_ids",
                localArrayList, paramOAuth);
    }

    public String home_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        return getResource("http://open.t.qq.com/api/statuses/home_timeline",
                localArrayList, paramOAuth);
    }

    public String home_timeline_ids(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("type", paramString5));
        localArrayList.add(new QParameter("contenttype", paramString6));
        localArrayList.add(new QParameter("accesslevel", paramString7));
        return getResource(
                "http://open.t.qq.com/api/statuses/home_timeline_ids",
                localArrayList, paramOAuth);
    }

    public String ht_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("httext", paramString2));
        localArrayList.add(new QParameter("pageflag", paramString3));
        localArrayList.add(new QParameter("pageinfo", paramString4));
        localArrayList.add(new QParameter("reqnum", paramString5));
        return getResource("http://open.t.qq.com/api/statuses/ht_timeline",
                localArrayList, paramOAuth);
    }

    public String mentions_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        return getResource(
                "http://open.t.qq.com/api/statuses/mentions_timeline",
                localArrayList, paramOAuth);
    }

    public String mentions_timeline_ids(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pagetime", paramString2));
        localArrayList.add(new QParameter("reqnum", paramString3));
        localArrayList.add(new QParameter("lastid", paramString4));
        localArrayList.add(new QParameter("type", paramString5));
        localArrayList.add(new QParameter("contenttype", paramString6));
        localArrayList.add(new QParameter("accesslevel", paramString7));
        return getResource(
                "http://open.t.qq.com/api/statuses/mentions_timeline_ids",
                localArrayList, paramOAuth);
    }

    public String public_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pos", paramString2));
        localArrayList.add(new QParameter("reqnum", paramString3));
        return getResource("http://open.t.qq.com/api/statuses/public_timeline",
                localArrayList, paramOAuth);
    }

    public String special_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        return getResource(
                "http://open.t.qq.com/api/statuses/special_timeline",
                localArrayList, paramOAuth);
    }

    public String user_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("name", paramString5));
        return getResource("http://open.t.qq.com/api/statuses/user_timeline",
                localArrayList, paramOAuth);
    }

    public String user_timeline_ids(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8, String paramString9) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        localArrayList.add(new QParameter("name", paramString6));
        localArrayList.add(new QParameter("type", paramString7));
        localArrayList.add(new QParameter("contenttype", paramString8));
        localArrayList.add(new QParameter("accesslevel", paramString9));
        return getResource(
                "http://open.t.qq.com/api/statuses/user_timeline_ids",
                localArrayList, paramOAuth);
    }

    public String users_timeline(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8, String paramString9) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        localArrayList.add(new QParameter("names", paramString6));
        localArrayList.add(new QParameter("type", paramString7));
        localArrayList.add(new QParameter("contenttype", paramString8));
        localArrayList.add(new QParameter("accesslevel", paramString9));
        return getResource("http://open.t.qq.com/api/statuses/users_timeline",
                localArrayList, paramOAuth);
    }

    public String users_timeline_ids(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8, String paramString9) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        localArrayList.add(new QParameter("names", paramString6));
        localArrayList.add(new QParameter("type", paramString7));
        localArrayList.add(new QParameter("contenttype", paramString8));
        localArrayList.add(new QParameter("accesslevel", paramString9));
        return getResource(
                "http://open.t.qq.com/api/statuses/users_timeline_ids",
                localArrayList, paramOAuth);
    }
}
