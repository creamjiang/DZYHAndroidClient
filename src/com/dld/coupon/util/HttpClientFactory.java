package com.dld.coupon.util;

import android.content.Context;

import com.dld.android.net.HttpUtil;
import com.dld.android.util.Device;
import com.dld.coupon.activity.ActivityManager;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {
    public static HttpClient create() {
        DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
        Object localObject = ActivityManager.getCurrent();
        String str = Device.getInstance().getNetType((Context) localObject);
        if ((!StringUtils.isEmpty(str)) && (str.equalsIgnoreCase("cmwap"))) {
            localObject = HttpUtil.getProxyToHttpClient((Context) localObject);
            localDefaultHttpClient.getParams().setParameter(
                    "http.route.default-proxy", localObject);
        }
        return (HttpClient) localDefaultHttpClient;
    }
}
