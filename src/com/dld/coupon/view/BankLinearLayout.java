package com.dld.coupon.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.AroundActivity;
import com.dld.coupon.activity.BankCouponDetailActivity;
import com.dld.coupon.db.Category;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.protocol.json.BankCoupon;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.List;

public class BankLinearLayout extends ListTab {
    private static final int PAGE_SIZE = 10;
    private DownloadListView.DownLoadAdapter adapter;
    private BankCoupon bankCoupon = new BankCoupon();
    DialogInterface.OnDismissListener bankListener = new DialogInterface.OnDismissListener() {
        public void onDismiss(DialogInterface paramDialogInterface) {
            BankLinearLayout.this.request(true);
        }
    };
    Handler handler = new Handler();
    private boolean isFirst = true;
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> paramAdapterView,
                View paramView, int paramInt, long paramLong) {
            Cache.put("coupon", BankLinearLayout.this.bankCoupon);
            Cache.put("position", Integer.valueOf(paramInt));
            Cache.put("protocol", BankLinearLayout.this.protocolCreator
                    .createProtocol(BankLinearLayout.this.activity, 3000, -1
                            + BankLinearLayout.this.pageIndex, 10,
                            BankLinearLayout.this.city,
                            BankLinearLayout.this.category.id,
                            BankLinearLayout.this.address,
                            BankLinearLayout.this.lat,
                            BankLinearLayout.this.lng,
                            BankLinearLayout.this.bankListener));
            Intent localIntent = new Intent();
            localIntent.setClass(BankLinearLayout.this.activity,
                    BankCouponDetailActivity.class);
            BankLinearLayout.this.activity.startActivity(localIntent);
        }
    };
    private String lastUrl = "";
    private double lat;
    private DownloadListView list;
    private double lng;
    LinearLayout noResultView;
    private ProtocolCreator protocolCreator;

    public BankLinearLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void initRefresh() {
        findViewById(R.id.refresh).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View paramView) {
                        BankLinearLayout.this.request(true);
                    }
                });
    }

    private void request(boolean paramBoolean) {
        if (!StringUtils.isEmpty(this.city)) {
            if (AroundActivity.location != null) {
                MapUtil.LatAndLon localLatAndLon = AroundActivity.location;
                this.lat = localLatAndLon.getLatitude();
                this.lng = localLatAndLon.getLongitude();
            }
            if (paramBoolean) {
                for (int i = 0; i < this.pageIndex; i++)
                    Cache.remove(this.protocolCreator.createProtocol(
                            this.activity, 3000, i, 10, this.city,
                            this.category.id, this.address, this.lat, this.lng,
                            null).getAbsoluteUrl());
                this.pageIndex = 1;
            }
            if (this.pageIndex == 1) {
                this.bankCoupon.details.clear();
                this.list.reset();
            }
            Protocol localProtocol = this.protocolCreator.createProtocol(
                    this.activity, 3000, -1 + this.pageIndex, 10, this.city,
                    this.category.id, this.address, this.lat, this.lng,
                    this.bankListener);
            this.lastUrl = localProtocol.getAbsoluteUrl();
            localProtocol.startTrans(new BankdProtocolResult());
        }
    }

    protected void init() {
        if (!this.isFirst)
            init(this.protocolCreator, null, false, this.category.id);
    }

    protected void init(ProtocolCreator paramProtocolCreator,
            String paramString, boolean paramBoolean, int paramInt) {
        this.protocolCreator = paramProtocolCreator;
        this.list = ((DownloadListView) findViewById(R.id.listview));
        this.list.setOnItemClickListener(this.itemClickListener);
        this.adapter = new BankListviewAdapter(this.list, this.bankCoupon);
        this.list.setAdapter(this.adapter);
        initAddressList(paramString, paramBoolean);
        initCategoryList(paramInt);
        initRefresh();
        request();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    protected void request() {
        request(false);
    }

    public void show(ProtocolCreator paramProtocolCreator, String paramString,
            boolean paramBoolean, int paramInt) {
        String str = SharePersistent.getInstance().getPerference(this.activity,
                "city_name");
        if (!this.isFirst) {
            if ((StringUtils.isEmpty(this.city)) || (this.city.equals(str))) {
                if (StringUtils.isEmpty(str)) {
                    SharePersistent.getInstance().getPerference(this.activity,
                            "city_name");
                    this.pageIndex = 1;
                    request();
                }
            } else
                init(paramProtocolCreator, paramString, paramBoolean, paramInt);
        } else {
            init(paramProtocolCreator, paramString, paramBoolean, paramInt);
            this.isFirst = false;
        }
    }

    protected class BankListviewAdapter extends BankCouponDownLoadAdapter {
        public BankListviewAdapter(DownloadListView paramBankCoupon,
                BankCoupon arg3) {
            super(paramBankCoupon, arg3);
        }

        public Context getContext() {
            return BankLinearLayout.this.activity;
        }

        public void onNotifyDownload() {
            BankLinearLayout.this.pageIndex = (1 + getListCount() / 10);
            BankLinearLayout.this.request();
        }
    }

    private class BankdProtocolResult extends Protocol.OnJsonProtocolResult {
        public BankdProtocolResult() {
            super();
        }

        private void notifyNoResult() {
            BankLinearLayout.this.handler.post(new Runnable() {
                public void run() {
                    BankLinearLayout.this.adapter.notifyNoResult();
                }
            });
        }

        public void onException(String paramString, Exception paramException) {
            if (BankLinearLayout.this.lastUrl.equals(paramString))
                notifyNoResult();
        }

        public void onResult(String paramString, Object paramObject) {
            if (BankLinearLayout.this.lastUrl.equals(paramString))
                if (paramObject == null) {
                    notifyNoResult();
                } else {
                    final BankCoupon localBankCoupon = (BankCoupon) paramObject;
                    if (localBankCoupon.details.size() != 0)
                        BankLinearLayout.this.handler.post(new Runnable() {
                            public void run() {
                                synchronized (BankLinearLayout.this) {
                                    if (!BankLinearLayout.this.bankCoupon.details
                                            .containsAll(localBankCoupon.details)) {
                                        BankLinearLayout.this.bankCoupon.total = localBankCoupon.total;
                                        BankLinearLayout.this.bankCoupon.details
                                                .addAll(localBankCoupon.details);
                                        BankLinearLayout.this.adapter
                                                .notifyDownloadBack();
                                    }
                                    return;
                                }
                            }
                        });
                    else
                        notifyNoResult();
                }
        }
    }

    public static abstract interface ProtocolCreator {
        public abstract Protocol createProtocol(Context paramContext,
                int paramInt1, int paramInt2, int paramInt3,
                String paramString1, int paramInt4, String paramString2,
                double paramDouble1, double paramDouble2,
                DialogInterface.OnDismissListener paramOnDismissListener);
    }
}
