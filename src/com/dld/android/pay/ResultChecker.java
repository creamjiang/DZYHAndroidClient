package com.dld.android.pay;

import org.json.JSONObject;

public class ResultChecker {
    public static final int RESULT_CHECK_SIGN_FAILED = 1;
    public static final int RESULT_CHECK_SIGN_SUCCEED = 2;
    public static final int RESULT_INVALID_PARAM = 0;
    String mContent;

    public ResultChecker(String paramString) {
        this.mContent = paramString;
    }

    public int checkSign() {
        int i = 2;
        try {
            String str1 = BaseHelper.string2JSON(this.mContent, ";").getString(
                    "result");
            String str2 = str1.substring(1, -1 + str1.length());
            str1 = str2.substring(0, str2.indexOf("&sign_type="));
            Object localObject = BaseHelper.string2JSON(str2, "&");
            str2 = ((JSONObject) localObject).getString("sign_type").replace(
                    "\"", "");
            localObject = ((JSONObject) localObject).getString("sign").replace(
                    "\"", "");
            if (str2.equalsIgnoreCase("RSA")) {
                boolean bool = Rsa
                        .doCheck(
                                str1,
                                (String) localObject,
                                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZZJ1/uhCk25i+mHmk/VTYFDTKH949RYEjotkVIobSlRw61nrKZ9kBgz58P8DJ/BydDtOMcJGMXJlNFhD7Z+FNdF5fJYXhQ5H9oaCyxKkaUb6t+wl/DT7YigeZBZKu4HhELLCj39qw3sUY8bX6HP1owoSmj/rIjG9zfrsQ35ob9wIDAQAB");
                if (!bool)
                    i = 1;
            }
            return i;
        } catch (Exception localException) {
            while (true) {
                i = 0;
                localException.printStackTrace();
            }
        }
    }

    String getSuccess() {
        Object localObject = null;
        try {
            String str = BaseHelper.string2JSON(this.mContent, ";").getString(
                    "result");
            localObject = BaseHelper.string2JSON(
                    str.substring(1, -1 + str.length()), "&").getString(
                    "success");
            str = ((String) localObject).replace("\"", "");
            localObject = str;
            return (String) localObject;
        } catch (Exception localException) {
            // while (true)
            localException.printStackTrace();
        }
        return (String) localObject;
    }

    boolean isPayOk() {
        boolean i = false;
        if ((getSuccess().equalsIgnoreCase("true")) && (checkSign() == 2))
            i = true;
        return i;
    }
}
