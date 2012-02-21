package com.dld.android.pay;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static boolean doCheck(String paramString1, String paramString2,
            String paramString3) {
        try {
            PublicKey localPublicKey = KeyFactory
                    .getInstance("RSA")
                    .generatePublic(
                            new X509EncodedKeySpec(Base64.decode(paramString3)));
            Signature localSignature = Signature.getInstance("SHA1WithRSA");
            localSignature.initVerify(localPublicKey);
            localSignature.update(paramString1.getBytes("utf-8"));
            boolean bool = localSignature.verify(Base64.decode(paramString2));
            bool = bool;
            return bool;
        } catch (Exception localException) {
            while (true) {
                localException.printStackTrace();
                int i = 0;
            }
        }
    }

    public static String encrypt(String paramString1, String paramString2) {
        Object localObject1 = null;
        try {
            PublicKey localPublicKey = getPublicKeyFromX509("RSA", paramString2);
            localObject1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            ((Cipher) localObject1).init(1, localPublicKey);
            localObject1 = new String(Base64.encode(((Cipher) localObject1)
                    .doFinal(paramString1.getBytes("UTF-8"))));
            return (String) localObject1;
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                Object localObject2 = null;
            }
        }
        return (String) localObject1;
    }

    private static PublicKey getPublicKeyFromX509(String paramString1,
            String paramString2) throws NoSuchAlgorithmException, Exception {
        X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(
                Base64.decode(paramString2));
        return KeyFactory.getInstance(paramString1).generatePublic(
                localX509EncodedKeySpec);
    }

    public static String sign(String paramString1, String paramString2) {
        Object localObject1 = null;
        try {
            localObject1 = new PKCS8EncodedKeySpec(Base64.decode(paramString2));
            PrivateKey localPrivateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate((KeySpec) localObject1);
            localObject1 = Signature.getInstance("SHA1WithRSA");
            ((Signature) localObject1).initSign(localPrivateKey);
            ((Signature) localObject1).update(paramString1.getBytes("utf-8"));
            localObject1 = Base64.encode(((Signature) localObject1).sign());
            localObject1 = localObject1;
            return (String) localObject1;
        } catch (Exception localException) {
            // while (true)
            {
                localException.printStackTrace();
                Object localObject2 = null;
            }
        }
        return (String) localObject1;
    }
}
