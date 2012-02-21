package com.dld.coupon.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.Serializable;
import java.util.ArrayList;

public class MailingAddress implements Serializable {
    public static final String DB_TABLENAME = "mailaddress";
    private static final long serialVersionUID = 1L;
    public String city;
    public String detailAddress;
    public int id;
    public String partAddress;
    public String province;
    public String receiverMobile;
    public String receiverName;
    public String zipCode;

    public MailingAddress() {
    }

    public MailingAddress(String paramString1, String paramString2,
            String paramString3, String paramString4, String paramString5,
            String paramString6, String paramString7, int paramInt) {
        this.province = paramString1;
        this.city = paramString2;
        this.partAddress = paramString3;
        this.detailAddress = paramString4;
        this.zipCode = paramString5;
        this.receiverName = paramString6;
        this.receiverMobile = paramString7;
        this.id = paramInt;
    }

    public static void delete(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL("delete from mailaddress where id=1",
                new Object[0]);
    }

    public static void deleteAndInsert(SQLiteDatabase paramSQLiteDatabase,
            MailingAddress paramMailingAddress) {
        paramSQLiteDatabase.beginTransaction();
        try {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = new Integer(paramMailingAddress.id);
            paramSQLiteDatabase.execSQL("delete from mailaddress where id=?",
                    arrayOfObject);
            arrayOfObject = new Object[8];
            arrayOfObject[0] = paramMailingAddress.province;
            arrayOfObject[1] = paramMailingAddress.city;
            arrayOfObject[2] = paramMailingAddress.partAddress;
            arrayOfObject[3] = paramMailingAddress.detailAddress;
            arrayOfObject[4] = paramMailingAddress.zipCode;
            arrayOfObject[5] = paramMailingAddress.receiverName;
            arrayOfObject[6] = paramMailingAddress.receiverMobile;
            arrayOfObject[7] = Integer.valueOf(paramMailingAddress.id);
            paramSQLiteDatabase
                    .execSQL(
                            "insert into mailaddress(province,city,partAddress,detialAddress,zipCode,receiverName,receiverMobile,id) values(?,?,?,?,?,?,?,?)",
                            arrayOfObject);
            paramSQLiteDatabase.setTransactionSuccessful();
            return;
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                paramSQLiteDatabase.endTransaction();
            }
        } finally {
            paramSQLiteDatabase.endTransaction();
        }
        // throw localObject;
    }

    public static void deleteAndUpdate(SQLiteDatabase paramSQLiteDatabase,
            MailingAddress paramMailingAddress) {
        paramSQLiteDatabase.beginTransaction();
        try {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = new Integer(1);
            paramSQLiteDatabase.execSQL("delete from mailaddress where id=?",
                    arrayOfObject);
            arrayOfObject = new Object[8];
            arrayOfObject[0] = paramMailingAddress.province;
            arrayOfObject[1] = paramMailingAddress.city;
            arrayOfObject[2] = paramMailingAddress.partAddress;
            arrayOfObject[3] = paramMailingAddress.detailAddress;
            arrayOfObject[4] = paramMailingAddress.zipCode;
            arrayOfObject[5] = paramMailingAddress.receiverName;
            arrayOfObject[6] = paramMailingAddress.receiverMobile;
            arrayOfObject[7] = Integer.valueOf(paramMailingAddress.id);
            paramSQLiteDatabase
                    .execSQL(
                            "insert into mailaddress(province,city,partAddress,detialAddress,zipCode,receiverName,receiverMobile,id) values(?,?,?,?,?,?,?,?)",
                            arrayOfObject);
            paramSQLiteDatabase.execSQL("update mailaddress set id=id-1",
                    new Object[0]);
            paramSQLiteDatabase.setTransactionSuccessful();
            return;
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                paramSQLiteDatabase.endTransaction();
            }
        } finally {
            paramSQLiteDatabase.endTransaction();
        }
        // throw localObject;
    }

    public static ArrayList<MailingAddress> getAllMailAddress(
            SQLiteDatabase paramSQLiteDatabase) {
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = paramSQLiteDatabase.rawQuery(
                "select * from mailaddress order by id asc", null);
        try {
            while (true) {
                boolean bool = localCursor.moveToNext();
                if (!bool) {
                    if (localCursor != null)
                        localCursor.close();
                    localCursor.close();
                    // return localArrayList;
                }
                localArrayList.add(new MailingAddress(localCursor
                        .getString(localCursor.getColumnIndex("province")),
                        localCursor.getString(localCursor
                                .getColumnIndex("city")), localCursor
                                .getString(localCursor
                                        .getColumnIndex("partAddress")),
                        localCursor.getString(localCursor
                                .getColumnIndex("detialAddress")), localCursor
                                .getString(localCursor
                                        .getColumnIndex("zipCode")),
                        localCursor.getString(localCursor
                                .getColumnIndex("receiverName")), localCursor
                                .getString(localCursor
                                        .getColumnIndex("receiverMobile")),
                        Integer.valueOf(
                                Integer.parseInt(localCursor
                                        .getString(localCursor
                                                .getColumnIndex("id"))))
                                .intValue()));
            }
        } catch (Exception localException) {
            // while (true)
            {
                if (localCursor != null)
                    localCursor.close();
                // continue;
            }
        } finally {
            if (localCursor != null)
                localCursor.close();
        }
        return localArrayList;
        // throw localObject;
    }

    public static int getMailAddCount(SQLiteDatabase paramSQLiteDatabase) {
        int i = 0;
        Cursor localCursor = null;
        try {
            localCursor = paramSQLiteDatabase.rawQuery(
                    "select count(*) from mailaddress", null);
            localCursor.moveToFirst();
            i = localCursor.getInt(0);
            i = i;
            // return i;
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                if (localCursor == null)
                    localCursor.close();
                // continue;
            }
        } finally {
            if (localCursor != null)
                localCursor.close();
        }
        return i;
        // throw localObject;
    }

    public static void initMailAddress(SQLiteDatabase paramSQLiteDatabase) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("create table ").append("if not exists ")
                .append("mailaddress").append(" (").append("province text,")
                .append("city text,").append("partAddress text,")
                .append("detialAddress text,").append("zipCode text,")
                .append("receiverName text,").append("receiverMobile text,")
                .append("id Integer").append(")");
        paramSQLiteDatabase.execSQL(localStringBuffer.toString());
    }

    public static void saveMailAddress(SQLiteDatabase paramSQLiteDatabase,
            MailingAddress paramMailingAddress) {
        Object[] arrayOfObject = new Object[8];
        arrayOfObject[0] = paramMailingAddress.province;
        arrayOfObject[1] = paramMailingAddress.city;
        arrayOfObject[2] = paramMailingAddress.partAddress;
        arrayOfObject[3] = paramMailingAddress.detailAddress;
        arrayOfObject[4] = paramMailingAddress.zipCode;
        arrayOfObject[5] = paramMailingAddress.receiverName;
        arrayOfObject[6] = paramMailingAddress.receiverMobile;
        arrayOfObject[7] = Integer.valueOf(paramMailingAddress.id);
        paramSQLiteDatabase
                .execSQL(
                        "insert into mailaddress(province,city,partAddress,detialAddress,zipCode,receiverName,receiverMobile,id) values(?,?,?,?,?,?,?,?)",
                        arrayOfObject);
    }

    public static void update(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL("update mailaddress set id=id-1",
                new Object[0]);
    }

    public String getCity() {
        return this.city;
    }

    public String getDetailAddress() {
        return this.detailAddress;
    }

    public String getPartAddress() {
        return this.partAddress;
    }

    public String getProvince() {
        return this.province;
    }

    public String getReceiverMobile() {
        return this.receiverMobile;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setCity(String paramString) {
        this.city = paramString;
    }

    public void setDetailAddress(String paramString) {
        this.detailAddress = paramString;
    }

    public void setPartAddress(String paramString) {
        this.partAddress = paramString;
    }

    public void setProvince(String paramString) {
        this.province = paramString;
    }

    public void setReceiverMobile(String paramString) {
        this.receiverMobile = paramString;
    }

    public void setReceiverName(String paramString) {
        this.receiverName = paramString;
    }

    public void setZipCode(String paramString) {
        this.zipCode = paramString;
    }
}
