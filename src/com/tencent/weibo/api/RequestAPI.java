package com.tencent.weibo.api;

import com.dld.coupon.util.XmlUtil;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import com.tencent.weibo.utils.OAuthClient;
import com.tencent.weibo.utils.QHttpClient;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class RequestAPI {
    QHttpClient http = new QHttpClient();

    public String getResource(String paramString, List<QParameter> paramList,
            OAuth paramOAuth) throws Exception {
        paramList.addAll(paramOAuth.getTokenParams());
        String str = new OAuthClient().getOauthParams(paramString, "GET",
                paramOAuth.getOauth_consumer_secret(),
                paramOAuth.getOauth_token_secret(), paramList);
        return this.http.httpGet(paramString, str);
    }

    public String[] message(String paramString) {
        String[] arrayOfString = new String[2];
        try {
            arrayOfString[0] = XmlUtil.getValue(
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(new ByteArrayInputStream(paramString
                                    .getBytes("UTF-8"))).getDocumentElement(),
                    "ret");
            switch (Integer.parseInt(arrayOfString[0])) {
            case 0:
                arrayOfString[1] = "操作成功";
                break;
            case 1:
                arrayOfString[1] = "参数错误";
                break;
            case 2:
                arrayOfString[1] = "参数错误";
                break;
            case 3:
                arrayOfString[1] = "参数错误";
                break;
            case 4:
                arrayOfString[1] = "参数错误";
                break;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            arrayOfString[0] = "4";
            arrayOfString[1] = localException.toString();
        }

        // arrayOfString[1] = "频率受限";
        // break label137;
        // arrayOfString[1] = "鉴权失败";
        // break label137;
        // arrayOfString[1] = "服务器内部错误";
        label137: return arrayOfString;
    }

    public String postContent(String paramString, List<QParameter> paramList,
            OAuth paramOAuth) throws Exception {
        paramList.addAll(paramOAuth.getTokenParams());
        String str = new OAuthClient().getOauthParams(paramString, "POST",
                paramOAuth.getOauth_consumer_secret(),
                paramOAuth.getOauth_token_secret(), paramList);
        return this.http.httpPost(paramString, str);
    }

    public String postFile(String paramString, List<QParameter> paramList1,
            List<QParameter> paramList2, OAuth paramOAuth) throws Exception {
        paramList1.addAll(paramOAuth.getTokenParams());
        String str = new OAuthClient().getOauthParams(paramString, "POST",
                paramOAuth.getOauth_consumer_secret(),
                paramOAuth.getOauth_token_secret(), paramList1);
        return this.http.httpPostWithFile(paramString, str, paramList2);
    }
}
