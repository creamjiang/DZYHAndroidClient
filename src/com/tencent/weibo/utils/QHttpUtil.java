package com.tencent.weibo.utils;

import com.tencent.weibo.beans.QParameter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class QHttpUtil {
    public static String formParamDecode(String paramString) {
        int i = 0;
        int j = 0;
        while (true) {
            Object localObject1;
            int k;
            Object localObject2;
            if (j >= paramString.length()) {
                localObject1 = new byte[i];
                k = 0;
                j = 0;
                if (k < paramString.length())
                    // break label68;
                    localObject2 = "";
            }
            // try
            // {
            // localObject1 = new String(localObject1, "UTF-8");
            // localObject2 = localObject1;
            // return localObject2;
            // if (paramString.charAt(localObject2) == '%')
            // localObject2 += 2;
            // localObject1++;
            // localObject2++;
            // continue;
            // label68: Object localObject3;
            // if (paramString.charAt(k) != '%')
            // {
            // localObject3 = localObject2 + 1;
            // localObject1[localObject2] = (byte)paramString.charAt(k);
            // }
            // for (localObject2 = localObject3; ; localObject2 = localObject3)
            // {
            // k++;
            // break;
            // StringBuilder localStringBuilder = new StringBuilder();
            // localStringBuilder.append(paramString.charAt(k + 1));
            // localStringBuilder.append(paramString.charAt(k + 2));
            // localObject3 = localObject2 + 1;
            // localObject1[localObject2] =
            // Integer.valueOf(localStringBuilder.toString(), 16).byteValue();
            // k += 2;
            // }
            // }
            // catch (UnsupportedEncodingException
            // localUnsupportedEncodingException)
            // {
            // while (true)
            // localUnsupportedEncodingException.printStackTrace();
            // }
        }
    }

    public static String getContentType(File paramFile) {
        return "png";
    }

    public static String getContentType(String paramString) {
        return null;
    }

    public static List<QParameter> getQueryParameters(String paramString) {
        if (paramString.startsWith("?"))
            paramString = paramString.substring(1);
        ArrayList localArrayList = new ArrayList();
        if ((paramString != null) && (!paramString.equals("")))
            for (Object localObject : paramString.split("&")) {
                if ((localObject == null)
                        || (((String) localObject).equals(""))
                        || (((String) localObject).indexOf('=') <= -1))
                    continue;
                localObject = ((String) localObject).split("=");
                // if (localObject.length <= 1)
                // continue;
                // localArrayList.add(new QParameter(localObject[0],
                // localObject[1]));
            }
        return (List<QParameter>) localArrayList;
    }
}
