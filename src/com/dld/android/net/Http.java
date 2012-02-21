package com.dld.android.net;

import com.dld.android.net.exception.HttpException;
import com.dld.android.util.Device;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class Http extends Connector {
    private Callback callback;
    private boolean cancel;
    private HttpConfig config;
    private HttpURLConnection conn;
    private InputStream in;
    private OutputStream outputstream;

    public Http(Callback paramCallback, HttpConfig paramHttpConfig) {
        this.callback = paramCallback;
        this.config = paramHttpConfig;
    }

    private void doRun() {
        int i = 0;
        int m = 0;
        if (i >= this.config.getRetry())
            label13: return;
        while (true) {
            Object localObject4 = null;
            ByteArrayOutputStream localByteArrayOutputStream = null;
            try {
                Object localObject2 = Device.getInstance().getNetType(
                        this.callback.getContext());
                if ((localObject2 != null)
                        && (!((String) localObject2).equals("")))
                    continue;
                this.callback.onException(new IOException(
                        "No available net service!"));
                close();
                // break label13;
                Object localObject6 = this.callback.getUrl();
                LogUtils.log("Http", "完整的uri" + (String) localObject6);
                if ((localObject6 != null)
                        && (!((String) localObject6).equals("")))
                    continue;
                this.callback.onException(new NullPointerException(
                        "Uri is null"));
                close();
                // break label13;
                int j = ((String) localObject6).indexOf("?");
                if (j != -1)
                    continue;
                Object localObject5 = localObject6;
                this.conn = null;
                if (!this.callback.custom)
                    continue;
                Object localObject3 = "GET";
                LogUtils.log("Http", localObject3);
                boolean bool2 = this.cancel;
                if (!bool2)
                    continue;
                close();
                // break label13;
                localObject5 = ((String) localObject6).substring(0,
                        ((String) localObject3).length());
                // continue;
                localObject3 = this.config.getRequestMethod();
                // continue;
                if (!((String) localObject2).equalsIgnoreCase("cmwap"))
                    continue;
                localObject6 = new URL(
                        NetHelper.getCMWapHost((String) localObject6));
                LogUtils.log("Http", "if" + localObject6);
                this.conn = ((HttpURLConnection) ((URL) localObject6)
                        .openConnection());
                this.conn.setRequestProperty("X-Online-Host",
                        NetHelper.getCMWapParam((String) localObject5));
                this.conn.setRequestProperty("Accept", "*/*");
                LogUtils.log("Http", "<Url: > " + localObject6);
                if (!this.config.isGzip())
                    continue;
                this.conn.setRequestProperty("Accept-Encoding", "gzip");
                if (!((String) localObject2).equalsIgnoreCase("cmwap"))
                    continue;
                this.conn.setConnectTimeout(2 * this.config.getTimeOut());
                this.conn.setReadTimeout(2 * this.config.getTimeOut());
                this.conn.setRequestMethod((String) localObject3);
                this.conn.setDoOutput(true);
                this.conn.setUseCaches(false);
                this.conn.setInstanceFollowRedirects(true);
                if ((!((String) localObject3).equalsIgnoreCase("POST"))
                        && ((!this.callback.custom) || (this.callback
                                .getPostParam() == null)))
                    continue;
                localObject3 = this.callback.getPostParam();
                if (localObject3 == null)
                    continue;
                this.outputstream = this.conn.getOutputStream();
                this.outputstream.write((byte[]) localObject3, 0,
                        ((byte[]) localObject3).length);
                this.outputstream.flush();
                this.outputstream.close();
                localObject2 = new StringBuilder("<Http Url>: ");
                localObject3 = new String((byte[]) localObject3, 4, -4
                        + ((byte[]) localObject3).length, "utf-8");
                LogUtils.log("DMessage", (String) localObject3);
                int k = this.conn.getResponseCode();
                LogUtils.log("DMessage", "<Response> : " + k);
                if ((k < 400) && (k >= 0))
                    continue;
                localObject2 = this.callback;
                localObject4 = new HttpException(k);
                ((Callback) localObject2).exception((Exception) localObject4);
                close();
                // break label13;
                LogUtils.log("Http", "<Url: > else");
                System.setProperty("http.keepAlive", "true");
                localObject6 = new URL((String) localObject6);
                this.conn = ((HttpURLConnection) ((URL) localObject6)
                        .openConnection());
                this.conn.setRequestProperty("Connection", "Keep-Alive");
                continue;
            } catch (EOFException localEOFException) {
                this.callback.recieve(null);
                i = 3;
                close();
                // break;
                this.conn.setConnectTimeout(this.config.getTimeOut());
                this.conn.setReadTimeout(this.config.getTimeOut());
                continue;
            } catch (IOException e) {
                LogUtils.e("test", e);
                i++;
                if (i < this.config.getRetry())
                    continue;
                this.callback.exception(e);
                close();
                // break;
                boolean bool1 = this.cancel;
                if (!bool1)
                    continue;
                close();
                // break label13;
                if (!StringUtils.equals("gzip",
                        this.conn.getHeaderField("Content-Encoding")))
                    continue;
                try {
                    this.in = new GZIPInputStream(this.conn.getInputStream());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                localObject4 = new byte[2048];
                localByteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    m = this.in.read((byte[]) localObject4);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if (m == -1) {
                    this.callback.recieve(localByteArrayOutputStream
                            .toByteArray());
                    i = 3;
                    close();
                    // break;
                    try {
                        this.in = this.conn.getInputStream();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    continue;
                }
            } finally {
                close();
                localByteArrayOutputStream.write((byte[]) localObject4, 0, m);
            }

            // m = m;
        }
    }

    // /** @deprecated */
    // public void cancel()
    // {
    // monitorenter;
    // try
    // {
    // this.cancel = true;
    // monitorexit;
    // return;
    // }
    // finally
    // {
    // localObject = finally;
    // monitorexit;
    // }
    // throw localObject;
    // }

    public void close() {
        try {
            if (this.outputstream != null) {
                this.outputstream.close();
                this.outputstream = null;
            }
            if (this.in != null) {
                this.in.close();
                this.in = null;
            }
            if (this.conn != null) {
                this.conn.disconnect();
                this.conn = null;
            }
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }

    public void run() {
        try {
            doRun();
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }
}
