package com.tencent.weibo.utils;

import java.io.File;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.dld.coupon.util.SimpleMultipartEntity;
import com.tencent.weibo.beans.QParameter;

public class QHttpClient {
    private static final int CONNECTION_TIMEOUT = 20000;

    public static HttpClient getClient() {
        Object localObject = new DefaultHttpClient();
        try {
            localObject = KeyStore.getInstance(KeyStore.getDefaultType());
            ((KeyStore) localObject).load(null, null);
            MySSLSocketFactory localMySSLSocketFactory = new MySSLSocketFactory(
                    (KeyStore) localObject);
            localMySSLSocketFactory
                    .setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            localObject = new BasicHttpParams();
            HttpProtocolParams.setVersion((HttpParams) localObject,
                    HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset((HttpParams) localObject,
                    "UTF-8");
            SchemeRegistry localSchemeRegistry = new SchemeRegistry();
            localSchemeRegistry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            localSchemeRegistry.register(new Scheme("https",
                    localMySSLSocketFactory, 443));
            localObject = new DefaultHttpClient(
                    new ThreadSafeClientConnManager((HttpParams) localObject,
                            localSchemeRegistry), (HttpParams) localObject);
            return (HttpClient) localObject;
        } catch (Exception localException) {
            // while (true)
        }
        return (HttpClient) localObject;
    }

    public String httpGet(String paramString1, String paramString2)
            throws Exception {
        if ((paramString2 != null) && (!paramString2.equals("")))
            paramString1 = paramString1 + "?" + paramString2;
        HttpClient localHttpClient = getClient();
        Object localObject1 = new HttpGet(paramString1);
        ((HttpGet) localObject1).getParams().setParameter(
                "http.socket.timeout", new Integer(20000));
        try {
            localObject1 = EntityUtils.toString(localHttpClient.execute(
                    (HttpUriRequest) localObject1).getEntity());
            return (String) localObject1;
        } catch (Exception localException) {
            // throw new Exception(localException);
        } finally {
        }
        return (String) localObject1;
        // throw localObject2;
    }

    public String httpPost(String paramString1, String paramString2)
            throws Exception {
        Object localObject1 = getClient();
        HttpPost localHttpPost = new HttpPost(paramString1);
        localHttpPost.addHeader("Content-Type",
                "application/x-www-form-urlencoded");
        localHttpPost.getParams().setParameter("http.socket.timeout",
                new Integer(20000));
        if ((paramString2 != null) && (!paramString2.equals("")))
            localHttpPost
                    .setEntity(new ByteArrayEntity(paramString2.getBytes()));
        try {
            localObject1 = EntityUtils.toString(((HttpClient) localObject1)
                    .execute(localHttpPost).getEntity());
            return (String) localObject1;
        } catch (Exception localException) {
            // throw new Exception(localException);
        } finally {
        }
        return null;
        // throw localObject2;
    }

    public String httpPostWithFile(String paramString1, String paramString2,
            List<QParameter> paramList) throws Exception {
        Object localObject2 = paramString1 + '?' + paramString2;
        HttpClient localHttpClient = getClient();
        HttpPost localHttpPost = new HttpPost((String) localObject2);
        // while (true)
        {
            Object localObject3 = null;
            try {
                localObject3 = QHttpUtil.getQueryParameters(paramString2);
                localObject2 = new SimpleMultipartEntity();
                localObject3 = ((List) localObject3).iterator();
                if (((Iterator) localObject3).hasNext())
                    ;
                // continue;
                localObject3 = paramList.iterator();
                if (!((Iterator) localObject3).hasNext()) {
                    localHttpPost.setEntity((HttpEntity) localObject2);
                    return EntityUtils.toString(localHttpClient.execute(
                            localHttpPost).getEntity());
                    // localObject4 =
                    // (QParameter)((Iterator)localObject3).next();
                    // ((SimpleMultipartEntity)localObject2).addPart(((QParameter)localObject4).getName(),
                    // QHttpUtil.formParamDecode(((QParameter)localObject4).getValue()));
                    // continue;
                }
            } catch (Exception localException) {
                // throw new Exception(localException);
            } finally {
            }
            QParameter localQParameter = (QParameter) ((Iterator) localObject3)
                    .next();
            Object localObject4 = new File(localQParameter.getValue());
            ((SimpleMultipartEntity) localObject2).addPart(
                    localQParameter.getName(), (File) localObject4,
                    "image/jpeg");
            return null;
        }
    }
}
