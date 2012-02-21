package com.dld.android.pay;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileSecurePayHelper {
    static final String TAG = "MobileSecurePayHelper";
    Context mContext = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            try {
                switch (paramMessage.what) {
                default:
                case 2:
                }
                // while (true)
                {
                    super.handleMessage(paramMessage);
                    // break;
                    MobileSecurePayHelper.this.closeProgress();
                    String str = (String) paramMessage.obj;
                    MobileSecurePayHelper.this.showInstallConfirmDialog(
                            MobileSecurePayHelper.this.mContext, str);
                }
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
    };
    private ProgressDialog mProgress = null;

    public MobileSecurePayHelper(Context paramContext) {
        this.mContext = paramContext;
    }

    public static PackageInfo getApkInfo(Context paramContext,
            String paramString) {
        return paramContext.getPackageManager().getPackageArchiveInfo(
                paramString, 128);
    }

    public String checkNewUpdate(PackageInfo paramPackageInfo) {
        String str = null;
        try {
            JSONObject localJSONObject = sendCheckNewUpdate(paramPackageInfo.versionName);
            if (localJSONObject.getString("needUpdate")
                    .equalsIgnoreCase("true")) {
                str = localJSONObject.getString("updateUrl");
                str = str;
            }
            return str;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    void closeProgress() {
        try {
            if (this.mProgress != null) {
                this.mProgress.dismiss();
                this.mProgress = null;
            }
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public boolean detectMobile_sp() {
        boolean bool = isMobile_spExist();
        if (!bool) {
            final String str = this.mContext.getCacheDir().getAbsolutePath()
                    + "/temp.apk";
            LogUtils.log("main", str);
            retrieveApkFromAssets(this.mContext, "alipay_plugin223_0309.apk",
                    str);
            this.mProgress = BaseHelper.showProgress(this.mContext, null,
                    "正在检测安全支付服务版本", false, true);
            new Thread(new Runnable() {
                public void run() {
                    Object localObject = MobileSecurePayHelper.getApkInfo(
                            MobileSecurePayHelper.this.mContext, str);
                    localObject = MobileSecurePayHelper.this
                            .checkNewUpdate((PackageInfo) localObject);
                    if (localObject != null)
                        MobileSecurePayHelper.this.retrieveApkFromNet(
                                MobileSecurePayHelper.this.mContext,
                                (String) localObject, str);
                    localObject = new Message();
                    ((Message) localObject).what = 2;
                    ((Message) localObject).obj = str;
                    MobileSecurePayHelper.this.mHandler
                            .sendMessage((Message) localObject);
                }
            }).start();
        }
        return bool;
    }

    public boolean isMobile_spExist() {
        boolean j = false;
        List localList = this.mContext.getPackageManager()
                .getInstalledPackages(0);
        int i = 0;
        while (i < localList.size()) {
            if (!((PackageInfo) localList.get(i)).packageName
                    .equalsIgnoreCase("com.alipay.android.app")) {
                i++;
                continue;
            }
            j = true;
        }
        return j;
    }

    public boolean retrieveApkFromAssets(Context paramContext,
            String paramString1, String paramString2) {
        boolean i = false;
        try {
            InputStream localInputStream = paramContext.getAssets().open(
                    paramString1);
            Object localObject = new File(paramString2);
            ((File) localObject).createNewFile();
            localObject = new FileOutputStream((File) localObject);
            byte[] arrayOfByte = new byte[1024];
            while (true) {
                int j = localInputStream.read(arrayOfByte);
                if (j <= 0) {
                    ((FileOutputStream) localObject).close();
                    localInputStream.close();
                    i = true;
                    break;
                }
                ((FileOutputStream) localObject).write(arrayOfByte, 0, j);
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return i;
    }

    public boolean retrieveApkFromNet(Context paramContext,
            String paramString1, String paramString2) {
        boolean bool = false;
        try {
            bool = new NetworkManager(this.mContext).urlDownloadToFile(
                    paramContext, paramString1, paramString2);
            bool = bool;
            return bool;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public JSONObject sendCheckNewUpdate(String paramString) {
        JSONObject localJSONObject1 = null;
        try {
            JSONObject localJSONObject2 = new JSONObject();
            localJSONObject2.put("action", "update");
            JSONObject localJSONObject3 = new JSONObject();
            localJSONObject3.put("platform", "android");
            localJSONObject3.put("version", paramString);
            localJSONObject3.put("partner", "");
            localJSONObject2.put("data", localJSONObject3);
            localJSONObject1 = sendRequest(localJSONObject2.toString());
            localJSONObject1 = localJSONObject1;
            return localJSONObject1;
        } catch (JSONException localJSONException) {
            while (true)
                localJSONException.printStackTrace();
        }
    }

    public JSONObject sendRequest(String paramString) {
        NetworkManager localNetworkManager = new NetworkManager(this.mContext);
        JSONObject localJSONObject = null;
        try {
            // monitorenter;
            try {
                String str = localNetworkManager.SendAndWaitResponse(
                        paramString, "https://msp.alipay.com/x.htm");
                // monitorexit;
                localJSONObject = new JSONObject(str);
                localJSONObject = localJSONObject;
                return localJSONObject;
            } finally {
                // monitorexit;
            }
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public void showInstallConfirmDialog(final Context paramContext,
            final String paramString) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
        localBuilder.setIcon(R.drawable.infoicon);
        localBuilder.setTitle(R.string.confirm_install_hint);
        localBuilder.setMessage(R.string.confirm_install);
        // localBuilder.setPositiveButton("确定", new
        // DialogInterface.OnClickListener(paramString, paramContext)
        localBuilder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                        BaseHelper.chmod("777", paramString);
                        try {
                            Thread.sleep(500L);
                            Intent localIntent = new Intent(
                                    "android.intent.action.VIEW");
                            localIntent.addFlags(268435456);
                            localIntent.setDataAndType(
                                    Uri.parse("file://" + paramString),
                                    "application/vnd.android.package-archive");
                            paramContext.startActivity(localIntent);
                            return;
                        } catch (InterruptedException localInterruptedException) {
                            while (true)
                                localInterruptedException.printStackTrace();
                        }
                    }
                });
        localBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface,
                            int paramInt) {
                    }
                });
        localBuilder.show();
    }
}
