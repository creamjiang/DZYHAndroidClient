package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Private_API extends RequestAPI {
    public String add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        return add(paramOAuth, paramString1, paramString2, paramString3, "",
                "", paramString4);
    }

    public String add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        localArrayList.add(new QParameter("name", paramString6));
        return postContent("http://open.t.qq.com/api/private/add",
                localArrayList, paramOAuth);
    }

    public String del(OAuth paramOAuth, String paramString1, String paramString2)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return postContent("http://open.t.qq.com/api/private/del",
                localArrayList, paramOAuth);
    }

    public String recv(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        return getResource("http://open.t.qq.com/api/private/recv",
                localArrayList, paramOAuth);
    }

    public String send(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("pageflag", paramString2));
        localArrayList.add(new QParameter("pagetime", paramString3));
        localArrayList.add(new QParameter("reqnum", paramString4));
        return getResource("http://open.t.qq.com/api/private/send",
                localArrayList, paramOAuth);
    }
}
