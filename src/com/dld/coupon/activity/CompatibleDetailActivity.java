package com.dld.coupon.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.coupon.view.BankCouponDetailView;
import com.dld.coupon.view.CouponDetailView;
import com.dld.coupon.view.DetailContainer;
import com.dld.coupon.view.GroupDetailView;
import com.dld.coupon.view.Showable;
import com.dld.coupon.view.StoreDetailView;
import com.dld.coupon.view.TicketView;
import com.dld.coupon.view.CouponDetailView.Listener;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.JsonBean;
import com.dld.protocol.json.Ticket;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompatibleDetailActivity extends BaseActivity implements
        CouponDetailView.Listener {
    private JsonBean bean;
    private LinearLayout container;
    private DetailContainer detailContainer;
    private boolean friend;
    Handler handler = new Handler();
    private LayoutInflater layoutInflater;
    private int position;
    private ProgressDialog progressbar;
    private Showable showable;
    private DragViewGroup viewGroup;
    private List<LinearLayout> views = new ArrayList();

    private void addEmptyView() {
        LinearLayout localLinearLayout = (LinearLayout) this.layoutInflater
                .inflate(R.layout.linear, null);
        this.viewGroup.addView(localLinearLayout);
        this.views.add(localLinearLayout);
    }

    private void destroy(LinearLayout paramLinearLayout) {
        if (paramLinearLayout.getChildCount() > 0) {
            View localView = paramLinearLayout.getChildAt(0);
            if (!(localView instanceof GroupDetailView)) {
                if ((localView instanceof TicketView))
                    ((TicketView) localView).onDestroy();
            } else
                ((GroupDetailView) localView).onDestroy();
            paramLinearLayout.removeAllViews();
        }
    }

    private void dismissProgressDialog() {
        if (this.progressbar != null)
            this.handler.post(new Runnable() {
                public void run() {
                    CompatibleDetailActivity.this.progressbar.dismiss();
                    CompatibleDetailActivity.this.progressbar = null;
                }
            });
    }

    private void init() {
        this.layoutInflater = LayoutInflater.from(this);
        this.container = ((LinearLayout) this.layoutInflater.inflate(
                R.layout.detail_container, null));
        this.detailContainer = ((DetailContainer) this.container
                .findViewById(R.id.container));
        this.detailContainer.init(true);
        this.viewGroup = ((DragViewGroup) this.container
                .findViewById(R.id.view_flipper));
        int j = 0;
        int i = this.bean.getDetails().size();
        while (j < i) {
            addEmptyView();
            j++;
        }
        this.viewGroup.setNavigation(new DragViewGroupNavigation() {
            public void onChange(int paramInt1, int paramInt2, int paramInt3) {
                CompatibleDetailActivity.this.position = paramInt1;
                CompatibleDetailActivity.this.initViews();
            }
        });
        setContentView(this.container);
    }

    private void initViews() {
        updateTitle();
        List localList = this.bean.getDetails();
        int j = 0;
        int i = this.views.size();
        while (j < i) {
            Object localObject1 = (LinearLayout) this.views.get(j);
            if ((j < -1 + this.position) || (j > 1 + this.position)) {
                destroy((LinearLayout) localObject1);
            } else {
                Object localObject2 = (DetailRef) localList.get(j);
                if (((LinearLayout) localObject1).getChildCount() == 0) {
                    if (!this.friend)
                        Cache.put("fav", Boolean.valueOf(true));
                    Object localObject3;
                    boolean bool;
                    switch (((DetailRef) localObject2).type) {
                    case 1:
                    default:
                        localObject2 = (CouponDetailView) this.layoutInflater
                                .inflate(R.layout.coupon_detail, null);
                        ((LinearLayout) localObject1)
                                .addView((View) localObject2);
                        ((CouponDetailView) localObject2)
                                .onCreate((CouponDetail) ((DetailRef) localList
                                        .get(j)).detail, this.showable, this,
                                        j, false);
                        break;
                    case 0:
                        localObject2 = this.layoutInflater.inflate(
                                R.layout.store_detail, null);
                        ((LinearLayout) localObject1)
                                .addView((View) localObject2);
                        localObject1 = (CouponDetail) ((DetailRef) localList
                                .get(j)).detail;
                        ((StoreDetailView) localObject2).onCreate(
                                (CouponDetail) localObject1, this.showable);
                        break;
                    case 2:
                        localObject2 = this.layoutInflater.inflate(
                                R.layout.group_detail, null);
                        ((LinearLayout) localObject1)
                                .addView((View) localObject2);
                        localObject1 = (GroupDetail) ((DetailRef) localList
                                .get(j)).detail;
                        localObject2 = (GroupDetailView) localObject2;
                        localObject3 = this.showable;
                        if (!this.friend)
                            bool = true;
                        else
                            bool = false;
                        ((GroupDetailView) localObject2).onCreate(
                                (GroupDetail) localObject1,
                                (Showable) localObject3, bool);
                        break;
                    case 3:
                        localObject2 = this.layoutInflater.inflate(
                                R.layout.bank_detail, null);
                        ((LinearLayout) localObject1)
                                .addView((View) localObject2);
                        localObject1 = (BankCouponDetail) ((DetailRef) localList
                                .get(j)).detail;
                        ((BankCouponDetailView) localObject2).onCreate(
                                (BankCouponDetail) localObject1, this.showable);
                        break;
                    case 4:
                        localObject2 = this.layoutInflater.inflate(
                                R.layout.ticket, null);
                        ((LinearLayout) localObject1)
                                .addView((View) localObject2);
                        localObject1 = (Ticket) ((DetailRef) localList.get(j)).detail;
                        localObject3 = (TicketView) localObject2;
                        localObject2 = findViewById(R.id.container);
                        Showable localShowable = this.showable;
                        if (!this.friend)
                            bool = true;
                        else
                            bool = false;
                        ((TicketView) localObject3).onCreate(
                                (View) localObject2, (Ticket) localObject1,
                                localShowable, bool);
                    }
                }
            }
            j++;
        }
    }

    private void updateTitle() {
        ((TextView) this.container.findViewById(R.id.title_text))
                .setText("商家详情 （" + (1 + this.position) + " / "
                        + this.bean.getTotal() + "）");
    }

    public void onComplete(int paramInt) {
        if (paramInt == this.position)
            dismissProgressDialog();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.bean = ((JsonBean) Cache.remove("bean"));
        this.position = ((Integer) Cache.remove("position")).intValue();
        this.showable = ((Showable) Cache.remove("showable"));
        boolean bool;
        if (Cache.remove("friend") == null)
            bool = false;
        else
            bool = true;
        this.friend = bool;
        init();
        initViews();
        initSegment();
        this.viewGroup.position = this.position;
    }

    protected void onDestroy() {
        super.onDestroy();
        Iterator localIterator = this.views.iterator();
        while (localIterator.hasNext())
            destroy((LinearLayout) localIterator.next());
        this.detailContainer.clear();
    }

    public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4)
            ;
        while (true) {
            try {
                boolean bool1 = this.detailContainer.onHide();
                if (bool1) {
                    bool1 = true;
                    return bool1;
                }
            } catch (Exception localException) {
                LogUtils.e("test", localException);
            }
            boolean bool2 = super.onKeyUp(paramInt, paramKeyEvent);
        }
    }
}
