package com.sina;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RequestHeader extends HttpHeaderFactory {
    public void addAdditionalParams(WeiboParameters paramWeiboParameters1,
            WeiboParameters paramWeiboParameters2) {
    }

    public String generateSignature(String paramString, Token paramToken)
            throws WeiboException {
        // ((byte[])null);
        try {
            Object localObject = Mac.getInstance("HmacSHA1");
            if (paramToken.getSecretKeySpec() == null)
                paramToken.setSecretKeySpec(new SecretKeySpec(
                        (encode(Weibo.APP_SECRET) + "&" + encode(paramToken
                                .getSecret())).getBytes(), "HmacSHA1"));
            ((Mac) localObject).init(paramToken.getSecretKeySpec());
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
        WeiboParameters localWeiboParameters = null;
        int i = 0;
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
            localWeiboParameters.add("oauth_token",
                    paramWeiboParameters.getValue("oauth_token"));
            localWeiboParameters.add("oauth_version",
                    paramWeiboParameters.getValue("oauth_version"));
            localWeiboParameters.add("source",
                    paramWeiboParameters.getValue("source"));
            i = -1 + paramWeiboParameters.getLocation("source");
        }
        // while (i < paramWeiboParameters.size())
        // {
        // String str = paramWeiboParameters.getKey(i);
        // localWeiboParameters.add(str, paramWeiboParameters.getValue(str));
        // i++;
        // continue;
        // localWeiboParameters = null;
        // }
        return localWeiboParameters;
    }
}
