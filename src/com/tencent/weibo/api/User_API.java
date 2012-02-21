package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import java.util.ArrayList;
import java.util.List;

public class User_API extends RequestAPI {
    public String info(OAuth paramOAuth, String paramString) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString));
        return getResource("http://open.t.qq.com/api/user/info",
                localArrayList, paramOAuth);
    }

    public String infos(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("names", paramString2));
        return getResource("http://open.t.qq.com/api/user/infos",
                localArrayList, paramOAuth);
    }

    public String other_info(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("name", paramString2));
        return getResource("http://open.t.qq.com/api/user/other_info",
                localArrayList, paramOAuth);
    }

    public String update(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            String paramString8, String paramString9, String paramString10)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("nick", paramString2));
        localArrayList.add(new QParameter("sex", paramString3));
        localArrayList.add(new QParameter("year", paramString4));
        localArrayList.add(new QParameter("month", paramString5));
        localArrayList.add(new QParameter("day", paramString6));
        localArrayList.add(new QParameter("countrycode", paramString7));
        localArrayList.add(new QParameter("provincecode", paramString8));
        localArrayList.add(new QParameter("citycode", paramString9));
        localArrayList.add(new QParameter("introduction", paramString10));
        return postContent("http://open.t.qq.com/api/user/update",
                localArrayList, paramOAuth);
    }

    public String update_edu(OAuth paramOAuth, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6) throws Exception {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(new QParameter("format", paramString1));
        localArrayList.add(new QParameter("feildid", paramString2));
        localArrayList.add(new QParameter("year", paramString3));
        localArrayList.add(new QParameter("schoolid", paramString4));
        localArrayList.add(new QParameter("departmentid", paramString5));
        localArrayList.add(new QParameter("level", paramString6));
        return postContent("http://open.t.qq.com/api/user/update_edu",
                localArrayList, paramOAuth);
    }

    public String update_head(OAuth paramOAuth, String paramString1,
            String paramString2) throws Exception {
        ArrayList localArrayList1 = new ArrayList();
        localArrayList1.add(new QParameter("format", paramString1));
        ArrayList localArrayList2 = new ArrayList();
        localArrayList2.add(new QParameter("pic", paramString2));
        return postFile("http://open.t.qq.com/api/user/update_head",
                localArrayList1, localArrayList2, paramOAuth);
    }
}
