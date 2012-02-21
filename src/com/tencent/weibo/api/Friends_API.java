package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class Friends_API extends RequestAPI {
    public String add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        return postContent("http://open.t.qq.com/api/friends/add",
                localArrayList, paramOAuth);
    }

    public String addblacklist(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        return postContent("http://open.t.qq.com/api/friends/addblacklist",
                localArrayList, paramOAuth);
    }

    public String addspecial(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        return postContent("http://open.t.qq.com/api/friends/addspecial",
                localArrayList, paramOAuth);
    }

    public String blacklist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        return getResource("http://open.t.qq.com/api/friends/blacklist",
                localArrayList, paramOAuth);
    }

    public String check(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("names", paramString2));
        localArrayList.add(new QParameter("flag", paramString3));
        return getResource("http://open.t.qq.com/api/friends/check",
                localArrayList, paramOAuth);
    }

    public String del(OAuth paramOAuth, String paramString1, String paramString2)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        return postContent("http://open.t.qq.com/api/friends/del",
                localArrayList, paramOAuth);
    }

    public String delblacklist(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        return postContent("http://open.t.qq.com/api/friends/delblacklist",
                localArrayList, paramOAuth);
    }

    public String delspecial(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        return postContent("http://open.t.qq.com/api/friends/delspecial",
                localArrayList, paramOAuth);
    }

    public String fanlist_s(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        return getResource("http://open.t.qq.com/api/friends/fanslist_s",
                localArrayList, paramOAuth);
    }

    public String fanslist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        return getResource("http://open.t.qq.com/api/friends/fanslist",
                localArrayList, paramOAuth);
    }

    public String idollist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        return getResource("http://open.t.qq.com/api/friends/idollist",
                localArrayList, paramOAuth);
    }

    public String idollist_s(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        return getResource("http://open.t.qq.com/api/friends/idollist_s",
                localArrayList, paramOAuth);
    }

    public String speciallist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        return getResource("http://open.t.qq.com/api/friends/speciallist",
                localArrayList, paramOAuth);
    }

    public String user_fanslist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        localArrayList.add(new QParameter("name", paramString4));
        return getResource("http://open.t.qq.com/api/friends/user_fanslist",
                localArrayList, paramOAuth);
    }

    public String user_idollist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        localArrayList.add(new QParameter("name", paramString4));
        return getResource("http://open.t.qq.com/api/friends/user_idollist",
                localArrayList, paramOAuth);
    }

    public String user_speciallist(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("reqnum", paramString2));
        localArrayList.add(new QParameter("startindex", paramString3));
        localArrayList.add(new QParameter("name", paramString4));
        return getResource("http://open.t.qq.com/api/friends/user_speciallist",
                localArrayList, paramOAuth);
    }
}
