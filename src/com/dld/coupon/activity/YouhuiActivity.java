package com.dld.coupon.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.dld.coupon.view.RecommendLinearLayout;
import com.dld.coupon.activity.R;

public class YouhuiActivity extends BaseActivity {
    RecommendLinearLayout layout;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.youhui);
        this.layout = ((RecommendLinearLayout) findViewById(R.id.layout));
        this.layout.onShow();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.layout.onHide();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return false;
    }
}
