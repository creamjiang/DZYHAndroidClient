package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Other_API extends RequestAPI {
    public String kownperson(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("ip", paramString2));
        localArrayList.add(new QParameter("country_code", paramString3));
        localArrayList.add(new QParameter("province_code", paramString4));
        localArrayList.add(new QParameter("city_code", paramString5));
        return getResource("http://open.t.qq.com/api/other/kownperson",
                localArrayList, paramOAuth);
    }

    public String shorturl(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("url", paramString2));
        return getResource("http://open.t.qq.com/api/other/shorturl",
                localArrayList, paramOAuth);
    }

    public String videokey(OAuth paramOAuth, String paramString)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString));
        return getResource("http://open.t.qq.com/api/other/videokey",
                localArrayList, paramOAuth);
    }
}
