package com.dld.android.net;

public class NetHelper {
    public static StringBuilder addParam(StringBuilder paramStringBuilder,
            String paramString1, String paramString2) {
        if (paramStringBuilder == null)
            paramStringBuilder = new StringBuilder();
        paramStringBuilder.append(paramString1 + "=" + paramString2 + "&");
        return paramStringBuilder;
    }

    public static String getCMWapHost(String paramString) {
        if (paramString.startsWith("http://"))
            paramString = paramString.substring(7);
        int i = paramString.indexOf("/");
        String str;
        if (i >= 0)
            str = "http://10.0.0.172/".concat(paramString.substring(i + 1,
                    paramString.length()));
        else
            str = "";
        return str;
    }

    public static String getCMWapParam(String paramString) {
        if (paramString.startsWith("http://"))
            paramString = paramString.substring(7);
        int i = paramString.indexOf("/");
        if (i < 0)
            i = paramString.length();
        return paramString.substring(0, i);
    }

    public static String getHost(String paramString) {
        int i = paramString.indexOf("?");
        if (i >= 0)
            paramString = paramString.substring(0, i);
        return paramString;
    }

    public static String getParam(String paramString) {
        int i = paramString.indexOf("?");
        String str;
        if (i >= 0)
            str = paramString.substring(i + 1, paramString.length());
        else
            str = null;
        return str;
    }
}
