package com.tenpay.android.service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.net.URL;

public class NetworkUtil {
    private int connectTimeout = 30000;
    Context mContext;
    java.net.Proxy mProxy = null;
    private int readTimeout = 30000;

    private NetworkUtil() {
    }

    public NetworkUtil(Context paramContext) {
        this.mContext = paramContext;
    }

    public void detectProxy() {
        Object localObject = android.net.Proxy.getDefaultHost();
        int i = android.net.Proxy.getDefaultPort();
        boolean bool = false;
        try {
            if (this.mContext != null) {
                WifiManager localWifiManager = (WifiManager) this.mContext
                        .getSystemService("wifi");
                if (localWifiManager != null) {
                    bool = localWifiManager.isWifiEnabled();
                    if (bool) {
                        int j = localWifiManager.getConnectionInfo()
                                .getIpAddress();
                        if (j == 0)
                            bool = false;
                    }
                }
            }
            label63: if ((!bool) && (localObject != null)) {
                localObject = new InetSocketAddress((String) localObject, i);
                this.mProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP,
                        (SocketAddress) localObject);
            }
            return;
        } catch (Exception localException) {
            // break label63;
        }
    }

    public boolean downloadUrlToFile(String paramString1, String paramString2) {
        boolean i = false;
        detectProxy();
        try {
            Object localObject1 = new URL(paramString1);
            Object localObject2 = null;
            byte[] arrayOfByte = null;
            if (this.mProxy != null) {
                localObject1 = (HttpURLConnection) ((URL) localObject1)
                        .openConnection(this.mProxy);
                ((HttpURLConnection) localObject1)
                        .setConnectTimeout(this.connectTimeout);
                ((HttpURLConnection) localObject1)
                        .setReadTimeout(this.readTimeout);
                ((HttpURLConnection) localObject1).setDoInput(true);
                ((HttpURLConnection) localObject1).connect();
                localObject1 = ((HttpURLConnection) localObject1)
                        .getInputStream();
                localObject2 = new File(paramString2);
                ((File) localObject2).createNewFile();
                localObject2 = new FileOutputStream((File) localObject2);
                arrayOfByte = new byte[1024];
            }
            while (true) {
                int j = ((InputStream) localObject1).read(arrayOfByte);
                if (j <= 0) {
                    ((FileOutputStream) localObject2).close();
                    ((InputStream) localObject1).close();
                    i = true;
                    // break label169;
                    localObject1 = (HttpURLConnection) ((URL) localObject1)
                            .openConnection();
                    break;
                }
                ((FileOutputStream) localObject2).write(arrayOfByte, 0, j);
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        label169: return i;
    }
}
