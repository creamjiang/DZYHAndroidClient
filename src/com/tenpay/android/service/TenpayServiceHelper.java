package com.tenpay.android.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TenpayServiceHelper {
    protected static final int MSG_DISMISS_PROGRESS_DLG = 1;
    protected static final int MSG_DOWNLOAD_INSTALL_ERROR = 2;
    protected static final int MSG_SHOW_PROGRESS_DLG = 0;
    protected static final String TAG = "TenpayServiceHelper";
    protected boolean bLogEnabled;
    IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        public void startActivity(String paramString1, String paramString2,
                Bundle paramBundle) throws RemoteException {
            Intent localIntent = new Intent();
            try {
                localIntent.putExtras(paramBundle);
                localIntent.setClassName(paramString1, paramString2);
                if (TenpayServiceHelper.this.mContext != null)
                    TenpayServiceHelper.this.mContext
                            .startActivity(localIntent);
                label46: return;
            } catch (Exception localException) {
                // break label46;
                return;
            }
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName paramComponentName,
                IBinder paramIBinder) {
            synchronized (TenpayServiceHelper.this.mLock) {
                if (TenpayServiceHelper.this.bLogEnabled)
                    Log.d("TenpayServiceHelper", "Service connected");
                TenpayServiceHelper.this.mService = ITenpayService.Stub
                        .asInterface(paramIBinder);
                TenpayServiceHelper.this.mLock.notify();
                return;
            }
        }

        public void onServiceDisconnected(ComponentName paramComponentName) {
            TenpayServiceHelper.this.mService = null;
        }
    };
    protected Context mContext;
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            try {
                switch (paramMessage.what) {
                case 0:
                    TenpayServiceHelper.this.mProgressDlg = new ProgressDialog(
                            TenpayServiceHelper.this.mContext);
                    TenpayServiceHelper.this.mProgressDlg
                            .setMessage("正在下载财付通手机安全支付服务应用，请稍候...");
                    TenpayServiceHelper.this.mProgressDlg
                            .setIndeterminate(true);
                    TenpayServiceHelper.this.mProgressDlg.setCancelable(false);
                    TenpayServiceHelper.this.mProgressDlg
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                public void onCancel(
                                        DialogInterface paramDialogInterface) {
                                    ((Activity) TenpayServiceHelper.this.mContext)
                                            .onKeyDown(4, null);
                                }
                            });
                    TenpayServiceHelper.this.mProgressDlg.show();
                case 1:
                case 2:
                }
            } catch (Exception localException) {
                localException.printStackTrace();
            }
            if (TenpayServiceHelper.this.mProgressDlg != null) {
                TenpayServiceHelper.this.mProgressDlg.dismiss();
                TenpayServiceHelper.this.mProgressDlg = null;
                // return;
                if (TenpayServiceHelper.this.mProgressDlg != null) {
                    TenpayServiceHelper.this.mProgressDlg.dismiss();
                    TenpayServiceHelper.this.mProgressDlg = null;
                }
                Toast.makeText(TenpayServiceHelper.this.mContext,
                        "下载失败，请检查网络设置后重试！", 1).show();
            }
        }
    };
    protected Object mLock;
    protected ProgressDialog mProgressDlg;
    protected ITenpayService mService = null;
    protected boolean mbLogining = false;
    protected boolean mbPaying = false;

    private TenpayServiceHelper() {
    }

    public TenpayServiceHelper(Context paramContext) {
        this.mContext = paramContext;
        this.mLock = new Object();
    }

    protected boolean chmod(String paramString1, String paramString2) {
        boolean i = false;
        try {
            String str = "chmod " + paramString1 + " " + paramString2;
            Runtime.getRuntime().exec(str).waitFor();
            i = true;
            return i;
        } catch (IOException localIOException) {
            // while (true)
            localIOException.printStackTrace();
        } catch (InterruptedException localInterruptedException) {
            // while (true)
            localInterruptedException.printStackTrace();
        }
        return i;
    }

    protected boolean getFileFromAssets(String paramString1, String paramString2) {
        boolean i = false;
        try {
            InputStream localInputStream = this.mContext.getAssets().open(
                    paramString1);
            Object localObject = new File(paramString2);
            if (((File) localObject).exists())
                ((File) localObject).delete();
            ((File) localObject).createNewFile();
            FileOutputStream localFileOutputStream = new FileOutputStream(
                    (File) localObject);
            localObject = new byte[1024];
            while (true) {
                int j = localInputStream.read((byte[]) localObject);
                if (j <= 0) {
                    localFileOutputStream.close();
                    localInputStream.close();
                    i = true;
                    break;
                }
                localFileOutputStream.write((byte[]) localObject, 0, j);
            }
        } catch (FileNotFoundException localFileNotFoundException) {
        } catch (IOException localIOException) {
        }
        return i;
    }

    public void installTenpayService() {
        String str = this.mContext.getCacheDir().getAbsolutePath()
                + "/TenpayService.apk";
        if (!getFileFromAssets("TenpayService.apk", str))
            showGetAppFromNetDlg(this.mContext,
                    "http://cl.tenpay.com/clientv1.0/pkg/TenpayService.apk");
        else
            showInstallConfirmDlg(this.mContext, str);
    }

    public boolean isTenpayServiceInstalled() {
        boolean i = false;
        PackageManager localPackageManager = this.mContext.getPackageManager();
        try {
            localPackageManager.getPackageInfo("com.tenpay.android.service", 0);
            if (this.bLogEnabled)
                Log.d("TenpayServiceHelper",
                        "isTenpayServiceExist() reutrn true");
            i = true;
            return i;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            while (true) {
                if (!this.bLogEnabled)
                    continue;
                Log.d("TenpayServiceHelper",
                        "isTenpayServiceExist() reutrn false");
            }
        }
    }

    public boolean pay(Map<String, String> paramMap, Handler paramHandler,
            int paramInt) {
        boolean i = false;
        if (this.bLogEnabled) {
            Log.d("TenpayServiceHelper", " enter pay()");
            Log.d("TenpayServiceHelper", "payInfo = " + paramMap.toString());
        }
        if (this.mbPaying)
            ;
        while (true) {
            // return i;
            boolean bool = false;
            this.mbPaying = true;
            if (this.mService == null) {
                if (this.bLogEnabled)
                    Log.d("TenpayServiceHelper", "will bindService now!");
                try {
                    bool = this.mContext.bindService(new Intent(
                            ITenpayService.class.getName()), this.mConnection,
                            1);
                    if (!this.bLogEnabled)
                        continue;
                    Log.d("TenpayServiceHelper",
                            "bindService fail, pay() return false!");
                } catch (Exception localException) {
                    localException.printStackTrace();
                }
                if (!this.bLogEnabled)
                    continue;
                Log.d("TenpayServiceHelper",
                        "bindService exception, pay() return false!");
                // continue;
            }
            if (bool) {
                label155: new Thread(new Runnable() {
                    public void run() {
                        if (TenpayServiceHelper.this.bLogEnabled)
                            Log.d("TenpayServiceHelper", "enter pay()");
                        try {
                            synchronized (TenpayServiceHelper.this.mLock) {
                                if (TenpayServiceHelper.this.mService == null) {
                                    if (TenpayServiceHelper.this.bLogEnabled)
                                        Log.d("TenpayServiceHelper",
                                                " service is null now, will wait...");
                                    TenpayServiceHelper.this.mLock.wait();
                                }
                                // TODO
                                // ??? =
                                // TenpayServiceHelper.this.mService.gotoPay(this.val$payInfo,
                                // TenpayServiceHelper.this.mCallback);
                                if (TenpayServiceHelper.this.bLogEnabled)
                                    // Log.d("TenpayServiceHelper",
                                    // "pay() return = " + (String)???);
                                    TenpayServiceHelper.this.mbPaying = false;
                                TenpayServiceHelper.this.mContext
                                        .unbindService(TenpayServiceHelper.this.mConnection);
                                Message localMessage = new Message();
                                // localMessage.what = this.val$msgID;
                                // localMessage.obj = ???;
                                // this.val$callback.sendMessage(localMessage);
                                return;
                            }
                        } catch (Exception localException) {
                            while (true)
                                localException.printStackTrace();
                        }
                    }
                }).start();
            }
            i = true;
        }
    }

    public void setLogEnabled(boolean paramBoolean) {
        this.bLogEnabled = paramBoolean;
    }

    public boolean shareLogin(Map<String, String> paramMap,
            Handler paramHandler, int paramInt) {
        boolean i = false;
        if (this.bLogEnabled) {
            Log.d("TenpayServiceHelper", " enter shareLogin()");
            Log.d("TenpayServiceHelper", "loginInfo = " + paramMap.toString()
                    + ",callback = " + paramHandler + ", msgID = " + paramInt);
        }
        if (this.mbLogining)
            ;
        while (true) {
            boolean bool = false;
            // return i;
            this.mbLogining = true;
            if (this.mService == null) {
                if (this.bLogEnabled)
                    Log.d("TenpayServiceHelper", "will bindService now!");
                try {
                    bool = this.mContext.bindService(new Intent(
                            ITenpayService.class.getName()), this.mConnection,
                            1);

                    if (!this.bLogEnabled)
                        continue;
                    Log.d("TenpayServiceHelper",
                            "bindService fail, shareLogin() return false!");
                } catch (Exception localException) {
                    localException.printStackTrace();
                }
                if (!this.bLogEnabled)
                    continue;
                Log.d("TenpayServiceHelper",
                        "bindService exception, shareLogin() return false!");
                // continue;
            }
            if (bool) {
                label178: new Thread(new Runnable() {
                    public void run() {
                        if (TenpayServiceHelper.this.bLogEnabled)
                            Log.d("TenpayServiceHelper", "enter shareLogin()");
                        try {
                            synchronized (TenpayServiceHelper.this.mLock) {
                                if (TenpayServiceHelper.this.mService == null) {
                                    if (TenpayServiceHelper.this.bLogEnabled)
                                        Log.d("TenpayServiceHelper",
                                                " service is null now, will wait...");
                                    TenpayServiceHelper.this.mLock.wait();
                                }
                                // TODO
                                // ??? =
                                // TenpayServiceHelper.this.mService.shareLogin(this.val$loginInfo,
                                // TenpayServiceHelper.this.mCallback);
                                if (TenpayServiceHelper.this.bLogEnabled)
                                    // Log.d("TenpayServiceHelper",
                                    // "shareLogin() return = " + (String)???);
                                    TenpayServiceHelper.this.mbLogining = false;
                                TenpayServiceHelper.this.mContext
                                        .unbindService(TenpayServiceHelper.this.mConnection);
                                Message localMessage = new Message();
                                // localMessage.what = this.val$msgID;
                                // localMessage.obj = ???;
                                // this.val$callback.sendMessage(localMessage);
                                return;
                            }
                        } catch (Exception localException) {
                            while (true)
                                localException.printStackTrace();
                        }
                    }
                }).start();
            }
            i = true;
        }
    }

    protected void showGetAppFromNetDlg(final Context paramContext,
            final String paramString) {
        final Thread local8 = new Thread() {
            public void run() {
                TenpayServiceHelper.this.mHandler.sendEmptyMessage(0);
                while (true) {
                    int j = 0;
                    try {
                        String str = paramContext.getCacheDir()
                                .getAbsolutePath() + "/TenpayService.apk";
                        boolean bool2 = false;
                        NetworkUtil localNetworkUtil = new NetworkUtil(
                                paramContext);
                        j = 3;
                        int i = j - 1;
                        // break label306;
                        if (!TenpayServiceHelper.this.bLogEnabled)
                            continue;
                        // Log.d("TenpayServiceHelper", "downloadRet = " + bool2
                        // + ",tryNumber = " + i);
                        if (bool2) {
                            boolean bool1 = TenpayServiceHelper.this.chmod(
                                    "666", str);
                            if (!TenpayServiceHelper.this.bLogEnabled)
                                continue;
                            Log.d("TenpayServiceHelper", "chmodRet = " + bool1);
                            TenpayServiceHelper.this.mHandler
                                    .sendEmptyMessage(1);
                            if (!bool1)
                                continue;
                            Intent localIntent = new Intent(
                                    "android.intent.action.VIEW");
                            localIntent.addFlags(268435456);
                            localIntent.setDataAndType(
                                    Uri.parse("file://" + str),
                                    "application/vnd.android.package-archive");
                            paramContext.startActivity(localIntent);
                            // break;
                            // TODO
                            // bool2 =
                            // localIntent.downloadUrlToFile(paramString, str);
                            if (bool2)
                                continue;
                            // j = i;
                            // int i = j - 1;
                            // break label306;
                            TenpayServiceHelper.this.mHandler
                                    .sendEmptyMessage(2);
                        }
                    } catch (Exception localException) {
                        TenpayServiceHelper.this.mHandler.sendEmptyMessage(2);
                        localException.printStackTrace();
                    }
                    TenpayServiceHelper.this.mHandler.sendEmptyMessage(1);
                    TenpayServiceHelper.this.mHandler.sendEmptyMessage(2);
                    // break;
                    // label306: int i = j - 1;
                    if (j > 0)
                        continue;
                }
            }
        };
        new AlertDialog.Builder(paramContext)
                .setTitle("提醒")
                .setMessage("是否下载并安装财付通手机安全支付服务应用？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        local8.start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                    }
                }).create().show();
    }

    protected void showInstallConfirmDlg(final Context paramContext,
            final String paramString) {
        new AlertDialog.Builder(paramContext)
                .setTitle("提醒")
                .setMessage("是否安装财付通手机安全支付服务？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        TenpayServiceHelper.this.chmod("777", paramString);
                        Intent localIntent = new Intent(
                                "android.intent.action.VIEW");
                        localIntent.addFlags(268435456);
                        localIntent.setDataAndType(
                                Uri.parse("file://" + paramString),
                                "application/vnd.android.package-archive");
                        paramContext.startActivity(localIntent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                    }
                }).setCancelable(false).create().show();
    }
}
