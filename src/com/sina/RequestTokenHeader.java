package com.sina;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RequestTokenHeader extends HttpHeaderFactory {
    public void addAdditionalParams(WeiboParameters paramWeiboParameters1,
            WeiboParameters paramWeiboParameters2) {
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
            localWeiboParameters.add("oauth_callback",
                    paramWeiboParameters.getValue("oauth_callback"));
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
            localWeiboParameters.add("source",
                    paramWeiboParameters.getValue("source"));
        } else {
            localWeiboParameters = null;
        }
        return localWeiboParameters;
    }
}
