package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Trends_API extends RequestAPI {
    public String ht(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("type", paramString2));
        localArrayList.add(new QParameter("reqnum", paramString3));
        localArrayList.add(new QParameter("pos", paramString4));
        return getResource("http://open.t.qq.com/api/trends/ht",
                localArrayList, paramOAuth);
    }

    public String t(OAuth paramOAuth, String paramString1, String paramString2,
            String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("pos", paramString3));
        return getResource("http://open.t.qq.com/api/trends/t", localArrayList,
                paramOAuth);
    }
}
