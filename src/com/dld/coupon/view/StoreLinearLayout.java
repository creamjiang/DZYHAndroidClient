package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.AroundActivity;
import com.dld.coupon.activity.StoreDetailActivity;
import com.dld.coupon.activity.CityActivity.NotifyChangeObserver;
import com.dld.coupon.db.Category;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class StoreLinearLayout extends ListTab implements MainViewBase,
        NotifyChangeObserver {
    protected DownloadListView.DownLoadAdapter adapter;
    Handler handler = new Handler();
    protected boolean isFirst = true;
    private String lastUrl = "";
    private double lat = 0.0D;
    protected DownloadListView list;
    private double lng = 0.0D;
    private ProtocolCreator protocolCreator;
    private Coupon store = new Coupon();

    public StoreLinearLayout(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void initRefresh() {
        findViewById(R.id.refresh).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        StoreLinearLayout.this.request(true);
                    }
                });
    }

    protected DownloadListView.DownLoadAdapter getAdapter(
            DownloadListView paramDownloadListView) {
        return new ListViewAdapter(paramDownloadListView, this.store);
    }

    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView,
                    View paramView, int paramInt, long paramLong) {
                Cache.put("coupon", StoreLinearLayout.this.store);
                Cache.put("protocol", StoreLinearLayout.this.protocolCreator
                        .createProtocol(ActivityManager.getCurrent(), 3000,
                                StoreLinearLayout.this.pageIndex, 10,
                                StoreLinearLayout.this.cityId,
                                StoreLinearLayout.this.category.id,
                                StoreLinearLayout.this.address,
                                StoreLinearLayout.this.lat,
                                StoreLinearLayout.this.lng));
                Intent localIntent = new Intent();
                localIntent.putExtra("position", paramInt);
                localIntent.setClass(ActivityManager.getCurrent(),
                        StoreDetailActivity.class);
                ActivityManager.getCurrent().startActivity(localIntent);
            }
        };
    }

    protected void init() {
        if (!this.isFirst)
            init(this.protocolCreator, null, false, this.category.id);
    }

    protected void init(ProtocolCreator paramProtocolCreator,
            String paramString, boolean paramBoolean, int paramInt) {
        if (paramProtocolCreator != null)
            this.protocolCreator = paramProtocolCreator;
        this.list = ((DownloadListView) findViewById(R.id.listview));
        this.list.setOnItemClickListener(getItemClickListener());
        this.adapter = getAdapter(this.list);
        this.list.setAdapter(this.adapter);
        initAddressList(paramString, paramBoolean);
        initCategoryList(paramInt);
        initRefresh();
        request();
    }

    public void onHide() {
        this.showing = false;
        hideList();
    }

    public void onShow() {
        show(null, null, false, 0, false);
    }

    protected void request() {
        request(false);
    }

    protected void request(boolean paramBoolean) {
        if (!StringUtils.isEmpty(this.cityId)) {
            if (paramBoolean) {
                for (int i = 1; i <= this.pageIndex; i++)
                    Cache.remove(this.protocolCreator.createProtocol(
                            ActivityManager.getCurrent(), 3000, i, 10,
                            this.cityId, this.category.id, this.address,
                            this.lat, this.lng).getAbsoluteUrl());
                this.pageIndex = 1;
            }
            if (this.pageIndex == 1) {
                if (AroundActivity.location != null) {
                    this.lat = AroundActivity.location.lat;
                    this.lng = AroundActivity.location.lon;
                }
                this.store.details.clear();
                this.list.reset();
            }
            Protocol localProtocol = this.protocolCreator.createProtocol(
                    ActivityManager.getCurrent(), 3000, this.pageIndex, 10,
                    this.cityId, this.category.id, this.address, this.lat,
                    this.lng);
            this.lastUrl = localProtocol.getAbsoluteUrl();
            localProtocol.startTrans(new StoreProtocolResult());
        }
    }

    public void show(ProtocolCreator paramProtocolCreator, String paramString,
            boolean paramBoolean1, int paramInt, boolean paramBoolean2) {
        this.showing = true;
        String str = SharePersistent.getInstance().getPerference(this.activity,
                "city_id");
        if (!this.isFirst) {
            if ((StringUtils.isEmpty(this.cityId)) || (this.cityId.equals(str))) {
                if (StringUtils.isEmpty(str)) {
                    SharePersistent.getInstance().getPerference(this.activity,
                            "city_id");
                    this.pageIndex = 0;
                    request();
                }
            } else
                init(paramProtocolCreator, paramString, paramBoolean1, paramInt);
        } else {
            init(paramProtocolCreator, paramString, paramBoolean1, paramInt);
            this.isFirst = false;
        }
    }

    private class ListViewAdapter extends CouponDownLoadAdapter {
        public ListViewAdapter(DownloadListView paramCoupon, Coupon arg3) {
            super(false, paramCoupon, arg3);
        }

        public Context getContext() {
            return StoreLinearLayout.this.activity;
        }

        public void onNotifyDownload() {
            StoreLinearLayout.this.pageIndex = (1 + (-1 + (10 + getListCount())) / 10);
            StoreLinearLayout.this.request();
        }
    }

    public static abstract interface ProtocolCreator {
        public abstract Protocol createProtocol(Context paramContext,
                int paramInt1, int paramInt2, int paramInt3,
                String paramString1, int paramInt4, String paramString2,
                double paramDouble1, double paramDouble2);
    }

    protected class StoreProtocolResult extends Protocol.OnJsonProtocolResult {
        public StoreProtocolResult() {
            super();
        }

        private void notifyNoResult() {
            StoreLinearLayout.this.handler.post(new Runnable() {
                public void run() {
                    StoreLinearLayout.this.adapter.notifyNoResult();
                }
            });
        }

        public void onException(String paramString, Exception paramException) {
            if (StoreLinearLayout.this.lastUrl.equals(paramString))
                notifyNoResult();
        }

        public void onResult(String paramString, Object paramObject) {
            if (StoreLinearLayout.this.lastUrl.equals(paramString))
                if (paramObject != null) {
                    final Coupon localCoupon = (Coupon) paramObject;
                    if (!localCoupon.details.isEmpty())
                        StoreLinearLayout.this.handler.post(new Runnable() {
                            public void run() {
                                synchronized (StoreLinearLayout.this) {
                                    StoreLinearLayout.this.store.details
                                            .addAll(localCoupon.details);
                                    StoreLinearLayout.this.store.pageInfo = localCoupon.pageInfo;
                                    StoreLinearLayout.this.adapter
                                            .notifyDownloadBack();
                                    return;
                                }
                            }
                        });
                    else
                        notifyNoResult();
                } else {
                    notifyNoResult();
                }
        }
    }
}
