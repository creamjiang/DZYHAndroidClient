package com.sina;

import android.text.TextUtils;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class XAuthHeader extends HttpHeaderFactory {
    public void addAdditionalParams(WeiboParameters paramWeiboParameters1,
            WeiboParameters paramWeiboParameters2) {
        if ((!TextUtils.isEmpty(paramWeiboParameters2
                .getValue("x_auth_password")))
                && (!TextUtils.isEmpty(paramWeiboParameters2
                        .getValue("x_auth_username")))) {
            paramWeiboParameters1.add("x_auth_password",
                    paramWeiboParameters2.getValue("x_auth_password"));
            paramWeiboParameters1.add("x_auth_username",
                    paramWeiboParameters2.getValue("x_auth_username"));
            paramWeiboParameters1.add("x_auth_mode", "client_auth");
        }
    }

    public String generateSignature(String paramString, Token paramToken)
            throws WeiboException {
        // ((byte[])null);
        try {
            Object localObject = Mac.getInstance("HmacSHA1");
            ((Mac) localObject).init(new SecretKeySpec(
                    (encode(Weibo.APP_SECRET) + "&").getBytes(), "HmacSHA1"));
            localObject = ((Mac) localObject).doFinal(paramString.getBytes());
            return String.valueOf(Utility.base64Encode((byte[]) localObject));
        } catch (InvalidKeyException localInvalidKeyException) {
            throw new WeiboException(localInvalidKeyException);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        }
        throw new WeiboException();
    }

    public WeiboParameters generateSignatureList(
            WeiboParameters paramWeiboParameters) {
        WeiboParameters localWeiboParameters;
        if ((paramWeiboParameters != null)
                && (paramWeiboParameters.size() != 0)) {
            localWeiboParameters = new WeiboParameters();
            localWeiboParameters.add("oauth_consumer_key",
                    paramWeiboParameters.getValue("oauth_consumer_key"));
            localWeiboParameters.add("oauth_nonce",
                    paramWeiboParameters.getValue("oauth_nonce"));
            localWeiboParameters.add("oauth_signature_method",
                    paramWeiboParameters.getValue("oauth_signature_method"));
            localWeiboParameters.add("oauth_timestamp",
                    paramWeiboParameters.getValue("oauth_timestamp"));
            localWeiboParameters.add("oauth_version",
                    paramWeiboParameters.getValue("oauth_version"));
            localWeiboParameters.add("source", Weibo.APP_KEY);
            localWeiboParameters.add("x_auth_mode", "client_auth");
            localWeiboParameters.add("x_auth_password",
                    paramWeiboParameters.getValue("x_auth_password"));
            localWeiboParameters.add("x_auth_username",
                    paramWeiboParameters.getValue("x_auth_username"));
        } else {
            localWeiboParameters = null;
        }
        return localWeiboParameters;
    }
}
