package com.dld.coupon.activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.R;

public class SettingScreen extends PreferenceActivity {
    CheckBoxPreference storeAndSendWeiboPreference = null;

    private void initWidget() {
        this.storeAndSendWeiboPreference = ((CheckBoxPreference) findPreference("key_storeand_send"));
        this.storeAndSendWeiboPreference.setChecked(SharePersistent
                .getInstance().getPerferenceBoolean(this, "isallowsendweibo"));
        this.storeAndSendWeiboPreference
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(
                            Preference paramPreference, Object paramObject) {
                        SettingScreen.this
                                .updateStoreandSendValue(((Boolean) paramObject)
                                        .booleanValue());
                        return true;
                    }
                });
    }

    private void updateStoreandSendValue(boolean paramBoolean) {
        SharePersistent.getInstance().savePerference(this, "isallowsendweibo",
                paramBoolean);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.screen_setting);
        initWidget();
    }
}
