package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Ht_API extends RequestAPI {
    public String ids(OAuth paramOAuth, String paramString1, String paramString2)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("httexts", paramString2));
        return getResource("http://open.t.qq.com/api/ht/ids", localArrayList,
                paramOAuth);
    }

    public String info(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("ids", paramString2));
        return getResource("http://open.t.qq.com/api/ht/info", localArrayList,
                paramOAuth);
    }
}
