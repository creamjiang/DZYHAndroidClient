package com.tencent.weibo.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.tencent.weibo.beans.OAuth;

public class TokenStore {
    public static String fileName = "token_store";

    public static void clear(Activity paramActivity) {
        SharedPreferences.Editor localEditor = paramActivity
                .getSharedPreferences(fileName, 0).edit();
        localEditor.clear();
        localEditor.commit();
    }

    public static String[] fetch(Activity paramActivity) {
        Object localObject = paramActivity.getSharedPreferences(fileName, 0);
        String str1 = ((SharedPreferences) localObject).getString(
                "oauth_token", null);
        String str2 = ((SharedPreferences) localObject).getString(
                "oauth_token_secret", null);
        String[] s = new String[2];
        s[0] = str1;
        s[1] = str2;
        return s;
    }

    public static void store(Activity paramActivity, OAuth paramOAuth) {
        SharedPreferences.Editor localEditor = paramActivity
                .getSharedPreferences(fileName, 0).edit();
        localEditor.putString("oauth_token", paramOAuth.getOauth_token());
        localEditor.putString("oauth_token_secret",
                paramOAuth.getOauth_token_secret());
        localEditor.commit();
    }
}
