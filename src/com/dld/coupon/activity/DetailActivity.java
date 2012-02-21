package com.dld.coupon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.DetailView;
import com.dld.coupon.view.DialogHelper;
import com.dld.coupon.view.StoreDetailView;
import com.dld.coupon.view.CouponDetailView.Listener;
import com.dld.protocol.json.AllResult;
import com.dld.protocol.json.Coupon;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.JsonBean;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetailActivity extends BaseActivity implements Listener {
    private JsonBean bean = new Coupon();
    Handler handler = new Handler();
    private boolean isSearch;
    private LayoutInflater layoutInflater;
    private int position;
    private ProgressDialog progressbar;
    private Protocol protocol;
    private Boolean useGps;
    private DragViewGroup viewGroup;
    private List<LinearLayout> views = new ArrayList();

    private void addEmptyView() {
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.linear, null);
        this.viewGroup.addView(localLinearLayout);
        this.views.add(localLinearLayout);
    }

    private void dismissProgressDialog() {
        if (this.progressbar != null)
            this.handler.post(new Runnable() {
                public void run() {
                    if (DetailActivity.this.progressbar != null)
                        ;
                    try {
                        DetailActivity.this.progressbar.dismiss();
                        DetailActivity.this.progressbar = null;
                        return;
                    } catch (Exception localException) {
                        while (true)
                            LogUtils.e("test", "", localException);
                    }
                }
            });
    }

    private void init() {
        this.layoutInflater = LayoutInflater.from(this);
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.detail_container, null);
        this.viewGroup = ((DragViewGroup) localLinearLayout
                .findViewById(R.id.view_flipper));
        int j = 0;
        int i = this.bean.getDetails().size();
        while (j < i) {
            addEmptyView();
            j++;
        }
        this.viewGroup.setNavigation(new DragViewGroupNavigation() {
            public void onChange(int paramInt1, int paramInt2, int paramInt3) {
                if ((paramInt1 >= paramInt3 - 8)
                        && (paramInt1 < Math.min(
                                -8 + DetailActivity.this.bean.getTotal(), 98))) {
                    int i = 0;
                    int j = Math.min(DetailActivity.this.bean.getTotal()
                            - paramInt3, 10);
                    while (i < j) {
                        DetailActivity.this.addEmptyView();
                        i++;
                    }
                    Object localObject1 = DetailActivity.this.protocol
                            .getParam();
                    LogUtils.log("test", ((Param) localObject1).toString());
                    if (!StringUtils.isEmpty(((Param) localObject1).get("pn")))
                        ((Param) localObject1).append("pn", 1 + String
                                .valueOf(Integer
                                        .parseInt(((Param) localObject1)
                                                .get("pn"))));
                    else
                        ((Param) localObject1).append("pi", 1 + String
                                .valueOf(Integer
                                        .parseInt(((Param) localObject1)
                                                .get("pi"))));
                    Protocol localProtocol = DetailActivity.this.protocol;
                    localObject1 = DetailActivity.this;
                    Object localObject2;
                    if (!DetailActivity.this.isSearch)
                        localObject2 = Coupon.class;
                    else
                        localObject2 = AllResult.class;
                    localProtocol
                            .startTrans(new DetailActivity.RecommendProtocolResult());
                }
                DetailActivity.this.position = paramInt1;
                DetailActivity.this.initViews();
            }
        });
        setContentView(localLinearLayout);
    }

    private void initViews() {
        dismissProgressDialog();
        updateTitle();
        int j = 0;
        int i = this.views.size();
        while (j < i) {
            LinearLayout localLinearLayout = (LinearLayout) this.views.get(j);
            if ((j < -1 + this.position) || (j > 1 + this.position)) {
                if (localLinearLayout.getChildCount() > 0) {
                    ((DetailView) localLinearLayout.getChildAt(0)).onDestroy();
                    localLinearLayout.removeAllViews();
                }
            } else {
                DetailView localDetailView;
                CouponDetail localCouponDetail;
                if (localLinearLayout.getChildCount() != 0) {
                    localDetailView = (DetailView) localLinearLayout
                            .getChildAt(0);
                    if ((localDetailView.isEmpty())
                            && (j < this.bean.getDetails().size())) {
                        localCouponDetail = (CouponDetail) this.bean
                                .getDetails().get(j);
                        if ((localCouponDetail.isOnlyShop())
                                && (!(localDetailView instanceof StoreDetailView))) {
                            localLinearLayout.removeAllViews();
                            localDetailView = addChild(localLinearLayout,
                                    localCouponDetail);
                        }
                        localDetailView.onCreate(localCouponDetail, null, this,
                                j, this.useGps.booleanValue());
                    }
                } else if (j >= this.bean.getDetails().size()) {
                    localDetailView = (DetailView) this.layoutInflater.inflate(
                            R.layout.coupon_detail, null);
                    localLinearLayout.addView(localDetailView);
                } else {
                    localCouponDetail = (CouponDetail) this.bean.getDetails()
                            .get(j);
                    localDetailView = addChild(localLinearLayout,
                            localCouponDetail);
                    localDetailView.onCreate(localCouponDetail, null, this, j,
                            this.useGps.booleanValue());
                }
                if ((j == this.position) && (!localDetailView.isReady())) {
                    this.progressbar = DialogHelper
                            .getProgressBar("正在加载列表，请稍候...");
                    if (localDetailView.isReady())
                        dismissProgressDialog();
                }
            }
            j++;
        }
    }

    private void updateTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("商家详情 ("
                + (1 + this.position) + "/" + this.bean.getTotal() + ")");
    }

    protected DetailView addChild(LinearLayout paramLinearLayout,
            CouponDetail paramCouponDetail) {
        int i;
        if (!paramCouponDetail.isOnlyShop())
            i = R.layout.coupon_detail;
        else
            i = R.layout.store_detail;
        DetailView localDetailView = (DetailView) this.layoutInflater.inflate(
                i, null);
        paramLinearLayout.addView(localDetailView);
        return localDetailView;
    }

    public void onComplete(int paramInt) {
        if (paramInt == this.position)
            dismissProgressDialog();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        JsonBean localJsonBean = (JsonBean) Cache.remove("coupon");
        if (localJsonBean != null) {
            this.bean.getDetails().addAll(localJsonBean.getDetails());
            this.bean.setTotal(localJsonBean.getTotal());
        }
        Object localObject = Cache.remove("protocol");
        if (localObject != null)
            this.protocol = ((Protocol) localObject);
        Intent localIntent = getIntent();
        int i = localIntent.getIntExtra("position", -1);
        if (i >= 0)
            this.position = i;
        this.useGps = Boolean.valueOf(localIntent.getBooleanExtra("use_gps",
                false));
        this.isSearch = (localJsonBean instanceof AllResult);
        init();
        initViews();
        initSegment();
        this.viewGroup.position = this.position;
    }

    protected void onDestroy() {
        super.onDestroy();
        Iterator localIterator = this.views.iterator();
        while (localIterator.hasNext()) {
            LinearLayout localLinearLayout = (LinearLayout) localIterator
                    .next();
            if (localLinearLayout.getChildCount() <= 0)
                continue;
            ((DetailView) localLinearLayout.getChildAt(0)).onDestroy();
            localLinearLayout.removeAllViews();
        }
    }

    private class RecommendProtocolResult extends Protocol.OnJsonProtocolResult {
        public RecommendProtocolResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.e("test", "", paramException);
        }

        public void onResult(String paramString, Object paramObject) {
            if (paramObject != null) {
                JsonBean localJsonBean = (JsonBean) paramObject;
                if (localJsonBean.getDetails().size() > 0) {
                    DetailActivity.this.bean.getDetails().addAll(
                            localJsonBean.getDetails());
                    DetailActivity.this.bean.setTotal(localJsonBean.getTotal());
                    DetailActivity.this.handler.post(new Runnable() {
                        public void run() {
                            DetailActivity.this.initViews();
                        }
                    });
                }
            }
        }
    }
}
