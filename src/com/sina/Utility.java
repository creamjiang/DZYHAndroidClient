package com.sina;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.dld.android.util.LogUtils;
import com.tencent.weibo.utils.Cache;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class Utility {
    public static final String BOUNDARY = "7cd4a6d158c";
    public static final String END_MP_BOUNDARY = "--7cd4a6d158c--";
    public static final String HTTPMETHOD_DELETE = "DELETE";
    public static final String HTTPMETHOD_GET = "GET";
    public static final String HTTPMETHOD_POST = "POST";
    public static final String MP_BOUNDARY = "--7cd4a6d158c";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final int SET_CONNECTION_TIMEOUT = 50000;
    private static final int SET_SOCKET_TIMEOUT = 200000;
    private static HttpHeaderFactory mAuth;
    private static WeiboParameters mRequestHeader = new WeiboParameters();
    private static Token mToken = null;

    public static char[] base64Encode(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
                .toCharArray();
        char[] arrayOfChar2 = new char[4 * ((2 + paramArrayOfByte.length) / 3)];
        int i = 0;
        for (int j = 0; i < paramArrayOfByte.length; j += 4) {
            int n = 0;
            int k = 0;
            int m = (0xFF & paramArrayOfByte[i]) << 8;
            if (i + 1 < paramArrayOfByte.length) {
                m |= 0xFF & paramArrayOfByte[(i + 1)];
                k = 1;
            }
            int i1 = m << 8;
            if (i + 2 < paramArrayOfByte.length) {
                i1 |= 0xFF & paramArrayOfByte[(i + 2)];
                n = 1;
            }
            m = j + 3;
            if (n == 0)
                n = 64;
            else
                n = i1 & 0x3F;
            arrayOfChar2[m] = arrayOfChar1[n];
            m = i1 >> 6;
            n = j + 2;
            if (k == 0)
                k = 64;
            else
                k = m & 0x3F;
            arrayOfChar2[n] = arrayOfChar1[k];
            k = m >> 6;
            arrayOfChar2[(j + 1)] = arrayOfChar1[(k & 0x3F)];
            k >>= 6;
            arrayOfChar2[(j + 0)] = arrayOfChar1[(k & 0x3F)];
            i += 3;
        }
        return arrayOfChar2;
    }

    public static void clearCookies(Context paramContext) {
        CookieSyncManager.createInstance(paramContext);
        CookieManager.getInstance().removeAllCookie();
    }

    public static void clearRequestHeader() {
        mRequestHeader.clear();
    }

    public static Bundle decodeUrl(String paramString) {
        Bundle localBundle = new Bundle();
        if (paramString != null) {
            String[] arrayOfString1 = paramString.split("&");
            int j = arrayOfString1.length;
            for (int i = 0; i < j; i++) {
                String[] arrayOfString2 = arrayOfString1[i].split("=");
                localBundle.putString(URLDecoder.decode(arrayOfString2[0]),
                        URLDecoder.decode(arrayOfString2[1]));
            }
        }
        return localBundle;
    }

    public static String encodeParameters(WeiboParameters paramWeiboParameters) {
        String str1 = "";
        if ((paramWeiboParameters == null)
                || (isBundleEmpty(paramWeiboParameters))) {
            return str1;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        while (true) {
            String str2;
            if (j >= paramWeiboParameters.size()) {
                str2 = localStringBuilder.toString();
                break;
            }
            String str3 = paramWeiboParameters.getKey(j);
            // if (str2 != 0)
            // localStringBuilder.append("&");
            // try
            // {
            // localStringBuilder.append(URLEncoder.encode(str3,
            // "UTF-8")).append("=").append(URLEncoder.encode(paramWeiboParameters.getValue(str3),
            // "UTF-8"));
            // label93: str2++;
            // j++;
            // }
            // catch (UnsupportedEncodingException
            // localUnsupportedEncodingException)
            // {
            // break label93;
            // }
        }
        return str1;
    }

    public static String encodePostBody(Bundle paramBundle, String paramString) {
        Object localObject;
        if (paramBundle != null) {
            localObject = new StringBuilder();
            Iterator localIterator = paramBundle.keySet().iterator();
            while (localIterator.hasNext()) {
                String str = (String) localIterator.next();
                if (paramBundle.getByteArray(str) != null)
                    continue;
                ((StringBuilder) localObject)
                        .append("Content-Disposition: form-data; name=\"" + str
                                + "\"\r\n\r\n" + paramBundle.getString(str));
                ((StringBuilder) localObject).append("\r\n--" + paramString
                        + "\r\n");
            }
            localObject = ((StringBuilder) localObject).toString();
        } else {
            localObject = "";
        }
        return (String) localObject;
    }

    public static String encodeUrl(WeiboParameters paramWeiboParameters) {
        Object localObject;
        if (paramWeiboParameters != null) {
            localObject = new StringBuilder();
            int j = 1;
            for (int i = 0; i < paramWeiboParameters.size(); i++) {
                if (j == 0)
                    ((StringBuilder) localObject).append("&");
                else
                    j = 0;
                ((StringBuilder) localObject).append(URLEncoder
                        .encode(paramWeiboParameters.getKey(i))
                        + "="
                        + URLEncoder.encode(paramWeiboParameters.getValue(i)));
            }
            localObject = ((StringBuilder) localObject).toString();
        } else {
            localObject = "";
        }
        return (String) localObject;
    }

    public static HttpClient getHttpClient(Context paramContext) {
        Object localObject1 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout((HttpParams) localObject1,
                50000);
        HttpConnectionParams.setSoTimeout((HttpParams) localObject1, 200000);
        localObject1 = new DefaultHttpClient((HttpParams) localObject1);
        if (!((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getNetworkInfo(1)
                .isConnected()) {
            Object localObject2 = Uri
                    .parse("content://telephony/carriers/preferapn");
            localObject2 = paramContext.getContentResolver().query(
                    (Uri) localObject2, null, null, null, null);
            if ((localObject2 != null)
                    && (((Cursor) localObject2).moveToFirst())) {
                localObject2 = ((Cursor) localObject2)
                        .getString(((Cursor) localObject2)
                                .getColumnIndex("proxy"));
                if ((localObject2 != null)
                        && (((String) localObject2).trim().length() > 0)) {
                    localObject2 = new HttpHost((String) localObject2, 80);
                    ((HttpClient) localObject1).getParams().setParameter(
                            "http.route.default-proxy", localObject2);
                }
            }
        }
        return (HttpClient) (HttpClient) localObject1;
    }

    public static UrlEncodedFormEntity getPostParamters(Bundle paramBundle)
            throws WeiboException {
        Object localObject = null;
        if ((paramBundle == null) || (paramBundle.isEmpty()))
            localObject = null;
        // while (true)
        {
            // return localObject;
            try {
                ArrayList localArrayList = new ArrayList();
                localObject = paramBundle.keySet().iterator();
                // while (true)
                {
                    if (!((Iterator) localObject).hasNext()) {
                        localObject = new UrlEncodedFormEntity(localArrayList,
                                "UTF-8");
                        // break;
                    }
                    String str = (String) ((Iterator) localObject).next();
                    localArrayList.add(new BasicNameValuePair(str, paramBundle
                            .getString(str)));
                }
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            }
        }
        return (UrlEncodedFormEntity) localObject;
        // throw new WeiboException(localUnsupportedEncodingException);
    }

    // ERROR //
    private static void imageContentToUpload(OutputStream paramOutputStream,
            android.graphics.Bitmap paramBitmap) throws WeiboException {
        // Byte code:
        // 0: new 115 java/lang/StringBuilder
        // 3: dup
        // 4: invokespecial 116 java/lang/StringBuilder:<init> ()V
        // 7: astore_2
        // 8: aload_2
        // 9: ldc 23
        // 11: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 14: ldc 183
        // 16: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 19: pop
        // 20: aload_2
        // 21: ldc_w 320
        // 24: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 27: ldc_w 322
        // 30: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 33: ldc_w 324
        // 36: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 39: pop
        // 40: aload_2
        // 41: ldc_w 326
        // 44: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 47: ldc_w 328
        // 50: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 53: ldc_w 330
        // 56: invokevirtual 132 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 59: pop
        // 60: aload_2
        // 61: invokevirtual 124 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 64: invokevirtual 334 java/lang/String:getBytes ()[B
        // 67: astore_2
        // 68: aload_0
        // 69: aload_2
        // 70: invokevirtual 340 java/io/OutputStream:write ([B)V
        // 73: aload_1
        // 74: getstatic 346 android/graphics/Bitmap$CompressFormat:PNG
        // Landroid/graphics/Bitmap$CompressFormat;
        // 77: bipush 75
        // 79: aload_0
        // 80: invokevirtual 352 android/graphics/Bitmap:compress
        // (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        // 83: pop
        // 84: aload_0
        // 85: ldc 183
        // 87: invokevirtual 334 java/lang/String:getBytes ()[B
        // 90: invokevirtual 340 java/io/OutputStream:write ([B)V
        // 93: aload_0
        // 94: ldc_w 354
        // 97: invokevirtual 334 java/lang/String:getBytes ()[B
        // 100: invokevirtual 340 java/io/OutputStream:write ([B)V
        // 103: iconst_0
        // 104: ifeq +7 -> 111
        // 107: aconst_null
        // 108: invokevirtual 359 java/io/BufferedInputStream:close ()V
        // 111: return
        // 112: astore_2
        // 113: new 290 com/sina/WeiboException
        // 116: dup
        // 117: aload_2
        // 118: invokespecial 314 com/sina/WeiboException:<init>
        // (Ljava/lang/Exception;)V
        // 121: athrow
        // 122: astore_2
        // 123: iconst_0
        // 124: ifeq +7 -> 131
        // 127: aconst_null
        // 128: invokevirtual 359 java/io/BufferedInputStream:close ()V
        // 131: aload_2
        // 132: athrow
        // 133: astore_2
        // 134: new 290 com/sina/WeiboException
        // 137: dup
        // 138: aload_2
        // 139: invokespecial 314 com/sina/WeiboException:<init>
        // (Ljava/lang/Exception;)V
        // 142: athrow
        // 143: astore_2
        // 144: new 290 com/sina/WeiboException
        // 147: dup
        // 148: aload_2
        // 149: invokespecial 314 com/sina/WeiboException:<init>
        // (Ljava/lang/Exception;)V
        // 152: athrow
        //
        // Exception table:
        // from to target type
        // 68 103 112 java/io/IOException
        // 68 103 122 finally
        // 113 122 122 finally
        // 127 131 133 java/io/IOException
        // 107 111 143 java/io/IOException
    }

    public static boolean isBundleEmpty(WeiboParameters paramWeiboParameters) {
        boolean i;
        if ((paramWeiboParameters != null)
                && (paramWeiboParameters.size() != 0))
            i = false;
        else
            i = true;
        return i;
    }

    public static String openUrl(Context paramContext, String paramString1,
            String paramString2, WeiboParameters paramWeiboParameters,
            Token paramToken) throws WeiboException {
        String str2 = "";
        String str1;
        for (int i = 0; i < paramWeiboParameters.size(); i++) {
            str1 = paramWeiboParameters.getKey(i);
            if (!str1.equals("pic"))
                continue;
            str2 = paramWeiboParameters.getValue(str1);
            paramWeiboParameters.remove(str1);
        }
        if (!TextUtils.isEmpty(str2))
            str1 = openUrl(paramContext, paramString1, paramString2,
                    paramWeiboParameters, str2, paramToken);
        else
            str1 = openUrl(paramContext, paramString1, paramString2,
                    paramWeiboParameters, null, paramToken);
        return str1;
    }

    public static String openUrl(Context paramContext, String paramString1,
            String paramString2, WeiboParameters paramWeiboParameters,
            String paramString3, Token paramToken) throws WeiboException {
        // Object localObject4;
        // while (true)
        // {
        // try
        // {
        // Object localObject1 = getHttpClient(paramContext);
        // HttpGet localHttpGet = null;
        // if (paramString2.equals("GET"))
        // {
        // paramString1 = paramString1 + "?" + encodeUrl(paramWeiboParameters);
        // localHttpGet = new HttpGet(paramString1);
        // setHeader(paramString2, localHttpGet, paramWeiboParameters,
        // paramString1, paramToken);
        // LogUtils.log("ShareActivity", paramString1);
        // localObject4 = ((HttpClient)localObject1).execute(localHttpGet);
        // localObject1 = ((HttpResponse)localObject4).getStatusLine();
        // int i = ((StatusLine)localObject1).getStatusCode();
        // LogUtils.log("main", "responseCode=" + i);
        // if (i == 200)
        // break;
        // read((HttpResponse)localObject4);
        // localObject1 = new
        // WeiboException(String.format(localObject1.toString(), new Object[0]),
        // i);
        // throw ((Throwable)localObject1);
        // }
        // }
        // catch (IOException localObject2)
        // {
        // LogUtils.log("main", "opurl exception");
        // localObject2 = new WeiboException();
        // throw ((Throwable)localObject2);
        // }
        // if (paramString2.equals("POST"))
        // {
        // // ((byte[])null);
        // localObject3 = new ByteArrayOutputStream(51200);
        // byte[] arrayOfByte;
        // if (Cache.getCache(paramString3) != null)
        // {
        // localObject4 = new HttpPost(paramString1);
        // LogUtils.log("ShareActivity", "byte 不为空");
        // paramToUpload((OutputStream)localObject3, paramWeiboParameters);
        // ((HttpPost)localObject4).setHeader("Content-Type",
        // "multipart/form-data; boundary=7cd4a6d158c");
        // arrayOfByte = (byte[])Cache.remove(paramString3);
        // imageContentToUpload((OutputStream)localObject3,
        // BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length));
        // }
        // while (true)
        // {
        // arrayOfByte = ((ByteArrayOutputStream)localObject3).toByteArray();
        // ((ByteArrayOutputStream)localObject3).close();
        // ((HttpPost)localObject4).setEntity(new ByteArrayEntity(arrayOfByte));
        // localObject3 = localObject4;
        // break;
        // LogUtils.log("ShareActivity", "byte 为空");
        // paramString1 = Weibo.SERVER + "statuses/update.json";
        // localObject4 = new HttpPost(paramString1);
        // ((HttpPost)localObject4).setHeader("Content-Type",
        // "application/x-www-form-urlencoded");
        // paramWeiboParameters.remove("pic");
        // ((ByteArrayOutputStream)localObject3).write(encodeParameters(paramWeiboParameters).getBytes("UTF-8"));
        // }
        // }
        // if (!paramString2.equals("DELETE"))
        // continue;
        // Object localObject3 = new HttpDelete(paramString1);
        // }
        // Object localObject2 = read((HttpResponse)localObject4);
        // LogUtils.log("main", "result==" + (String)localObject2);
        // return (String)(String)(String)(String)localObject2;
        return null;
    }

    public static String openUrlforLogin(Context paramContext,
            String paramString1, String paramString2,
            WeiboParameters paramWeiboParameters, Token paramToken)
            throws WeiboException {
        String str2 = "";
        String str1;
        for (int i = 0; i < paramWeiboParameters.size(); i++) {
            str1 = paramWeiboParameters.getKey(i);
            if (!str1.equals("pic"))
                continue;
            str2 = paramWeiboParameters.getValue(str1);
            paramWeiboParameters.remove(str1);
        }
        if (!TextUtils.isEmpty(str2))
            str1 = openUrlforLogin(paramContext, paramString1, paramString2,
                    paramWeiboParameters, str2, paramToken);
        else
            str1 = openUrlforLogin(paramContext, paramString1, paramString2,
                    paramWeiboParameters, null, paramToken);
        return str1;
    }

    public static String openUrlforLogin(Context paramContext,
            String paramString1, String paramString2,
            WeiboParameters paramWeiboParameters, String paramString3,
            Token paramToken) throws WeiboException {
        Object localObject3;
        // while (true)
        // {
        // try
        // {
        // HttpClient localHttpClient = getHttpClient(paramContext);
        // localObject2 = null;
        // if (paramString2.equals("GET"))
        // {
        // paramString1 = paramString1 + "?" + encodeUrl(paramWeiboParameters);
        // localObject2 = new HttpGet(paramString1);
        // setHeader(paramString2, (HttpUriRequest)localObject2,
        // paramWeiboParameters, paramString1, paramToken);
        // LogUtils.log("ShareActivity", paramString1);
        // localObject3 = localHttpClient.execute((HttpUriRequest)localObject2);
        // localObject2 = ((HttpResponse)localObject3).getStatusLine();
        // int i = ((StatusLine)localObject2).getStatusCode();
        // if (i == 200)
        // break;
        // read((HttpResponse)localObject3);
        // WeiboException localWeiboException = new
        // WeiboException(String.format(localObject2.toString(), new Object[0]),
        // i);
        // throw localWeiboException;
        // }
        // }
        // catch (IOException localObject1)
        // {
        // localObject1 = new WeiboException();
        // throw ((Throwable)localObject1);
        // }
        // if (paramString2.equals("POST"))
        // {
        // // ((byte[])null);
        // localObject3 = new ByteArrayOutputStream(51200);
        // byte[] arrayOfByte;
        // if (Cache.getCache(paramString3) != null)
        // {
        // localObject2 = new HttpPost(paramString1);
        // LogUtils.log("ShareActivity", "byte 不为空");
        // paramToUpload((OutputStream)localObject3, paramWeiboParameters);
        // ((HttpPost)localObject2).setHeader("Content-Type",
        // "multipart/form-data; boundary=7cd4a6d158c");
        // arrayOfByte = (byte[])Cache.getCache(paramString3);
        // imageContentToUpload((OutputStream)localObject3,
        // BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length));
        // }
        // while (true)
        // {
        // arrayOfByte = ((ByteArrayOutputStream)localObject3).toByteArray();
        // ((ByteArrayOutputStream)localObject3).close();
        // ((HttpPost)localObject2).setEntity(new ByteArrayEntity(arrayOfByte));
        // localObject2 = localObject2;
        // break;
        // LogUtils.log("ShareActivity", "byte 为空");
        // localObject2 = new HttpPost(paramString1);
        // ((HttpPost)localObject2).setHeader("Content-Type",
        // "application/x-www-form-urlencoded");
        // paramWeiboParameters.remove("pic");
        // ((ByteArrayOutputStream)localObject3).write(encodeParameters(paramWeiboParameters).getBytes("UTF-8"));
        // }
        // }
        // if (!paramString2.equals("DELETE"))
        // continue;
        // Object localObject2 = new HttpDelete(paramString1);
        // }
        // Object localObject1 = read((HttpResponse)localObject3);
        // return (String)(String)(String)localObject1;
        return null;
    }

    private static void paramToUpload(OutputStream paramOutputStream,
            WeiboParameters paramWeiboParameters) throws WeiboException {
        int i = 0;
        while (true) {
            if (i >= paramWeiboParameters.size())
                return;
            String str = paramWeiboParameters.getKey(i);
            Object localObject = new StringBuilder(10);
            ((StringBuilder) localObject).setLength(0);
            ((StringBuilder) localObject).append("--7cd4a6d158c")
                    .append("\r\n");
            ((StringBuilder) localObject)
                    .append("content-disposition: form-data; name=\"")
                    .append(str).append("\"\r\n\r\n");
            ((StringBuilder) localObject).append(
                    paramWeiboParameters.getValue(str)).append("\r\n");
            localObject = ((StringBuilder) localObject).toString().getBytes();
            try {
                paramOutputStream.write(((StringBuilder) localObject)
                        .toString().getBytes());
                i++;
            } catch (IOException localIOException) {
            }
        }
        // throw new WeiboException();
    }

    public static Bundle parseUrl(String paramString) {
        Object localObject = paramString.replace("fbconnect", "http");
        try {
            URL localURL = new URL((String) localObject);
            localObject = decodeUrl(localURL.getQuery());
            ((Bundle) localObject).putAll(decodeUrl(localURL.getRef()));
            return (Bundle) localObject;
        } catch (MalformedURLException localMalformedURLException) {
            // while (true)
            localObject = new Bundle();
        }
        return (Bundle) localObject;
    }

    private static String read(InputStream paramInputStream) throws IOException {
        StringBuilder localStringBuilder = new StringBuilder();
        BufferedReader localBufferedReader = new BufferedReader(
                new InputStreamReader(paramInputStream), 1000);
        for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader
                .readLine())
            localStringBuilder.append(str);
        paramInputStream.close();
        return localStringBuilder.toString();
    }

    private static String read(HttpResponse paramHttpResponse)
            throws WeiboException {
        Object localObject1 = paramHttpResponse.getEntity();
        try {
            Object localObject2 = ((HttpEntity) localObject1).getContent();
            localObject1 = new ByteArrayOutputStream();
            Object localObject3 = paramHttpResponse
                    .getFirstHeader("Content-Encoding");
            if ((localObject3 != null)
                    && (((Header) localObject3).getValue().toLowerCase()
                            .indexOf("gzip") > -1)) {
                LogUtils.log("main", "isGzip");
                localObject2 = new GZIPInputStream((InputStream) localObject2);
            }
            localObject3 = new byte[512];
            // while (true)
            // {
            // int i = ((InputStream)localObject2).read(localObject3);
            // if (i == -1)
            // return new
            // String(((ByteArrayOutputStream)localObject1).toByteArray());
            // ((ByteArrayOutputStream)localObject1).write(localObject3, 0, i);
            // }
        } catch (IllegalStateException localIllegalStateException) {
            LogUtils.log("main", "read IllegalStateException");
            // throw new WeiboException(localIllegalStateException);
        } catch (IOException localIOException) {
            LogUtils.log("main", "read IOException");
        }
        return null;
        // throw new WeiboException(localIOException);
    }

    public static void setAuthorization(HttpHeaderFactory paramHttpHeaderFactory) {
        mAuth = paramHttpHeaderFactory;
    }

    public static void setHeader(String paramString1,
            HttpUriRequest paramHttpUriRequest,
            WeiboParameters paramWeiboParameters, String paramString2,
            Token paramToken) throws WeiboException {
        if (!isBundleEmpty(mRequestHeader))
            for (int i = 0; i < mRequestHeader.size(); i++) {
                String str = mRequestHeader.getKey(i);
                paramHttpUriRequest
                        .setHeader(str, mRequestHeader.getValue(str));
            }
        if (!isBundleEmpty(paramWeiboParameters))
            paramHttpUriRequest.setHeader("Authorization", mAuth
                    .getWeiboAuthHeader(paramString1, paramString2,
                            paramWeiboParameters, Weibo.APP_KEY,
                            Weibo.APP_SECRET, paramToken));
        paramHttpUriRequest.setHeader("User-Agent", System.getProperties()
                .getProperty("http.agent") + " WeiboAndroidSDK");
    }

    public static void setRequestHeader(WeiboParameters paramWeiboParameters) {
        mRequestHeader.addAll(paramWeiboParameters);
    }

    public static void setRequestHeader(String paramString1, String paramString2) {
        mRequestHeader.add(paramString1, paramString2);
    }

    public static void setTokenObject(Token paramToken) {
        mToken = paramToken;
    }

    public static void showAlert(Context paramContext, String paramString1,
            String paramString2) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
        localBuilder.setTitle(paramString1);
        localBuilder.setMessage(paramString2);
        localBuilder.create().show();
    }
}
