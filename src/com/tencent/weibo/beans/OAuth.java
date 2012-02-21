package com.tencent.weibo.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OAuth {
    private Account account = new Account();
    private String msg = "";
    private String oauth_callback = "null";
    private String oauth_consumer_key = "801004440";
    private String oauth_consumer_secret = "7f5c91da03125b971b44b54f98130dbe";
    private String oauth_nonce = "";
    private String oauth_signature_method = "HMAC-SHA1";
    private String oauth_timestamp = "";
    private String oauth_token = "";
    private String oauth_token_secret = "";
    private String oauth_verifier = "";
    private String oauth_version = "1.0";
    private Random random = new Random();
    private int status = 0;

    public OAuth() {
    }

    public OAuth(String paramString) {
        this.oauth_callback = paramString;
    }

    public OAuth(String paramString1, String paramString2, String paramString3) {
        this.oauth_consumer_key = paramString1;
        this.oauth_consumer_secret = paramString2;
        this.oauth_callback = paramString3;
    }

    private String generateNonce() {
        return String.valueOf(123400 + this.random.nextInt(9876599));
    }

    private String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public List<QParameter> getAccessParams() {
        List localList = getTokenParams();
        if ((this.oauth_verifier != null) && (!"".equals(this.oauth_verifier)))
            localList
                    .add(new QParameter("oauth_verifier", this.oauth_verifier));
        return localList;
    }

    public Account getAccount() {
        return this.account;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getOauth_callback() {
        return this.oauth_callback;
    }

    public String getOauth_consumer_key() {
        return this.oauth_consumer_key;
    }

    public String getOauth_consumer_secret() {
        return this.oauth_consumer_secret;
    }

    public String getOauth_nonce() {
        return this.oauth_nonce;
    }

    public String getOauth_signature_method() {
        return this.oauth_signature_method;
    }

    public String getOauth_timestamp() {
        return this.oauth_timestamp;
    }

    public String getOauth_token() {
        return this.oauth_token;
    }

    public String getOauth_token_secret() {
        return this.oauth_token_secret;
    }

    public String getOauth_verifier() {
        return this.oauth_verifier;
    }

    public String getOauth_version() {
        return this.oauth_version;
    }

    public List<QParameter> getParams() {
        ArrayList localArrayList = new ArrayList();
        this.oauth_timestamp = generateTimeStamp();
        this.oauth_nonce = generateNonce();
        if ((this.oauth_consumer_key != null)
                && (!"".equals(this.oauth_consumer_key.trim())))
            localArrayList.add(new QParameter("oauth_consumer_key",
                    this.oauth_consumer_key));
        if ((this.oauth_signature_method != null)
                && (!"".equals(this.oauth_signature_method.trim())))
            localArrayList.add(new QParameter("oauth_signature_method",
                    this.oauth_signature_method));
        if ((this.oauth_timestamp != null)
                && (!"".equals(this.oauth_timestamp.trim())))
            localArrayList.add(new QParameter("oauth_timestamp",
                    this.oauth_timestamp));
        if ((this.oauth_nonce != null) && (!"".equals(this.oauth_nonce.trim())))
            localArrayList.add(new QParameter("oauth_nonce", this.oauth_nonce));
        if ((this.oauth_callback != null)
                && (!"".equals(this.oauth_callback.trim())))
            localArrayList.add(new QParameter("oauth_callback",
                    this.oauth_callback));
        if ((this.oauth_version != null)
                && (!"".equals(this.oauth_version.trim())))
            localArrayList.add(new QParameter("oauth_version",
                    this.oauth_version));
        return localArrayList;
    }

    public int getStatus() {
        return this.status;
    }

    public List<QParameter> getTokenParams() {
        ArrayList localArrayList = new ArrayList();
        this.oauth_timestamp = generateTimeStamp();
        this.oauth_nonce = generateNonce();
        localArrayList.add(new QParameter("oauth_consumer_key",
                this.oauth_consumer_key));
        localArrayList.add(new QParameter("oauth_signature_method",
                this.oauth_signature_method));
        localArrayList.add(new QParameter("oauth_timestamp",
                this.oauth_timestamp));
        localArrayList.add(new QParameter("oauth_nonce", this.oauth_nonce));
        localArrayList.add(new QParameter("oauth_token", this.oauth_token));
        localArrayList.add(new QParameter("oauth_version", this.oauth_version));
        return localArrayList;
    }

    public void setAccount(Account paramAccount) {
        this.account = paramAccount;
    }

    public void setMsg(String paramString) {
        this.msg = paramString;
    }

    public void setOauth_callback(String paramString) {
        this.oauth_callback = paramString;
    }

    public void setOauth_consumer_key(String paramString) {
        this.oauth_consumer_key = paramString;
    }

    public void setOauth_consumer_secret(String paramString) {
        this.oauth_consumer_secret = paramString;
    }

    public void setOauth_nonce(String paramString) {
        this.oauth_nonce = paramString;
    }

    public void setOauth_signature_method(String paramString) {
        this.oauth_signature_method = paramString;
    }

    public void setOauth_timestamp(String paramString) {
        this.oauth_timestamp = paramString;
    }

    public void setOauth_token(String paramString) {
        this.oauth_token = paramString;
    }

    public void setOauth_token_secret(String paramString) {
        this.oauth_token_secret = paramString;
    }

    public void setOauth_verifier(String paramString) {
        this.oauth_verifier = paramString;
    }

    public void setOauth_version(String paramString) {
        this.oauth_version = paramString;
    }

    public void setStatus(int paramInt) {
        this.status = paramInt;
    }
}
