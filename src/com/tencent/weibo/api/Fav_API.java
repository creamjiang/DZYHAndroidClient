package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Fav_API extends RequestAPI {
    public String addht(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return postContent("http://open.t.qq.com/api/fav/addht",
                localArrayList, paramOAuth);
    }

    public String addt(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return postContent("http://open.t.qq.com/api/fav/addt", localArrayList,
                paramOAuth);
    }

    public String delht(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return postContent("http://open.t.qq.com/api/fav/delht",
                localArrayList, paramOAuth);
    }

    public String delt(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return postContent("http://open.t.qq.com/api/fav/delt", localArrayList,
                paramOAuth);
    }

    public String list_ht(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("pageflag", paramString3));
        localArrayList.add(new QParameter("pagetime", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        return getResource("http://open.t.qq.com/api/fav/list_ht",
                localArrayList, paramOAuth);
    }

    public String list_t(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        localArrayList.add(new QParameter("lastid", paramString5));
        return getResource("http://open.t.qq.com/api/fav/list_t",
                localArrayList, paramOAuth);
    }
}
