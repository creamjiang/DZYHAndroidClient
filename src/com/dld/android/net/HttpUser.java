package com.dld.android.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.dld.android.util.Device;
import com.dld.android.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class HttpUser extends Connector {
    private Callback callback;
    private boolean cancel;
    private HttpConfig config;
    private HttpURLConnection conn;
    private Context context;
    private InputStream in;
    private OutputStream outputstream;
    private Param params;

    public HttpUser(Callback paramCallback, HttpConfig paramHttpConfig,
            Param paramParam, Context paramContext) {
        this.callback = paramCallback;
        this.config = paramHttpConfig;
        this.params = paramParam;
        this.context = paramContext;
    }

    public static HttpClient getHttpClient(Context paramContext) {
        Object localObject1 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout((HttpParams) localObject1,
                10000);
        HttpConnectionParams.setSoTimeout((HttpParams) localObject1, 60000);
        localObject1 = new DefaultHttpClient((HttpParams) localObject1);
        Object localObject2 = new DefaultHttpRequestRetryHandler();
        NoRetryNoResponseRetryHandler localNoRetryNoResponseRetryHandler = new NoRetryNoResponseRetryHandler();
        localNoRetryNoResponseRetryHandler
                .setHandler((HttpRequestRetryHandler) localObject2);
        ((DefaultHttpClient) localObject1)
                .setHttpRequestRetryHandler(localNoRetryNoResponseRetryHandler);
        localObject2 = ((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getNetworkInfo(1);
        LogUtils.log("main", "wifi is connected="
                + ((NetworkInfo) localObject2).isConnected());
        if (!((NetworkInfo) localObject2).isConnected()) {
            localObject2 = Uri.parse("content://telephony/carriers/preferapn");
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
                    ((DefaultHttpClient) localObject1).getParams()
                            .setParameter("http.route.default-proxy",
                                    localObject2);
                }
            }
        }
        return (HttpClient) (HttpClient) localObject1;
    }

    public static byte[] read(HttpResponse paramHttpResponse) {
        Object localObject = paramHttpResponse.getEntity();
        ByteArrayOutputStream localByteArrayOutputStream = null;
        try {
            localObject = ((HttpEntity) localObject).getContent();
            localByteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                try {
                    Header localHeader = paramHttpResponse
                            .getFirstHeader("Content-Encoding");
                    if ((localHeader == null)
                            || (localHeader.getValue().toLowerCase()
                                    .indexOf("gzip") <= -1))
                        continue;
                    localObject = new GZIPInputStream((InputStream) localObject);
                    byte[] arrayOfByte = new byte[512];
                    int i = ((InputStream) localObject).read(arrayOfByte);
                    if (i != -1)
                        continue;
                    localByteArrayOutputStream = localByteArrayOutputStream;
                    localByteArrayOutputStream.write(arrayOfByte, 0, i);
                    continue;
                } catch (Exception localException1) {
                    localByteArrayOutputStream = localByteArrayOutputStream;
                    label106: localException1.printStackTrace();
                }
            }
        } catch (Exception localException2) {
            // break label106;
            localException2.printStackTrace();
        }
        return localByteArrayOutputStream.toByteArray();
    }

    // ERROR //
    private void requestByGet(String paramString1, String paramString2)
            throws Exception {
        // Byte code:
        // 0: aconst_null
        // 1: astore_3
        // 2: new 234 java/lang/StringBuffer
        // 5: dup
        // 6: aload_1
        // 7: invokespecial 235 java/lang/StringBuffer:<init>
        // (Ljava/lang/String;)V
        // 10: astore 4
        // 12: aload_2
        // 13: ifnull +15 -> 28
        // 16: aload 4
        // 18: ldc 237
        // 20: invokevirtual 240 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 23: aload_2
        // 24: invokevirtual 240 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 27: pop
        // 28: new 242 org/apache/http/client/methods/HttpGet
        // 31: dup
        // 32: aload 4
        // 34: invokevirtual 243 java/lang/StringBuffer:toString
        // ()Ljava/lang/String;
        // 37: invokespecial 244 org/apache/http/client/methods/HttpGet:<init>
        // (Ljava/lang/String;)V
        // 40: astore 4
        // 42: aload_0
        // 43: getfield 36 com/doujiao/android/net/HttpUser:context
        // Landroid/content/Context;
        // 46: invokestatic 246 com/doujiao/android/net/HttpUser:getHttpClient
        // (Landroid/content/Context;)Lorg/apache/http/client/HttpClient;
        // 49: astore_3
        // 50: aload_3
        // 51: aload 4
        // 53: invokeinterface 252 2 0
        // 58: astore 4
        // 60: aload 4
        // 62: invokeinterface 256 1 0
        // 67: invokeinterface 261 1 0
        // 72: sipush 200
        // 75: if_icmpne +40 -> 115
        // 78: aload_0
        // 79: getfield 30 com/doujiao/android/net/HttpUser:callback
        // Lcom/doujiao/android/net/Callback;
        // 82: aload 4
        // 84: invokestatic 263 com/doujiao/android/net/HttpUser:read
        // (Lorg/apache/http/HttpResponse;)[B
        // 87: invokevirtual 269 com/doujiao/android/net/Callback:recieve ([B)V
        // 90: aload_3
        // 91: ifnull +23 -> 114
        // 94: aload_3
        // 95: invokeinterface 273 1 0
        // 100: ifnull +14 -> 114
        // 103: aload_3
        // 104: invokeinterface 273 1 0
        // 109: invokeinterface 278 1 0
        // 114: return
        // 115: aload_0
        // 116: getfield 30 com/doujiao/android/net/HttpUser:callback
        // Lcom/doujiao/android/net/Callback;
        // 119: new 280 org/apache/http/HttpException
        // 122: dup
        // 123: invokespecial 281 org/apache/http/HttpException:<init> ()V
        // 126: invokevirtual 285 com/doujiao/android/net/Callback:exception
        // (Ljava/lang/Exception;)V
        // 129: goto -39 -> 90
        // 132: pop
        // 133: aload_0
        // 134: getfield 30 com/doujiao/android/net/HttpUser:callback
        // Lcom/doujiao/android/net/Callback;
        // 137: new 280 org/apache/http/HttpException
        // 140: dup
        // 141: invokespecial 281 org/apache/http/HttpException:<init> ()V
        // 144: invokevirtual 285 com/doujiao/android/net/Callback:exception
        // (Ljava/lang/Exception;)V
        // 147: aload_3
        // 148: ifnull -34 -> 114
        // 151: aload_3
        // 152: invokeinterface 273 1 0
        // 157: ifnull -43 -> 114
        // 160: aload_3
        // 161: invokeinterface 273 1 0
        // 166: invokeinterface 278 1 0
        // 171: goto -57 -> 114
        // 174: astore_3
        // 175: aload_3
        // 176: invokevirtual 230 java/lang/Exception:printStackTrace ()V
        // 179: goto -65 -> 114
        // 182: astore 4
        // 184: aload_3
        // 185: ifnull +23 -> 208
        // 188: aload_3
        // 189: invokeinterface 273 1 0
        // 194: ifnull +14 -> 208
        // 197: aload_3
        // 198: invokeinterface 273 1 0
        // 203: invokeinterface 278 1 0
        // 208: aload 4
        // 210: athrow
        // 211: astore_3
        // 212: aload_3
        // 213: invokevirtual 230 java/lang/Exception:printStackTrace ()V
        // 216: goto -8 -> 208
        // 219: astore_3
        // 220: aload_3
        // 221: invokevirtual 230 java/lang/Exception:printStackTrace ()V
        // 224: goto -110 -> 114
        //
        // Exception table:
        // from to target type
        // 2 90 132 java/lang/Exception
        // 115 129 132 java/lang/Exception
        // 160 171 174 java/lang/Exception
        // 2 90 182 finally
        // 115 129 182 finally
        // 133 147 182 finally
        // 197 208 211 java/lang/Exception
        // 103 114 219 java/lang/Exception
    }

    // ERROR //
    private void requestByPost(String paramString, byte[] paramArrayOfByte)
            throws Exception {
        // Byte code:
        // 0: aconst_null
        // 1: astore_3
        // 2: new 289 org/apache/http/client/methods/HttpPost
        // 5: dup
        // 6: aload_1
        // 7: invokespecial 290 org/apache/http/client/methods/HttpPost:<init>
        // (Ljava/lang/String;)V
        // 10: astore 4
        // 12: aload 4
        // 14: ldc_w 292
        // 17: ldc_w 294
        // 20: invokevirtual 297
        // org/apache/http/client/methods/HttpPost:setHeader
        // (Ljava/lang/String;Ljava/lang/String;)V
        // 23: aload 4
        // 25: ldc_w 299
        // 28: ldc_w 301
        // 31: invokevirtual 297
        // org/apache/http/client/methods/HttpPost:setHeader
        // (Ljava/lang/String;Ljava/lang/String;)V
        // 34: aload 4
        // 36: ldc_w 303
        // 39: ldc_w 305
        // 42: invokevirtual 297
        // org/apache/http/client/methods/HttpPost:setHeader
        // (Ljava/lang/String;Ljava/lang/String;)V
        // 45: aload 4
        // 47: ldc_w 307
        // 50: ldc_w 309
        // 53: invokevirtual 297
        // org/apache/http/client/methods/HttpPost:setHeader
        // (Ljava/lang/String;Ljava/lang/String;)V
        // 56: aload_2
        // 57: ifnull +16 -> 73
        // 60: aload 4
        // 62: new 311 org/apache/http/entity/ByteArrayEntity
        // 65: dup
        // 66: aload_2
        // 67: invokespecial 313 org/apache/http/entity/ByteArrayEntity:<init>
        // ([B)V
        // 70: invokevirtual 317
        // org/apache/http/client/methods/HttpPost:setEntity
        // (Lorg/apache/http/HttpEntity;)V
        // 73: aload_0
        // 74: getfield 36 com/doujiao/android/net/HttpUser:context
        // Landroid/content/Context;
        // 77: invokestatic 246 com/doujiao/android/net/HttpUser:getHttpClient
        // (Landroid/content/Context;)Lorg/apache/http/client/HttpClient;
        // 80: astore_3
        // 81: aload_3
        // 82: aload 4
        // 84: invokeinterface 252 2 0
        // 89: astore 4
        // 91: aload 4
        // 93: invokeinterface 256 1 0
        // 98: invokeinterface 261 1 0
        // 103: sipush 200
        // 106: if_icmpne +40 -> 146
        // 109: aload_0
        // 110: getfield 30 com/doujiao/android/net/HttpUser:callback
        // Lcom/doujiao/android/net/Callback;
        // 113: aload 4
        // 115: invokestatic 263 com/doujiao/android/net/HttpUser:read
        // (Lorg/apache/http/HttpResponse;)[B
        // 118: invokevirtual 269 com/doujiao/android/net/Callback:recieve ([B)V
        // 121: aload_3
        // 122: ifnull +23 -> 145
        // 125: aload_3
        // 126: invokeinterface 273 1 0
        // 131: ifnull +14 -> 145
        // 134: aload_3
        // 135: invokeinterface 273 1 0
        // 140: invokeinterface 278 1 0
        // 145: return
        // 146: aload_0
        // 147: getfield 30 com/doujiao/android/net/HttpUser:callback
        // Lcom/doujiao/android/net/Callback;
        // 150: new 280 org/apache/http/HttpException
        // 153: dup
        // 154: invokespecial 281 org/apache/http/HttpException:<init> ()V
        // 157: invokevirtual 285 com/doujiao/android/net/Callback:exception
        // (Ljava/lang/Exception;)V
        // 160: goto -39 -> 121
        // 163: pop
        // 164: aload_0
        // 165: getfield 30 com/doujiao/android/net/HttpUser:callback
        // Lcom/doujiao/android/net/Callback;
        // 168: new 280 org/apache/http/HttpException
        // 171: dup
        // 172: invokespecial 281 org/apache/http/HttpException:<init> ()V
        // 175: invokevirtual 285 com/doujiao/android/net/Callback:exception
        // (Ljava/lang/Exception;)V
        // 178: aload_3
        // 179: ifnull -34 -> 145
        // 182: aload_3
        // 183: invokeinterface 273 1 0
        // 188: ifnull -43 -> 145
        // 191: aload_3
        // 192: invokeinterface 273 1 0
        // 197: invokeinterface 278 1 0
        // 202: goto -57 -> 145
        // 205: astore_3
        // 206: aload_3
        // 207: invokevirtual 230 java/lang/Exception:printStackTrace ()V
        // 210: goto -65 -> 145
        // 213: astore 4
        // 215: aload_3
        // 216: ifnull +23 -> 239
        // 219: aload_3
        // 220: invokeinterface 273 1 0
        // 225: ifnull +14 -> 239
        // 228: aload_3
        // 229: invokeinterface 273 1 0
        // 234: invokeinterface 278 1 0
        // 239: aload 4
        // 241: athrow
        // 242: astore_3
        // 243: aload_3
        // 244: invokevirtual 230 java/lang/Exception:printStackTrace ()V
        // 247: goto -8 -> 239
        // 250: astore_3
        // 251: aload_3
        // 252: invokevirtual 230 java/lang/Exception:printStackTrace ()V
        // 255: goto -110 -> 145
        //
        // Exception table:
        // from to target type
        // 2 121 163 java/lang/Exception
        // 146 160 163 java/lang/Exception
        // 191 202 205 java/lang/Exception
        // 2 121 213 finally
        // 146 160 213 finally
        // 164 178 213 finally
        // 228 239 242 java/lang/Exception
        // 134 145 250 java/lang/Exception
    }

    // /** @deprecated */
    // public void cancel()
    // {
    // monitorenter;
    // try
    // {
    // this.cancel = true;
    // monitorexit;
    // return;
    // }
    // finally
    // {
    // localObject = finally;
    // monitorexit;
    // }
    // throw localObject;
    // }

    public void close() {
        try {
            if (this.outputstream != null) {
                this.outputstream.close();
                this.outputstream = null;
            }
            if (this.in != null) {
                this.in.close();
                this.in = null;
            }
            if (this.conn != null) {
                this.conn.disconnect();
                this.conn = null;
            }
            return;
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }

    public void run() {
        int i = 0 + 1;
        try {
            String str1 = Device.getInstance().getNetType(
                    this.callback.getContext());
            if ((str1 == null) || (str1.equals("")))
                this.callback.onException(new IOException(
                        "No available net service!"));
            while (true) {
                // return;
                str1 = this.callback.getUrl();
                LogUtils.log("Http", "完整的uri" + str1);
                LogUtils.log("main", "完整的url" + str1);
                if ((str1 != null) && (!str1.equals("")))
                    break;
                this.callback.onException(new NullPointerException(
                        "Uri is null"));
                close();
            }
            int j = str1.indexOf("?");
            if (j == -1) {
                // str2 = str1;
                label160: this.conn = null;
                if (!this.callback.custom)
                    ;
                // break label229;
            }
            label229: for (str1 = "GET";; str1 = this.config.getRequestMethod()) {
                LogUtils.log("main", "method==" + str1);
                boolean bool = this.cancel;
                if (!bool)
                    return;
                // break label240;
                close();
                break;
                // str2 = str1.substring(0, str2);
                // break label160;
            }
            label240: LogUtils.log("Http", "<Url: > " + null);
            LogUtils.log("main", str1 + "///");
            if (str1.equalsIgnoreCase("POST")) {
                if (this.params != null)
                    // requestByPost(str2,
                    // this.params.toString().getBytes("UTF-8"));
                    while (true) {
                        // LogUtils.log("main", "请求的url" + str2);
                        close();
                        break;
                        // requestByPost(str2, null);
                    }
            }
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                close();
            }
        } finally {
            close();
        }
        // throw localObject;
    }

    private static class NoRetryNoResponseRetryHandler implements
            HttpRequestRetryHandler {
        private HttpRequestRetryHandler handler;

        public boolean retryRequest(IOException paramIOException, int paramInt,
                HttpContext paramHttpContext) {
            boolean bool;
            if ((paramIOException == null)
                    || (paramIOException.getClass() != NoHttpResponseException.class))
                bool = this.handler.retryRequest(paramIOException, paramInt,
                        paramHttpContext);
            else
                bool = false;
            return bool;
        }

        public void setHandler(
                HttpRequestRetryHandler paramHttpRequestRetryHandler) {
            this.handler = paramHttpRequestRetryHandler;
        }
    }
}
