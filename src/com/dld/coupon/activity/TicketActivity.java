package com.dld.coupon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.android.util.LogUtils;
import com.dld.android.view.DragViewGroup;
import com.dld.android.view.DragViewGroupNavigation;
import com.dld.coupon.view.DetailContainer;
import com.dld.coupon.view.Showable;
import com.dld.coupon.view.TicketView;
import com.dld.protocol.json.Ticket;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TicketActivity extends BaseActivity {
    private DetailContainer detailContainer;
    private LayoutInflater layoutInflater;
    private int position;
    private Showable showable;
    private List<Ticket> tickets;
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
        View localView = this.layoutInflater.inflate(R.layout.detail_container,
                null);
        this.detailContainer = ((DetailContainer) localView
                .findViewById(R.id.container));
        this.detailContainer.init(false);
        this.viewGroup = ((DragViewGroup) localView
                .findViewById(R.id.view_flipper));
        ((TextView) localView.findViewById(R.id.title_text)).setText("优惠信息");
        int i = 0;
        int j = this.tickets.size();
        while (i < j) {
            addEmptyView();
            i++;
        }
        this.viewGroup.setNavigation(new DragViewGroupNavigation() {
            public void onChange(int paramInt1, int paramInt2, int paramInt3) {
                TicketActivity.this.position = paramInt1;
                TicketActivity.this.initViews();
            }
        });
        setContentView(localView);
    }

    private void initViews() {
        updateTitle();
        int j = 0;
        int i = this.views.size();
        while (j < i) {
            LinearLayout localLinearLayout = (LinearLayout) this.views.get(j);
            if ((j < -1 + this.position) || (j > 1 + this.position)) {
                if (localLinearLayout.getChildCount() > 0) {
                    ((TicketView) localLinearLayout.getChildAt(0)).onDestroy();
                    localLinearLayout.removeAllViews();
                }
            } else if (localLinearLayout.getChildCount() != 0) {
                if (j < this.tickets.size())
                    ((TicketView) localLinearLayout.getChildAt(0)).onCreate(
                            findViewById(R.id.container),
                            (Ticket) this.tickets.get(j), this.showable, false);
            } else {
                TicketView localTicketView = (TicketView) this.layoutInflater
                        .inflate(R.layout.ticket, null);
                localLinearLayout.addView(localTicketView);
                if (j < this.tickets.size())
                    localTicketView.onCreate(findViewById(R.id.container),
                            (Ticket) this.tickets.get(j), this.showable, false);
            }
            j++;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        try {
            this.tickets = ((List) Cache.remove("tickets"));
            this.position = ((Integer) Cache.remove("position")).intValue();
            this.showable = ((Showable) Cache.remove("showable"));
            init();
            initViews();
            initSegment();
            this.viewGroup.position = this.position;
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Iterator localIterator = this.views.iterator();
        while (true) {
            if (!localIterator.hasNext())
                ;
            try {
                this.detailContainer.clear();
                // return;
                LinearLayout localLinearLayout = (LinearLayout) localIterator
                        .next();
                if (localLinearLayout.getChildCount() <= 0)
                    continue;
                ((TicketView) localLinearLayout.getChildAt(0)).onDestroy();
                localLinearLayout.removeAllViews();
            } catch (Exception localException) {
                while (true)
                    localException.printStackTrace();
            }
        }
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

    protected void segmentOnFocusCallback() {
        ActivityManager.getCurrent().finish();
    }

    public void updateTitle() {
        ((TextView) findViewById(R.id.title_text)).setText("优惠券详情 ("
                + (1 + this.position) + "/" + this.tickets.size() + ")");
    }
}
