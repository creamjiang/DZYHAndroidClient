package com.dld.protocol;

import android.app.Activity;
import android.content.Context;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.AroundActivity;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.PhoneUtil;
import com.dld.coupon.util.MapUtil.LatAndLon;
import com.dld.coupon.view.CardDialogue;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Protocol;

public class CommonProtocolHelper {
    public static Protocol allSearch(String paramString1, int paramInt1,
            String paramString2, int paramInt2) {
        Activity localActivity = ActivityManager.getCurrent();
        String str2 = null;
        String str3 = null;
        if (AroundActivity.location != null) {
            str2 = String.valueOf(AroundActivity.location.getLatitude());
            str3 = String.valueOf(AroundActivity.location.getLongitude());
        }
        String str4 = SharePersistent.getInstance().getPerference(
                localActivity, "city_id");
        String str1 = str4.substring(0, 2);
        str4 = str4.substring(2, 4);
        if (str4.equals("00"))
            str4 = null;
        String str5 = CardDialogue.getBankCodes(null);
        return new CProtocol(localActivity, null, "all_search", new Param()
                .append("q", paramString1)
                .append("cat", String.valueOf(paramInt1))
                .append("lat", String.valueOf(str2)).append("lng", str3)
                .append("range", paramString2).append("lpi", str1)
                .append("lci", str4).append("pi", String.valueOf(paramInt2))
                .append("banks", str5));
    }

    public static Protocol feedback(Context paramContext, String paramString1,
            String paramString2, String paramString3) {
        return new CProtocol(paramContext, null, "feedback", new Param()
                .append("content", paramString1).append("email", paramString2)
                .append("phone", paramString3), false);
    }

    public static Protocol getVersion(Context paramContext) {
        return new CProtocol(paramContext, null, "version", new Param().append(
                "imei", PhoneUtil.getIMEI(paramContext)), false);
    }

    public static Protocol like(Context paramContext, String paramString) {
        return new CProtocol(paramContext, null, "like", new Param().append(
                "id", paramString), false);
    }

    public static Protocol pay(Context paramContext, String paramString1,
            String paramString2, String paramString3, String paramString4) {
        return new CProtocol(paramContext, null, "pay", new Param()
                .append("website", paramString1).append("amount", paramString2)
                .append("card", paramString3).append("phone", paramString4),
                false);
    }

    public static Protocol searchAddress() {
        Activity localActivity = ActivityManager.getCurrent();
        MapUtil.LatAndLon localLatAndLon = AroundActivity.location;
        return new CProtocol(localActivity, null, "address", new Param()
                .append("lat", String.valueOf(localLatAndLon.lat)).append(
                        "lng", String.valueOf(localLatAndLon.lon)));
    }

    public static Protocol sendEmail(Context paramContext, int paramInt,
            String paramString) {
        return new CProtocol(paramContext, null, "email", new Param().append(
                "id", String.valueOf(paramInt)).append("r",
                String.valueOf(paramString)), false);
    }

    public static Protocol shareToMicroblog(Context paramContext,
            String paramString1, String paramString2, String paramString3) {
        return new CProtocol(paramContext, null, "weibo", new Param()
                .append("name", paramString1).append("pwd", paramString2)
                .append("content", paramString3), false);
    }

    public static Protocol tip(String paramString) {
        return new CProtocol(ActivityManager.getCurrent(), null, "tip",
                new Param().append("prefix", paramString));
    }

    public static Protocol upload(Context paramContext, String paramString1,
            String paramString2, byte[] paramArrayOfByte) {
        CProtocol localCProtocol = new CProtocol(paramContext, null, "upload",
                new Param().append("shop", paramString1).append("description",
                        paramString2), false);
        localCProtocol.data = paramArrayOfByte;
        return localCProtocol;
    }
}
