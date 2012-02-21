package com.dld.coupon.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.Serializable;
import java.util.ArrayList;

public class CustomerBank implements Serializable {
    private static final long serialVersionUID = 1L;
    public String accountHolder;
    public String address;
    public String bankcardNumber;
    public String depositBank;
    public Integer id;
    public String identityId;
    public String mobile;
    public String totalInfo;
    public Integer type;

    public CustomerBank(Integer paramInteger1, String paramString1,
            String paramString2, String paramString3, String paramString4,
            String paramString5, String paramString6, String paramString7,
            Integer paramInteger2) {
        this.type = paramInteger1;
        this.address = paramString1;
        this.accountHolder = paramString2;
        this.bankcardNumber = paramString3;
        this.depositBank = paramString4;
        this.identityId = paramString5;
        this.totalInfo = paramString6;
        this.mobile = paramString7;
        this.id = paramInteger2;
    }

    public static void deleteAndInsertBank(SQLiteDatabase paramSQLiteDatabase,
            CustomerBank paramCustomerBank) {
        paramSQLiteDatabase.beginTransaction();
        try {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = paramCustomerBank.id;
            paramSQLiteDatabase.execSQL("delete from userbankinfo where id=?",
                    arrayOfObject);
            arrayOfObject = new Object[9];
            arrayOfObject[0] = paramCustomerBank.type;
            arrayOfObject[1] = paramCustomerBank.address;
            arrayOfObject[2] = paramCustomerBank.accountHolder;
            arrayOfObject[3] = paramCustomerBank.bankcardNumber;
            arrayOfObject[4] = paramCustomerBank.depositBank;
            arrayOfObject[5] = paramCustomerBank.identityId;
            arrayOfObject[6] = paramCustomerBank.totalInfo;
            arrayOfObject[7] = paramCustomerBank.mobile;
            arrayOfObject[8] = paramCustomerBank.id;
            paramSQLiteDatabase
                    .execSQL(
                            "insert into userbankinfo(type,address,accountHolder,bankcardNumber,depositBank,identityId,totalInfo,mobile,id) values(?,?,?,?,?,?,?,?,?)",
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
            CustomerBank paramCustomerBank) {
        paramSQLiteDatabase.beginTransaction();
        try {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = new Integer(1);
            paramSQLiteDatabase.execSQL("delete from userbankinfo where id=?",
                    arrayOfObject);
            arrayOfObject = new Object[9];
            arrayOfObject[0] = paramCustomerBank.type;
            arrayOfObject[1] = paramCustomerBank.address;
            arrayOfObject[2] = paramCustomerBank.accountHolder;
            arrayOfObject[3] = paramCustomerBank.bankcardNumber;
            arrayOfObject[4] = paramCustomerBank.depositBank;
            arrayOfObject[5] = paramCustomerBank.identityId;
            arrayOfObject[6] = paramCustomerBank.totalInfo;
            arrayOfObject[7] = paramCustomerBank.mobile;
            arrayOfObject[8] = paramCustomerBank.id;
            paramSQLiteDatabase
                    .execSQL(
                            "insert into userbankinfo(type,address,accountHolder,bankcardNumber,depositBank,identityId,totalInfo,mobile,id) values(?,?,?,?,?,?,?,?,?)",
                            arrayOfObject);
            paramSQLiteDatabase.execSQL("update userbankinfo set id=id-1",
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

    public static ArrayList<CustomerBank> getAllUserBankInfo(
            SQLiteDatabase paramSQLiteDatabase) {
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = paramSQLiteDatabase.rawQuery(
                "select * from userbankinfo order by id asc", null);
        try {
            // while (true)
            {
                boolean bool = localCursor.moveToNext();
                if (!bool) {
                    if (localCursor != null)
                        localCursor.close();
                    localCursor.close();
                    // return localArrayList;
                }
                Integer localInteger2 = Integer.valueOf(Integer
                        .parseInt(localCursor.getString(localCursor
                                .getColumnIndex("type"))));
                String str5 = localCursor.getString(localCursor
                        .getColumnIndex("address"));
                String str1 = localCursor.getString(localCursor
                        .getColumnIndex("accountHolder"));
                String str3 = localCursor.getString(localCursor
                        .getColumnIndex("bankcardNumber"));
                String str6 = localCursor.getString(localCursor
                        .getColumnIndex("depositBank"));
                String str4 = localCursor.getString(localCursor
                        .getColumnIndex("identityId"));
                String str2 = localCursor.getString(localCursor
                        .getColumnIndex("totalInfo"));
                Integer localInteger1 = Integer.valueOf(Integer
                        .parseInt(localCursor.getString(localCursor
                                .getColumnIndex("id"))));
                localArrayList.add(new CustomerBank(localInteger2, str5, str1,
                        str3, str6, str4, str2,
                        localCursor.getString(localCursor
                                .getColumnIndex("mobile")), localInteger1));
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

    public static int getUserBankInfoCount(SQLiteDatabase paramSQLiteDatabase) {
        int j = 0;
        Cursor localCursor = null;
        try {
            localCursor = paramSQLiteDatabase.rawQuery(
                    "select count(*) from userbankinfo", null);
            localCursor.moveToFirst();
            int i = localCursor.getInt(0);
            j = i;
            // return j;
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                if (localCursor != null)
                    localCursor.close();
                // continue;
            }
        } finally {
            if (localCursor != null)
                localCursor.close();
        }
        return j;
        // throw localObject;
    }

    public static void initUserBankTable(SQLiteDatabase paramSQLiteDatabase) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("create table ").append("if not exists ")
                .append("userbankinfo").append(" (").append("type Integer,")
                .append("address text,").append("accountHolder text,")
                .append("bankcardNumber text,").append("depositBank text,")
                .append("identityId text,").append("totalInfo text,")
                .append("mobile text,").append("id Integer").append(")");
        paramSQLiteDatabase.execSQL(localStringBuffer.toString());
    }

    public static void saveUserBankInfo(SQLiteDatabase paramSQLiteDatabase,
            CustomerBank paramCustomerBank) {
        Object[] arrayOfObject = new Object[9];
        arrayOfObject[0] = paramCustomerBank.type;
        arrayOfObject[1] = paramCustomerBank.address;
        arrayOfObject[2] = paramCustomerBank.accountHolder;
        arrayOfObject[3] = paramCustomerBank.bankcardNumber;
        arrayOfObject[4] = paramCustomerBank.depositBank;
        arrayOfObject[5] = paramCustomerBank.identityId;
        arrayOfObject[6] = paramCustomerBank.totalInfo;
        arrayOfObject[7] = paramCustomerBank.mobile;
        arrayOfObject[8] = paramCustomerBank.id;
        paramSQLiteDatabase
                .execSQL(
                        "insert into userbankinfo(type,address,accountHolder,bankcardNumber,depositBank,identityId,totalInfo,mobile,id) values(?,?,?,?,?,?,?,?,?)",
                        arrayOfObject);
    }
}
