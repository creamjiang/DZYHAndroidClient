package com.dld.android.application;

import android.app.Application;

import com.dld.android.util.LogUtils;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;

import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", formUri = "http://www.dld.com/account/crash")
public class MyApplication extends Application {
    public void onCreate() {
        ACRA.init(this);
        TrustManager[] arrayOfTrustManager = new TrustManager[1];
        arrayOfTrustManager[0] = new X509TrustManager() {
            private boolean verify(Principal paramPrincipal) {
                return paramPrincipal.getName().startsWith("O=DouJiao,");
            }

            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
                int i = paramArrayOfX509Certificate.length;
                int j = 0;
                while (j < i) {
                    X509Certificate localX509Certificate = paramArrayOfX509Certificate[j];
                    if ((!verify(localX509Certificate.getIssuerDN()))
                            || (!verify(localX509Certificate.getSubjectDN()))) {
                        j++;
                        continue;
                    }
                    return;
                }
                throw new CertificateException();
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            SSLContext localSSLContext = SSLContext.getInstance("SSL");
            localSSLContext.init(null, arrayOfTrustManager, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(localSSLContext
                    .getSocketFactory());
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String paramString,
                                SSLSession paramSSLSession) {
                            return StringUtils.equals(paramString, "dld.com");
                        }
                    });
            super.onCreate();
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }

    public void onTerminate() {
        MapUtil.destory();
        super.onTerminate();
    }
}