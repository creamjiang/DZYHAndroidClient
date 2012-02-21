package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Info_API extends RequestAPI {
    public String update(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("op", paramString2));
        localArrayList.add(new QParameter("type", paramString3));
        return getResource("http://open.t.qq.com/api/info/update",
                localArrayList, paramOAuth);
    }
}
