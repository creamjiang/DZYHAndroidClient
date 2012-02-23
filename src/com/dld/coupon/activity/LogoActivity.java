package com.dld.coupon.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.acra.ErrorReporter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.ServiceUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.UserConfig;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;

public class LogoActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHANGE_CITY = 100;
    private static long lastRun;
    private static final Object lock = new Object();
    private boolean firstRun = false;
    Handler handler = new Handler();
    private boolean inited = false;

    static {
        lastRun = System.currentTimeMillis();
    }

    private void init() {
        try {
            if (GenericDAO.list.isEmpty())
                GenericDAO.initCityList();
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void nextActivity() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, MainActivity.class);
        startActivity(localIntent);
        finish();
    }

    protected void onActivityResult(int paramInt1, int paramInt2,
            Intent paramIntent) {
        if ((paramInt2 == -1) && (paramInt1 == REQUEST_CODE_CHANGE_CITY)) {
            if (this.firstRun)
                UserConfig.setAllowLocationService(this, true);
            nextActivity();
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        String str = getResources().getString(R.string.version);
        ErrorReporter.getInstance().putCustomData("version", str);
        long l = System.currentTimeMillis();
        if (l - lastRun > 1200000L) {
            Cache.clear();
            lastRun = l;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logo);
        this.firstRun = StringUtils.isEmpty(SharePersistent.getInstance()
                .getPerference(getApplicationContext(), "allow_run_app"));
      
        new Timer().schedule(new TimerTask() {
            private void checkInit() {
                if (LogoActivity.this.inited) {
                    if (!LogoActivity.this.firstRun)
                        return;
                    Intent localIntent = new Intent();
                    localIntent.setClass(LogoActivity.this, CityActivity.class);
                    LogoActivity.this.startActivityForResult(localIntent,
                            REQUEST_CODE_CHANGE_CITY);
                }
                
            }

            public void run() {
            	/**
                 if (LogoActivity.this.firstRun)
                 ServiceUtil.show(new Runnable() {
                 public void run() {
                 checkInit();
                 }
                 }, LogoActivity.this.handler);
                 else {
                 checkInit();
                 }
                 **/
            	checkInit();
            }
        }, 500);
        new Thread() {
            public void run() {
                LogoActivity.this.init();
                LogoActivity.this.inited = true;
            }
        }.start();
        
    }
    
   
      

}
