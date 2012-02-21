package com.dld.coupon.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public int id;
    public String name;
    public Integer pid;

    public static Category get(SQLiteDatabase paramSQLiteDatabase, int paramInt) {
        Category localCategory;
        if (paramInt != 255) {
            Cursor localCursor = paramSQLiteDatabase.rawQuery(
                    "SELECT * FROM category WHERE id = " + paramInt, null);
            localCursor.moveToFirst();
            localCategory = toCategory(localCursor);
            localCursor.close();
            localCategory = localCategory;
        } else {
            localCategory = new Category();
            localCategory.pid = Integer.valueOf(0);
            localCategory.id = 255;
            localCategory.name = "全部分类";
        }
        return localCategory;
    }

    public static List<Category> list(SQLiteDatabase paramSQLiteDatabase) {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(get(paramSQLiteDatabase, 255));
        Cursor localCursor = paramSQLiteDatabase.rawQuery(
                "SELECT * FROM category WHERE pid IS NULL", null);
        if (localCursor.moveToFirst())
            do
                localArrayList.add(toCategory(localCursor));
            while (localCursor.moveToNext());
        localCursor.close();
        return localArrayList;
    }

    public static List<Category> list(SQLiteDatabase paramSQLiteDatabase,
            Category paramCategory) {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(paramCategory);
        Object localObject = paramSQLiteDatabase.rawQuery(
                "SELECT * FROM category WHERE pid = " + paramCategory.id, null);
        if (((Cursor) localObject).moveToFirst())
            do
                localArrayList.add(toCategory((Cursor) localObject));
            while (((Cursor) localObject).moveToNext());
        ((Cursor) localObject).close();
        if (localArrayList.size() >= 2) {
            Category localCategory = (Category) localArrayList.get(1);
            localObject = MapUtil.getCity();
            if ((StringUtils.equals(localCategory.name, "北京菜"))
                    && (!StringUtils.equals((String) localObject, "北京")))
                localArrayList.remove(1);
        }
        return (List<Category>) localArrayList;
    }

    private static Category toCategory(Cursor paramCursor) {
        Category localCategory = new Category();
        localCategory.id = paramCursor.getInt(0);
        localCategory.pid = Integer.valueOf(paramCursor.getInt(1));
        localCategory.name = paramCursor.getString(2);
        return localCategory;
    }

    public boolean equals(Object paramObject) {
        boolean i;
        if ((!(paramObject instanceof Category))
                || (((Category) paramObject).id != this.id))
            i = false;
        else
            i = true;
        return i;
    }
}
