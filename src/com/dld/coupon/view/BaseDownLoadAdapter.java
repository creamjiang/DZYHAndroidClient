package com.dld.coupon.view;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dld.android.view.MyHtmlTagHandler;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.image.ImageProtocol;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.CouponDetail;
import com.dld.protocol.json.GroupDetail;
import com.dld.coupon.activity.R;
import java.net.URLEncoder;

public abstract class BaseDownLoadAdapter extends
        DownloadListView.DownLoadAdapter {
    protected View bankDetailToView(BankCouponDetail paramBankCouponDetail) {
        View localView = ((LayoutInflater) getContext().getSystemService(
                "layout_inflater")).inflate(R.layout.bank_item_layout, null);
        ((TextView) localView.findViewById(R.id.coupon_name))
                .setText(paramBankCouponDetail.name);
        ((TextView) localView.findViewById(R.id.coupon_address))
                .setText(paramBankCouponDetail.address);
        TextView localTextView = (TextView) localView
                .findViewById(R.id.coupon_distance_text);
        if ((paramBankCouponDetail.distance <= 0)
                || (paramBankCouponDetail.distance > 5000))
            localTextView.setVisibility(8);
        else
            localTextView.setText(paramBankCouponDetail.distance + "米");
        localTextView = (TextView) localView.findViewById(R.id.coupon_tag);
        Object localObject = new StringBuilder();
        if (!StringUtils.isEmpty(paramBankCouponDetail.landmark))
            ((StringBuilder) localObject)
                    .append(paramBankCouponDetail.landmark);
        if (!StringUtils.isEmpty(paramBankCouponDetail.trade_name)) {
            if (((StringBuilder) localObject).length() != 0)
                ((StringBuilder) localObject).append("/");
            ((StringBuilder) localObject)
                    .append(paramBankCouponDetail.trade_name);
        }
        localObject = ((StringBuilder) localObject).toString();
        if (!StringUtils.isEmpty((String) localObject))
            localTextView.setText((CharSequence) localObject);
        else
            localTextView.setVisibility(8);
        return (View) localView;
    }

    protected View couponDetailToView(CouponDetail paramCouponDetail,
            boolean paramBoolean1, boolean paramBoolean2) {
        View localView = ((LayoutInflater) getContext().getSystemService(
                "layout_inflater")).inflate(R.layout.list_item_layout, null);
        ((ImageView) localView.findViewById(R.id.coupon_image))
                .setVisibility(View.GONE);
        ((TextView) localView.findViewById(R.id.coupon_name))
                .setText(paramCouponDetail.name);
        ((TextView) localView.findViewById(R.id.coupon_address))
                .setText(paramCouponDetail.address);
        TextView localTextView = (TextView) localView
                .findViewById(R.id.coupon_distance_text);
        if ((paramCouponDetail.distance <= 0)
                || (paramCouponDetail.distance > 5000))
            localTextView.setVisibility(8);
        else
            localTextView.setText(paramCouponDetail.distance + "米");
        localTextView = (TextView) localView.findViewById(R.id.coupon_tag);
        StringBuilder localStringBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(paramCouponDetail.landmark)) {
            String str = paramCouponDetail.landmark.split(" ")[0];
            if (str.length() > 10)
                str = str.substring(0, 10) + "...";
            localStringBuilder.append(str);
        }
        if (!StringUtils.isEmpty(paramCouponDetail.trade_name)) {
            if (localStringBuilder.length() != 0)
                localStringBuilder.append("/");
            localStringBuilder.append(paramCouponDetail.trade_name);
        }
        String str = localStringBuilder.toString();
        if (!StringUtils.isEmpty(str))
            localTextView.setText(str);
        else
            localTextView.setVisibility(8);
        return localView;
    }

    protected View groupDetailToView(GroupDetail paramGroupDetail) {
        View localView = ((LayoutInflater) getContext().getSystemService(
                "layout_inflater")).inflate(R.layout.group_item_layout, null);
        Object localObject1 = (ImageView) localView
                .findViewById(R.id.coupon_image);
        Object localObject2 = (TextView) localView.findViewById(R.id.website);
        if (!StringUtils.isEmpty(paramGroupDetail.website))
            localObject1 = paramGroupDetail.website;
        else
            localObject1 = "其他";
        ((TextView) localObject2).setText((CharSequence) localObject1);

        localObject2 = paramGroupDetail.image;
        if (localObject2 != null)
            new ImageProtocol(getContext(),
                    "http://www.dld.com/tuan/viewimage?u="
                            + URLEncoder.encode((String) localObject2)
                            + "&w=200&h=200")
                    .startTrans((ImageView) localObject1);
        ((TextView) localView.findViewById(R.id.price))
                .setText(numberToString(paramGroupDetail.price));
        ((TextView) localView.findViewById(R.id.value)).setText(Html.fromHtml(
                "<s>" + numberToString(paramGroupDetail.value) + "</s>", null,
                new MyHtmlTagHandler()));
        ((TextView) localView.findViewById(R.id.bought))
                .setText(paramGroupDetail.bought);
        localObject1 = (TextView) localView.findViewById(R.id.distance);
        if ((paramGroupDetail.distance < 0)
                || (paramGroupDetail.distance > 5000)) {
            if (!StringUtils.isEmpty(paramGroupDetail.range)) {
                if (paramGroupDetail.range.length() > 6)
                    ((TextView) localObject1).setText(paramGroupDetail.range
                            .substring(0, 5) + "...");
                else
                    ((TextView) localObject1).setText(paramGroupDetail.range);
            } else
                localView.findViewById(R.id.distance_image).setVisibility(
                        View.GONE);
        } else
            ((TextView) localObject1).setText(paramGroupDetail.distance + "米 ");
        ((TextView) localView.findViewById(R.id.title)).setText(StringUtils
                .breakLines(paramGroupDetail.title));
        return (View) (View) localView;
    }

    protected String numberToString(double paramDouble) {
        String str = String.valueOf(paramDouble);
        if (str.endsWith(".0"))
            str = str.substring(0, -2 + str.length());
        return str;
    }
}
