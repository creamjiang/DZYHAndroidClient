package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class T_API extends RequestAPI {
    public String add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3) throws Exception {
        return add(paramOAuth, paramString1, paramString2, paramString3, "", "");
    }

    public String add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        return postContent("http://open.t.qq.com/api/t/add", localArrayList,
                paramOAuth);
    }

    public String add_music(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        localArrayList.add(new QParameter("url", paramString6));
        localArrayList.add(new QParameter("title", paramString7));
        localArrayList.add(new QParameter("author", paramString8));
        return postContent("http://open.t.qq.com/api/t/add_music",
                localArrayList, paramOAuth);
    }

    public String add_pic(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        return add_pic(paramOAuth, paramString1, paramString2, paramString3,
                "", "", paramString4);
    }

    public String add_pic(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList2 = new ArrayList();
        localArrayList2.add(new QParameter("format", paramString1));
        localArrayList2.add(new QParameter("content", paramString2));
        localArrayList2.add(new QParameter("clientip", paramString3));
        localArrayList2.add(new QParameter("jing", paramString4));
        localArrayList2.add(new QParameter("wei", paramString5));
        ArrayList localArrayList1 = new ArrayList();
        localArrayList1.add(new QParameter("pic", paramString6));
        return postFile("http://open.t.qq.com/api/t/add_pic", localArrayList2,
                localArrayList1, paramOAuth);
    }

    public String add_video(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        localArrayList.add(new QParameter("url", paramString6));
        return postContent("http://open.t.qq.com/api/t/add_video",
                localArrayList, paramOAuth);
    }

    public String comment(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        return comment(paramOAuth, paramString1, paramString2, paramString3,
                "", "", paramString4);
    }

    public String comment(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        localArrayList.add(new QParameter("reid", paramString6));
        return postContent("http://open.t.qq.com/api/t/comment",
                localArrayList, paramOAuth);
    }

    public String del(OAuth paramOAuth, String paramString1, String paramString2)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return postContent("http://open.t.qq.com/api/t/del", localArrayList,
                paramOAuth);
    }

    public String getvideoinfo(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("url", paramString2));
        return postContent("http://open.t.qq.com/api/t/getvideoinfo",
                localArrayList, paramOAuth);
    }

    public String list(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("ids", paramString2));
        return getResource("http://open.t.qq.com/api/t/list", localArrayList,
                paramOAuth);
    }

    public String re_add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        return re_add(paramOAuth, paramString1, paramString2, paramString3, "",
                "", paramString4);
    }

    public String re_add(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        localArrayList.add(new QParameter("reid", paramString6));
        return postContent("http://open.t.qq.com/api/t/re_add", localArrayList,
                paramOAuth);
    }

    public String re_count(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("ids", paramString2));
        return getResource("http://open.t.qq.com/api/t/re_count",
                localArrayList, paramOAuth);
    }

    public String re_list(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("rootid", paramString2));
        localArrayList.add(new QParameter("pageflag", paramString3));
        localArrayList.add(new QParameter("pagetime", paramString4));
        return getResource("http://open.t.qq.com/api/t/re_list",
                localArrayList, paramOAuth);
    }

    public String reply(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws Exception {
        return reply(paramOAuth, paramString1, paramString2, paramString3, "",
                "", paramString4);
    }

    public String reply(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("content", paramString2));
        localArrayList.add(new QParameter("clientip", paramString3));
        localArrayList.add(new QParameter("jing", paramString4));
        localArrayList.add(new QParameter("wei", paramString5));
        localArrayList.add(new QParameter("reid", paramString6));
        return postContent("http://open.t.qq.com/api/t/reply", localArrayList,
                paramOAuth);
    }

    public String show(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("id", paramString2));
        return getResource("http://open.t.qq.com/api/t/show", localArrayList,
                paramOAuth);
    }
}
