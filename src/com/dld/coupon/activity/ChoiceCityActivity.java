package com.dld.coupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.dld.coupon.db.CityBean;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;
import java.util.ArrayList;

public class ChoiceCityActivity extends CityActivity {
    private boolean isFirst = true;

    public void initView() {
        super.initView();
        this.list = ((ArrayList) this.list.clone());
        if (this.isFirst) {
            this.list.remove(0);
            this.isFirst = false;
        }
        this.mAdapter = new CityActivity.cityListAdapter(this);
        this.expandableListView = ((ExpandableListView) findViewById(R.id.public_city_list));
        this.expandableListView.setAdapter(this.mAdapter);
        this.expandableListView.expandGroup(0);
        this.expandableListView.setChildIndicator(null);
        this.expandableListView.setGroupIndicator(null);
        this.expandableListView
                .setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    public boolean onGroupClick(
                            ExpandableListView paramExpandableListView,
                            View paramView, int paramInt, long paramLong) {
                        for (int i = 0;; i++) {
                            try {
                                if (i < ChoiceCityActivity.this.mAdapter
                                        .getGroupCount()) {
                                    if (i != paramInt) {
                                        paramExpandableListView
                                                .collapseGroup(i);
                                        continue;
                                    }
                                    paramExpandableListView.expandGroup(i);
                                }
                            } catch (Exception localException) {
                            }
                            return true;
                        }
                    }
                });
        this.expandableListView
                .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    public boolean onChildClick(
                            ExpandableListView paramExpandableListView,
                            View paramView, int paramInt1, int paramInt2,
                            long paramLong) {
                        Object localObject1;
                        if (paramInt1 != 0)
                            localObject1 = ((CityBean) ChoiceCityActivity.this.list
                                    .get(paramInt1)).cityName;
                        else
                            localObject1 = null;
                        Object localObject2 = ((CityBean) ((CityBean) ChoiceCityActivity.this.list
                                .get(paramInt1)).chirdrenCityList
                                .get(paramInt2)).cityName;
                        if (!StringUtils.isEmpty((String) localObject1))
                            if ("港澳地区".equals(localObject1)) {
                                localObject1 = null;
                            } else {
                                localObject1 = new StringBuffer(
                                        (String) localObject1);
                                ((StringBuffer) localObject1).append("省");
                                localObject1 = ((StringBuffer) localObject1)
                                        .toString();
                            }
                        if ((!"香港".equals(localObject2))
                                && (!"澳门".equals(localObject2))) {
                            localObject2 = new StringBuffer(
                                    (String) localObject2);
                            ((StringBuffer) localObject2).append("市");
                            localObject2 = ((StringBuffer) localObject2)
                                    .toString();
                        }
                        Bundle localBundle = new Bundle();
                        localBundle
                                .putString("province", (String) localObject1);
                        localBundle.putString("city", (String) localObject2);
                        ChoiceCityActivity.this.intent = new Intent(
                                ChoiceCityActivity.this,
                                AddNewAddressActivity.class);
                        ChoiceCityActivity.this.intent.putExtras(localBundle);
                        ChoiceCityActivity.this.setResult(1,
                                ChoiceCityActivity.this.intent);
                        ChoiceCityActivity.this.finish();
                        return false;
                    }
                });
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }
}
