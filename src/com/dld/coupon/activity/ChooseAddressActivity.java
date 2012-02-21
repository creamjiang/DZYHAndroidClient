package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.activity.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseAddressActivity extends Activity {
    private Map<Integer, List<String>> addressesMap;
    private String city;
    public Context context;
    private GenericDAO dao;
    private List<String> districts;
    public ExpandableListView expandableListView;
    private String keyword;
    public ExpandableListAdapter mAdapter;
    private int type;

    private void initData() {
        this.city = SharePersistent.getInstance().getPerference(this.context,
                "city_name");
        this.districts = this.dao.getDistricts(this.city);
        this.addressesMap = new HashMap();
        Intent localIntent = getIntent();
        this.keyword = localIntent.getStringExtra("keyword");
        this.type = localIntent.getIntExtra("type", 0);
    }

    private void initView() {
        setContentView(R.layout.choose_address_view);
        this.mAdapter = new MyAdapter();
        this.expandableListView = ((ExpandableListView) findViewById(R.id.listview));
        this.expandableListView.setAdapter(this.mAdapter);
        this.expandableListView.expandGroup(0);
        this.expandableListView.setChildIndicator(null);
        this.expandableListView.setGroupIndicator(null);
        this.expandableListView
                .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    public boolean onChildClick(
                            ExpandableListView paramExpandableListView,
                            View paramView, int paramInt1, int paramInt2,
                            long paramLong) {
                        String str = null;
                        boolean bool = true;
                        if (paramInt2 != 0) {
                            bool = false;
                            str = (String) ((List) ChooseAddressActivity.this.addressesMap
                                    .get(Integer.valueOf(paramInt1)))
                                    .get(paramInt2);
                        } else if (paramInt1 != 0) {
                            str = (String) ChooseAddressActivity.this.districts
                                    .get(paramInt1);
                        }
                        Activity localActivity = ActivityManager.getCurrent();
                        Intent localIntent = new Intent();
                        localIntent.setClass(localActivity,
                                SearchResultActivity.class);
                        localIntent.putExtra("keyword",
                                ChooseAddressActivity.this.keyword);
                        localIntent.putExtra("type",
                                ChooseAddressActivity.this.type);
                        localIntent.putExtra("address", str);
                        localIntent.putExtra("is_district", bool);
                        localActivity.startActivity(localIntent);
                        ChooseAddressActivity.this.finish();
                        return false;
                    }
                });
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.dao = GenericDAO.getInstance(this.context);
        this.context = getApplicationContext();
        requestWindowFeature(1);
        setRequestedOrientation(1);
        initData();
        initView();
    }

    class MyAdapter extends BaseExpandableListAdapter {
        MyAdapter() {
        }

        private List<String> getAddresses(int paramInt) {
            Object localObject = (List) ChooseAddressActivity.this.addressesMap
                    .get(Integer.valueOf(paramInt));
            if (localObject == null) {
                localObject = (String) ChooseAddressActivity.this.districts
                        .get(paramInt);
                localObject = ChooseAddressActivity.this.dao.getAddresses(
                        ChooseAddressActivity.this.city, (String) localObject);
                ChooseAddressActivity.this.addressesMap.put(
                        Integer.valueOf(paramInt), (List<String>) localObject);
            }
            return (List<String>) localObject;
        }

        public Object getChild(int paramInt1, int paramInt2) {
            return ChooseAddressActivity.this.districts.get(paramInt1);
        }

        public long getChildId(int paramInt1, int paramInt2) {
            return paramInt2;
        }

        public View getChildView(int paramInt1, int paramInt2,
                boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
            View localView = ((LayoutInflater) ChooseAddressActivity.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.address_textview_layout, null);
            TextView localTextView = (TextView) localView
                    .findViewById(R.id.single_textview);
            localTextView.setTextColor(-12369085);
            localTextView.setText((CharSequence) getAddresses(paramInt1).get(
                    paramInt2));
            return localView;
        }

        public int getChildrenCount(int paramInt) {
            return getAddresses(paramInt).size();
        }

        public Object getGroup(int paramInt) {
            return null;
        }

        public int getGroupCount() {
            return ChooseAddressActivity.this.districts.size();
        }

        public long getGroupId(int paramInt) {
            return paramInt;
        }

        public View getGroupView(int paramInt, boolean paramBoolean,
                View paramView, ViewGroup paramViewGroup) {
            View localView = ((LayoutInflater) ChooseAddressActivity.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.address_item_layout, null);
            ImageView localImageView = (ImageView) localView
                    .findViewById(2131492890);
            int i;
            if (!paramBoolean)
                i = R.drawable.right_small;
            else
                i = R.drawable.down_small;
            localImageView.setImageResource(i);
            ((TextView) localView.findViewById(2131492889))
                    .setText((String) ChooseAddressActivity.this.districts
                            .get(paramInt));
            return localView;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int paramInt1, int paramInt2) {
            return true;
        }
    }

    public static abstract interface NotifyChangeObserver {
        public abstract void onChangeCity(String paramString1,
                String paramString2);
    }
}
