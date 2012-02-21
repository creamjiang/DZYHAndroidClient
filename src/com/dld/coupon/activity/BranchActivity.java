package com.dld.coupon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.coupon.view.CouponDownLoadAdapter;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class BranchActivity extends BaseActivity {
    protected static final int PAGE_SIZE = 10;
    private Activity activity;
    protected DownloadListView.DownLoadAdapter adapter;
    private Coupon coupon = new Coupon();
    protected Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {
            case 1:
                BranchActivity.this.adapter.notifyDownloadBack();
                break;
            case 2:
                BranchActivity.this.adapter.notifyNoResult();
            }
        }
    };
    private String lastUrl = "";
    protected DownloadListView list;
    protected int pageIndex = 1;
    private int shopid;

    protected DownloadListView.DownLoadAdapter getAdapter(
            DownloadListView paramDownloadListView) {
        return new ListViewAdapter(paramDownloadListView, this.coupon);
    }

    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAdapterView,
                    View paramView, int paramInt, long paramLong) {
                Cache.put("coupon", BranchActivity.this.coupon);
                double d2 = 0.0D;
                double d1 = 0.0D;
                if (AroundActivity.location != null) {
                    d2 = AroundActivity.location.getLatitude();
                    d1 = AroundActivity.location.getLongitude();
                }
                Cache.put("protocol", ProtocolHelper.recommendByCity(
                        BranchActivity.this, 500,
                        BranchActivity.this.pageIndex, 10, null, null, d2, d1,
                        4, BranchActivity.this.shopid, -1));
                Intent localIntent = new Intent();
                localIntent.putExtra("position", paramInt);
                localIntent.putExtra("use_gps", false);
                localIntent.setClass(ActivityManager.getCurrent(),
                        DetailActivity.class);
                ActivityManager.getCurrent().startActivity(localIntent);
                if (BranchActivity.this.activity != null)
                    BranchActivity.this.activity.finish();
                BranchActivity.this.finish();
            }
        };
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.branch_view);
        this.shopid = ((Integer) Cache.remove("shopid")).intValue();
        this.activity = ((Activity) Cache.remove("activity"));
        this.list = ((DownloadListView) findViewById(R.id.listview));
        this.list.setOnItemClickListener(getItemClickListener());
        this.adapter = getAdapter(this.list);
        this.list.setAdapter(this.adapter);
        this.pageIndex = 1;
        request();
    }

    protected void request() {
        if (this.pageIndex == 1) {
            this.coupon.details.clear();
            this.list.reset();
        }
        double d1 = 0.0D;
        double d2 = 0.0D;
        if (AroundActivity.location != null) {
            d1 = AroundActivity.location.getLatitude();
            d2 = AroundActivity.location.getLongitude();
        }
        Protocol localProtocol = ProtocolHelper.recommendByCity(this, 500,
                this.pageIndex, 10, null, null, d1, d2, 4, this.shopid, -1);
        this.lastUrl = localProtocol.getAbsoluteUrl();
        localProtocol.startTrans(new RecommendProtocolResult());
    }

    private class ListViewAdapter extends CouponDownLoadAdapter {
        public ListViewAdapter(DownloadListView paramCoupon, Coupon arg3) {
            super(false, paramCoupon, arg3);
        }

        public Context getContext() {
            return BranchActivity.this;
        }

        public void onNotifyDownload() {
            BranchActivity.this.pageIndex = (1 + getListCount() / 10);
            BranchActivity.this.request();
        }
    }

    protected class RecommendProtocolResult extends
            Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            if (BranchActivity.this.lastUrl.equals(paramString))
                BranchActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (BranchActivity.this.lastUrl.equals(paramString))
                if (paramObject != null) {
                    Coupon localCoupon = (Coupon) paramObject;
                    if (!localCoupon.details.isEmpty()) {
                        BranchActivity.this.coupon.details
                                .addAll(localCoupon.details);
                        BranchActivity.this.coupon.pageInfo = localCoupon.pageInfo;
                        BranchActivity.this.handler.sendEmptyMessage(1);
                    } else {
                        BranchActivity.this.handler.sendEmptyMessage(2);
                    }
                } else {
                    BranchActivity.this.handler.sendEmptyMessage(2);
                }
        }
    }
}
