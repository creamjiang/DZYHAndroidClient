package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Tag_API extends RequestAPI {
    public String add(OAuth paramOAuth, String paramString1, String paramString2)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("tag", paramString2));
        return postContent("http://open.t.qq.com/api/tag/add", localArrayList,
                paramOAuth);
    }

    public String del(OAuth paramOAuth, String paramString1, String paramString2)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("tagid", paramString2));
        return postContent("http://open.t.qq.com/api/tag/del", localArrayList,
                paramOAuth);
    }
}
