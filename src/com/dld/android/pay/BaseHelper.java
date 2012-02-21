package com.dld.android.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.dld.coupon.activity.ConfirmPayActivity;
import com.dld.coupon.activity.ConfirmPayActivity.AlixOnCancelListener;

import java.io.IOException;
import org.json.JSONObject;

public class BaseHelper {
    public static void chmod(String paramString1, String paramString2) {
        try {
            String str = "chmod " + paramString1 + " " + paramString2;
            Runtime.getRuntime().exec(str);
            return;
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }

    // ERROR //
    public static String convertStreamToString(
            java.io.InputStream paramInputStream) {
        // Byte code:
        // 0: new 49 java/io/BufferedReader
        // 3: dup
        // 4: new 51 java/io/InputStreamReader
        // 7: dup
        // 8: aload_0
        // 9: invokespecial 54 java/io/InputStreamReader:<init>
        // (Ljava/io/InputStream;)V
        // 12: invokespecial 57 java/io/BufferedReader:<init>
        // (Ljava/io/Reader;)V
        // 15: astore_2
        // 16: new 17 java/lang/StringBuilder
        // 19: dup
        // 20: invokespecial 58 java/lang/StringBuilder:<init> ()V
        // 23: astore_1
        // 24: aload_2
        // 25: invokevirtual 61 java/io/BufferedReader:readLine
        // ()Ljava/lang/String;
        // 28: astore_3
        // 29: aload_3
        // 30: ifnonnull +12 -> 42
        // 33: aload_0
        // 34: invokevirtual 66 java/io/InputStream:close ()V
        // 37: aload_1
        // 38: invokevirtual 32 java/lang/StringBuilder:toString
        // ()Ljava/lang/String;
        // 41: areturn
        // 42: aload_1
        // 43: aload_3
        // 44: invokevirtual 26 java/lang/StringBuilder:append
        // (Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 47: pop
        // 48: goto -24 -> 24
        // 51: astore_2
        // 52: aload_2
        // 53: invokevirtual 45 java/io/IOException:printStackTrace ()V
        // 56: aload_0
        // 57: invokevirtual 66 java/io/InputStream:close ()V
        // 60: goto -23 -> 37
        // 63: astore_2
        // 64: aload_2
        // 65: invokevirtual 45 java/io/IOException:printStackTrace ()V
        // 68: goto -31 -> 37
        // 71: astore_1
        // 72: aload_0
        // 73: invokevirtual 66 java/io/InputStream:close ()V
        // 76: aload_1
        // 77: athrow
        // 78: astore_2
        // 79: aload_2
        // 80: invokevirtual 45 java/io/IOException:printStackTrace ()V
        // 83: goto -7 -> 76
        // 86: astore_2
        // 87: aload_2
        // 88: invokevirtual 45 java/io/IOException:printStackTrace ()V
        // 91: goto -54 -> 37
        //
        // Exception table:
        // from to target type
        // 24 29 51 java/io/IOException
        // 42 48 51 java/io/IOException
        // 56 60 63 java/io/IOException
        // 24 29 71 finally
        // 42 48 71 finally
        // 52 56 71 finally
        // 72 76 78 java/io/IOException
        // 33 37 86 java/io/IOException
        return null;
    }

    public static void showDialog(Activity paramActivity, String paramString1,
            String paramString2, int paramInt) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(
                paramActivity);
        localBuilder.setIcon(paramInt);
        localBuilder.setTitle(paramString1);
        localBuilder.setMessage(paramString2);
        localBuilder.setPositiveButton("确定", null);
        localBuilder.show();
    }

    public static ProgressDialog showProgress(Context paramContext,
            CharSequence paramCharSequence1, CharSequence paramCharSequence2,
            boolean paramBoolean1, boolean paramBoolean2) {
        ProgressDialog localProgressDialog = new ProgressDialog(paramContext);
        localProgressDialog.setTitle(paramCharSequence1);
        localProgressDialog.setMessage(paramCharSequence2);
        localProgressDialog.setIndeterminate(paramBoolean1);
        localProgressDialog.setCancelable(false);
        localProgressDialog
                .setOnCancelListener(new ConfirmPayActivity.AlixOnCancelListener(
                        (Activity) paramContext));
        localProgressDialog.show();
        return localProgressDialog;
    }

    public static JSONObject string2JSON(String paramString1,
            String paramString2) {
        JSONObject localJSONObject = new JSONObject();
        try {
            String[] arrayOfString2 = paramString1.split(paramString2);
            for (int i = 0; i < arrayOfString2.length; i++) {
                String[] arrayOfString1 = arrayOfString2[i].split("=");
                localJSONObject.put(arrayOfString1[0], arrayOfString2[i]
                        .substring(1 + arrayOfString1[0].length()));
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return localJSONObject;
    }

    class AlixOnCancelListener implements DialogInterface.OnCancelListener {
        Activity mcontext;

        AlixOnCancelListener(Activity arg2) {
            // Object localObject;
            this.mcontext = arg2;
        }

        public void onCancel(DialogInterface paramDialogInterface) {
            this.mcontext.onKeyDown(4, null);
        }
    }
}

/*
 * Location: G:\DEV\MobileDev\反编译\classes_dex2jar\ Qualified Name:
 * com.dld.android.pay.BaseHelper JD-Core Version: 0.6.0
 */