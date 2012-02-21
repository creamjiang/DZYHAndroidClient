package com.tencent.weibo.utils;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;

import com.dld.coupon.util.XmlUtil;
import com.tencent.weibo.api.User_API;
import com.tencent.weibo.beans.Account;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;

public class OAuthClient {
    private static final String hashAlgorithmName = "HmacSHA1";

    private static String encodeParams(List<QParameter> paramList) {
        StringBuilder localStringBuilder = new StringBuilder();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            QParameter localQParameter = (QParameter) localIterator.next();
            if (localStringBuilder.length() != 0)
                localStringBuilder.append("&");
            localStringBuilder.append(QStr.encode(localQParameter.getName()));
            localStringBuilder.append("=");
            localStringBuilder.append(QStr.encode(localQParameter.getValue()));
        }
        return localStringBuilder.toString();
    }

    private String generateSignature(String paramString1, String paramString2,
            String paramString3) {
        Object localObject;
        try {
            localObject = Mac.getInstance("HmacSHA1");
            StringBuilder localStringBuilder = new StringBuilder(
                    String.valueOf(QStr.encode(paramString2))).append("&");
            if (paramString3 == null)
                ;
            for (String str = "";; str = str) {
                ((Mac) localObject).init(new SecretKeySpec(str.getBytes(),
                        "HmacSHA1"));
                localObject = new String(
                        Base64Encoder.encode(((Mac) localObject)
                                .doFinal(paramString1.getBytes())));
                break;
                // str = QStr.encode(paramString3);
            }
        } catch (Exception localException) {
            localObject = null;
        }
        return (String) localObject;
    }

    private String generateSignature(URL paramURL, String paramString1,
            String paramString2, String paramString3, List<QParameter> paramList) {
        return generateSignature(
                generateSignatureBase(paramURL, paramString3, paramList),
                paramString1, paramString2);
    }

    private String generateSignatureBase(URL paramURL, String paramString,
            List<QParameter> paramList) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString.toUpperCase());
        localStringBuilder.append("&");
        localStringBuilder.append(QStr.encode(getNormalizedUrl(paramURL)));
        localStringBuilder.append("&");
        localStringBuilder.append(QStr.encode(encodeParams(paramList)));
        return localStringBuilder.toString();
    }

    private static String getNormalizedUrl(URL paramURL) {
        Object localObject = null;
        try {
            localObject = new StringBuilder();
            ((StringBuilder) localObject).append(paramURL.getProtocol());
            ((StringBuilder) localObject).append("://");
            ((StringBuilder) localObject).append(paramURL.getHost());
            if (((paramURL.getProtocol().equals("http")) || (paramURL
                    .getProtocol().equals("https")))
                    && (paramURL.getPort() != -1)) {
                ((StringBuilder) localObject).append(":");
                ((StringBuilder) localObject).append(paramURL.getPort());
            }
            ((StringBuilder) localObject).append(paramURL.getPath());
            localObject = ((StringBuilder) localObject).toString();
            localObject = localObject;
            return (String) localObject;
        } catch (Exception localException) {
            // while (true)

        }
        return (String) localObject;
    }

    public OAuth accessToken(OAuth paramOAuth) throws Exception {
        if (!parseToken(new QHttpClient().httpGet(
                "https://open.t.qq.com/cgi-bin/access_token",
                getOauthParams("https://open.t.qq.com/cgi-bin/access_token",
                        "GET", paramOAuth.getOauth_consumer_secret(),
                        paramOAuth.getOauth_token_secret(),
                        paramOAuth.getAccessParams())), paramOAuth))
            paramOAuth.setStatus(2);
        return paramOAuth;
    }

    public Account getAccount(OAuth paramOAuth) throws Exception {
        Account localAccount = new Account();
        Object localObject1 = new User_API().info(paramOAuth, "xml");
        localObject1 = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(((String) localObject1)
                        .getBytes("UTF-8"))).getDocumentElement();
        if (Integer.parseInt(XmlUtil.getValue((Element) localObject1, "ret")) == 0) {
            Object localObject2 = (Element) ((Element) localObject1)
                    .getElementsByTagName("data").item(0);
            String str4 = XmlUtil.getValue((Element) localObject2, "name");
            String str1 = XmlUtil.getValue((Element) localObject2, "nick");
            String str2 = XmlUtil.getValue((Element) localObject2, "head");
            String str6 = XmlUtil.getValue((Element) localObject2, "isvip");
            String str5 = XmlUtil.getValue((Element) localObject2, "sex");
            String str3 = XmlUtil.getValue((Element) localObject2, "fansnum");
            localObject1 = XmlUtil.getValue((Element) localObject2, "idolnum");
            localObject2 = XmlUtil.getValue((Element) localObject2, "tweetnum");
            localAccount.setName(str4);
            localAccount.setNick(str1);
            localAccount.setHead(str2);
            localAccount.setIsvip(str6);
            localAccount.setSex(str5);
            localAccount.setFansnum(str3);
            localAccount.setIdolnum((String) localObject1);
            localAccount.setTweetnum((String) localObject2);
        }
        return (Account) (Account) localAccount;
    }

    public String getOauthParams(String paramString1, String paramString2,
            String paramString3, String paramString4, List<QParameter> paramList) {
        Collections.sort(paramList);
        String str2 = paramString1;
        String str1 = encodeParams(paramList);
        if ((str1 != null) && (!str1.equals("")))
            str2 = str2 + "?" + str1;
        Object localObject = null;
        try {
            localObject = new URL(str2);
            localObject = localObject;
            localObject = generateSignature((URL) localObject, paramString3,
                    paramString4, paramString2, paramList);
            return new StringBuilder(String.valueOf(str1)).append(
                    "&oauth_signature=").toString()
                    + QStr.encode((String) localObject);
        } catch (MalformedURLException localMalformedURLException) {
            while (true)
                System.err.println("URL parse error:"
                        + localMalformedURLException.getLocalizedMessage());
        }
    }

    public boolean parseToken(String paramString, OAuth paramOAuth)
            throws Exception {
        boolean i = false;
        if ((paramString != null) && (!paramString.equals(""))) {
            paramOAuth.setMsg(paramString);
            String[] arrayOfString = paramString.split("&");
            if (arrayOfString.length >= 2) {
                Object localObject2 = arrayOfString[0];
                Object localObject1 = arrayOfString[1];
                localObject2 = ((String) localObject2).split("=");
                // if (localObject2.length >= 2)
                // {
                // paramOAuth.setOauth_token(localObject2[1]);
                // localObject1 = ((String)localObject1).split("=");
                // if (localObject1.length >= 2)
                // {
                // paramOAuth.setOauth_token_secret(localObject1[1]);
                // if (arrayOfString.length == 3)
                // {
                // arrayOfString = arrayOfString[2].split("=");
                // if ("name".equals(arrayOfString[0]))
                // arrayOfString.length;
                // }
                // i = true;
                // }
                // }
            }
        }
        return i;
    }

    public OAuth requestToken(OAuth paramOAuth) throws Exception {
        Object localObject = new QHttpClient();
        String str = getOauthParams(
                "https://open.t.qq.com/cgi-bin/request_token", "GET",
                paramOAuth.getOauth_consumer_secret(), "",
                paramOAuth.getParams());
        System.out.println("queryString:" + str);
        localObject = ((QHttpClient) localObject).httpGet(
                "https://open.t.qq.com/cgi-bin/request_token", str);
        System.out.println("responseData:" + (String) localObject);
        if (!parseToken((String) localObject, paramOAuth))
            paramOAuth.setStatus(1);
        return (OAuth) paramOAuth;
    }
}
