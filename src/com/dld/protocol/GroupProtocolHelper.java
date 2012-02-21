package com.dld.protocol;

import android.content.Context;

import com.dld.android.net.Param;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Protocol;

public class GroupProtocolHelper {
    public static Protocol buildBySelected(Context paramContext, int paramInt) {
        return new CProtocol(paramContext, null, "group", new Param()
                .append("selected", "true")
                .append("pi", String.valueOf(paramInt))
                .append("city", MapUtil.getGpsCity()).append("sort", "3"));
    }

    public static Protocol buildSearchByLatLon(Context paramContext,
            double paramDouble1, double paramDouble2, int paramInt1,
            int paramInt2, String paramString, int paramInt3, int paramInt4) {
        return new CProtocol(paramContext, null, "group", new Param()
                .append("lat", String.valueOf(paramDouble2))
                .append("lng", String.valueOf(paramDouble1))
                .append("city", MapUtil.getGpsCity())
                .append("pi", String.valueOf(paramInt1))
                .append("cat", String.valueOf(paramInt3))
                .append("distance", "5000")
                .append("sort", String.valueOf(paramInt4)));
    }

    public static Protocol getComments(Context paramContext, int paramInt,
            String paramString) {
        return new CProtocol(paramContext, null, "comment", new Param()
                .append("pi", String.valueOf(paramInt)).append("psize", "5")
                .append("dpid", paramString));
    }

    public static Protocol recommendByCity(Context paramContext, int paramInt1,
            int paramInt2, String paramString) {
        Param localParam = new Param().append("city", MapUtil.getCity())
                .append("cat", String.valueOf(paramInt1))
                .append("pi", String.valueOf(paramInt2)).append("sort", "3");
        if (paramString != null)
            localParam.append("range", paramString);
        return new CProtocol(paramContext, null, "group", localParam);
    }

    public static Protocol recommendByCity(Context paramContext,
            String paramString1, int paramInt1, int paramInt2,
            String paramString2) {
        return recommendByCity(paramContext, paramString1, paramInt1,
                paramInt2, paramString2, 0);
    }

    public static Protocol recommendByCity(Context paramContext,
            String paramString1, int paramInt1, int paramInt2,
            String paramString2, int paramInt3) {
        return recommendByCity(paramContext, paramString1, paramInt1,
                paramInt2, paramString2, paramInt3, false);
    }

    public static Protocol recommendByCity(Context paramContext,
            String paramString1, int paramInt1, int paramInt2,
            String paramString2, int paramInt3, boolean paramBoolean) {
        Param localParam = new Param().append("city", MapUtil.getCity())
                .append("pi", String.valueOf(paramInt1))
                .append("q", paramString1).append("sort", "3");
        localParam.append("cat", String.valueOf(paramInt2));
        if (!StringUtils.isEmpty(paramString2))
            localParam.append("range", paramString2.trim());
        if (paramInt3 > 0)
            localParam.append("method", String.valueOf(paramInt3));
        return new CProtocol(paramContext, null, "group", localParam);
    }
}
