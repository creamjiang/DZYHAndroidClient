package com.dld.coupon.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.dld.android.net.HttpUtil;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.protocol.image.ImageCache;
import com.dld.protocol.image.ImageProtocol;
import com.dld.protocol.json.User;
import com.tencent.weibo.utils.Cache;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUtil {
    public static final String BACKUPUHUODONG_SDCARD_PATH;
    public static final String LAUNCHLOGO_PATH;
    private static Handler handler = new Handler(Looper.getMainLooper());
    public static ExecutorService pool = Executors.newFixedThreadPool(3);

    static {
        BACKUPUHUODONG_SDCARD_PATH = Environment.getExternalStorageDirectory()
                + "/doujiao/huodongcache";
        LAUNCHLOGO_PATH = Environment.getExternalStorageDirectory()
                + "/doujiao/huodongcache/";
    }

    public static void deleteGroupImage(String paramString) {
        deleteImage(paramString, 200, 200);
        deleteImage(paramString, 400, 400);
    }

    private static void deleteImage(String paramString, int paramInt1,
            int paramInt2) {
        try {
            String str = genFileName(paramString, paramInt1, paramInt2);
            new File(new File(Environment.getExternalStorageDirectory(),
                    "doujiao"), str).delete();
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    public static void deleteTicketImage(String paramString) {
        deleteImage(paramString, 140, 200);
        deleteImage(paramString, 600, 600);
    }

    public static String genFileName(String paramString, int paramInt1,
            int paramInt2) {
        return paramInt1 + "_" + paramInt2 + "_"
                + URLEncoder.encode(paramString).replaceAll(":|/", "_");
    }

    // ERROR //
    public static Bitmap getBitmapFromInputStream(InputStream paramInputStream) {
        // Byte code:
        // 0: new 136 java/io/ByteArrayOutputStream
        // 3: dup
        // 4: invokespecial 137 java/io/ByteArrayOutputStream:<init> ()V
        // 7: astore_1
        // 8: aconst_null
        // 9: astore_2
        // 10: aload_0
        // 11: invokevirtual 143 java/io/InputStream:read ()I
        // 14: istore_3
        // 15: iload_3
        // 16: bipush 255
        // 18: if_icmpne +39 -> 57
        // 21: aload_1
        // 22: invokevirtual 147 java/io/ByteArrayOutputStream:toByteArray ()[B
        // 25: astore_2
        // 26: aload_2
        // 27: iconst_0
        // 28: aload_2
        // 29: arraylength
        // 30: invokestatic 153 android/graphics/BitmapFactory:decodeByteArray
        // ([BII)Landroid/graphics/Bitmap;
        // 33: astore_2
        // 34: aconst_null
        // 35: checkcast 155 [B
        // 38: pop
        // 39: aload_0
        // 40: ifnull +7 -> 47
        // 43: aload_0
        // 44: invokevirtual 158 java/io/InputStream:close ()V
        // 47: aload_1
        // 48: ifnull +7 -> 55
        // 51: aload_1
        // 52: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 55: aload_2
        // 56: areturn
        // 57: aload_1
        // 58: iload_3
        // 59: invokevirtual 163 java/io/ByteArrayOutputStream:write (I)V
        // 62: aload_0
        // 63: invokevirtual 143 java/io/InputStream:read ()I
        // 66: istore_3
        // 67: iload_3
        // 68: istore_3
        // 69: goto -54 -> 15
        // 72: astore_3
        // 73: aload_3
        // 74: invokevirtual 166 java/io/IOException:printStackTrace ()V
        // 77: aload_0
        // 78: ifnull +7 -> 85
        // 81: aload_0
        // 82: invokevirtual 158 java/io/InputStream:close ()V
        // 85: aload_1
        // 86: ifnull -31 -> 55
        // 89: aload_1
        // 90: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 93: goto -38 -> 55
        // 96: astore_1
        // 97: aload_1
        // 98: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 101: goto -46 -> 55
        // 104: astore_3
        // 105: aload_3
        // 106: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 109: goto -24 -> 85
        // 112: astore_2
        // 113: aload_0
        // 114: ifnull +7 -> 121
        // 117: aload_0
        // 118: invokevirtual 158 java/io/InputStream:close ()V
        // 121: aload_1
        // 122: ifnull +7 -> 129
        // 125: aload_1
        // 126: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 129: aload_2
        // 130: athrow
        // 131: astore_3
        // 132: aload_3
        // 133: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 136: goto -15 -> 121
        // 139: astore_1
        // 140: aload_1
        // 141: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 144: goto -15 -> 129
        // 147: astore_3
        // 148: aload_3
        // 149: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 152: goto -105 -> 47
        // 155: astore_1
        // 156: aload_1
        // 157: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 160: goto -105 -> 55
        //
        // Exception table:
        // from to target type
        // 10 39 72 java/io/IOException
        // 57 67 72 java/io/IOException
        // 89 93 96 java/lang/Exception
        // 81 85 104 java/lang/Exception
        // 10 39 112 finally
        // 57 67 112 finally
        // 73 77 112 finally
        // 117 121 131 java/lang/Exception
        // 125 129 139 java/lang/Exception
        // 43 47 147 java/lang/Exception
        // 51 55 155 java/lang/Exception
        return null;
    }

    // ERROR //
    public static byte[] getByteFromInputStream(InputStream paramInputStream) {
        // Byte code:
        // 0: new 136 java/io/ByteArrayOutputStream
        // 3: dup
        // 4: invokespecial 137 java/io/ByteArrayOutputStream:<init> ()V
        // 7: astore_1
        // 8: aconst_null
        // 9: checkcast 155 [B
        // 12: astore_2
        // 13: aload_0
        // 14: invokevirtual 143 java/io/InputStream:read ()I
        // 17: istore_3
        // 18: iload_3
        // 19: bipush 255
        // 21: if_icmpne +28 -> 49
        // 24: aload_1
        // 25: invokevirtual 147 java/io/ByteArrayOutputStream:toByteArray ()[B
        // 28: astore_2
        // 29: aload_2
        // 30: astore_2
        // 31: aload_0
        // 32: ifnull +7 -> 39
        // 35: aload_0
        // 36: invokevirtual 158 java/io/InputStream:close ()V
        // 39: aload_1
        // 40: ifnull +7 -> 47
        // 43: aload_1
        // 44: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 47: aload_2
        // 48: areturn
        // 49: aload_1
        // 50: iload_3
        // 51: invokevirtual 163 java/io/ByteArrayOutputStream:write (I)V
        // 54: aload_0
        // 55: invokevirtual 143 java/io/InputStream:read ()I
        // 58: istore_3
        // 59: iload_3
        // 60: istore_3
        // 61: goto -43 -> 18
        // 64: astore_3
        // 65: aload_3
        // 66: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 69: aload_0
        // 70: ifnull +7 -> 77
        // 73: aload_0
        // 74: invokevirtual 158 java/io/InputStream:close ()V
        // 77: aload_1
        // 78: ifnull -31 -> 47
        // 81: aload_1
        // 82: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 85: goto -38 -> 47
        // 88: astore_1
        // 89: aload_1
        // 90: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 93: goto -46 -> 47
        // 96: astore_3
        // 97: aload_3
        // 98: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 101: goto -24 -> 77
        // 104: astore_2
        // 105: aload_0
        // 106: ifnull +7 -> 113
        // 109: aload_0
        // 110: invokevirtual 158 java/io/InputStream:close ()V
        // 113: aload_1
        // 114: ifnull +7 -> 121
        // 117: aload_1
        // 118: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 121: aload_2
        // 122: athrow
        // 123: astore_3
        // 124: aload_3
        // 125: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 128: goto -15 -> 113
        // 131: astore_1
        // 132: aload_1
        // 133: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 136: goto -15 -> 121
        // 139: astore_3
        // 140: aload_3
        // 141: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 144: goto -105 -> 39
        // 147: astore_1
        // 148: aload_1
        // 149: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 152: goto -105 -> 47
        //
        // Exception table:
        // from to target type
        // 13 29 64 java/lang/Exception
        // 49 59 64 java/lang/Exception
        // 81 85 88 java/lang/Exception
        // 73 77 96 java/lang/Exception
        // 13 29 104 finally
        // 49 59 104 finally
        // 65 69 104 finally
        // 109 113 123 java/lang/Exception
        // 117 121 131 java/lang/Exception
        // 35 39 139 java/lang/Exception
        // 43 47 147 java/lang/Exception
        return null;
    }

    // ERROR //
    public static Bitmap getDrawbleInCache(String paramString) throws Exception {
        // Byte code:
        // 0: aconst_null
        // 1: astore_1
        // 2: aconst_null
        // 3: astore_3
        // 4: aconst_null
        // 5: astore_2
        // 6: new 85 java/io/File
        // 9: dup
        // 10: new 41 java/lang/StringBuilder
        // 13: dup
        // 14: getstatic 68 com/doujiao/coupon/util/FileUtil:LAUNCHLOGO_PATH
        // Ljava/lang/String;
        // 17: invokestatic 174 java/lang/String:valueOf
        // (Ljava/lang/Object;)Ljava/lang/String;
        // 20: invokespecial 113 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 23: aload_0
        // 24: invokevirtual 58 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 27: invokevirtual 62 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 30: invokespecial 175 java/io/File:<init> (Ljava/lang/String;)V
        // 33: astore 4
        // 35: aload 4
        // 37: invokevirtual 178 java/io/File:exists ()Z
        // 40: istore 5
        // 42: iload 5
        // 44: ifne +39 -> 83
        // 47: iconst_0
        // 48: ifeq +7 -> 55
        // 51: aconst_null
        // 52: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 55: iconst_0
        // 56: ifeq +7 -> 63
        // 59: aconst_null
        // 60: invokevirtual 181 java/io/FileInputStream:close ()V
        // 63: aconst_null
        // 64: astore_1
        // 65: aload_1
        // 66: areturn
        // 67: astore_1
        // 68: aload_1
        // 69: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 72: goto -17 -> 55
        // 75: astore_1
        // 76: aload_1
        // 77: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 80: goto -17 -> 63
        // 83: new 180 java/io/FileInputStream
        // 86: dup
        // 87: aload 4
        // 89: invokespecial 184 java/io/FileInputStream:<init>
        // (Ljava/io/File;)V
        // 92: astore_2
        // 93: new 136 java/io/ByteArrayOutputStream
        // 96: dup
        // 97: invokespecial 137 java/io/ByteArrayOutputStream:<init> ()V
        // 100: astore_3
        // 101: aload_2
        // 102: invokevirtual 185 java/io/FileInputStream:read ()I
        // 105: istore 4
        // 107: iload 4
        // 109: bipush 255
        // 111: if_icmpne +46 -> 157
        // 114: aload_2
        // 115: invokevirtual 181 java/io/FileInputStream:close ()V
        // 118: aload_3
        // 119: invokevirtual 147 java/io/ByteArrayOutputStream:toByteArray ()[B
        // 122: astore 4
        // 124: aload 4
        // 126: iconst_0
        // 127: aload 4
        // 129: arraylength
        // 130: invokestatic 153 android/graphics/BitmapFactory:decodeByteArray
        // ([BII)Landroid/graphics/Bitmap;
        // 133: astore_1
        // 134: aload_1
        // 135: astore_1
        // 136: aload_3
        // 137: ifnull +7 -> 144
        // 140: aload_3
        // 141: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 144: aload_2
        // 145: ifnull +121 -> 266
        // 148: aload_2
        // 149: invokevirtual 181 java/io/FileInputStream:close ()V
        // 152: aload_1
        // 153: astore_1
        // 154: goto -89 -> 65
        // 157: aload_3
        // 158: iload 4
        // 160: invokevirtual 163 java/io/ByteArrayOutputStream:write (I)V
        // 163: aload_2
        // 164: invokevirtual 185 java/io/FileInputStream:read ()I
        // 167: istore 4
        // 169: iload 4
        // 171: istore 4
        // 173: goto -66 -> 107
        // 176: astore 4
        // 178: aload 4
        // 180: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 183: aload_3
        // 184: ifnull +7 -> 191
        // 187: aload_3
        // 188: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 191: aload_2
        // 192: ifnull -40 -> 152
        // 195: aload_2
        // 196: invokevirtual 181 java/io/FileInputStream:close ()V
        // 199: goto -47 -> 152
        // 202: astore_2
        // 203: aload_2
        // 204: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 207: goto -55 -> 152
        // 210: astore_3
        // 211: aload_3
        // 212: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 215: goto -24 -> 191
        // 218: astore_1
        // 219: aload_3
        // 220: ifnull +7 -> 227
        // 223: aload_3
        // 224: invokevirtual 159 java/io/ByteArrayOutputStream:close ()V
        // 227: aload_2
        // 228: ifnull +7 -> 235
        // 231: aload_2
        // 232: invokevirtual 181 java/io/FileInputStream:close ()V
        // 235: aload_1
        // 236: athrow
        // 237: astore_3
        // 238: aload_3
        // 239: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 242: goto -15 -> 227
        // 245: astore_2
        // 246: aload_2
        // 247: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 250: goto -15 -> 235
        // 253: astore_3
        // 254: aload_3
        // 255: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 258: goto -114 -> 144
        // 261: astore_2
        // 262: aload_2
        // 263: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 266: goto -114 -> 152
        // 269: astore_1
        // 270: aload_2
        // 271: astore_2
        // 272: goto -53 -> 219
        // 275: astore_1
        // 276: aload_2
        // 277: astore_2
        // 278: aload_3
        // 279: astore_3
        // 280: goto -61 -> 219
        // 283: astore 4
        // 285: aload_2
        // 286: astore_2
        // 287: goto -109 -> 178
        // 290: astore 4
        // 292: aload_2
        // 293: astore_2
        // 294: aload_3
        // 295: astore_3
        // 296: goto -118 -> 178
        //
        // Exception table:
        // from to target type
        // 51 55 67 java/lang/Exception
        // 59 63 75 java/lang/Exception
        // 6 42 176 java/lang/Exception
        // 83 93 176 java/lang/Exception
        // 195 199 202 java/lang/Exception
        // 187 191 210 java/lang/Exception
        // 6 42 218 finally
        // 83 93 218 finally
        // 178 183 218 finally
        // 223 227 237 java/lang/Exception
        // 231 235 245 java/lang/Exception
        // 140 144 253 java/lang/Exception
        // 148 152 261 java/lang/Exception
        // 93 101 269 finally
        // 101 134 275 finally
        // 157 169 275 finally
        // 93 101 283 java/lang/Exception
        // 101 134 290 java/lang/Exception
        // 157 169 290 java/lang/Exception
        return null;
    }

    // ERROR //
    public static byte[] getFileContent(String paramString) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_1
        // 2: new 85 java/io/File
        // 5: dup
        // 6: aload_0
        // 7: invokespecial 175 java/io/File:<init> (Ljava/lang/String;)V
        // 10: astore_2
        // 11: new 189 java/io/BufferedInputStream
        // 14: dup
        // 15: new 180 java/io/FileInputStream
        // 18: dup
        // 19: aload_2
        // 20: invokespecial 184 java/io/FileInputStream:<init>
        // (Ljava/io/File;)V
        // 23: invokespecial 192 java/io/BufferedInputStream:<init>
        // (Ljava/io/InputStream;)V
        // 26: astore_1
        // 27: aload_2
        // 28: invokevirtual 196 java/io/File:length ()J
        // 31: l2i
        // 32: istore_3
        // 33: iload_3
        // 34: newarray byte
        // 36: astore_2
        // 37: aload_1
        // 38: aload_2
        // 39: iconst_0
        // 40: iload_3
        // 41: invokevirtual 199 java/io/BufferedInputStream:read ([BII)I
        // 44: pop
        // 45: aload_1
        // 46: ifnull +7 -> 53
        // 49: aload_1
        // 50: invokevirtual 200 java/io/BufferedInputStream:close ()V
        // 53: aload_2
        // 54: areturn
        // 55: astore_2
        // 56: ldc 96
        // 58: ldc 98
        // 60: aload_2
        // 61: invokestatic 104 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 64: aload_1
        // 65: ifnull +7 -> 72
        // 68: aload_1
        // 69: invokevirtual 200 java/io/BufferedInputStream:close ()V
        // 72: aconst_null
        // 73: astore_2
        // 74: goto -21 -> 53
        // 77: astore_2
        // 78: aload_1
        // 79: ifnull +7 -> 86
        // 82: aload_1
        // 83: invokevirtual 200 java/io/BufferedInputStream:close ()V
        // 86: aload_2
        // 87: athrow
        // 88: pop
        // 89: goto -36 -> 53
        // 92: pop
        // 93: goto -21 -> 72
        // 96: pop
        // 97: goto -11 -> 86
        // 100: astore_2
        // 101: aload_1
        // 102: astore_1
        // 103: goto -25 -> 78
        // 106: astore_2
        // 107: aload_1
        // 108: astore_1
        // 109: goto -53 -> 56
        //
        // Exception table:
        // from to target type
        // 2 27 55 java/io/IOException
        // 2 27 77 finally
        // 56 64 77 finally
        // 49 53 88 java/lang/Exception
        // 68 72 92 java/lang/Exception
        // 82 86 96 java/lang/Exception
        // 27 45 100 finally
        // 27 45 106 java/io/IOException
        return null;
    }

    public static User getUserInfo() throws IOException {
        Object localObject = new File(new File(
                Environment.getExternalStorageDirectory(), "doujiao"),
                "userinfo");
        Properties localProperties = new Properties();
        User localUser = new User();
        localObject = new FileInputStream((File) localObject);
        localProperties.load((InputStream) localObject);
        localUser.email = localProperties.getProperty("email", null);
        localUser.mobile = localProperties.getProperty("mobile", null);
        localUser.username = localProperties.getProperty("username", null);
        localUser.gender = new Integer(localProperties.getProperty("gender",
                "0")).intValue();
        localUser.qqcode = localProperties.getProperty("qqcode", null);
        localUser.msn = localProperties.getProperty("msn", null);
        localUser.weibo = localProperties.getProperty("weibo", null);
        localUser.idcard = localProperties.getProperty("idcard", null);
        localUser.cityname = localProperties.getProperty("cityname", null);
        localUser.lastLoginTime = localProperties.getProperty("lastLoginTime",
                null);
        localUser.registerDate = localProperties.getProperty("registerDate",
                null);
        ((FileInputStream) localObject).close();
        return (User) localUser;
    }

    // ERROR //
    public static byte[] readFile(String paramString) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_1
        // 2: new 85 java/io/File
        // 5: dup
        // 6: new 85 java/io/File
        // 9: dup
        // 10: invokestatic 49
        // android/os/Environment:getExternalStorageDirectory ()Ljava/io/File;
        // 13: ldc 87
        // 15: invokespecial 90 java/io/File:<init>
        // (Ljava/io/File;Ljava/lang/String;)V
        // 18: aload_0
        // 19: invokespecial 90 java/io/File:<init>
        // (Ljava/io/File;Ljava/lang/String;)V
        // 22: astore_2
        // 23: new 189 java/io/BufferedInputStream
        // 26: dup
        // 27: new 180 java/io/FileInputStream
        // 30: dup
        // 31: aload_2
        // 32: invokespecial 184 java/io/FileInputStream:<init>
        // (Ljava/io/File;)V
        // 35: invokespecial 192 java/io/BufferedInputStream:<init>
        // (Ljava/io/InputStream;)V
        // 38: astore_1
        // 39: aload_2
        // 40: invokevirtual 196 java/io/File:length ()J
        // 43: l2i
        // 44: istore_3
        // 45: iload_3
        // 46: newarray byte
        // 48: astore_2
        // 49: aload_1
        // 50: aload_2
        // 51: iconst_0
        // 52: iload_3
        // 53: invokevirtual 199 java/io/BufferedInputStream:read ([BII)I
        // 56: pop
        // 57: aload_1
        // 58: ifnull +7 -> 65
        // 61: aload_1
        // 62: invokevirtual 200 java/io/BufferedInputStream:close ()V
        // 65: aload_2
        // 66: areturn
        // 67: astore_1
        // 68: aload_1
        // 69: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 72: goto -7 -> 65
        // 75: astore_2
        // 76: ldc 96
        // 78: ldc 98
        // 80: aload_2
        // 81: invokestatic 104 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 84: aload_1
        // 85: ifnull +7 -> 92
        // 88: aload_1
        // 89: invokevirtual 200 java/io/BufferedInputStream:close ()V
        // 92: aconst_null
        // 93: astore_2
        // 94: goto -29 -> 65
        // 97: astore_1
        // 98: aload_1
        // 99: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 102: goto -10 -> 92
        // 105: astore_2
        // 106: aload_1
        // 107: ifnull +7 -> 114
        // 110: aload_1
        // 111: invokevirtual 200 java/io/BufferedInputStream:close ()V
        // 114: aload_2
        // 115: athrow
        // 116: astore_1
        // 117: aload_1
        // 118: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 121: goto -7 -> 114
        // 124: astore_2
        // 125: aload_1
        // 126: astore_1
        // 127: goto -21 -> 106
        // 130: astore_2
        // 131: aload_1
        // 132: astore_1
        // 133: goto -57 -> 76
        //
        // Exception table:
        // from to target type
        // 61 65 67 java/lang/Exception
        // 2 39 75 java/lang/Exception
        // 88 92 97 java/lang/Exception
        // 2 39 105 finally
        // 76 84 105 finally
        // 110 114 116 java/lang/Exception
        // 39 57 124 finally
        // 39 57 130 java/lang/Exception
        return null;
    }

    public static void readImage(final ImageView paramImageView,
            final String paramString, int paramInt1, int paramInt2) {
        if (paramString != null)
            try {
                if (ImageCache.getInstance().get(paramString) != null) {
                    paramImageView.setImageBitmap(ImageCache.getInstance().get(
                            paramString));
                    Log.e("main", "get url in cache");
                    paramImageView.setTag(Tag.IMG_CHANGED);
                }
                // while (true)
                {
                    Log.e("main", "cache size==" + ImageCache.mSoftCache.size());
                    // break;
                    Object localObject = readFile(genFileName(paramString,
                            paramInt1, paramInt2));
                    localObject = new SoftReference(
                            BitmapFactory.decodeByteArray((byte[]) localObject,
                                    0, ((byte[]) localObject).length));
                    ImageCache.getInstance();
                    ImageCache.mSoftCache.put(paramString,
                            (SoftReference<Bitmap>) localObject);
                    Log.e("main", "decode ");
                    pool.submit(new Runnable() {
                        public void run() {
                            FileUtil.handler.post(new Runnable() {
                                public void run() {
                                    paramImageView
                                            .setImageBitmap((Bitmap) ImageCache
                                                    .getInstance().get(
                                                            paramString));
                                }
                            });
                        }
                    });
                    paramImageView.setTag(Tag.IMG_CHANGED);
                }
            } catch (Exception localException) {
                Log.e("main", "readImage exception");
                new ImageProtocol(ActivityManager.getCurrent(),
                        "http://www.dld.com/tuan/viewimage?u="
                                + URLEncoder.encode(paramString) + "&w="
                                + paramInt1 + "&h=" + paramInt2)
                        .startTrans(paramImageView);
            }
    }

    public static void saveFile(String paramString, byte[] paramArrayOfByte) {
        saveFile(paramString, paramArrayOfByte, false);
    }

    // ERROR //
    public static void saveFile(String paramString, byte[] paramArrayOfByte,
            boolean paramBoolean) {
        // Byte code:
        // 0: aconst_null
        // 1: astore_3
        // 2: new 85 java/io/File
        // 5: dup
        // 6: invokestatic 49 android/os/Environment:getExternalStorageDirectory
        // ()Ljava/io/File;
        // 9: ldc 87
        // 11: invokespecial 90 java/io/File:<init>
        // (Ljava/io/File;Ljava/lang/String;)V
        // 14: astore 4
        // 16: aload 4
        // 18: invokevirtual 178 java/io/File:exists ()Z
        // 21: ifne +9 -> 30
        // 24: aload 4
        // 26: invokevirtual 369 java/io/File:mkdir ()Z
        // 29: pop
        // 30: new 85 java/io/File
        // 33: dup
        // 34: aload 4
        // 36: aload_0
        // 37: invokespecial 90 java/io/File:<init>
        // (Ljava/io/File;Ljava/lang/String;)V
        // 40: astore 4
        // 42: aload 4
        // 44: invokevirtual 178 java/io/File:exists ()Z
        // 47: istore 5
        // 49: iload 5
        // 51: ifeq +24 -> 75
        // 54: iload_2
        // 55: ifne +20 -> 75
        // 58: iconst_0
        // 59: ifeq +7 -> 66
        // 62: aconst_null
        // 63: invokevirtual 372 java/io/OutputStream:close ()V
        // 66: return
        // 67: astore_3
        // 68: aload_3
        // 69: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 72: goto -6 -> 66
        // 75: aload 4
        // 77: invokevirtual 375 java/io/File:createNewFile ()Z
        // 80: pop
        // 81: new 377 java/io/FileOutputStream
        // 84: dup
        // 85: aload 4
        // 87: invokespecial 378 java/io/FileOutputStream:<init>
        // (Ljava/io/File;)V
        // 90: astore_3
        // 91: aload_3
        // 92: aload_1
        // 93: invokevirtual 381 java/io/OutputStream:write ([B)V
        // 96: aload_3
        // 97: invokevirtual 372 java/io/OutputStream:close ()V
        // 100: aload_3
        // 101: ifnull +66 -> 167
        // 104: aload_3
        // 105: invokevirtual 372 java/io/OutputStream:close ()V
        // 108: goto -42 -> 66
        // 111: astore 4
        // 113: ldc 96
        // 115: ldc 98
        // 117: aload 4
        // 119: invokestatic 104 com/doujiao/android/util/LogUtils:e
        // (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        // 122: aload_3
        // 123: ifnull -57 -> 66
        // 126: aload_3
        // 127: invokevirtual 372 java/io/OutputStream:close ()V
        // 130: goto -64 -> 66
        // 133: astore_3
        // 134: aload_3
        // 135: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 138: goto -72 -> 66
        // 141: astore 4
        // 143: aload_3
        // 144: ifnull +7 -> 151
        // 147: aload_3
        // 148: invokevirtual 372 java/io/OutputStream:close ()V
        // 151: aload 4
        // 153: athrow
        // 154: astore_3
        // 155: aload_3
        // 156: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 159: goto -8 -> 151
        // 162: astore_3
        // 163: aload_3
        // 164: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 167: goto -101 -> 66
        // 170: astore 4
        // 172: aload_3
        // 173: astore_3
        // 174: goto -31 -> 143
        // 177: astore 4
        // 179: aload_3
        // 180: astore_3
        // 181: goto -68 -> 113
        //
        // Exception table:
        // from to target type
        // 62 66 67 java/lang/Exception
        // 2 49 111 java/lang/Exception
        // 75 91 111 java/lang/Exception
        // 126 130 133 java/lang/Exception
        // 2 49 141 finally
        // 75 91 141 finally
        // 113 122 141 finally
        // 147 151 154 java/lang/Exception
        // 104 108 162 java/lang/Exception
        // 91 100 170 finally
        // 91 100 177 java/lang/Exception
    }

    public static void saveGroupImage(String paramString) {
        saveImage(paramString, 200, 200);
        saveImage(paramString, 400, 400);
    }

    private static void saveImage(final String paramString,
            final int paramInt1, final int paramInt2) {
        try {
            byte[] arrayOfByte = (byte[]) Cache
                    .getCache("http://www.dld.com/tuan/viewimage?u="
                            + URLEncoder.encode(paramString) + "&w="
                            + paramInt1 + "&h=" + paramInt2);
            if ((arrayOfByte == null) || (arrayOfByte.length < 10))
                new Thread() {
                    public void run() {
                        try {
                            byte[] arrayOfByte = FileUtil
                                    .getByteFromInputStream(HttpUtil
                                            .getInputStreamByUrl(paramString,
                                                    ActivityManager
                                                            .getCurrent()));
                            if ((arrayOfByte != null)
                                    && (arrayOfByte.length >= 0))
                                FileUtil.saveFile(FileUtil.genFileName(
                                        paramString, paramInt1, paramInt2),
                                        arrayOfByte);
                        } catch (Exception localException) {
                            localException.printStackTrace();
                        }
                    }
                };
            else
                saveFile(genFileName(paramString, paramInt1, paramInt2),
                        arrayOfByte);
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    public static void saveTicketImage(String paramString) {
        saveImage(paramString, 140, 200);
        saveImage(paramString, 600, 600);
    }

    public static boolean saveUserInfo(User paramUser) throws IOException {
        File localFile = new File(new File(
                Environment.getExternalStorageDirectory(), "doujiao"),
                "userinfo");
        Properties localProperties = new Properties();
        new OutputStreamWriter(new PrintStream(localFile), "UTF-8");
        localProperties.setProperty("email", paramUser.email);
        localProperties.setProperty("mobile", paramUser.mobile);
        localProperties.setProperty("username", paramUser.username);
        localProperties.setProperty("realname", paramUser.realname);
        localProperties.setProperty("gender", String.valueOf(paramUser.gender));
        localProperties.setProperty("qqcode", paramUser.qqcode);
        localProperties.setProperty("msn", paramUser.msn);
        localProperties.setProperty("weibo", paramUser.weibo);
        localProperties.setProperty("idcard", paramUser.idcard);
        localProperties.setProperty("cityname", paramUser.cityname);
        localProperties.setProperty("lastLoginTime", paramUser.lastLoginTime);
        localProperties.setProperty("registerDate", paramUser.registerDate);
        return true;
    }

    // ERROR //
    public static boolean writehuodongCache(InputStream paramInputStream,
            String paramString) throws IOException {
        // Byte code:
        // 0: iconst_0
        // 1: istore_2
        // 2: aload_0
        // 3: ifnonnull +5 -> 8
        // 6: iload_2
        // 7: ireturn
        // 8: ldc_w 289
        // 11: ldc_w 420
        // 14: invokestatic 424 com/doujiao/android/util/LogUtils:log
        // (Ljava/lang/String;Ljava/lang/Object;)V
        // 17: getstatic 64
        // com/doujiao/coupon/util/FileUtil:BACKUPUHUODONG_SDCARD_PATH
        // Ljava/lang/String;
        // 20: astore 5
        // 22: new 41 java/lang/StringBuilder
        // 25: dup
        // 26: getstatic 68 com/doujiao/coupon/util/FileUtil:LAUNCHLOGO_PATH
        // Ljava/lang/String;
        // 29: invokestatic 174 java/lang/String:valueOf
        // (Ljava/lang/Object;)Ljava/lang/String;
        // 32: invokespecial 113 java/lang/StringBuilder:<init>
        // (Ljava/lang/String;)V
        // 35: aload_1
        // 36: invokevirtual 58 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 39: invokevirtual 62 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 42: astore_3
        // 43: aconst_null
        // 44: astore 4
        // 46: aconst_null
        // 47: astore_2
        // 48: new 85 java/io/File
        // 51: dup
        // 52: aload 5
        // 54: invokespecial 175 java/io/File:<init> (Ljava/lang/String;)V
        // 57: invokevirtual 427 java/io/File:mkdirs ()Z
        // 60: pop
        // 61: new 85 java/io/File
        // 64: dup
        // 65: aload_3
        // 66: invokespecial 175 java/io/File:<init> (Ljava/lang/String;)V
        // 69: invokevirtual 375 java/io/File:createNewFile ()Z
        // 72: pop
        // 73: new 189 java/io/BufferedInputStream
        // 76: dup
        // 77: aload_0
        // 78: ldc_w 428
        // 81: invokespecial 431 java/io/BufferedInputStream:<init>
        // (Ljava/io/InputStream;I)V
        // 84: astore_2
        // 85: new 433 java/io/BufferedOutputStream
        // 88: dup
        // 89: new 377 java/io/FileOutputStream
        // 92: dup
        // 93: aload_3
        // 94: invokespecial 434 java/io/FileOutputStream:<init>
        // (Ljava/lang/String;)V
        // 97: ldc_w 428
        // 100: invokespecial 437 java/io/BufferedOutputStream:<init>
        // (Ljava/io/OutputStream;I)V
        // 103: astore 4
        // 105: sipush 4096
        // 108: newarray byte
        // 110: astore 5
        // 112: aload_2
        // 113: aload 5
        // 115: invokevirtual 440 java/io/InputStream:read ([B)I
        // 118: istore_3
        // 119: iload_3
        // 120: bipush 255
        // 122: if_icmpne +26 -> 148
        // 125: aload 4
        // 127: ifnull +8 -> 135
        // 130: aload 4
        // 132: invokevirtual 372 java/io/OutputStream:close ()V
        // 135: aload_2
        // 136: ifnull +124 -> 260
        // 139: aload_2
        // 140: invokevirtual 158 java/io/InputStream:close ()V
        // 143: iconst_1
        // 144: istore_2
        // 145: goto -139 -> 6
        // 148: aload 4
        // 150: aload 5
        // 152: iconst_0
        // 153: iload_3
        // 154: invokevirtual 443 java/io/OutputStream:write ([BII)V
        // 157: goto -45 -> 112
        // 160: astore_3
        // 161: aload_2
        // 162: astore_2
        // 163: aload 4
        // 165: astore 4
        // 167: aload_3
        // 168: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 171: aload 4
        // 173: ifnull +8 -> 181
        // 176: aload 4
        // 178: invokevirtual 372 java/io/OutputStream:close ()V
        // 181: aload_2
        // 182: ifnull -39 -> 143
        // 185: aload_2
        // 186: invokevirtual 158 java/io/InputStream:close ()V
        // 189: goto -46 -> 143
        // 192: astore_2
        // 193: aload_2
        // 194: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 197: goto -54 -> 143
        // 200: astore_3
        // 201: aload_3
        // 202: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 205: goto -24 -> 181
        // 208: astore_3
        // 209: aload 4
        // 211: ifnull +8 -> 219
        // 214: aload 4
        // 216: invokevirtual 372 java/io/OutputStream:close ()V
        // 219: aload_2
        // 220: ifnull +7 -> 227
        // 223: aload_2
        // 224: invokevirtual 158 java/io/InputStream:close ()V
        // 227: aload_3
        // 228: athrow
        // 229: astore 4
        // 231: aload 4
        // 233: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 236: goto -17 -> 219
        // 239: astore_2
        // 240: aload_2
        // 241: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 244: goto -17 -> 227
        // 247: astore_3
        // 248: aload_3
        // 249: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 252: goto -117 -> 135
        // 255: astore_2
        // 256: aload_2
        // 257: invokevirtual 167 java/lang/Exception:printStackTrace ()V
        // 260: goto -117 -> 143
        // 263: astore_3
        // 264: aload_2
        // 265: astore_2
        // 266: goto -57 -> 209
        // 269: astore_3
        // 270: aload_2
        // 271: astore_2
        // 272: aload 4
        // 274: astore 4
        // 276: goto -67 -> 209
        // 279: astore_3
        // 280: goto -113 -> 167
        // 283: astore_3
        // 284: aload_2
        // 285: astore_2
        // 286: goto -119 -> 167
        //
        // Exception table:
        // from to target type
        // 105 119 160 java/lang/Exception
        // 148 157 160 java/lang/Exception
        // 185 189 192 java/lang/Exception
        // 176 181 200 java/lang/Exception
        // 48 85 208 finally
        // 167 171 208 finally
        // 214 219 229 java/lang/Exception
        // 223 227 239 java/lang/Exception
        // 130 135 247 java/lang/Exception
        // 139 143 255 java/lang/Exception
        // 85 105 263 finally
        // 105 119 269 finally
        // 148 157 269 finally
        // 48 85 279 java/lang/Exception
        // 85 105 283 java/lang/Exception
        return false;
    }
}
