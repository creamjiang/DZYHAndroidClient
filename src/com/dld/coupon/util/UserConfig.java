package com.dld.coupon.util;

import android.content.Context;

import com.dld.android.persistent.SharePersistent;

import java.util.ArrayList;

public class UserConfig {
    private static ArrayList<String> favoriteKeywords;
    private static String locationKeyword;

    public static boolean allowUploadSina(Context paramContext) {
        boolean i;
        if (!Boolean.parseBoolean(SharePersistent.getInstance().getPerference(
                paramContext, "allow_upload_to_sina")))
            i = false;
        else
            i = true;
        return i;
    }

    // ERROR //
    public static String getChannelNum(Context paramContext) {
        // Byte code:
        // 0: aload_0
        // 1: invokevirtual 43 android/content/Context:getResources
        // ()Landroid/content/res/Resources;
        // 4: ldc 44
        // 6: invokevirtual 50 android/content/res/Resources:openRawResource
        // (I)Ljava/io/InputStream;
        // 9: astore_2
        // 10: new 52 java/io/ByteArrayOutputStream
        // 13: dup
        // 14: invokespecial 53 java/io/ByteArrayOutputStream:<init> ()V
        // 17: astore_1
        // 18: aload_2
        // 19: invokevirtual 59 java/io/InputStream:read ()I
        // 22: istore_3
        // 23: iload_3
        // 24: bipush 255
        // 26: if_icmpne +23 -> 49
        // 29: aload_2
        // 30: ifnull +7 -> 37
        // 33: aload_2
        // 34: invokevirtual 62 java/io/InputStream:close ()V
        // 37: new 64 java/lang/String
        // 40: dup
        // 41: aload_1
        // 42: invokevirtual 68 java/io/ByteArrayOutputStream:toByteArray ()[B
        // 45: invokespecial 71 java/lang/String:<init> ([B)V
        // 48: areturn
        // 49: aload_1
        // 50: iload_3
        // 51: invokevirtual 75 java/io/ByteArrayOutputStream:write (I)V
        // 54: goto -36 -> 18
        // 57: astore_3
        // 58: aload_3
        // 59: invokevirtual 78 java/io/IOException:printStackTrace ()V
        // 62: aload_2
        // 63: ifnull -26 -> 37
        // 66: aload_2
        // 67: invokevirtual 62 java/io/InputStream:close ()V
        // 70: goto -33 -> 37
        // 73: astore_2
        // 74: aload_2
        // 75: invokevirtual 78 java/io/IOException:printStackTrace ()V
        // 78: goto -41 -> 37
        // 81: astore_1
        // 82: aload_2
        // 83: ifnull +7 -> 90
        // 86: aload_2
        // 87: invokevirtual 62 java/io/InputStream:close ()V
        // 90: aload_1
        // 91: athrow
        // 92: astore_2
        // 93: aload_2
        // 94: invokevirtual 78 java/io/IOException:printStackTrace ()V
        // 97: goto -7 -> 90
        // 100: astore_2
        // 101: aload_2
        // 102: invokevirtual 78 java/io/IOException:printStackTrace ()V
        // 105: goto -68 -> 37
        //
        // Exception table:
        // from to target type
        // 18 23 57 java/io/IOException
        // 49 54 57 java/io/IOException
        // 66 70 73 java/io/IOException
        // 18 23 81 finally
        // 49 54 81 finally
        // 58 62 81 finally
        // 86 90 92 java/io/IOException
        // 33 37 100 java/io/IOException
        return null;
    }

    public static ArrayList<String> getFavoriteWord(Context paramContext) {
        Object localObject;
        if (favoriteKeywords == null) {
            favoriteKeywords = new ArrayList();
            localObject = SharePersistent.getInstance().getPerference(
                    paramContext, "user_favorite_keyword");
            if (!StringUtils.isEmpty((String) localObject)) {
                favoriteKeywords = StringUtils.stringToStringList(
                        (String) localObject, ",");
                localObject = favoriteKeywords;
            } else {
                localObject = favoriteKeywords;
            }
        } else {
            localObject = favoriteKeywords;
        }
        return (ArrayList<String>) localObject;
    }

    public static String getFavoriteWordString(Context paramContext) {
        return StringUtils.toString(getFavoriteWord(paramContext), ",")
                .replace(",", "|");
    }

    public static String getLocationKeyword(Context paramContext) {
        String str;
        if (locationKeyword == null) {
            locationKeyword = SharePersistent.getInstance().getPerference(
                    paramContext, "user_location_keyword");
            str = locationKeyword;
        } else {
            str = locationKeyword;
        }
        return str;
    }

    public static String getUserPhone(Context paramContext) {
        return SharePersistent.getInstance().getPerference(paramContext,
                "user_phone");
    }

    public static void saveFavoriteKeyword(Context paramContext,
            String paramString) {
        if (favoriteKeywords == null)
            favoriteKeywords = new ArrayList();
        if (!StringUtils.isEmpty(paramString)) {
            if (!favoriteKeywords.contains(paramString))
                favoriteKeywords.add(paramString);
            SharePersistent.getInstance().savePerference(paramContext,
                    "user_favorite_keyword",
                    StringUtils.toString(favoriteKeywords, ","));
        }
    }

    public static void saveFavoriteKeyword(Context paramContext,
            ArrayList<String> paramArrayList) {
        if (favoriteKeywords == null)
            favoriteKeywords = new ArrayList();
        for (int i = 0; i < paramArrayList.size(); i++) {
            String str = (String) paramArrayList.get(i);
            if (favoriteKeywords.contains(str))
                continue;
            favoriteKeywords.add(str);
        }
        SharePersistent.getInstance().savePerference(paramContext,
                "user_favorite_keyword",
                StringUtils.toString(favoriteKeywords, ","));
    }

    public static void saveLocationKeyword(Context paramContext,
            String paramString) {
        locationKeyword = paramString;
        SharePersistent.getInstance().savePerference(paramContext,
                "user_location_keyword", paramString);
    }

    public static void setAllowLocationService(Context paramContext,
            boolean paramBoolean) {
        SharePersistent.getInstance().savePerference(paramContext,
                "location_test", String.valueOf(paramBoolean));
    }

    public static void setAllowUploadSina(Context paramContext,
            boolean paramBoolean) {
        SharePersistent.getInstance().savePerference(paramContext,
                "allow_upload_to_sina", String.valueOf(paramBoolean));
    }
}
