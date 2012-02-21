package com.dld.android.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.System;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import org.apache.http.HttpHost;

public class HttpUtil {
    public static final String GET = "GET";
    public static final String POST = "POST";

    public static boolean checkNet(Context paramContext) {
        boolean bool = false;
        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
                .getSystemService("connectivity");
        if (localConnectivityManager.getActiveNetworkInfo() != null)
            bool = localConnectivityManager.getActiveNetworkInfo()
                    .isAvailable();
        return bool;
    }

    public static String getAccessPoint(Context paramContext) {
        Object localObject;
        if (!Settings.System.getString(paramContext.getContentResolver(),
                "wifi_on").equals("1")) {
            localObject = ((ConnectivityManager) paramContext
                    .getSystemService("connectivity")).getActiveNetworkInfo();
            if (localObject == null) {
                localObject = "null";
            } else {
                localObject = ((NetworkInfo) localObject).getExtraInfo();
                if (localObject == null)
                    localObject = "null";
            }
        } else {
            localObject = "WIFI";
        }
        return (String) localObject;
    }

    public static String getApnName(Context paramContext) {
        return ((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getActiveNetworkInfo()
                .getTypeName();
    }

    public static Proxy getApnProxy(Context paramContext) {
        Proxy localProxy = null;
        if (!((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getNetworkInfo(1)
                .isConnected()) {
            Cursor localCursor = getCurrentApn(paramContext);
            String str1 = null;
            String str2 = null;
            if (localCursor.getCount() > 0) {
                localCursor.moveToFirst();
                localCursor.getLong(localCursor.getColumnIndex("_id"));
                str1 = localCursor.getString(localCursor
                        .getColumnIndex("proxy"));
                str2 = localCursor
                        .getString(localCursor.getColumnIndex("port"));
                localCursor.close();
                if ((str1 != null) && (!str1.equals("")))
                    ;
            } else {
                localCursor.close();
                // break label165;
                return localProxy;
            }
            if ((str2 != null) && (!str2.equals("")))
                localProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        str1, Integer.valueOf(str2).intValue()));
        }
        label165: return localProxy;
    }

    public static HttpURLConnection getConnection(String paramString,
            Context paramContext) {
        Object localObject2 = null;
        try {
            URL localURL = new URL(paramString);
            Object localObject1 = getApnProxy(paramContext);
            if (localObject1 != null)
                localObject1 = (HttpURLConnection) localURL
                        .openConnection((Proxy) localObject1);
            else
                localObject1 = (HttpURLConnection) localURL.openConnection();
        } catch (Exception local) {
            local.printStackTrace();
            localObject2 = null;
        }
        return (HttpURLConnection) localObject2;
    }

    public static Cursor getCurrentApn(Context paramContext) {
        return paramContext.getContentResolver().query(
                Uri.parse("content://telephony/carriers/preferapn"), null,
                null, null, null);
    }

    public static InputStream getInputStreamByUrl(String paramString,
            Context paramContext) throws Exception {
        Object localObject;
        if ((paramString == null) || (!paramString.equals(""))) {
            URL localURL = new URL(paramString);
            localObject = getApnProxy(paramContext);
            if (localObject != null)
                localObject = (HttpURLConnection) localURL
                        .openConnection((Proxy) localObject);
            else
                localObject = (HttpURLConnection) localURL.openConnection();
            ((HttpURLConnection) localObject).setDoOutput(true);
            ((HttpURLConnection) localObject).setDoInput(true);
            localObject = ((HttpURLConnection) localObject).getInputStream();
        } else {
            localObject = null;
        }
        return (InputStream) localObject;
    }

    public static HttpHost getProxyToHttpClient(Context paramContext) {
        HttpHost localHttpHost = null;
        if (!((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getNetworkInfo(1)
                .isConnected()) {
            Cursor localCursor = getCurrentApn(paramContext);
            String str1 = null;
            String str2 = null;
            if (localCursor.getCount() > 0) {
                localCursor.moveToFirst();
                localCursor.getLong(localCursor.getColumnIndex("_id"));
                str1 = localCursor.getString(localCursor
                        .getColumnIndex("proxy"));
                str2 = localCursor
                        .getString(localCursor.getColumnIndex("port"));
                localCursor.close();
                if ((str1 != null) && (!str1.equals("")))
                    ;
            } else {
                localCursor.close();
                // break label145;
                return localHttpHost;
            }
            if ((str2 != null) && (!str2.equals("")))
                localHttpHost = new HttpHost(str1, Integer.parseInt(str2));
        }
        label145: return localHttpHost;
    }

    public static Bitmap getUrlImage(String paramString, Context paramContext)
            throws Exception {
        Object localObject1 = null;
        if ((paramString == null) || (!paramString.equals(""))) {
            localObject1 = new URL(paramString);
            Object localObject2 = getApnProxy(paramContext);
            HttpURLConnection localHttpURLConnection;
            if (localObject2 != null)
                localHttpURLConnection = (HttpURLConnection) ((URL) localObject1)
                        .openConnection((Proxy) localObject2);
            else
                localHttpURLConnection = (HttpURLConnection) ((URL) localObject1)
                        .openConnection();
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setDoInput(true);
            localObject1 = localHttpURLConnection.getInputStream();
            localObject2 = new ByteArrayOutputStream();
            for (int i = ((InputStream) localObject1).read(); i != -1; i = ((InputStream) localObject1)
                    .read())
                ((ByteArrayOutputStream) localObject2).write(i);
            Object localObject3 = ((ByteArrayOutputStream) localObject2)
                    .toByteArray();
            localObject3 = BitmapFactory.decodeByteArray((byte[]) localObject3,
                    0, ((byte[]) localObject3).length);
            ((ByteArrayOutputStream) localObject2).close();
            // ((byte[])null);
            if (localObject1 != null)
                ((InputStream) localObject1).close();
            if (localHttpURLConnection != null)
                localHttpURLConnection.disconnect();
            localObject1 = localObject3;
        }
        return (Bitmap) (Bitmap) (Bitmap) localObject1;
    }

    public static boolean isWifi(Context paramContext) {
        int i = 1;
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getActiveNetworkInfo();
        if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()))
            i = 0;
        else if (localNetworkInfo.getType() != i)
            i = 0;
        return i == 0 ? false : true;
    }

    public boolean isOnline(Context paramContext) {
        return ((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getActiveNetworkInfo()
                .isConnectedOrConnecting();
    }
}
