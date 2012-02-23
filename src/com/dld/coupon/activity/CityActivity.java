package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.db.CityBean;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.HashMap;

public class CityActivity extends Activity {
    public static ArrayList<NotifyChangeObserver> observers = new ArrayList();
    ImageView cityCheck;
    public Context context;
    public ExpandableListView expandableListView;
    public HashMap<Integer, ImageView> groups = new HashMap();
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what == 2)
                CityActivity.this.initView();
        }
    };
    public Intent intent;
    public ArrayList<CityBean> list = GenericDAO.list;
    public ExpandableListAdapter mAdapter;
    OnCityChoosedListener onCityChoosedListener;

    public static void notifyChange(String paramString1, String paramString2) {
        int i = 0;
        try {
            while (i < observers.size()) {
                ((NotifyChangeObserver) observers.get(i)).onChangeCity(
                        paramString1, paramString2);
                i++;
            }
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    public static void registObserver(
            NotifyChangeObserver paramNotifyChangeObserver) {
        if (!observers.contains(paramNotifyChangeObserver))
            observers.add(paramNotifyChangeObserver);
    }

    public static void unregistObserver(
            NotifyChangeObserver paramNotifyChangeObserver) {
        if (observers.contains(paramNotifyChangeObserver))
            observers.remove(paramNotifyChangeObserver);
    }

    public void initView() {
        setContentView(R.layout.city_view);
        this.mAdapter = new cityListAdapter(this);
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
                                if (i < CityActivity.this.mAdapter
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
                    private void cityChoosed(CityBean paramCityBean) {
                        Bundle localBundle = new Bundle();
                        if (paramCityBean.cityId.length() == 2)
                            paramCityBean.cityId += "00";
                        localBundle.putString("city_id", paramCityBean.cityId);
                        localBundle.putString("city_name",
                                paramCityBean.cityName);
                        CityActivity.this.intent.putExtras(localBundle);
                        SharePersistent.getInstance().savePerference(
                                CityActivity.this.context, "city_id",
                                paramCityBean.cityId);
                        SharePersistent.getInstance().savePerference(
                                CityActivity.this.context, "city_name",
                                paramCityBean.cityName);
                        CityActivity.this.setResult(-1,
                                CityActivity.this.intent);
                        CityActivity.notifyChange(paramCityBean.cityName,
                                paramCityBean.cityId);
                    }

                    public boolean onChildClick(
                            ExpandableListView paramExpandableListView,
                            View paramView, int paramInt1, int paramInt2,
                            long paramLong) {
                        CityBean localCityBean = (CityBean) ((CityBean) CityActivity.this.list
                                .get(paramInt1)).chirdrenCityList
                                .get(paramInt2);
                        if (CityActivity.this.onCityChoosedListener != null)
                            CityActivity.this.onCityChoosedListener
                                    .onCityChoosed(localCityBean);
                        else
                            cityChoosed(localCityBean);
                        CityActivity.this.finish();
                        return false;
                    }
                });
    }

    protected void initialData() {
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.onCityChoosedListener = ((OnCityChoosedListener) Cache
                .getCache("city_choosed_listener"));
        this.context = getApplicationContext();
        this.intent = getIntent();
        requestWindowFeature(1);
        setRequestedOrientation(1);
        if (!"".equals(SharePersistent.getInstance().getPerference(
                this.context, "first_use_db"))) {
            initialData();
            initView();
        } else {
            setContentView(R.layout.loading);
            ((TextView) findViewById(R.id.loading_text))
                    .setText("正在初始化城市列表，请稍候");
            new Thread() {
                public void run() {
                    CityActivity.this.initialData();
                    //CityActivity.this.handler.sendEmptyMessage(2);
                }
            }.start();
        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        boolean bool;
        if ((paramInt != 4)
                || (!StringUtils.isEmpty(SharePersistent.getInstance()
                        .getPerference(getApplicationContext(), "city_id"))))
            bool = super.onKeyDown(paramInt, paramKeyEvent);
        else
            bool = true;
        return bool;
    }

    public static abstract interface NotifyChangeObserver {
        public abstract void onChangeCity(String paramString1,
                String paramString2);
    }

    public static abstract interface OnCityChoosedListener {
        public abstract void onCityChoosed(CityBean paramCityBean);
    }

    class cityListAdapter extends BaseExpandableListAdapter {
        Activity activity;

        public cityListAdapter(Activity arg2) {
            Object localObject;
            this.activity = arg2;
        }

        public Object getChild(int paramInt1, int paramInt2) {
            return CityActivity.this.list.get(paramInt1);
        }

        public long getChildId(int paramInt1, int paramInt2) {
            return paramInt2;
        }

        public View getChildView(int paramInt1, int paramInt2,
                boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
            CityBean localCityBean = (CityBean) ((CityBean) CityActivity.this.list
                    .get(paramInt1)).chirdrenCityList.get(paramInt2);
            View localView = ((LayoutInflater) CityActivity.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.city_textview_layout, null);
            Object localObject = (TextView) localView
                    .findViewById(R.id.single_textview);
            CityActivity.this.cityCheck = ((ImageView) localView
                    .findViewById(R.id.cityImg));
            ((TextView) localObject).setTextColor(-12369085);
            ((TextView) localObject).setText(localCityBean.cityName);
            try {
                localObject = SharePersistent.get("city_name");
                if ((!StringUtils.isEmpty((String) localObject))
                        && (localCityBean.cityName.equals(localObject)))
                    localView.findViewById(R.id.cityImg).setVisibility(0);
                return localView;
            } catch (Exception localException) {
                while (true)
                    LogUtils.e("test", "", localException);
            }
        }

        public int getChildrenCount(int paramInt) {
            return ((CityBean) CityActivity.this.list.get(paramInt)).chirdrenCityList
                    .size();
        }

        public Object getGroup(int paramInt) {
            return CityActivity.this.list.get(paramInt);
        }

        public int getGroupCount() {
            return CityActivity.this.list.size();
        }

        public long getGroupId(int paramInt) {
            return paramInt;
        }

        public View getGroupView(int paramInt, boolean paramBoolean,
                View paramView, ViewGroup paramViewGroup) {
            View localView = getGroupView((CityBean) CityActivity.this.list
                    .get(paramInt));
            ImageView localImageView = (ImageView) localView
                    .findViewById(R.id.indicator);
            int i;
            if (!paramBoolean)
                i = R.drawable.right;
            else
                i = R.drawable.down;
            localImageView.setImageResource(i);
            CityActivity.this.groups.put(Integer.valueOf(paramInt),
                    localImageView);
            return localView;
        }

        public View getGroupView(CityBean paramCityBean) {
            View localView = ((LayoutInflater) CityActivity.this.context
                    .getSystemService("layout_inflater")).inflate(
                    R.layout.citylist_item_layout, null);
            String str = paramCityBean.cityName;
            ((TextView) localView.findViewById(R.id.city_item_title))
                    .setText(str);
            return localView;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int paramInt1, int paramInt2) {
            return true;
        }
    }
}
