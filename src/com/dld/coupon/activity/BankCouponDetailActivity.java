package com.dld.coupon.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.coupon.view.BankCouponDetailView;
import com.dld.coupon.view.Showable;
import com.dld.protocol.json.BankCoupon;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.List;

public class BankCouponDetailActivity extends BaseActivity {
    private BankCoupon coupon = new BankCoupon();
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
        int i = 0;
        int j = this.coupon.details.size();
        while (i < j) {
            addEmptyView();
            i++;
        }
        this.viewGroup.setNavigation(new DragViewGroupNavigation() {
            public void onChange(int paramInt1, int paramInt2, int paramInt3) {
                if ((paramInt1 >= paramInt3 - 8)
                        && (paramInt1 < Math.min(-8
                                + BankCouponDetailActivity.this.coupon.total,
                                98))) {
                    int i = 0;
                    int j = Math.min(BankCouponDetailActivity.this.coupon.total
                            - paramInt3, 10);
                    while (i < j) {
                        BankCouponDetailActivity.this.addEmptyView();
                        i++;
                    }
                    Param localParam = BankCouponDetailActivity.this.protocol
                            .getParam();
                    localParam.append("pi", 1 +localParam.get("pi"));
                    localParam.append("pi", 1 + localParam.get("pi"));
                    BankCouponDetailActivity.this.protocol
                            .startTrans(new BankCouponDetailActivity.RecommendProtocolResult());
                }
                BankCouponDetailActivity.this.position = paramInt1;
                BankCouponDetailActivity.this.initViews();
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
                        localObject = (BankCouponDetail) this.coupon.details
                                .get(j);
                        ((BankCouponDetailView) localLinearLayout.getChildAt(0))
                                .onCreate((BankCouponDetail) localObject,
                                        this.showable);
                    }
                } else {
                    localObject = (BankCouponDetailView) this.layoutInflater
                            .inflate(R.layout.bank_detail, null);
                    localLinearLayout.addView((View) localObject);
                    if (j < this.coupon.details.size())
                        ((BankCouponDetailView) localObject).onCreate(
                                (BankCouponDetail) this.coupon.details.get(j),
                                this.showable);
                }
            }
            j++;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        BankCoupon localBankCoupon = (BankCoupon) Cache.remove("coupon");
        this.coupon.details.addAll(localBankCoupon.details);
        this.coupon.total = localBankCoupon.total;
        this.position = ((Integer) Cache.remove("position")).intValue();
        this.showable = ((Showable) Cache.remove("showable"));
        this.protocol = ((Protocol) Cache.remove("protocol"));
        init();
        initViews();
        initSegment();
        this.viewGroup.position = this.position;
    }

    public void updateTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("银行卡优惠详情 （"
                + (1 + this.position) + "/" + this.coupon.total + "）");
    }

    private class RecommendProtocolResult extends Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            BankCouponDetailActivity.this.handler.sendEmptyMessage(2);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                BankCoupon localBankCoupon = (BankCoupon) paramObject;
                if (localBankCoupon.details.size() > 0) {
                    BankCouponDetailActivity.this.coupon.details
                            .addAll(localBankCoupon.details);
                    BankCouponDetailActivity.this.coupon.total = BankCouponDetailActivity.this.coupon.total;
                    BankCouponDetailActivity.this.handler.post(new Runnable() {
                        public void run() {
                            BankCouponDetailActivity.this.initViews();
                        }
                    });
                }
            }
        }
    }
}
