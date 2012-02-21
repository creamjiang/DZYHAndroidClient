package com.dld.coupon.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.sina.AccessToken;
import com.sina.AsyncWeiboRunner;
import com.sina.Weibo;
import com.sina.WeiboException;
import com.sina.WeiboParameters;
import com.tencent.weibo.api.T_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Cache;
import com.tencent.weibo.utils.TokenStore;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

public class SendWeiboUtils {
    private static Context context;
    private static OAuth oauth;
    private static SendWeiboUtils sendWeiboUtils = null;
    private Weibo mWeibo = null;

    private SendWeiboUtils(Context paramContext) {
        context = paramContext;
    }

    public static SendWeiboUtils getInstance(Context paramContext) {
        if (sendWeiboUtils == null)
            sendWeiboUtils = new SendWeiboUtils(paramContext);
        return sendWeiboUtils;
    }

    public static String getLocalIpAddress() {
        Object localObject2 = null;
        try {
            while (true) {
                Enumeration localEnumeration1 = NetworkInterface
                        .getNetworkInterfaces();
                while (true)
                    if (localEnumeration1.hasMoreElements()) {
                        Enumeration localEnumeration2 = ((NetworkInterface) localEnumeration1
                                .nextElement()).getInetAddresses();
                        if (!localEnumeration2.hasMoreElements())
                            continue;
                        Object localObject1 = (InetAddress) localEnumeration2
                                .nextElement();
                        if (((InetAddress) localObject1).isLoopbackAddress())
                            break;
                        localObject1 = ((InetAddress) localObject1)
                                .getHostAddress().toString();
                        localObject1 = localObject1;
                    }
            }
        } catch (SocketException local2) {
            LogUtils.e("test", local2);
            // localObject2 = null;
        }
        return (String) localObject2;
    }

    public static void initOauth() {
        Object localObject = TokenStore.fetch(ActivityManager.getCurrent());
        String str = ((String[]) localObject)[0];
        String local = ((String[]) localObject)[1];
        oauth = new OAuth("doujiao://TencentWeiboActivity");
        oauth.setOauth_token(str);
        oauth.setOauth_token_secret((String) local);
        oauth.setOauth_token(str);
        oauth.setOauth_token_secret((String) local);
    }

    private void initWeibo() {
        this.mWeibo = Weibo.getInstance();
        Object localObject = SharePersistent.getInstance().getAccessToken(
                ActivityManager.getCurrent());
        String str = ((AccessToken) localObject).getToken();
        localObject = ((AccessToken) localObject).getSecret();
        this.mWeibo.setAccessToken(new AccessToken(str, (String) localObject));
    }

    public static boolean isSendWeiboSwitchOn() {
        return true;
    }

