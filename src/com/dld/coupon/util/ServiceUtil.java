package com.dld.coupon.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;

public class ServiceUtil {
    public static String get() {
        return "店连店网站（www.dld.com）所提供的各项服务的所有权和运作权归店连店运营公司所有。\n　　服务条款的修改权归店连店运营公司所有。用户应当随时关注本服务条款的修改，并决定是否继续使用本网站提供的各项服务。 用户使用店连店网站各项服务的行为，将被视为用户对本服务条款的同意和承诺遵守。\n　　店连店保留随时修改或中断服务而不需知照用户的权利。\n　　店连店行使修改或中断服务的权利，不需对用户或第三方负责。如果您对本网站条款或本网站的使用还存有任何疑问，您可以联系我们。\n　　条款详细信息可参见网站客户端下载提示。";
    }

    public static void show(final Runnable paramRunnable, final Handler paramHandler) {
        final Activity localActivity = ActivityManager.getCurrent();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(
                localActivity).setTitle("店连店网服务条款").setCancelable(false)
                .setMessage(get());
        if (paramRunnable == null)
            localBuilder.setPositiveButton("确定", null);
        else
            localBuilder.setPositiveButton("同意",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                             SharePersistent.getInstance().savePerference(localActivity,"allow_run_app", String.valueOf(true));
                             paramRunnable.run();
                        }
                    }).setNegativeButton("拒绝",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                                localActivity.finish();
                        }
                    });
        paramHandler.post(new Runnable() {
            public void run() {
                ServiceUtil.show(new Runnable() {
                    public void run() {
                        }
                        }, new Handler());
            }
        });
    }
}
