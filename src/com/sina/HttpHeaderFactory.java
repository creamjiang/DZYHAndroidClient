package com.sina;

import android.os.Bundle;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public abstract class HttpHeaderFactory {
    public static final String CONST_HMAC_SHA1 = "HmacSHA1";
    public static final String CONST_OAUTH_VERSION = "1.0";
    public static final String CONST_SIGNATURE_METHOD = "HMAC-SHA1";

    public static String constructRequestURL(String paramString) {
        int i = paramString.indexOf("?");
        if (-1 != i)
            paramString = paramString.substring(0, i);
        int k = paramString.indexOf("/", 8);
        String str = paramString.substring(0, k).toLowerCase();
        int j = str.indexOf(":", 8);
        if (-1 != j)
            if ((!str.startsWith("http://")) || (!str.endsWith(":80"))) {
                if ((str.startsWith("https://")) && (str.endsWith(":443")))
                    str = str.substring(0, j);
            } else
                str = str.substring(0, j);
        return str + paramString.substring(k);
    }

    public static String encode(String paramString) {
        Object localObject = null;
        // try
        // {
        // String str = URLEncoder.encode(paramString, "UTF-8");
        // localObject = str;
        // label11: StringBuffer localStringBuffer = new
        // StringBuffer(localObject.length());
        // int i = 0;
        // if (i >= localObject.length())
        // return localStringBuffer.toString();
        // char c = localObject.charAt(i);
        // if (c == '*')
        // localStringBuffer.append("%2A");
        // while (true)
        // {
        // i++;
        // break;
        // if (c == '+')
        // {
        // localStringBuffer.append("%20");
        // continue;
        // }
        // if ((c == '%') && (i + 1 < localObject.length()) &&
        // (localObject.charAt(i + 1) == '7') && (localObject.charAt(i + 2) ==
        // 'E'))
        // {
        // localStringBuffer.append('~');
        // i += 2;
        // continue;
        // }
        // localStringBuffer.append(c);
        // }
        // }
        // catch (UnsupportedEncodingException
        // localUnsupportedEncodingException)
        // {
        // break label11;
        // }
        return null;
    }

    public static String encodeParameters(Bundle paramBundle,
            String paramString, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        Iterator localIterator = paramBundle.keySet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            if (localStringBuffer.length() != 0) {
                if (paramBoolean)
                    localStringBuffer.append("\"");
                localStringBuffer.append(paramString);
            }
            localStringBuffer.append(encode(str)).append("=");
            if (paramBoolean)
                localStringBuffer.append("\"");
            localStringBuffer.append(encode(paramBundle.getString(str)));
        }
        if ((localStringBuffer.length() != 0) && (paramBoolean))
            localStringBuffer.append("\"");
        return localStringBuffer.toString();
    }

    public static String encodeParameters(WeiboParameters paramWeiboParameters,
            String paramString, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        for (int i = 0; i < paramWeiboParameters.size(); i++) {
            if (localStringBuffer.length() != 0) {
                if (paramBoolean)
                    localStringBuffer.append("\"");
                localStringBuffer.append(paramString);
            }
            localStringBuffer.append(encode(paramWeiboParameters.getKey(i)))
                    .append("=");
            if (paramBoolean)
                localStringBuffer.append("\"");
            localStringBuffer.append(encode(paramWeiboParameters.getValue(i)));
        }
        if ((localStringBuffer.length() != 0) && (paramBoolean))
            localStringBuffer.append("\"");
        return localStringBuffer.toString();
    }

    private WeiboParameters generateAuthParameters(long paramLong1,
            long paramLong2, Token paramToken) {
        WeiboParameters localWeiboParameters = new WeiboParameters();
        localWeiboParameters.add("oauth_consumer_key", Weibo.APP_KEY);
        localWeiboParameters.add("oauth_nonce", String.valueOf(paramLong1));
        localWeiboParameters.add("oauth_signature_method", "HMAC-SHA1");
        localWeiboParameters.add("oauth_timestamp", String.valueOf(paramLong2));
        localWeiboParameters.add("oauth_version", "1.0");
        if (paramToken == null)
            localWeiboParameters.add("source", Weibo.APP_KEY);
        else
            localWeiboParameters.add("oauth_token", paramToken.getToken());
        return localWeiboParameters;
    }

    private String generateAuthSignature(String paramString1,
            WeiboParameters paramWeiboParameters, String paramString2,
            Token paramToken) {
        StringBuffer localStringBuffer = new StringBuffer(paramString1)
                .append("&").append(encode(constructRequestURL(paramString2)))
                .append("&");
        localStringBuffer.append(encode(encodeParameters(paramWeiboParameters,
                "&", false)));
        return localStringBuffer.toString();
    }

    private WeiboParameters generateSignatureParameters(
            WeiboParameters paramWeiboParameters1,
            WeiboParameters paramWeiboParameters2, String paramString)
            throws WeiboException {
        WeiboParameters localWeiboParameters = new WeiboParameters();
        localWeiboParameters.addAll(paramWeiboParameters1);
        localWeiboParameters.add("source", Weibo.APP_KEY);
        localWeiboParameters.addAll(paramWeiboParameters2);
        parseUrlParameters(paramString, localWeiboParameters);
        return generateSignatureList(localWeiboParameters);
    }

    public abstract void addAdditionalParams(
            WeiboParameters paramWeiboParameters1,
            WeiboParameters paramWeiboParameters2);

    public abstract String generateSignature(String paramString,
            Token paramToken) throws WeiboException;

    public abstract WeiboParameters generateSignatureList(
            WeiboParameters paramWeiboParameters);

    public String getWeiboAuthHeader(String paramString1, String paramString2,
            WeiboParameters paramWeiboParameters, String paramString3,
            String paramString4, Token paramToken) throws WeiboException {
        long l = System.currentTimeMillis() / 1000L;
        WeiboParameters localWeiboParameters = generateAuthParameters(l
                + new Random().nextInt(), l, paramToken);
        localWeiboParameters.add(
                "oauth_signature",
                generateSignature(
                        generateAuthSignature(
                                paramString1,
                                generateSignatureParameters(
                                        localWeiboParameters,
                                        paramWeiboParameters, paramString2),
                                paramString2, paramToken), paramToken));
        addAdditionalParams(localWeiboParameters, paramWeiboParameters);
        return "OAuth " + encodeParameters(localWeiboParameters, ",", true);
    }

    public void parseUrlParameters(String paramString,
            WeiboParameters paramWeiboParameters) throws WeiboException {
        // int i = 0;
        // UnsupportedEncodingException localUnsupportedEncodingException2 =
        // paramString.indexOf("?");
        // String[] arrayOfString1;
        // if (-1 != localUnsupportedEncodingException2)
        // arrayOfString1 =
        // paramString.substring(localUnsupportedEncodingException2 +
        // 1).split("&");
        // while (true)
        // {
        // try
        // {
        // localUnsupportedEncodingException2 = arrayOfString1.length;
        // break label112;
        // String[] arrayOfString2 = arrayOfString1[i].split("=");
        // if (arrayOfString2.length != 2)
        // continue;
        // paramWeiboParameters.add(URLDecoder.decode(arrayOfString2[0],
        // "UTF-8"), URLDecoder.decode(arrayOfString2[1], "UTF-8"));
        // break label119;
        // paramWeiboParameters.add(URLDecoder.decode(arrayOfString2[0],
        // "UTF-8"), "");
        // }
        // catch (UnsupportedEncodingException
        // localUnsupportedEncodingException1)
        // {
        // throw new WeiboException(localUnsupportedEncodingException1);
        // }
        // label112:
        // while (localUnsupportedEncodingException1 >=
        // localUnsupportedEncodingException2)
        // {
        // return;
        // label119: localUnsupportedEncodingException1++;
        // }
        // }
    }
}
