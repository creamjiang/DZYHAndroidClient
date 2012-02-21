package com.dld.protocol.json;

import android.content.Context;

import com.dld.android.net.Callback;
import com.dld.android.net.Http;
import com.dld.android.net.HttpConfig;
import com.dld.android.net.HttpUser;
import com.dld.android.net.Param;
import com.dld.android.util.LogUtils;
import com.dld.android.util.Tools;
import com.dld.protocol.DataConvert;
import com.dld.protocol.ProtocolHelper;
import com.tencent.weibo.utils.Cache;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Protocol {
    protected boolean cache = true;
    protected Callback callback;
    protected Context context;
    public byte[] data;
    protected String host;
    protected Http http;
    protected HttpUser httpUser;
    protected boolean isInflat;
    protected String method;
    protected OnJsonProtocolResult onProtocolResult;
    protected Param param;
    protected String type;

    public Protocol() {
    }

    public Protocol(Context paramContext, String paramString1,
            String paramString2, Param paramParam) {
        this(paramContext, "/NetGate_v2/GetJson?", paramString1, paramString2,
                paramParam, true, true);
    }

    public Protocol(Context paramContext, String paramString1,
            String paramString2, Param paramParam, boolean paramBoolean) {
        this(paramContext, "/NetGate_v2/GetJson?", paramString1, paramString2,
                paramParam, true, paramBoolean);
    }

    public Protocol(Context paramContext, String paramString1,
            String paramString2, String paramString3, Param paramParam,
            boolean paramBoolean1, boolean paramBoolean2) {
        this.type = paramString2;
        this.method = paramString3;
        this.host = paramString1;
        this.param = paramParam;
        this.context = paramContext;
        this.isInflat = paramBoolean1;
        this.cache = paramBoolean2;
    }

    public String getAbsoluteUrl() {
        return getUrl();
    }

    protected Callback getCallback() {
        return new Callback() {
            public Context getContext() {
                return Protocol.this.context;
            }

            public byte[] getPostParam() {
                byte[] arrayOfByte2;
                try {
                    if (this.custom) {
                        arrayOfByte2 = Protocol.this.data;
                    } else {
                        byte[] arrayOfByte1 = Protocol.this.getParam()
                                .toString().getBytes("utf-8");
                        arrayOfByte2 = new byte[4 + arrayOfByte1.length];
                        DataConvert.writeInt(arrayOfByte2, 0,
                                arrayOfByte1.length);
                        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 4,
                                arrayOfByte1.length);
                    }
                } catch (Exception localException) {
                    onException(new Exception("Get post param error !"));
                    arrayOfByte2 = null;
                }
                return arrayOfByte2;
            }

            public String getUrl() {
                String str = Protocol.this.getUrl();
                if (this.custom)
                    str = str + Protocol.this.getParam().toString();
                return str;
            }

            public void onException(Exception paramException) {
                Tools.printException(paramException);
                if (Protocol.this.onProtocolResult != null) {
                    if (!(paramException instanceof IOException))
                        Protocol.this.onProtocolResult.onException(getUrl(),
                                paramException);
                    else
                        Protocol.this.onProtocolResult.onException(getUrl(),
                                new IOException());
                } else
                    LogUtils.log("IOException", paramException.toString());
            }

            public void onRecieve(byte[] paramArrayOfByte) {
                try {
                    if (Protocol.this.onProtocolResult == null) {
                        Object localObject1 = ProtocolHelper.readToByteArray(
                                new ByteArrayInputStream(paramArrayOfByte))
                                .toByteArray();
                        localObject1 = new String((byte[]) localObject1, 0,
                                ((byte[]) localObject1).length, "utf-8");
                        LogUtils.log("DMessage", "On Json Recieve: "
                                + (String) localObject1);
                        return;
                    }
                    if (Protocol.this.onProtocolResult.getJsonBeanClass() == null)
                        Protocol.this.onProtocolResult.onResult(getUrl(), null);
                } catch (Exception localException) {
                    LogUtils.log("Protocol", "这里抛异常了");
                    localException.printStackTrace();
                    onException(localException);
                }
                Object localObject2 = null;
                try {
                    localObject2 = ProtocolHelper.readToByteArray(
                            new ByteArrayInputStream(paramArrayOfByte))
                            .toByteArray();
                    localObject2 = new String((byte[]) localObject2, 0,
                            ((byte[]) localObject2).length, "utf-8");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                LogUtils.log("DMessage", "On Json Recieve: "
                        + (String) localObject2);
                LogUtils.log("Protocol", "没有抛异常");
                if (Protocol.this.onProtocolResult.getJsonBeanClass().equals(
                        String.class)) {
                    Protocol.this.onProtocolResult.onResult(getUrl(),
                            localObject2);
                } else {
                    try {
                        localObject2 = Protocol.this
                                .parse((String) localObject2);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Protocol.this.onProtocolResult.onResult(getUrl(),
                            localObject2);
                    if (Protocol.this.cache)
                        Cache.put(Protocol.this.getUrl()
                                + Protocol.this.getParam().toString(),
                                localObject2);
                }
            }
        };
    }

    public Param getParam() {
        return null;
    }

    public String getUrl() {
        return "http://clientservice.12580.com:8180" + this.host;
    }

    protected Object parse(String paramString) throws Exception {
        return null;
    }

    protected Object parseXml(String paramString) throws Exception {
        Element localElement = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(paramString.getBytes("UTF-8")))
                .getDocumentElement();
        return new Response().parseXml(localElement);
    }

    public void startTrans() {
        startTrans(null);
    }

    public void startTrans(OnJsonProtocolResult paramOnJsonProtocolResult) {
        startTrans(paramOnJsonProtocolResult, 10);
    }

    public void startTrans(OnJsonProtocolResult paramOnJsonProtocolResult,
            int paramInt) {
        startTrans(paramOnJsonProtocolResult, paramInt, 0);
    }

    public void startTrans(OnJsonProtocolResult paramOnJsonProtocolResult,
            int paramInt1, int paramInt2) {
        String str = getUrl();
        Object localObject = getParam().toString();
        LogUtils.log("test", "url: " + str + (String) localObject);
        LogUtils.log("test", "method: " + this.method);
        if (paramOnJsonProtocolResult != null) {
            this.onProtocolResult = paramOnJsonProtocolResult;
            localObject = Cache.getCache(str + (String) localObject);
            if ((localObject != null) && (this.cache))
                ;
        } else {
            this.callback = getCallback();
            LogUtils.log("Protocol", this.callback.custom + "/");
            if (paramInt2 <= 0)
                localObject = new HttpConfig(paramInt1);
            else
                localObject = new HttpConfig(paramInt1, paramInt2);
            this.http = new Http(this.callback, (HttpConfig) localObject);
            this.http.start();
            return;
        }
        this.onProtocolResult.onResult(getAbsoluteUrl(), localObject);
    }

    public void startTransForResetPsw(
            OnJsonProtocolResult paramOnJsonProtocolResult) {
        startTrans(paramOnJsonProtocolResult, 60, 1);
    }

    public void startTransForUser(
            OnJsonProtocolResult paramOnJsonProtocolResult, int paramInt1,
            int paramInt2, Param paramParam) {
        Object localObject = getUrl();
        String str = getParam().toString();
        if (paramOnJsonProtocolResult != null) {
            this.onProtocolResult = paramOnJsonProtocolResult;
            localObject = Cache.getCache(localObject + str);
            if ((localObject != null) && (this.cache))
                ;
        } else {
            this.callback = getCallback();
            this.callback.custom = false;
            if (paramInt2 <= 0)
                localObject = new HttpConfig(paramInt1);
            else
                localObject = new HttpConfig(paramInt1, paramInt2);
            this.httpUser = new HttpUser(this.callback,
                    (HttpConfig) localObject, paramParam, this.context);
            this.httpUser.start();
            return;
        }
        this.onProtocolResult.onResult(getAbsoluteUrl(), localObject);
    }

    public void startTransForUser(
            OnJsonProtocolResult paramOnJsonProtocolResult, int paramInt,
            Param paramParam) {
        startTransForUser(paramOnJsonProtocolResult, paramInt, 1, paramParam);
    }

    public void startTransForUser(
            OnJsonProtocolResult paramOnJsonProtocolResult, Param paramParam) {
        startTransForUser(paramOnJsonProtocolResult, 30, paramParam);
    }

    public void startTransForUserGet(
            OnJsonProtocolResult paramOnJsonProtocolResult, int paramInt1,
            int paramInt2, Param paramParam) {
        String str = getUrl();
        Object localObject = getParam().toString();
        LogUtils.log("test", "url: " + str + (String) localObject);
        LogUtils.log("test", "method: " + this.method);
        if (paramOnJsonProtocolResult == null) {
            paramInt2 = 1;
            paramInt1 = 600;
        } else {
            this.onProtocolResult = paramOnJsonProtocolResult;
            localObject = Cache.getCache(str + (String) localObject);
            if ((localObject != null) && (this.cache))
                this.onProtocolResult.onResult(getAbsoluteUrl(), localObject);
            // break label190;
        }
        this.callback = getCallback();
        if (paramInt2 <= 0)
            localObject = new HttpConfig(paramInt1);
        else
            localObject = new HttpConfig(paramInt1, paramInt2);
        this.httpUser = new HttpUser(this.callback, (HttpConfig) localObject,
                paramParam, this.context);
        this.httpUser.start();
        return;
        // label190: this.onProtocolResult.onResult(getAbsoluteUrl(),
        // localObject);
    }

    public void startTransForUserGet(
            OnJsonProtocolResult paramOnJsonProtocolResult, int paramInt,
            Param paramParam) {
        startTransForUserGet(paramOnJsonProtocolResult, paramInt, 1, paramParam);
    }

    public void startTransForUserGet(
            OnJsonProtocolResult paramOnJsonProtocolResult, Param paramParam) {
        startTransForUserGet(paramOnJsonProtocolResult, 30, paramParam);
    }

    public void startTransForUserNoRetry(
            OnJsonProtocolResult paramOnJsonProtocolResult, int paramInt,
            Param paramParam) {
        startTransForUser(paramOnJsonProtocolResult, paramInt, 1, paramParam);
    }

    public void startTransForUserNoRetry(
            OnJsonProtocolResult paramOnJsonProtocolResult, Param paramParam) {
        startTransForUserNoRetry(paramOnJsonProtocolResult, 10, paramParam);
    }

    public static abstract class OnJsonProtocolResult {
        private Class<?> jsonBeanClass;

        public OnJsonProtocolResult() {
        }

        public OnJsonProtocolResult(Class<?> paramClass) {
            this.jsonBeanClass = paramClass;
        }

        public Class<?> getJsonBeanClass() {
            return this.jsonBeanClass;
        }

        public abstract void onException(String paramString,
                Exception paramException);

        public abstract void onResult(String paramString, Object paramObject);
    }
}