    public static void sendTecentWeibo(String paramString) {
        initOauth();
        try {
            new T_API().add(oauth, "json", paramString, getLocalIpAddress());
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public static void update(final Weibo paramWeibo,
            final String paramString1, final String paramString2,
            final String paramString3, String paramString4)
            throws MalformedURLException, IOException, WeiboException {
        new Thread(new Runnable() {
            public void run() {
                WeiboParameters localWeiboParameters = new WeiboParameters();
                localWeiboParameters.add("source",
                        SendWeiboUtils.getLocalIpAddress());
                localWeiboParameters.add("status", paramString1);
                if (!TextUtils.isEmpty(paramString2))
                    localWeiboParameters.add("lon", paramString2);
                if (!TextUtils.isEmpty(paramString3))
                    localWeiboParameters.add("lat", paramString3);
                String str = Weibo.SERVER + "statuses/update.json";
                new AsyncWeiboRunner(paramWeibo).request(
                        SendWeiboUtils.context, str, localWeiboParameters,
                        "POST", null);
            }
        }).start();
    }

    public static void upload(final Weibo paramWeibo,
            final String paramString1, final String paramString2,
            final String paramString3, final String paramString4,
            String paramString5) throws WeiboException {
        new Thread(new Runnable() {
            public void run() {
                WeiboParameters localWeiboParameters = new WeiboParameters();
                localWeiboParameters.add("source",
                        SendWeiboUtils.getLocalIpAddress());
                localWeiboParameters.add("pic", paramString1);
                localWeiboParameters.add("status", paramString2);
                if (!TextUtils.isEmpty(paramString3))
                    localWeiboParameters.add("lon", paramString3);
                if (!TextUtils.isEmpty(paramString4))
                    localWeiboParameters.add("lat", paramString4);
                String str = Weibo.SERVER + "statuses/upload.json";
                new AsyncWeiboRunner(paramWeibo).request(
                        SendWeiboUtils.context, str, localWeiboParameters,
                        "POST", null);
            }
        }).start();
    }

    public String getWeiboFlag() {
        return SharePersistent.getInstance()
                .getPerference(context, "weibotype");
    }

    public void sendWeibo(final String paramString1, String paramString2,
            String paramString3, int paramInt) {
        // if (!"1".equals(paramString1))
        // {
        // if ("2".equals(paramString1))
        // new Thread(new Runnable()
        // {
        // public void run()
        // {
        // try
        // {
        // SendWeiboUtils.initOauth();
        // if (paramString1 != null)
        // {
        // Object localObject = null;
        // if (this.val$imgUrlType == 1)
        // localObject = "http://www.dld.com/tuan/viewimage?u=" +
        // URLEncoder.encode(this.val$imgUrl) + "&w=200&h=200";
        // while (true)
        // {
        // localObject = (byte[])Cache.getCache((String)localObject);
        // if (localObject != null)
        // break;
        // new T_API().add(SendWeiboUtils.oauth, "json", this.val$content,
        // SendWeiboUtils.getLocalIpAddress());
        // return;
        // if (this.val$imgUrlType != 2)
        // continue;
        // localObject = "http://www.dld.com/tuan/viewimage?u=" +
        // URLEncoder.encode(this.val$imgUrl) + "&w=140&h=200";
        // }
        // FileUtil.saveFile("temp", localObject, true);
        // localObject = new File(Environment.getExternalStorageDirectory(),
        // "doujiao/temp").getPath();
        // new T_API().add_pic(SendWeiboUtils.oauth, "json", this.val$content,
        // SendWeiboUtils.getLocalIpAddress(), (String)localObject);
        // }
        // }
        // catch (Exception localException)
        // {
        // localException.printStackTrace();
        // }
        // new T_API().add(SendWeiboUtils.oauth, "json", this.val$content,
        // SendWeiboUtils.getLocalIpAddress());
        // }
        // }).start();
        // }
        // else
        // new Thread()
        // {
        // public void run()
        // {
        // try
        // {
        // SendWeiboUtils.this.initWeibo();
        // String str = null;
        // if (this.val$imgUrl != null)
        // {
        // if (this.val$imgUrlType == 1)
        // str = "http://www.dld.com/tuan/viewimage?u=" +
        // URLEncoder.encode(this.val$imgUrl) + "&w=200&h=200";
        // while (true)
        // {
        // LogUtils.log("main", "run");
        // SendWeiboUtils.upload(SendWeiboUtils.this.mWeibo, Weibo.APP_KEY, str,
        // this.val$content + " @打折店网", "", "");
        // break;
        // if (this.val$imgUrlType != 2)
        // continue;
        // str = "http://www.dld.com/tuan/viewimage?u=" +
        // URLEncoder.encode(this.val$imgUrl) + "&w=140&h=200";
        // }
        // }
        // SendWeiboUtils.update(SendWeiboUtils.this.mWeibo, Weibo.APP_KEY,
        // this.val$content + "@打折店网", "", "");
        // }
        // catch (Exception localException)
        // {
        // localException.printStackTrace();
        // }
        // }
        // }
        // .run();
    }
}
