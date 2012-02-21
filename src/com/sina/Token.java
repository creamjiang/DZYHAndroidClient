package com.sina;

import javax.crypto.spec.SecretKeySpec;

public class Token {
    protected String mOauth_Token_Secret = "";
    private String mOauth_verifier = "";
    protected SecretKeySpec mSecretKeySpec;
    private String mToken = "";
    protected String[] responseStr = null;

    public Token() {
    }

    public Token(String paramString) {
        this.responseStr = paramString.split("&");
        this.mOauth_Token_Secret = getParameter("oauth_token_secret");
        this.mToken = getParameter("oauth_token");
    }

    public Token(String paramString1, String paramString2) {
        this.mToken = paramString1;
        this.mOauth_Token_Secret = paramString2;
    }

    public String getParameter(String paramString) {
        String str2 = null;
        String[] arrayOfString = this.responseStr;
        int j = arrayOfString.length;
        int i = 0;
        while (i < j) {
            String str1 = arrayOfString[i];
            if (!str1.startsWith(paramString + '=')) {
                i++;
                continue;
            }
            str2 = str1.split("=")[1].trim();
        }
        return str2;
    }

    public String getSecret() {
        return this.mOauth_Token_Secret;
    }

    protected SecretKeySpec getSecretKeySpec() {
        return this.mSecretKeySpec;
    }

    public String getToken() {
        return this.mToken;
    }

    public String getVerifier() {
        return this.mOauth_verifier;
    }

    protected void setSecretKeySpec(SecretKeySpec paramSecretKeySpec) {
        this.mSecretKeySpec = paramSecretKeySpec;
    }

    public void setVerifier(String paramString) {
        this.mOauth_verifier = paramString;
    }
}
