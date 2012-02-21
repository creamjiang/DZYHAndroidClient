package com.dld.coupon.db;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.dld.android.util.LogUtils;
import com.dld.android.util.ReflectionFactory;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.Detail;
import com.dld.protocol.json.DetailDelete;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.Ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class Fav {
    public static final int BANK = 3;
    public static final int COUPON = 1;
    public static final int GROUP = 2;
    public static final int STORE = 0;
    public static final int TICKET = 4;

    public static void delete(SQLiteDatabase paramSQLiteDatabase, int paramInt) {
        LogUtils.log("main", paramInt + "delete id");
        String[] arrayOfString = new String[1];
        arrayOfString[0] = String.valueOf(paramInt);
        paramSQLiteDatabase.delete("fav", "id = ?", arrayOfString);
    }

    public static void deleteSyn(SQLiteDatabase paramSQLiteDatabase,
            List<DetailDelete> paramList) {
    }

    public static String getAllStores(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        String str = null;
        StringBuffer localStringBuffer = new StringBuffer();
        Object localObject = paramSQLiteDatabase.rawQuery("SELECT * FROM fav "
                + paramString + " ORDER BY id DESC", null);
        if (((Cursor) localObject).moveToFirst())
            ;
        try {
            int i = ((Cursor) localObject).getInt(1);
            JSONObject localJSONObject = new JSONObject(
                    ((Cursor) localObject).getString(2));
            switch (i) {
            default:
            case 0:
                while (!((Cursor) localObject).moveToNext()) {
                    ((Cursor) localObject).close();
                    localObject = localStringBuffer.toString();
                    if (((String) localObject).length() != 0)
                        // break label194;
                        return str;
                    localStringBuffer.append(
                            ((CouponDetail) ReflectionFactory.create(
                                    localJSONObject, CouponDetail.class)).id)
                            .append(",");
                }
            case 1:
            }
        } catch (Exception localException) {
            // while (true)
            {
                LogUtils.e("test", "", localException);
                // continue;
                // localStringBuffer.append(((CouponDetail)ReflectionFactory.create(localException,
                // CouponDetail.class)).id).append(",");
                // continue;
                label194: str = ((String) localObject).substring(0, -1
                        + ((String) localObject).length());
            }
        }
        return str;
    }

    // ERROR //
    public static String getCanByGroupString(
            SQLiteDatabase paramSQLiteDatabase, String paramString) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_3
        // 2: aconst_null
        // 3: astore_2
        // 4: new 67 java/lang/StringBuffer
        // 7: dup
        // 8: invokespecial 68 java/lang/StringBuffer:<init> ()V
        // 11: astore 4
        // 13: aload_0
        // 14: new 25 java/lang/StringBuilder
        // 17: dup
        // 18: ldc 70
        // 20: invokespecial 34 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 23: aload_1
        // 24: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 27: ldc 72
        // 29: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 32: invokevirtual 44 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 35: aconst_null
        // 36: invokevirtual 76 android/database/sqlite/SQLiteDatabase:rawQuery
        // (Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        // 39: astore_2
        // 40: aload_2
        // 41: invokeinterface 82 1 0
        // 46: istore 5
        // 48: iload 5
        // 50: ifeq +67 -> 117
        // 53: new 88 org/json/JSONObject
        // 56: dup
        // 57: aload_2
        // 58: iconst_2
        // 59: invokeinterface 91 2 0
        // 64: invokespecial 92 org/json/JSONObject:<init> (Ljava/lang/String;)V
        // 67: ldc 135
        // 69: invokestatic 111
        // com/doujiao/android/util/ReflectionFactory:create
        // (Lorg/json/JSONObject;Ljava/lang/Class;)Ljava/lang/Object;
        // 72: checkcast 135 com/doujiao/protocol/json/GroupDetail
        // 75: astore 5
        // 77: aload 5
        // 79: getfield 137 com/doujiao/protocol/json/GroupDetail:id I
        // 82: ifle +22 -> 104
        // 85: aload 4
        // 87: aload 5
        // 89: getfield 137 com/doujiao/protocol/json/GroupDetail:id I
        // 92: invokevirtual 140 java/lang/StringBuffer:append
        // (I)Ljava/lang/StringBuffer;
        // 95: pop
        // 96: aload 4
        // 98: ldc 120
        // 100: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 103: pop
        // 104: aload_2
        // 105: invokeinterface 95 1 0
        // 110: istore 5
        // 112: iload 5
        // 114: ifne -61 -> 53
        // 117: aload_2
        // 118: ifnull +9 -> 127
        // 121: aload_2
        // 122: invokeinterface 98 1 0
        // 127: aload 4
        // 129: invokevirtual 99 java/lang/StringBuffer:toString
        // ()Ljava/lang/String;
        // 132: astore_2
        // 133: aload_2
        // 134: invokevirtual 103 java/lang/String:length ()I
        // 137: ifne +50 -> 187
        // 140: aload_3
        // 141: areturn
        // 142: astore 5
        // 144: ldc 122
        // 146: ldc 124
        // 148: aload 5
        // 150: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 153: goto -49 -> 104
        // 156: pop
        // 157: aload_2
        // 158: ifnull -31 -> 127
        // 161: aload_2
        // 162: invokeinterface 98 1 0
        // 167: goto -40 -> 127
        // 170: pop
        // 171: goto -44 -> 127
        // 174: astore_3
        // 175: aload_2
        // 176: ifnull +9 -> 185
        // 179: aload_2
        // 180: invokeinterface 98 1 0
        // 185: aload_3
        // 186: athrow
        // 187: aload_2
        // 188: iconst_0
        // 189: bipush 255
        // 191: aload_2
        // 192: invokevirtual 103 java/lang/String:length ()I
        // 195: iadd
        // 196: invokevirtual 132 java/lang/String:substring
        // (II)Ljava/lang/String;
        // 199: astore_3
        // 200: goto -60 -> 140
        // 203: pop
        // 204: goto -19 -> 185
        // 207: pop
        // 208: goto -81 -> 127
        //
        // Exception table:
        // from to target type
        // 53 104 142 java/lang/Exception
        // 13 48 156 java/lang/Exception
        // 104 112 156 java/lang/Exception
        // 144 153 156 java/lang/Exception
        // 161 167 170 java/lang/Exception
        // 13 48 174 finally
        // 53 104 174 finally
        // 104 112 174 finally
        // 144 153 174 finally
        // 179 185 203 java/lang/Exception
        // 121 127 207 java/lang/Exception
        return null;
    }

    public static long getCount(SQLiteDatabase paramSQLiteDatabase) {
        return DatabaseUtils.queryNumEntries(paramSQLiteDatabase, "fav");
    }

    // ERROR //
    public static String getCouponIdString(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_2
        // 2: aconst_null
        // 3: astore 4
        // 5: new 67 java/lang/StringBuffer
        // 8: dup
        // 9: invokespecial 68 java/lang/StringBuffer:<init> ()V
        // 12: astore_3
        // 13: aload_0
        // 14: new 25 java/lang/StringBuilder
        // 17: dup
        // 18: ldc 70
        // 20: invokespecial 34 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 23: aload_1
        // 24: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 27: ldc 72
        // 29: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 32: invokevirtual 44 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 35: aconst_null
        // 36: invokevirtual 76 android/database/sqlite/SQLiteDatabase:rawQuery
        // (Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        // 39: astore 4
        // 41: aload 4
        // 43: invokeinterface 82 1 0
        // 48: istore 5
        // 50: iload 5
        // 52: ifeq +55 -> 107
        // 55: aload_3
        // 56: new 88 org/json/JSONObject
        // 59: dup
        // 60: aload 4
        // 62: iconst_2
        // 63: invokeinterface 91 2 0
        // 68: invokespecial 92 org/json/JSONObject:<init> (Ljava/lang/String;)V
        // 71: ldc 105
        // 73: invokestatic 111
        // com/doujiao/android/util/ReflectionFactory:create
        // (Lorg/json/JSONObject;Ljava/lang/Class;)Ljava/lang/Object;
        // 76: checkcast 105 com/doujiao/protocol/json/CouponDetail
        // 79: getfield 115 com/doujiao/protocol/json/CouponDetail:id
        // Ljava/lang/String;
        // 82: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 85: pop
        // 86: aload_3
        // 87: ldc 120
        // 89: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 92: pop
        // 93: aload 4
        // 95: invokeinterface 95 1 0
        // 100: istore 5
        // 102: iload 5
        // 104: ifne -49 -> 55
        // 107: aload 4
        // 109: ifnull +10 -> 119
        // 112: aload 4
        // 114: invokeinterface 98 1 0
        // 119: aload_3
        // 120: invokevirtual 99 java/lang/StringBuffer:toString
        // ()Ljava/lang/String;
        // 123: astore_3
        // 124: aload_3
        // 125: invokevirtual 103 java/lang/String:length ()I
        // 128: ifne +97 -> 225
        // 131: aload_2
        // 132: areturn
        // 133: astore 5
        // 135: ldc 122
        // 137: ldc 124
        // 139: aload 5
        // 141: invokestatic 154 android/util/Log:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        // 144: pop
        // 145: goto -52 -> 93
        // 148: astore 5
        // 150: aload 5
        // 152: invokevirtual 157 java/lang/Exception:printStackTrace ()V
        // 155: aload 4
        // 157: ifnull -38 -> 119
        // 160: aload 4
        // 162: invokeinterface 98 1 0
        // 167: goto -48 -> 119
        // 170: astore 4
        // 172: ldc 122
        // 174: ldc 124
        // 176: aload 4
        // 178: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 181: goto -62 -> 119
        // 184: astore_2
        // 185: aload 4
        // 187: ifnull +10 -> 197
        // 190: aload 4
        // 192: invokeinterface 98 1 0
        // 197: aload_2
        // 198: athrow
        // 199: astore_3
        // 200: ldc 122
        // 202: ldc 124
        // 204: aload_3
        // 205: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 208: goto -11 -> 197
        // 211: astore 4
        // 213: ldc 122
        // 215: ldc 124
        // 217: aload 4
        // 219: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 222: goto -103 -> 119
        // 225: aload_3
        // 226: iconst_0
        // 227: bipush 255
        // 229: aload_3
        // 230: invokevirtual 103 java/lang/String:length ()I
        // 233: iadd
        // 234: invokevirtual 132 java/lang/String:substring
        // (II)Ljava/lang/String;
        // 237: astore_2
        // 238: goto -107 -> 131
        //
        // Exception table:
        // from to target type
        // 55 93 133 java/lang/Exception
        // 13 50 148 java/lang/Exception
        // 93 102 148 java/lang/Exception
        // 135 145 148 java/lang/Exception
        // 160 167 170 java/lang/Exception
        // 13 50 184 finally
        // 55 93 184 finally
        // 93 102 184 finally
        // 135 145 184 finally
        // 150 155 184 finally
        // 190 197 199 java/lang/Exception
        // 112 119 211 java/lang/Exception
        return null;
    }

    // ERROR //
    public static int getFavCountByType(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        // Byte code:
        // 0: iconst_0
        // 1: istore_3
        // 2: aconst_null
        // 3: astore_2
        // 4: aload_0
        // 5: new 25 java/lang/StringBuilder
        // 8: dup
        // 9: ldc 161
        // 11: invokespecial 34 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 14: aload_1
        // 15: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 18: ldc 72
        // 20: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 23: invokevirtual 44 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 26: aconst_null
        // 27: invokevirtual 76 android/database/sqlite/SQLiteDatabase:rawQuery
        // (Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        // 30: astore_2
        // 31: aload_2
        // 32: invokeinterface 82 1 0
        // 37: pop
        // 38: aload_2
        // 39: iconst_0
        // 40: invokeinterface 86 2 0
        // 45: istore_3
        // 46: iload_3
        // 47: istore_3
        // 48: aload_2
        // 49: ifnull +9 -> 58
        // 52: aload_2
        // 53: invokeinterface 98 1 0
        // 58: iload_3
        // 59: ireturn
        // 60: pop
        // 61: aload_2
        // 62: ifnull -4 -> 58
        // 65: aload_2
        // 66: invokeinterface 98 1 0
        // 71: goto -13 -> 58
        // 74: pop
        // 75: goto -17 -> 58
        // 78: astore_3
        // 79: aload_2
        // 80: ifnull +9 -> 89
        // 83: aload_2
        // 84: invokeinterface 98 1 0
        // 89: aload_3
        // 90: athrow
        // 91: pop
        // 92: goto -3 -> 89
        // 95: pop
        // 96: goto -38 -> 58
        //
        // Exception table:
        // from to target type
        // 4 46 60 java/lang/Exception
        // 65 71 74 java/lang/Exception
        // 4 46 78 finally
        // 83 89 91 java/lang/Exception
        // 52 58 95 java/lang/Exception
        return 0;
    }

    // ERROR //
    public static String getIdString(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_2
        // 2: aconst_null
        // 3: astore_3
        // 4: new 67 java/lang/StringBuffer
        // 7: dup
        // 8: invokespecial 68 java/lang/StringBuffer:<init> ()V
        // 11: astore 4
        // 13: aload_0
        // 14: new 25 java/lang/StringBuilder
        // 17: dup
        // 18: ldc 70
        // 20: invokespecial 34 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 23: aload_1
        // 24: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 27: ldc 72
        // 29: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 32: invokevirtual 44 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 35: aconst_null
        // 36: invokevirtual 76 android/database/sqlite/SQLiteDatabase:rawQuery
        // (Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        // 39: astore_3
        // 40: aload_3
        // 41: invokeinterface 82 1 0
        // 46: istore 5
        // 48: iload 5
        // 50: ifeq +70 -> 120
        // 53: aload_3
        // 54: iconst_1
        // 55: invokeinterface 86 2 0
        // 60: istore 5
        // 62: iload 5
        // 64: iconst_3
        // 65: if_icmpne +80 -> 145
        // 68: aload 4
        // 70: new 88 org/json/JSONObject
        // 73: dup
        // 74: aload_3
        // 75: iconst_2
        // 76: invokeinterface 91 2 0
        // 81: invokespecial 92 org/json/JSONObject:<init> (Ljava/lang/String;)V
        // 84: ldc 164
        // 86: invokestatic 111
        // com/doujiao/android/util/ReflectionFactory:create
        // (Lorg/json/JSONObject;Ljava/lang/Class;)Ljava/lang/Object;
        // 89: checkcast 164 com/doujiao/protocol/json/BankCouponDetail
        // 92: getfield 165 com/doujiao/protocol/json/BankCouponDetail:id I
        // 95: invokevirtual 140 java/lang/StringBuffer:append
        // (I)Ljava/lang/StringBuffer;
        // 98: pop
        // 99: aload 4
        // 101: ldc 120
        // 103: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 106: pop
        // 107: aload_3
        // 108: invokeinterface 95 1 0
        // 113: istore 5
        // 115: iload 5
        // 117: ifne -64 -> 53
        // 120: aload_3
        // 121: ifnull +9 -> 130
        // 124: aload_3
        // 125: invokeinterface 98 1 0
        // 130: aload 4
        // 132: invokevirtual 99 java/lang/StringBuffer:toString
        // ()Ljava/lang/String;
        // 135: astore_3
        // 136: aload_3
        // 137: invokevirtual 103 java/lang/String:length ()I
        // 140: ifne +158 -> 298
        // 143: aload_2
        // 144: areturn
        // 145: iload 5
        // 147: iconst_4
        // 148: if_icmpne +77 -> 225
        // 151: aload 4
        // 153: new 88 org/json/JSONObject
        // 156: dup
        // 157: aload_3
        // 158: iconst_2
        // 159: invokeinterface 91 2 0
        // 164: invokespecial 92 org/json/JSONObject:<init>
        // (Ljava/lang/String;)V
        // 167: ldc 167
        // 169: invokestatic 111
        // com/doujiao/android/util/ReflectionFactory:create
        // (Lorg/json/JSONObject;Ljava/lang/Class;)Ljava/lang/Object;
        // 172: checkcast 167 com/doujiao/protocol/json/Ticket
        // 175: getfield 168 com/doujiao/protocol/json/Ticket:id I
        // 178: invokevirtual 140 java/lang/StringBuffer:append
        // (I)Ljava/lang/StringBuffer;
        // 181: pop
        // 182: aload 4
        // 184: ldc 120
        // 186: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 189: pop
        // 190: goto -83 -> 107
        // 193: astore 5
        // 195: ldc 122
        // 197: ldc 124
        // 199: aload 5
        // 201: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 204: goto -97 -> 107
        // 207: pop
        // 208: aload_3
        // 209: ifnull -79 -> 130
        // 212: aload_3
        // 213: invokeinterface 98 1 0
        // 218: goto -88 -> 130
        // 221: pop
        // 222: goto -92 -> 130
        // 225: iload 5
        // 227: iconst_2
        // 228: if_icmpne -121 -> 107
        // 231: new 88 org/json/JSONObject
        // 234: dup
        // 235: aload_3
        // 236: iconst_2
        // 237: invokeinterface 91 2 0
        // 242: invokespecial 92 org/json/JSONObject:<init>
        // (Ljava/lang/String;)V
        // 245: ldc 135
        // 247: invokestatic 111
        // com/doujiao/android/util/ReflectionFactory:create
        // (Lorg/json/JSONObject;Ljava/lang/Class;)Ljava/lang/Object;
        // 250: checkcast 135 com/doujiao/protocol/json/GroupDetail
        // 253: astore 5
        // 255: aload 5
        // 257: getfield 137 com/doujiao/protocol/json/GroupDetail:id I
        // 260: ifgt -153 -> 107
        // 263: aload 4
        // 265: aload 5
        // 267: getfield 171 com/doujiao/protocol/json/GroupDetail:dealUrl
        // Ljava/lang/String;
        // 270: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 273: pop
        // 274: aload 4
        // 276: ldc 120
        // 278: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 281: pop
        // 282: goto -175 -> 107
        // 285: astore_2
        // 286: aload_3
        // 287: ifnull +9 -> 296
        // 290: aload_3
        // 291: invokeinterface 98 1 0
        // 296: aload_2
        // 297: athrow
        // 298: aload_3
        // 299: iconst_0
        // 300: bipush 255
        // 302: aload_3
        // 303: invokevirtual 103 java/lang/String:length ()I
        // 306: iadd
        // 307: invokevirtual 132 java/lang/String:substring
        // (II)Ljava/lang/String;
        // 310: astore_2
        // 311: goto -168 -> 143
        // 314: pop
        // 315: goto -19 -> 296
        // 318: pop
        // 319: goto -189 -> 130
        //
        // Exception table:
        // from to target type
        // 53 107 193 java/lang/Exception
        // 151 190 193 java/lang/Exception
        // 231 282 193 java/lang/Exception
        // 13 48 207 java/lang/Exception
        // 107 115 207 java/lang/Exception
        // 195 204 207 java/lang/Exception
        // 212 218 221 java/lang/Exception
        // 13 48 285 finally
        // 53 107 285 finally
        // 107 115 285 finally
        // 151 190 285 finally
        // 195 204 285 finally
        // 231 282 285 finally
        // 290 296 314 java/lang/Exception
        // 124 130 318 java/lang/Exception
        return null;
    }

    // ERROR //
    public static String getStroeIdString(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_2
        // 2: new 67 java/lang/StringBuffer
        // 5: dup
        // 6: invokespecial 68 java/lang/StringBuffer:<init> ()V
        // 9: astore_3
        // 10: aconst_null
        // 11: astore 4
        // 13: aload_0
        // 14: new 25 java/lang/StringBuilder
        // 17: dup
        // 18: ldc 70
        // 20: invokespecial 34 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 23: aload_1
        // 24: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 27: ldc 72
        // 29: invokevirtual 40 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 32: invokevirtual 44 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 35: aconst_null
        // 36: invokevirtual 76 android/database/sqlite/SQLiteDatabase:rawQuery
        // (Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        // 39: astore 4
        // 41: aload 4
        // 43: invokeinterface 82 1 0
        // 48: istore 5
        // 50: iload 5
        // 52: ifeq +55 -> 107
        // 55: aload_3
        // 56: new 88 org/json/JSONObject
        // 59: dup
        // 60: aload 4
        // 62: iconst_2
        // 63: invokeinterface 91 2 0
        // 68: invokespecial 92 org/json/JSONObject:<init> (Ljava/lang/String;)V
        // 71: ldc 105
        // 73: invokestatic 111
        // com/doujiao/android/util/ReflectionFactory:create
        // (Lorg/json/JSONObject;Ljava/lang/Class;)Ljava/lang/Object;
        // 76: checkcast 105 com/doujiao/protocol/json/CouponDetail
        // 79: getfield 115 com/doujiao/protocol/json/CouponDetail:id
        // Ljava/lang/String;
        // 82: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 85: pop
        // 86: aload_3
        // 87: ldc 120
        // 89: invokevirtual 118 java/lang/StringBuffer:append
        // (Ljava/lang/String;)Ljava/lang/StringBuffer;
        // 92: pop
        // 93: aload 4
        // 95: invokeinterface 95 1 0
        // 100: istore 5
        // 102: iload 5
        // 104: ifne -49 -> 55
        // 107: aload 4
        // 109: ifnull +10 -> 119
        // 112: aload 4
        // 114: invokeinterface 98 1 0
        // 119: aload_3
        // 120: invokevirtual 99 java/lang/StringBuffer:toString
        // ()Ljava/lang/String;
        // 123: astore_3
        // 124: aload_3
        // 125: invokevirtual 103 java/lang/String:length ()I
        // 128: ifne +91 -> 219
        // 131: aload_2
        // 132: areturn
        // 133: astore 5
        // 135: ldc 122
        // 137: ldc 124
        // 139: aload 5
        // 141: invokestatic 154 android/util/Log:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        // 144: pop
        // 145: goto -52 -> 93
        // 148: pop
        // 149: aload 4
        // 151: ifnull -32 -> 119
        // 154: aload 4
        // 156: invokeinterface 98 1 0
        // 161: goto -42 -> 119
        // 164: astore 4
        // 166: ldc 122
        // 168: ldc 124
        // 170: aload 4
        // 172: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 175: goto -56 -> 119
        // 178: astore_2
        // 179: aload 4
        // 181: ifnull +10 -> 191
        // 184: aload 4
        // 186: invokeinterface 98 1 0
        // 191: aload_2
        // 192: athrow
        // 193: astore_3
        // 194: ldc 122
        // 196: ldc 124
        // 198: aload_3
        // 199: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 202: goto -11 -> 191
        // 205: astore 4
        // 207: ldc 122
        // 209: ldc 124
        // 211: aload 4
        // 213: invokestatic 128 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 216: goto -97 -> 119
        // 219: aload_3
        // 220: iconst_0
        // 221: bipush 255
        // 223: aload_3
        // 224: invokevirtual 103 java/lang/String:length ()I
        // 227: iadd
        // 228: invokevirtual 132 java/lang/String:substring
        // (II)Ljava/lang/String;
        // 231: astore_2
        // 232: goto -101 -> 131
        //
        // Exception table:
        // from to target type
        // 55 93 133 java/lang/Exception
        // 13 50 148 java/lang/Exception
        // 93 102 148 java/lang/Exception
        // 135 145 148 java/lang/Exception
        // 154 161 164 java/lang/Exception
        // 13 50 178 finally
        // 55 93 178 finally
        // 93 102 178 finally
        // 135 145 178 finally
        // 184 191 193 java/lang/Exception
        // 112 119 205 java/lang/Exception
        return null;
    }

    public static void init(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase
                .execSQL("CREATE TABLE IF NOT EXISTS fav(id INTEGER PRIMARY KEY, type INTEGER, content TEXT)");
    }

    public static List<DetailRef> list(SQLiteDatabase paramSQLiteDatabase,
            String paramString) {
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT * FROM fav "
                + paramString + " ORDER BY id DESC", null);
        if (localCursor.moveToFirst())
            ;
        try {
            int j = localCursor.getInt(0);
            int i = localCursor.getInt(1);
            switch (i) {
            case 3:
            case 4:
                // while (true)
            {
                Object localObject = new DetailRef();
                Detail localDetail = (Detail) ReflectionFactory.create(
                        new JSONObject(localCursor.getString(2)),
                        DetailRef.class);
                ((DetailRef) localObject).type = i;
                ((DetailRef) localObject).detail = localDetail;
                localDetail.setDBId(j);
                localArrayList.add(localObject);
                if (localCursor.moveToNext())
                    // break;
                    localCursor.close();
                return localArrayList;
                // localObject = BankCouponDetail.class;
                // continue;
                // localObject = Ticket.class;
            }
            default:
            case 2:
            }
        } catch (Exception localException) {
            // while (true)
            {
                LogUtils.e("test", "", localException);
                // continue;
                Object localObject = CouponDetail.class;
                // continue;
                localObject = GroupDetail.class;
            }
        }
        return localArrayList;
    }

    public static Map<String, DetailRef> listAsMap(
            SQLiteDatabase paramSQLiteDatabase, String paramString) {
        HashMap localHashMap = new HashMap();
        Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT * FROM fav "
                + paramString + " ORDER BY id DESC", null);
        if (localCursor.moveToFirst())
            ;
        try {
            do {
                int i = localCursor.getInt(1);
                int j = localCursor.getInt(0);
                switch (i) {
                case 3:
                    Object localObject = new DetailRef();
                    Detail localDetail = (Detail) ReflectionFactory.create(
                            new JSONObject(localCursor.getString(2)),
                            DetailRef.class);
                    ((DetailRef) localObject).type = i;
                    ((DetailRef) localObject).detail = localDetail;
                    localDetail.setDBId(j);
                    localHashMap.put(localDetail.getId(), localObject);
                case 4:
                default:
                case 2:
                }
            } while (localCursor.moveToNext());
        } catch (Exception localException1) {
            // while (true)
            {
                try {
                    localCursor.close();
                    // return localHashMap;
                    // localObject = BankCouponDetail.class;
                    // continue;
                    // localObject = Ticket.class;
                    // continue;
                    localException1 = localException1;
                    LogUtils.e("test", "", localException1);
                } catch (Exception localException2) {
                    // continue;
                    // localObject = CouponDetail.class;
                }
                // continue;
                Object localObject = GroupDetail.class;
            }
        }
        return localHashMap;
    }

    public static void save(SQLiteDatabase paramSQLiteDatabase, int paramInt,
            Object paramObject) {
        String str = ReflectionFactory.toJSON(paramObject).toString();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(paramInt);
        arrayOfObject[1] = str;
        paramSQLiteDatabase.execSQL(
                "INSERT INTO fav(type, content) VALUES(?, ?)", arrayOfObject);
    }

    public String[] getAllIdByType() {
        return null;
    }
}
