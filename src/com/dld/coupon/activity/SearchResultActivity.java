package com.dld.coupon.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.view.AllLinearLayout;
import com.dld.coupon.view.BankLinearLayout;
import com.dld.coupon.view.CardDialogue;
import com.dld.coupon.view.GroupLinearLayout;
import com.dld.coupon.view.ListTab;
import com.dld.coupon.view.RecommendLinearLayout;
import com.dld.coupon.view.StoreLinearLayout;
import com.dld.coupon.view.AllLinearLayout.ProtocolCreator;
import com.dld.protocol.BankCouponProtocolHelper;
import com.dld.protocol.CommonProtocolHelper;
import com.dld.protocol.GroupProtocolHelper;
import com.dld.protocol.ProtocolHelper;
import com.dld.protocol.json.Protocol;
import com.dld.coupon.activity.R;

public class SearchResultActivity extends BaseActivity {
    LinearLayout layout;

    private void initContent() {
        Intent localIntent = getIntent();
        final String str1 = localIntent.getStringExtra("keyword");
        int j = localIntent.getIntExtra("type", 0);
        String str2 = localIntent.getStringExtra("address");
        boolean bool = localIntent.getBooleanExtra("is_district", false);
        int i = localIntent.getIntExtra("cat", 255);
        if ((StringUtils.isEmpty(str2)) && (!bool)
                && (MapUtil.getCity().equals(MapUtil.cityOfRange))) {
            str2 = MapUtil.range;
            bool = false;
        }
        bool = bool;
        str2 = str2;
        Object localObject;
        switch (j) {
        default:
            localObject = (AllLinearLayout) findViewById(R.id.allcontent);
            ((AllLinearLayout) localObject).setVisibility(0);
            ((AllLinearLayout) localObject).show(
                    new AllLinearLayout.ProtocolCreator() {
                        public Protocol createProtocol(Context paramContext,
                                int paramInt1, int paramInt2, int paramInt3,
                                String paramString1, String paramString2,
                                double paramDouble1, double paramDouble2,
                                int paramInt4) {
                            return CommonProtocolHelper.allSearch(str1,
                                    paramInt4, paramString2, paramInt2 - 1);
                        }
                    }, str2, bool, i);
            this.layout = ((LinearLayout) localObject);
            break;
        case 1:
            localObject = (RecommendLinearLayout) findViewById(R.id.recommendcontent);
            ((RecommendLinearLayout) localObject).setVisibility(0);
            ((RecommendLinearLayout) localObject).show(
                    new RecommendLinearLayout.ProtocolCreator() {
                        public Protocol createProtocol(Context paramContext,
                                int paramInt1, int paramInt2, int paramInt3,
                                String paramString1, int paramInt4,
                                String paramString2, double paramDouble1,
                                double paramDouble2) {
                            return ProtocolHelper.recommendByCity(paramContext,
                                    paramInt1, paramInt2, paramInt3, str1,
                                    paramString2, paramDouble1, paramDouble2,
                                    3, 0, paramInt4);
                        }
                    }, str2, bool, i, true);
            this.layout = ((LinearLayout) localObject);
            break;
        case 2:
            localObject = (GroupLinearLayout) findViewById(R.id.groupcontent);
            ((GroupLinearLayout) localObject).setVisibility(0);
            ((GroupLinearLayout) localObject).show(
                    new GroupLinearLayout.ProtocolCreator() {
                        public Protocol createProtocol(Context paramContext,
                                int paramInt1, int paramInt2, int paramInt3,
                                String paramString1, int paramInt4,
                                String paramString2) {
                            return GroupProtocolHelper.recommendByCity(
                                    paramContext, str1, paramInt2, paramInt4,
                                    paramString2, 1, true);
                        }
                    }, str2, bool, i);
            this.layout = ((LinearLayout) localObject);
            break;
        case 3:
            localObject = (BankLinearLayout) findViewById(R.id.bankcontent);
            ((BankLinearLayout) localObject).setVisibility(0);
            ((BankLinearLayout) localObject).show(
                    new BankLinearLayout.ProtocolCreator() {
                        public Protocol createProtocol(
                                Context paramContext,
                                int paramInt1,
                                int paramInt2,
                                int paramInt3,
                                String paramString1,
                                int paramInt4,
                                String paramString2,
                                double paramDouble1,
                                double paramDouble2,
                                DialogInterface.OnDismissListener paramOnDismissListener) {
                            return BankCouponProtocolHelper
                                    .buildSearchByKeyword(
                                            ActivityManager.getCurrent(),
                                            CardDialogue
                                                    .getBankCodes(paramOnDismissListener),
                                            str1, paramInt2, paramDouble1,
                                            paramDouble2, paramInt4,
                                            paramString2);
                        }
                    }, str2, bool, i);
            this.layout = ((LinearLayout) localObject);
            break;
        case 4:
            localObject = (StoreLinearLayout) findViewById(R.id.storecontent);
            ((StoreLinearLayout) localObject).setVisibility(0);
            ((StoreLinearLayout) localObject).show(
                    new StoreLinearLayout.ProtocolCreator() {
                        public Protocol createProtocol(Context paramContext,
                                int paramInt1, int paramInt2, int paramInt3,
                                String paramString1, int paramInt4,
                                String paramString2, double paramDouble1,
                                double paramDouble2) {
                            return ProtocolHelper.storeSearch(paramContext,
                                    paramInt2, str1, paramString2, paramInt4);
                        }
                    }, str2, bool, i, true);
            this.layout = ((LinearLayout) localObject);
        }
    }

    private void initTitle() {
        setContentView(R.layout.search_result);
        ((TextView) findViewById(R.id.title_text)).setText("搜索结果");
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        initTitle();
        initContent();
        initSegment();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        boolean i;
        if (((paramInt == 4) && ((this.layout instanceof ListTab)) && (((ListTab) this.layout)
                .onBack())) || (super.onKeyDown(paramInt, paramKeyEvent)))
            i = true;
        else
            i = false;
        return i;
    }
}
