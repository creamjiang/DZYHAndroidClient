package com.dld.android.pay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IAlixPay.Stub;
import com.alipay.android.app.IRemoteServiceCallback;

public class MobileSecurePayer {
    static String TAG = "MobileSecurePayer";
    Integer lock = Integer.valueOf(0);
    Activity mActivity = null;
    IAlixPay mAlixPay = null;
    private ServiceConnection mAlixPayConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName paramComponentName,
                IBinder paramIBinder) {
            synchronized (MobileSecurePayer.this.lock) {
                MobileSecurePayer.this.mAlixPay = IAlixPay.Stub
                        .asInterface(paramIBinder);
                MobileSecurePayer.this.lock.notify();
                return;
            }
        }

        public void onServiceDisconnected(ComponentName paramComponentName) {
            MobileSecurePayer.this.mAlixPay = null;
        }
    };
    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        public void startActivity(String paramString1, String paramString2,
                int paramInt, Bundle paramBundle) throws RemoteException {
            Intent localIntent = new Intent("android.intent.action.MAIN", null);
            if (paramBundle == null)
                paramBundle = new Bundle();
            try {
                paramBundle.putInt("CallingPid", paramInt);
                localIntent.putExtras(paramBundle);
                localIntent.setClassName(paramString1, paramString2);
                MobileSecurePayer.this.mActivity.startActivity(localIntent);
                return;
            } catch (Exception localException) {
                while (true)
                    localException.printStackTrace();
            }
        }
    };
    boolean mbPaying = false;

    public boolean pay(String paramString, Handler paramHandler, int paramInt,
            Activity paramActivity) {
        boolean i = true;
        if (!this.mbPaying) {
            this.mbPaying = i;
            this.mActivity = paramActivity;
            if (this.mAlixPay == null)
                // this.mActivity.bindService(new
                // Intent(IAlixPay.class.getName()), this.mAlixPayConnection,
                // i);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            synchronized (MobileSecurePayer.this.lock) {
                                if (MobileSecurePayer.this.mAlixPay == null)
                                    MobileSecurePayer.this.lock.wait();
                                MobileSecurePayer.this.mAlixPay
                                        .registerCallback(MobileSecurePayer.this.mCallback);
                                // ??? =
                                // MobileSecurePayer.this.mAlixPay.Pay(this.val$strOrderInfo);
                                MobileSecurePayer.this.mbPaying = false;
                                MobileSecurePayer.this.mAlixPay
                                        .unregisterCallback(MobileSecurePayer.this.mCallback);
                                MobileSecurePayer.this.mActivity
                                        .unbindService(MobileSecurePayer.this.mAlixPayConnection);
                                Message localMessage = new Message();
                                // localMessage.what = this.val$myWhat;
                                // localMessage.obj = ???;
                                // this.val$callback.sendMessage(localMessage);
                                return;
                            }
                        } catch (Exception localException) {
                            while (true) {
                                localException.printStackTrace();
                                // ??? = new Message();
                                // ((Message)???).what = this.val$myWhat;
                                // ((Message)???).obj =
                                // localException.toString();
                                // this.val$callback.sendMessage((Message)???);
                            }
                        }
                    }
                }).start();
        } else {
            i = false;
        }
        return i;
    }
}
