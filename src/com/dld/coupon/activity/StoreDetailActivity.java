package com.dld.coupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.coupon.view.Showable;
import com.dld.coupon.view.StoreDetailView;
import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.PageInfo;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.List;

public class StoreDetailActivity extends BaseActivity {
    private Coupon coupon = new Coupon();
    private Handler handler = new Handler();
    private LayoutInflater layoutInflater;
    private int position;
    private Protocol protocol;
    private Showable showable;
    private DragViewGroup viewGroup;
    private List<LinearLayout> views = new ArrayList();

    private void addEmptyView() {
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.linear, null);
        this.viewGroup.addView(localLinearLayout);
        this.views.add(localLinearLayout);
    }

    private void init() {
        this.layoutInflater = LayoutInflater.from(this);
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.detail_container, null);
        this.viewGroup = ((DragViewGroup) localLinearLayout
                .findViewById(R.id.view_flipper));
        int j = 0;
        int i = this.coupon.details.size();
        while (j < i) {
            addEmptyView();
            j++;
        }
        this.viewGroup.setNavigation(new DragViewGroupNavigation() {
            public void onChange(int paramInt1, int paramInt2, int paramInt3) {
                if ((paramInt1 >= paramInt3 - 5)
                        && (paramInt1 < Math
                                .min(-5
                                        + StoreDetailActivity.this.coupon.pageInfo.total,
                                        95))) {
                    int i = 0;
                    int j = Math.min(
                            StoreDetailActivity.this.coupon.pageInfo.total
                                    - paramInt3, 10);
                    while (i < j) {
                        StoreDetailActivity.this.addEmptyView();
                        i++;
                    }
                    Param localParam = StoreDetailActivity.this.protocol
                            .getParam();
                    localParam.append("pn", 1 + String.valueOf(Integer
                            .parseInt(localParam.get("pn"))));
                    StoreDetailActivity.this.protocol
                            .startTrans(new StoreDetailActivity.RecommendProtocolResult());
                }
                StoreDetailActivity.this.position = paramInt1;
                StoreDetailActivity.this.initViews();
            }
        });
        setContentView(localLinearLayout);
    }

    private void initViews() {
        updateTitle();
        int j = 0;
        int i = this.views.size();
        while (j < i) {
            LinearLayout localLinearLayout = (LinearLayout) this.views.get(j);
            if ((j < -1 + this.position) || (j > 1 + this.position)) {
                if (localLinearLayout.getChildCount() > 0)
                    localLinearLayout.removeAllViews();
            } else {
                Object localObject;
                if (localLinearLayout.getChildCount() != 0) {
                    if (j < this.coupon.details.size()) {
                        localObject = (CouponDetail) this.coupon.details.get(j);
                        ((StoreDetailView) localLinearLayout.getChildAt(0))
                                .onCreate((CouponDetail) localObject,
                                        this.showable);
                    }
                } else {
                    localObject = (StoreDetailView) this.layoutInflater
                            .inflate(R.layout.store_detail, null);
                    localLinearLayout.addView((View) localObject);
                    if (j < this.coupon.details.size())
                        ((StoreDetailView) localObject).onCreate(
                                (CouponDetail) this.coupon.details.get(j),
                                this.showable);
                }
            }
            j++;
        }
    }

    private void updateTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("商家详情 ("
                + (1 + this.position) + "/" + this.coupon.pageInfo.total + ")");
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        try {
            Coupon localCoupon = (Coupon) Cache.remove("coupon");
            this.coupon.details.addAll(localCoupon.details);
            this.coupon.pageInfo.total = localCoupon.pageInfo.total;
            this.position = getIntent().getIntExtra("position", 0);
            this.showable = ((Showable) Cache.remove("showable"));
            this.protocol = ((Protocol) Cache.remove("protocol"));
            init();
            initViews();
            initSegment();
            this.viewGroup.position = this.position;
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }

    private class RecommendProtocolResult extends Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            StoreDetailActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                Coupon localCoupon = (Coupon) paramObject;
                if (localCoupon.details.size() > 0) {
                    StoreDetailActivity.this.coupon.details
                            .addAll(localCoupon.details);
                    StoreDetailActivity.this.coupon.pageInfo.total = StoreDetailActivity.this.coupon.pageInfo.total;
                    StoreDetailActivity.this.handler.post(new Runnable() {
                        public void run() {
                            StoreDetailActivity.this.initViews();
                        }
                    });
                }
            }
        }
    }
}
