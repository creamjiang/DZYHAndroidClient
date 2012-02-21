package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Search_API extends RequestAPI {
    public String t(OAuth paramOAuth, String paramString1, String paramString2,
            String paramString3, String paramString4) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("keyword", paramString2));
        localArrayList.add(new QParameter("pagesize", paramString3));
        localArrayList.add(new QParameter("page", paramString4));
        return getResource("http://open.t.qq.com/api/search/t", localArrayList,
                paramOAuth);
    }

    public String user(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("keyword", paramString2));
        localArrayList.add(new QParameter("pagesize", paramString3));
        localArrayList.add(new QParameter("page", paramString4));
        return getResource("http://open.t.qq.com/api/search/user",
                localArrayList, paramOAuth);
    }

    public String userbytag(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("keyword", paramString2));
        localArrayList.add(new QParameter("pagesize", paramString3));
        localArrayList.add(new QParameter("page", paramString4));
        return getResource("http://open.t.qq.com/api/search/userbytag",
                localArrayList, paramOAuth);
    }
}
