package com.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class Weibo {
    public static String APP_KEY;
    public static String APP_SECRET;
    public static String SERVER = "http://api.t.sina.com.cn/";
    public static String URL_ACCESS_TOKEN;
    public static String URL_AUTHENTICATION;
    public static String URL_AUTHORIZE;
    public static String URL_OAUTH_TOKEN = "http://api.t.sina.com.cn/oauth/request_token";
    private static Weibo mWeiboInstance;
    private AccessToken mAccessToken = null;
    private RequestToken mRequestToken = null;

    static {
        URL_AUTHORIZE = "http://api.t.sina.com.cn/oauth/authorize";
        URL_ACCESS_TOKEN = "http://api.t.sina.com.cn/oauth/access_token";
        URL_AUTHENTICATION = "http://api.t.sina.com.cn/oauth/authenticate";
        APP_KEY = "3514322953";
        APP_SECRET = "b962e4ae4b6ecdae3e62239d69585b39";
        mWeiboInstance = null;
    }

    private Weibo() {
        Utility.setRequestHeader("Accept-Encoding", "gzip");
        Utility.setTokenObject(this.mRequestToken);
    }

    public static Weibo getInstance() {
        if (mWeiboInstance == null)
            mWeiboInstance = new Weibo();
        return mWeiboInstance;
    }

    private void startActivitySignOn(Activity paramActivity,
            String paramString1, String paramString2) {
    }

    private void startDialogAuth(Activity paramActivity, String paramString1,
            String paramString2) {
    }

    public void addOauthverifier(String paramString) {
        this.mRequestToken.setVerifier(paramString);
    }

    public void authorizeCallBack(int paramInt1, int paramInt2,
            Intent paramIntent) {
    }

    public AccessToken generateAccessToken(Context paramContext,
            RequestToken paramRequestToken) throws WeiboException {
        Utility.setAuthorization(new AccessTokenHeader());
        Object localObject = new WeiboParameters();
        ((WeiboParameters) localObject).add("oauth_verifier",
                this.mRequestToken.getVerifier());
        ((WeiboParameters) localObject).add("source", APP_KEY);
        localObject = new AccessToken(Utility.openUrlforLogin(paramContext,
                URL_ACCESS_TOKEN, "POST", (WeiboParameters) localObject,
                this.mRequestToken));
        this.mAccessToken = ((AccessToken) localObject);
        return (AccessToken) localObject;
    }

    public AccessToken getAccessToken() {
        return this.mAccessToken;
    }

    public RequestToken getRequestToken(Context paramContext,
            String paramString1, String paramString2, String paramString3)
            throws WeiboException {
        Utility.setAuthorization(new RequestTokenHeader());
        Object localObject = new WeiboParameters();
        ((WeiboParameters) localObject).add("oauth_callback", paramString3);
        localObject = new RequestToken(Utility.openUrlforLogin(paramContext,
                URL_OAUTH_TOKEN, "POST", (WeiboParameters) localObject, null));
        this.mRequestToken = ((RequestToken) localObject);
        return (RequestToken) localObject;
    }

    public AccessToken getXauthAccessToken(Context paramContext,
            String paramString1, String paramString2, String paramString3,
            String paramString4) throws WeiboException {
        Utility.setAuthorization(new XAuthHeader());
        Object localObject = new WeiboParameters();
        ((WeiboParameters) localObject).add("x_auth_username", paramString3);
        ((WeiboParameters) localObject).add("x_auth_password", paramString4);
        ((WeiboParameters) localObject).add("oauth_consumer_key", APP_KEY);
        localObject = new AccessToken(Utility.openUrl(paramContext,
                URL_ACCESS_TOKEN, "POST", (WeiboParameters) localObject, null));
        this.mAccessToken = ((AccessToken) localObject);
        return (AccessToken) localObject;
    }

    public String request(Context paramContext, String paramString1,
            WeiboParameters paramWeiboParameters, String paramString2,
            AccessToken paramAccessToken) throws WeiboException {
        Utility.setAuthorization(new RequestHeader());
        return Utility.openUrl(paramContext, paramString1, paramString2,
                paramWeiboParameters, this.mAccessToken);
    }

    public void setAccessToken(AccessToken paramAccessToken) {
        this.mAccessToken = paramAccessToken;
    }

    public void setRequestToken(RequestToken paramRequestToken) {
        this.mRequestToken = paramRequestToken;
    }

    public void setupConsumerConfig(String paramString1, String paramString2) {
        APP_KEY = paramString1;
        APP_SECRET = paramString2;
    }

    public boolean share2weibo(Activity paramActivity, String paramString1,
            String paramString2, String paramString3, String paramString4)
            throws WeiboException {
        if (!TextUtils.isEmpty(paramString1)) {
            if (!TextUtils.isEmpty(paramString2)) {
                if ((!TextUtils.isEmpty(paramString3))
                        || (!TextUtils.isEmpty(paramString4)))
                    return true;
                throw new WeiboException("weibo content can not be null!");
            }
            throw new WeiboException("secret can not be null!");
        }
        throw new WeiboException("token can not be null!");
    }
}
