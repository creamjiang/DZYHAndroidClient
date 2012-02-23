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
import com.dld.coupon.activity.DetailActivity;
import com.dld.coupon.db.Category;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class RecommendLinearLayout extends ListTab implements MainViewBase {
    protected DownloadListView.DownLoadAdapter adapter;
    private Coupon coupon = new Coupon();
    Handler handler = new Handler();
    protected boolean isFirst = true;
    private String lastUrl = "";
    private double lat = 0.0D;
    protected DownloadListView list;
    private double lng = 0.0D;
    protected int pageIndex = 1;
    private ProtocolCreator protocolCreator;
    private boolean search;

    public RecommendLinearLayout(Context paramContext,
            AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void initRefresh() {
        findViewById(R.id.refresh).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        RecommendLinearLayout.this.request(true);
                    }
                });
    }

    protected DownloadListView.DownLoadAdapter getAdapter(
            DownloadListView paramDownloadListView, boolean paramBoolean) {
        return new ListViewAdapter(paramDownloadListView, this.coupon,
                paramBoolean);
    }

    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView,
                    View paramView, int paramInt, long paramLong) {
                Cache.put("coupon", RecommendLinearLayout.this.coupon);
                Cache.put("protocol",
                        RecommendLinearLayout.this.protocolCreator
                                .createProtocol(ActivityManager.getCurrent(),
                                        3000,
                                        RecommendLinearLayout.this.pageIndex,
                                        10, RecommendLinearLayout.this.cityId,
                                        RecommendLinearLayout.this.category.id,
                                        RecommendLinearLayout.this.address,
                                        RecommendLinearLayout.this.lat,
                                        RecommendLinearLayout.this.lng));
                Intent localIntent = new Intent();
                localIntent.putExtra("position", paramInt);
                localIntent.putExtra("use_gps", false);
                localIntent.setClass(ActivityManager.getCurrent(),
                        DetailActivity.class);
                ActivityManager.getCurrent().startActivity(localIntent);
            }
        };
    }

    protected void init() {
        if (!this.isFirst)
            init(this.protocolCreator, null, false, this.category.id,
                    this.search);
    }

    protected void init(ProtocolCreator paramProtocolCreator,
            String paramString, boolean paramBoolean1, int paramInt,
            boolean paramBoolean2) {
        this.search = paramBoolean2;
        if (paramProtocolCreator != null)
            this.protocolCreator = paramProtocolCreator;
        else
            this.protocolCreator = new ProtocolCreator() {
                public Protocol createProtocol(Context paramContext,
                        int paramInt1, int paramInt2, int paramInt3,
                        String paramString1, int paramInt4,
                        String paramString2, double paramDouble1,
                        double paramDouble2) {
                    return ProtocolHelper.recommendByCity(paramContext,
                            paramInt1, paramInt2, 10, "102", "101120103",
                            RecommendLinearLayout.this.lat,
                            RecommendLinearLayout.this.lng, 1, 0, paramInt4);
                }
            };
        this.list = ((DownloadListView) findViewById(R.id.listview));
        this.list.setOnItemClickListener(getItemClickListener());
        this.adapter = getAdapter(this.list, paramBoolean2);
        this.list.setAdapter(this.adapter);
        initAddressList(paramString, paramBoolean1);
        initCategoryList(paramInt);
        initRefresh();
        request();
    }

    public void onHide() {
        this.showing = false;
        hideList();
    }

    public void onShow() {
        show(null, null, false, 255, false);
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
                this.coupon.details.clear();
                this.list.reset();
            }
            Protocol localProtocol = this.protocolCreator.createProtocol(
                    ActivityManager.getCurrent(), 3000, this.pageIndex, 10,
                    this.cityId, this.category.id, this.address, this.lat,
                    this.lng);
            this.lastUrl = localProtocol.getAbsoluteUrl();
            localProtocol.startTrans(new RecommendProtocolResult());
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
                init(paramProtocolCreator, paramString, paramBoolean1,
                        paramInt, paramBoolean2);
        } else {
            init(paramProtocolCreator, paramString, paramBoolean1, paramInt,
                    paramBoolean2);
            this.isFirst = false;
        }
    }

    private class ListViewAdapter extends CouponDownLoadAdapter {
        public ListViewAdapter(DownloadListView paramCoupon,
                Coupon paramBoolean, boolean arg4) {
            super(arg4, paramCoupon, paramBoolean);
        }

        public Context getContext() {
            return RecommendLinearLayout.this.activity;
        }

        public View getView(int paramInt) {
            View localView = super.getView(paramInt);
            localView.findViewById(R.id.coupon_distance_text).setVisibility(
                    View.GONE);
            return localView;
        }

        public void onNotifyDownload() {
            RecommendLinearLayout.this.pageIndex = (1 + (-1 + (10 + getListCount())) / 10);
            RecommendLinearLayout.this.request();
        }
    }

    public static abstract interface ProtocolCreator {
        public abstract Protocol createProtocol(Context paramContext,
                int paramInt1, int paramInt2, int paramInt3,
                String paramString1, int paramInt4, String paramString2,
                double paramDouble1, double paramDouble2);
    }

    protected class RecommendProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        private void notifyNoResult() {
            RecommendLinearLayout.this.handler.post(new Runnable() {
                public void run() {
                    RecommendLinearLayout.this.adapter.notifyNoResult();
                }
            });
        }

        public void onException(String paramString, Exception paramException) {
            if (RecommendLinearLayout.this.lastUrl.equals(paramString))
                notifyNoResult();
        }

        public void onResult(String paramString, Object paramObject) {
            if (RecommendLinearLayout.this.lastUrl.equals(paramString))
                if (paramObject != null) {
                    Coupon localCoupon = (Coupon) paramObject;
                    if (!localCoupon.details.isEmpty())
                        RecommendLinearLayout.this.handler.post(new Runnable() {
                            public void run() {
                                synchronized (RecommendLinearLayout.this) {
                                    RecommendLinearLayout.this.coupon.details
                                            .addAll(coupon.details);
                                    RecommendLinearLayout.this.coupon.pageInfo = coupon.pageInfo;
                                    RecommendLinearLayout.this.adapter
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
