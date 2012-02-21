package com.dld.coupon.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Address {
    private static List<String> list;

    public static List<String> getAddresses(SQLiteDatabase paramSQLiteDatabase,
            String paramString1, String paramString2) {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("全" + paramString2);
        Object[] localObject1 = new String[1];
        localObject1[0] = "name";
        Object[] localObject2 = new String[2];
        localObject2[0] = paramString1;
        localObject2[1] = paramString2;
        Cursor cursor = paramSQLiteDatabase.query("address",
                (String[]) localObject1, "city = ? AND district = ?",
                (String[]) localObject2, null, null, null);
        if (cursor.moveToFirst())
            ;
        try {
            do {
                localObject2[0] = cursor.getString(0);
                if (localArrayList.contains(localObject2))
                    continue;
                localArrayList.add(localObject2);
            } while (cursor.moveToNext());
            cursor.close();
            return localArrayList;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    public static String getDistrict(SQLiteDatabase paramSQLiteDatabase,
            String paramString1, String paramString2) {
        String str = null;
        Cursor localCursor = paramSQLiteDatabase.rawQuery(
                "SELECT district FROM address WHERE city = '" + paramString1
                        + "' AND name = '" + paramString2 + "'", null);
        LogUtils.log("test", "SELECT district FROM address WHERE city = '"
                + paramString1 + "' AND name = '" + paramString2 + "'");
        if (localCursor.moveToFirst())
            str = localCursor.getString(0);
        localCursor.close();
        return str;
    }

    public static List<String> getDistricts(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        list = new ArrayList();
        list.add("热门商圈");
        Object[] localObject1 = new String[1];
        localObject1[0] = "district";
        Object[] localObject2 = new String[1];
        localObject2[0] = paramString;
        Cursor cursor = paramSQLiteDatabase.query("address",
                (String[]) localObject1, "city = ?", (String[]) localObject2,
                "district", null, null);
        if (cursor.moveToFirst())
            ;
        try {
            do {
                localObject2[0] = cursor.getString(0);
                list.add((String) localObject2[0]);
            } while (cursor.moveToNext());
            cursor.close();
            return list;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    public static List<String> getHotAddresses(
            SQLiteDatabase paramSQLiteDatabase, String paramString) {
        ArrayList localArrayList;
        if (paramString.equals("北京")) {
            localArrayList = new ArrayList();
            localArrayList.add("全北京 ");
            localArrayList.add("和平里");
            localArrayList.add("王府井");
            localArrayList.add("建国门");
            localArrayList.add("东单");
            localArrayList.add("西单");
            localArrayList.add("朝阳门");
            localArrayList.add("菜市口");
            localArrayList.add("崇文门 ");
            localArrayList.add("三里屯");
            localArrayList.add("国贸 ");
            localArrayList.add("双井 ");
            localArrayList.add("工体北门 ");
            localArrayList = localArrayList;
        }
        else
        {
            // return localArrayList;
            localArrayList = new ArrayList();
            localArrayList.add("全" + paramString);
            Object[] localObject = new String[1];
            localObject[0] = "name";
            String[] arrayOfString = new String[1];
            arrayOfString[0] = paramString;
            Cursor cursor = paramSQLiteDatabase.query("address",
                    (String[]) localObject, "city = ?", arrayOfString, null,
                    null, null, "9");
            if (cursor.moveToFirst())
                ;
            try {
                do
                    localArrayList.add(cursor.getString(0));
                while (cursor.moveToNext());
                cursor.close();
                localArrayList = localArrayList;
            } catch (Exception localException) {
                // while (true)
                LogUtils.e("test", "", localException);
            }
        }
        return localArrayList;
    }

    public static void init(Context paramContext,
            SQLiteDatabase paramSQLiteDatabase) {
        try {
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(paramContext.getResources()
                            .openRawResource(R.raw.address), "utf-8"));
            while (true) {
                String str = localBufferedReader.readLine();
                if (str == null)
                    break;
                paramSQLiteDatabase.execSQL(str);
            }
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }
}
