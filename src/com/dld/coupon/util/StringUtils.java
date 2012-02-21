package com.dld.coupon.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StringUtils {
    public static String breakLines(String paramString) {
        String str;
        if (!isEmpty(paramString)) {
            char c2 = paramString.charAt(0);
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(c2);
            int j = 1;
            int i = paramString.length();
            while (j < i) {
                char c1 = paramString.charAt(j);
                if (((c1 < '') && (c2 >= ''))
                        || ((c1 >= '') && (c2 < '') && (c1 != ' ') && (c2 != ' ')))
                    localStringBuilder.append(' ');
                localStringBuilder.append(c1);
                c2 = c1;
                j++;
            }
            str = localStringBuilder.toString();
        } else {
            str = "";
        }
        return str;
    }

    public static String breakLines(String paramString, int paramInt1,
            int paramInt2) {
        String str = "";
        if (!isEmpty(paramString)) {
            char c2 = paramString.charAt(0);
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(c2);
            int m = charLength(c2);
            int j = paramInt1 * 4;
            int i = paramInt2 * 4;
            int k = 1;
            int n = 1;
            int i1 = paramString.length();
            while (true) {
                if (n < i1) {
                    char c1 = paramString.charAt(n);
                    if (((c1 < '') && (c2 >= ''))
                            || ((c1 >= '') && (c2 < '') && (c1 != ' ') && (c2 != ' '))) {
                        localStringBuilder.append(' ');
                        m++;
                    }
                    localStringBuilder.append(c1);
                    c2 = c1;
                    m += charLength(c1);
                    if ((m < j) || (k == 0)) {
                        if ((m < i) || (n == i1 - 1))
                            n++;
                        // break label208;
                        localStringBuilder.append("...");
                    }
                } else {
                    str = localStringBuilder.toString();
                    break;
                }
                k = 0;
                localStringBuilder.append("\n");
                label208: n++;
            }
        }
        return str;
    }

    private static int charLength(char paramChar) {
        int i;
        if (paramChar < '') {
            if (!isAlphaOrNumeric(paramChar))
                i = 1;
            else
                i = 2;
        } else
            i = 4;
        return i;
    }

    public static boolean checkPhone(String paramString) {
        return paramString.matches("\\+?\\d*?(13|15|18)\\d{9}$");
    }

    public static String convertByteArrayToString(byte[] paramArrayOfByte)
            throws UnsupportedEncodingException {
        return convertByteArrayToString(paramArrayOfByte, "utf-8");
    }

    public static String convertByteArrayToString(byte[] paramArrayOfByte,
            String paramString) throws UnsupportedEncodingException {
        return new String(paramArrayOfByte, paramString);
    }

    public static String encryptNick(String paramString) {
        if (checkPhone(paramString)) {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(paramString.substring(0, 4));
            localStringBuilder.append("****");
            localStringBuilder.append(paramString.substring(8,
                    paramString.length()));
            paramString = localStringBuilder.toString();
        }
        return paramString;
    }

    public static boolean equals(String paramString1, String paramString2) {
        boolean bool;
        if ((paramString1 != null) || (paramString2 != null)) {
            if ((paramString1 != null) && (paramString2 != null))
                bool = paramString1.equals(paramString2);
            else
                bool = false;
        } else
            bool = true;
        return bool;
    }

    public static long formatTimeToMillisecond(String paramString)
            throws Exception {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        long l = 0L;
        try {
            l = localSimpleDateFormat.parse(paramString).getTime();
            l = l;
            label25: return l;
        } catch (Exception localException) {
            // break label25;
        }
        return l;
    }

    public static String getDiscountText(String paramString) {
        return "";
    }

    public static String getPhoneNumber(Context paramContext) {
        return ((TelephonyManager) paramContext.getSystemService("phone"))
                .getLine1Number();
    }

    public static String getPhoneNumberFromSIm(Context paramContext) {
        return ((TelephonyManager) paramContext.getSystemService("phone"))
                .getLine1Number();
    }

    private static boolean isAlphaOrNumeric(char paramChar) {
        boolean i;
        if (((paramChar >= 'A') && (paramChar <= 'Z'))
                || ((paramChar >= 'a') && (paramChar <= 'z'))
                || ((paramChar < '0') || (paramChar > '9')
                        || (paramChar == 'i') || (paramChar == 'l') || (paramChar == '1')))
            i = false;
        else
            i = true;
        return i;
    }

    public static boolean isEmpty(String paramString) {
        boolean i;
        if ((paramString == null) || ("".equals(paramString.trim())))
            i = true;
        else
            i = false;
        return i;
    }

    public static String secondToDate(long paramLong) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long
                .valueOf(paramLong));
    }

    public static String shortenNumber(double paramDouble) {
        String str = Double.toString(paramDouble);
        if (str.endsWith(".0"))
            str = str.substring(0, -2 + str.length());
        return str;
    }

    public static ArrayList<String> stringToStringList(String paramString1,
            String paramString2) {
        ArrayList localArrayList;
        try {
            String[] arrayOfString = paramString1.split(paramString2);
            localArrayList = new ArrayList();
            for (int i = 0; i < arrayOfString.length; i++)
                localArrayList.add(arrayOfString[i]);
        } catch (Exception localException) {
            localArrayList = new ArrayList();
        }
        return localArrayList;
    }

    public static Set<String> stringToStringSet(String paramString1,
            String paramString2) {
        HashSet localHashSet = null;
        if (isEmpty(paramString1))
            localHashSet = new HashSet();
        // while (true)
        {
            // return localHashSet;
            try {
                String[] arrayOfString = paramString1.split(paramString2);
                localHashSet = new HashSet();
                for (int i = 0; i < arrayOfString.length; i++)
                    localHashSet.add(arrayOfString[i]);
            } catch (Exception localException) {
                // localHashSet = new HashSet();
            }
        }
        return localHashSet;
    }

    public static String toString(Iterable<?> paramIterable, String paramString) {
        Object localObject;
        try {
            StringBuilder localStringBuilder = new StringBuilder();
            localObject = paramIterable.iterator();
            while (true) {
                if (!((Iterator) localObject).hasNext()) {
                    localObject = localStringBuilder.toString();
                    break;
                }
                localStringBuilder.append(((Iterator) localObject).next());
                if (!((Iterator) localObject).hasNext())
                    continue;
                localStringBuilder.append(paramString);
            }
        } catch (Exception localException) {
            localObject = "";
        }
        return (String) localObject;
    }
}
