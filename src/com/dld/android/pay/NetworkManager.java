package com.dld.android.pay;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class NetworkManager {
    static final String TAG = "NetworkManager";
    private int connectTimeout = 30000;
    Context mContext;
    java.net.Proxy mProxy = null;
    private int readTimeout = 30000;

    public NetworkManager(Context paramContext) {
        this.mContext = paramContext;
        setDefaultHostnameVerifier();
    }

    private void setDefaultHostnameVerifier() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String paramString, SSLSession paramSSLSession) {
                return true;
            }
        });
    }

    public String SendAndWaitResponse(String paramString1, String paramString2) {
        detectProxy();
        String str = null;
        Object localObject2 = new ArrayList();
        ((ArrayList) localObject2).add(new BasicNameValuePair("requestData",
                paramString1));
        HttpURLConnection localHttpURLConnection = null;
        try {
            localObject2 = new UrlEncodedFormEntity((List) localObject2,
                    "utf-8");
            Object localObject3 = new URL(paramString2);
            if (this.mProxy != null)
                ;
            for (localHttpURLConnection = (HttpURLConnection) ((URL) localObject3)
                    .openConnection(this.mProxy);; localHttpURLConnection = (HttpURLConnection) ((URL) localObject3)
                    .openConnection()) {
                localHttpURLConnection.setConnectTimeout(this.connectTimeout);
                localHttpURLConnection.setReadTimeout(this.readTimeout);
                localHttpURLConnection.setDoOutput(true);
                localHttpURLConnection.addRequestProperty("Content-type",
                        "application/x-www-form-urlencoded;charset=utf-8");
                localHttpURLConnection.connect();
                localObject3 = localHttpURLConnection.getOutputStream();
                ((UrlEncodedFormEntity) localObject2)
                        .writeTo((OutputStream) localObject3);
                ((OutputStream) localObject3).flush();
                str = BaseHelper.convertStreamToString(localHttpURLConnection
                        .getInputStream());
                str = str;
                return str;
            }
        } catch (IOException localIOException) {
            while (true) {
                localIOException.printStackTrace();
                localHttpURLConnection.disconnect();
            }
        } finally {
            localHttpURLConnection.disconnect();
        }
        // throw localObject1;
    }

    public void detectProxy() {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) this.mContext
                .getSystemService("connectivity")).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable())
                && (localNetworkInfo.getType() == 0)) {
            String str = android.net.Proxy.getDefaultHost();
            int i = android.net.Proxy.getDefaultPort();
            if (str != null) {
                InetSocketAddress localInetSocketAddress = new InetSocketAddress(
                        str, i);
                this.mProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
                        localInetSocketAddress);
            }
        }
    }

    public boolean urlDownloadToFile(Context paramContext, String paramString1,
            String paramString2) {
        boolean i = false;
        detectProxy();
        try {
            Object localObject = new URL(paramString1);
            FileOutputStream localFileOutputStream = null;
            byte[] arrayOfByte = null;
            if (this.mProxy != null) {
                localObject = (HttpURLConnection) ((URL) localObject)
                        .openConnection(this.mProxy);
                ((HttpURLConnection) localObject)
                        .setConnectTimeout(this.connectTimeout);
                ((HttpURLConnection) localObject)
                        .setReadTimeout(this.readTimeout);
                ((HttpURLConnection) localObject).setDoInput(true);
                ((HttpURLConnection) localObject).connect();
                localObject = ((HttpURLConnection) localObject)
                        .getInputStream();
                File localFile = new File(paramString2);
                localFile.createNewFile();
                localFileOutputStream = new FileOutputStream(localFile);
                arrayOfByte = new byte[1024];
            }
            while (true) {
                int j = ((InputStream) localObject).read(arrayOfByte);
                if (j <= 0) {
                    localFileOutputStream.close();
                    ((InputStream) localObject).close();
                    i = true;
                    // break label171;
                    localObject = (HttpURLConnection) ((URL) localObject)
                            .openConnection();
                    break;
                }
                localFileOutputStream.write(arrayOfByte, 0, j);
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        label171: return i;
    }
}
