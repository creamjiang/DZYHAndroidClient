package com.dld.android.persistent;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.json.User;
import com.sina.AccessToken;
import org.json.JSONObject;

public class SharePersistent {
    public static final String ACTIVATE = "activate";
    public static final String DEFAULT_PREFS_NAME = "dldclient";
    public static final String USERINFO = "userinfo";
    public static final String VERSION = "versiondata";
    private static SharePersistent instance;

    public static String get(String paramString) {
        return getInstance().getPerference(ActivityManager.getCurrent(),
                paramString);
    }

    public static boolean getActivateState(Context paramContext) {
        return paramContext.getSharedPreferences("dldclient", 0).getBoolean(
                "isActivate", false);
    }

    public static SharePersistent getInstance() {
        if (instance == null)
            instance = new SharePersistent();
        return instance;
    }

    public static String getPhone(String paramString) {
        // Object localObject;
        String str = null;
        try {
            str = get("customer_mobile");
            if (StringUtils.isEmpty(str))
                str = null;
            else
                str = (String) new JSONObject(str).get(paramString);
        } catch (Exception localException) {
            LogUtils.e("test", localException);
            str = null;
        }
        return str;
    }

    public static String getVersion(Context paramContext) {
        return paramContext.getSharedPreferences("versiondata", 0).getString(
                "version", null);
    }

    private void putStringIfNotEmpty(SharedPreferences.Editor paramEditor,
            String paramString1, String paramString2) {
        if (!StringUtils.isEmpty(paramString2))
            paramEditor.putString(paramString1, paramString2);
    }

    public static void saveActivateState(Context paramContext) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("dldclient", 0).edit();
        localEditor.putBoolean("isActivate", true);
        localEditor.commit();
    }

    public static void savePhone(String paramString1, String paramString2) {
        try {
            String localObject = get("customer_mobile");
            if (!StringUtils.isEmpty((String) localObject))
                ;
            // for (localObject = new JSONObject((String)localObject); ;
            // localObject = localObject)
            // {
            // ((JSONObject)localObject).put(paramString1, paramString2);
            // set("customer_mobile", ((JSONObject)localObject).toString());
            // break;
            // localObject = new JSONObject();
            // }
        } catch (Exception localException) {
            LogUtils.e("test", localException);
        }
    }

    public static void saveVersion(Context paramContext, String paramString) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("versiondata", 0).edit();
        localEditor.putString("version", paramString);
        localEditor.commit();
    }

    public static void set(String paramString1, String paramString2) {
        getInstance().savePerference(ActivityManager.getCurrent(),
                paramString1, paramString2);
    }

    public void clearAccessToken(Context paramContext) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("tokeninfo", 0).edit();
        localEditor.putString("accesstoken", null);
        localEditor.putString("mtokensecret", null);
        localEditor.commit();
    }

    public AccessToken getAccessToken(Context paramContext) {
        SharedPreferences localSharedPreferences = paramContext
                .getSharedPreferences("tokeninfo", 0);
        return new AccessToken(localSharedPreferences.getString("accesstoken",
                null), localSharedPreferences.getString("mtokensecret", null));
    }

    public String getPerference(Context paramContext, String paramString) {
        return paramContext.getSharedPreferences("dldclient", 0).getString(
                paramString, "");
    }

    public boolean getPerferenceBoolean(Context paramContext, String paramString) {
        return paramContext.getSharedPreferences("dldclient", 0).getBoolean(
                paramString, true);
    }

    public User getUserInfo(Context paramContext) {
        SharedPreferences localSharedPreferences = paramContext
                .getSharedPreferences("userinfo", 0);
        User localUser = new User();
        localUser.username = localSharedPreferences.getString("username", "");
        localUser.password = localSharedPreferences.getString("password", "");
        localUser.mobile = localSharedPreferences.getString("mobile", null);
        localUser.id = localSharedPreferences.getString("user_id", null);
        localUser.email = localSharedPreferences.getString("email", "");
        localUser.mobile = localSharedPreferences.getString("mobile", "");
        localUser.customername = localSharedPreferences.getString(
                "customername", "");
        localUser.cityname = localSharedPreferences.getString("cityname", "");
        localUser.birthday = localSharedPreferences.getString("birthday", "");
        localUser.intrests = localSharedPreferences.getString("intrests", "");
        localUser.cards = localSharedPreferences.getString("cards", "");
        localUser.profile = localSharedPreferences.getString("profile", "");
        return localUser;
    }

    public void removePerference(Context paramContext, String paramString) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("dldclient", 0).edit();
        localEditor.remove(paramString);
        localEditor.commit();
    }

    public void saveAccessToken(Context paramContext,
            AccessToken paramAccessToken) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("tokeninfo", 0).edit();
        localEditor.putString("accesstoken", paramAccessToken.getToken());
        localEditor.putString("mtokensecret", paramAccessToken.getSecret());
        localEditor.commit();
    }

    public void savePerference(Context paramContext, String paramString1,
            String paramString2) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("dldclient", 0).edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.commit();
    }

    public void savePerference(Context paramContext, String paramString1,
            String paramString2, String paramString3, String paramString4) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("dldclient", 0).edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.putString(paramString3, paramString4);
        localEditor.commit();
    }

    public void savePerference(Context paramContext, String paramString,
            boolean paramBoolean) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("dldclient", 0).edit();
        localEditor.putBoolean(paramString, paramBoolean);
        localEditor.commit();
    }

    public void saveUserInfo(Context paramContext, User paramUser) {
        LogUtils.log("test", paramUser);
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences("userinfo", 0).edit();
        putStringIfNotEmpty(localEditor, "username", paramUser.username);
        putStringIfNotEmpty(localEditor, "password", paramUser.password);
        putStringIfNotEmpty(localEditor, "mobile", paramUser.mobile);
        putStringIfNotEmpty(localEditor, "user_id", paramUser.id);
        putStringIfNotEmpty(localEditor, "email", paramUser.email);
        putStringIfNotEmpty(localEditor, "mobile", paramUser.mobile);
        putStringIfNotEmpty(localEditor, "customername", paramUser.customername);
        putStringIfNotEmpty(localEditor, "cityname", paramUser.cityname);
        putStringIfNotEmpty(localEditor, "birthday", paramUser.birthday);
        putStringIfNotEmpty(localEditor, "intrests", paramUser.intrests);
        putStringIfNotEmpty(localEditor, "cards", paramUser.cards);
        putStringIfNotEmpty(localEditor, "profile", paramUser.profile);
        localEditor.commit();
    }
}