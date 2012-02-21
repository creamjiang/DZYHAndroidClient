package com.dld.coupon.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dld.coupon.util.StringUtils;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.DetailRef;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.JsonBean;
import com.dld.protocol.json.Ticket;
import com.dld.coupon.activity.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class CompatibleDownloadAdapter extends BaseDownLoadAdapter {
    protected JsonBean bean;

    public CompatibleDownloadAdapter(JsonBean paramJsonBean) {
        this.bean = paramJsonBean;
    }

    private View getCouponView(DetailRef paramDetailRef) {
        return couponDetailToView((CouponDetail) paramDetailRef.detail, true,
                false);
    }

    private View getGroupView(DetailRef paramDetailRef) {
        return groupDetailToView((GroupDetail) paramDetailRef.detail);
    }

    private View getTicketView(DetailRef paramDetailRef) {
        Ticket localTicket = (Ticket) paramDetailRef.detail;
        View localView = ((LayoutInflater) getContext().getSystemService(
                "layout_inflater")).inflate(R.layout.ticket_item_noimage, null);
        localView.setClickable(false);
        ((TextView) localView.findViewById(R.id.ticket_title))
                .setText(localTicket.title);
        TextView localTextView = (TextView) localView
                .findViewById(R.id.ticket_end);
        String str = new SimpleDateFormat("截止时间：yyyy年M月d日").format(new Date(
                localTicket.end_time));
        if (!StringUtils.isEmpty(localTicket.source))
            str = str + "\n来源：" + localTicket.source;
        localTextView.setText(str);
        return localView;
    }

    protected View getBankView(DetailRef paramDetailRef) {
        return bankDetailToView((BankCouponDetail) paramDetailRef.detail);
    }

    public Object getItem(int paramInt) {
        return this.bean.getDetails().get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public int getListCount() {
        return this.bean.getDetails().size();
    }

    public int getMax() {
        return this.bean.getTotal();
    }

    public View getView(int paramInt) {
        Object localObject = (DetailRef) this.bean.getDetails().get(paramInt);
        switch (((DetailRef) localObject).type) {
        default:
            localObject = getCouponView((DetailRef) localObject);
            break;
        case 2:
            localObject = getGroupView((DetailRef) localObject);
            break;
        case 3:
            localObject = getBankView((DetailRef) localObject);
            break;
        case 4:
            localObject = getTicketView((DetailRef) localObject);
        }
        return (View) localObject;
    }
}
