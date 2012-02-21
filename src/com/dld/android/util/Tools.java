package com.dld.android.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {
    private static final String TAG = "DMessage";

    public static boolean isEmptyString(String paramString) {
        boolean i;
        if ((paramString == null) || (paramString.equals("")))
            i = true;
        else
            i = false;
        return i;
    }

    public static String md5encrypt(String paramString)
            throws NoSuchAlgorithmException {
        Object localObject = MessageDigest.getInstance("MD5");
        ((MessageDigest) localObject).update(paramString.getBytes());
        localObject = ((MessageDigest) localObject).digest();
        StringBuffer localStringBuffer = new StringBuffer("");
        for (int i = 0; i < ((byte[]) localObject).length; i++) {
            int k = 0xFF & ((byte[]) localObject)[i];
            int j = k >>> 4;
            k &= 15;
            int m;
            if (j >= 10)
                m = 87;
            else
                m = 48;
            localStringBuffer.append((char) (m + j));
            if (k >= 10)
                j = 87;
            else
                j = 48;
            localStringBuffer.append((char) (j + k));
        }
        return (String) localStringBuffer.toString();
    }

    public static void printByteArray(String paramString,
            byte[] paramArrayOfByte) {
        LogUtils.log("DMessage", paramString);
        char[] arrayOfChar = new char[16];
        arrayOfChar[0] = 48;
        arrayOfChar[1] = 49;
        arrayOfChar[2] = 50;
        arrayOfChar[3] = 51;
        arrayOfChar[4] = 52;
        arrayOfChar[5] = 53;
        arrayOfChar[6] = 54;
        arrayOfChar[7] = 55;
        arrayOfChar[8] = 56;
        arrayOfChar[9] = 57;
        arrayOfChar[10] = 65;
        arrayOfChar[11] = 66;
        arrayOfChar[12] = 67;
        arrayOfChar[13] = 68;
        arrayOfChar[14] = 69;
        arrayOfChar[15] = 70;
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            if (i != 0)
                if (i % 16 != 0) {
                    if (i % 4 == 0)
                        System.out.print(" ");
                } else
                    System.out.println("");
            int k = paramArrayOfByte[i];
            if (k < 0)
                k = 128 + (k + 128);
            int j = k / 16;
            String str = "" + arrayOfChar[j];
            k %= 16;
            str = str + arrayOfChar[k];
            System.out.print(str + " ");
        }
        System.out.println("\n-------------------------------------------\n");
    }

    public static void printException(Exception paramException) {
        String str = paramException.getMessage();
        if (str == null)
            str = "null";
        LogUtils.log("DMessage", str);
        StackTraceElement[] arrayOfStackTraceElement = paramException
                .getStackTrace();
        for (int i = 0; i < arrayOfStackTraceElement.length; i++)
            LogUtils.log("DMessage", arrayOfStackTraceElement[i].getFileName()
                    + " " + arrayOfStackTraceElement[i].getMethodName()
                    + " line:" + arrayOfStackTraceElement[i].getLineNumber());
    }
}
