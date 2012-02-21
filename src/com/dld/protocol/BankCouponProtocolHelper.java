package com.dld.protocol;

import android.content.Context;

import com.dld.android.net.Param;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Protocol;

public class BankCouponProtocolHelper {
    public static Protocol buildSearchByKeyword(Context paramContext,
            String paramString1, String paramString2, int paramInt1,
            double paramDouble1, double paramDouble2, int paramInt2,
            String paramString3) {
        Param localParam = new Param().append("city", MapUtil.getCity())
                .append("banks", paramString1)
                .append("lat", String.valueOf(paramDouble1))
                .append("lng", String.valueOf(paramDouble2))
                .append("method", "1").append("pi", String.valueOf(paramInt1));
        if (!StringUtils.isEmpty(paramString2))
            localParam.append("q", paramString2);
        localParam.append("cat", String.valueOf(paramInt2));
        if (!StringUtils.isEmpty(paramString3))
            localParam.append("address", paramString3);
        return new CProtocol(paramContext, null, "bank", localParam);
    }

    public static Protocol buildSearchByLatLon(Context paramContext,
            String paramString1, double paramDouble1, double paramDouble2,
            int paramInt1, int paramInt2, String paramString2, int paramInt3,
            int paramInt4) {
        String str = MapUtil.getGpsCity();
        return new CProtocol(paramContext, null, "bank", new Param()
                .append("lat", String.valueOf(paramDouble1))
                .append("lng", String.valueOf(paramDouble2))
                .append("banks", paramString1).append("city", str)
                .append("pi", String.valueOf(paramInt1))
                .append("cat", String.valueOf(paramInt3))
                .append("distance", "5000")
                .append("sort", String.valueOf(paramInt4)));
    }
}
