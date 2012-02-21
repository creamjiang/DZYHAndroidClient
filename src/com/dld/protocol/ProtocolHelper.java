package com.dld.protocol;

import android.content.Context;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.Protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProtocolHelper {
    public static final String CHANNEL = "000000000000045";
    public static final String CLIENT_VERSION = "1.0";
    public static final String HOST = "http://clientservice.12580.com:8180";
    public static final String IMAGE = "/NetGate_v2/GetJsonImage?";
    public static final String JSON = "/NetGate_v2/GetJson?";
    public static final String LOGIN = "/NetGate_v2/Login?";
    public static final String MAP = "/NetGate_v2/GetJsonMap?";
    public static final String UPDATE_PHONE = "/NetGate_v2/UpdatePhone?";
    public static final String USER = "/NetGate_v2/GetUserPhone?";
    public static final String VER = "0015433301";

    public static CProtocol addComment(Context paramContext,
            String paramString1, Param paramParam, String paramString2,
            boolean paramBoolean) {
        return new CProtocol(paramContext, paramString1, paramString2,
                paramParam, false);
    }

    public static Protocol buildBySelected(Context paramContext, int paramInt1,
            int paramInt2) {
        return new CProtocol(paramContext, null, "selected", new Param()
                .append("ps", String.valueOf(paramInt2))
                .append("pn", String.valueOf(paramInt1)).append("method", "5")
                .append("city", MapUtil.getGpsCity()));
    }

    public static Protocol buildCheckRegist(Context paramContext) {
        return new Protocol(paramContext, "/NetGate_v2/GetUserPhone?", "user",
                "getUserLogin", null, true, false);
    }

    public static Protocol buildComment(Context paramContext,
            String paramString1, String paramString2) {
        return new Protocol(paramContext, "live", "addComment", new Param()
                .append("userId",
                        SharePersistent.getInstance().getPerference(
                                paramContext, "user_id"))
                .append("name",
                        SharePersistent.getInstance().getPerference(
                                paramContext, "user_nick"))
                .append("handset",
                        SharePersistent.getInstance().getPerference(
                                paramContext, "user_phone"))
                .append("source", "1").append("comment", paramString1)
                .append("businessId", paramString2), false);
    }

    public static Protocol buildFeedback(Context paramContext,
            String paramString1, String paramString2) {
        return new Protocol(paramContext, "user", "commitIdeas", new Param()
                .append("userId",
                        SharePersistent.getInstance().getPerference(
                                paramContext, "user_id"))
                .append("info", paramString1).append("contact", paramString2),
                false);
    }

    public static Protocol buildSearchByLatLon(boolean paramBoolean,
            Context paramContext, double paramDouble1, double paramDouble2,
            int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        Object localObject1 = null;
        String str = MapUtil.getGpsCityId();
        Param localParam = new Param().append("ps", String.valueOf(paramInt2))
                .append("pn", String.valueOf(paramInt1));
        Object localObject2 = new StringBuilder("lat:").append(paramDouble1)
                .append(";lon:").append(paramDouble2).append(";");
        if (!paramBoolean)
            localObject1 = "";
        else
            localObject1 = "category:" + paramInt3;
        localObject1 = localParam.append("q", (String) localObject1).append(
                "cat", String.valueOf(paramInt3));
        if (!paramBoolean) {
            localObject2 = str.substring(0, 2);
            str = str.substring(2, 4);
            ((Param) localObject1).append("method", "2")
                    .append("lpi", (String) localObject2)
                    .append("sort", String.valueOf(paramInt4));
            if (!str.equals("00"))
                ((Param) localObject1).append("lci", str);
        } else {
            localObject2 = str.substring(0, 2);
            str = str.substring(2, 4);
            ((Param) localObject1).append("method", "0")
                    .append("customize", "1").append("lbb", "5000")
                    .append("lpi", (String) localObject2);
            if (paramInt4 == 1)
                ((Param) localObject1).append("near_order_by", "dis");
            if (!str.equals("00"))
                ((Param) localObject1).append("lci", str);
        }
        if (!paramBoolean)
            str = "coupon";
        else
            str = "store";
        return (Protocol) (Protocol) new CProtocol(paramContext, null, str,
                (Param) localObject1);
    }

    public static CProtocol caiFuTongpayRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "caifutongpay", paramParam,
                false);
    }

    public static CProtocol cancellCaiFutong(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "cancellpay", paramParam,
                false);
    }

    public static CProtocol deleteFavRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "delete_fav", paramParam,
                false);
    }

    public static Protocol getCouponComment(Context paramContext,
            String paramString, int paramInt1, int paramInt2) {
        return new Protocol(paramContext, "live", "findCommentByBusinessId",
                new Param().append("businessId", paramString)
                        .append("pageOn", String.valueOf(paramInt1))
                        .append("pageSize", String.valueOf(paramInt2)), false);
    }

    public static Param getDefParam(Context paramContext) {
        String str1 = SharePersistent.getInstance().getPerference(paramContext,
                "user_id");
        String str2 = SharePersistent.getInstance().getPerference(paramContext,
                "user_phone");
        Param localParam = new Param();
        localParam.append("UID", str1);
        localParam.append("userId", str1);
        localParam.append("NAME", str1);
        localParam.append("PASS", "000000");
        localParam.append("MOBILE", str2);
        localParam.append("phone", str2);
        localParam.append("VER", "0015433301");
        localParam.append("PUBLISER", "000000000000045");
        localParam.append("SKINID", "1001");
        return localParam;
    }

    public static CProtocol getHuoDongRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "huodong", paramParam, false);
    }

    public static Protocol getUserComment(Context paramContext,
            String paramString, int paramInt1, int paramInt2) {
        return new Protocol(paramContext, "live", "findCommentByTel",
                new Param().append("tel", paramString)
                        .append("pageOn", String.valueOf(paramInt1))
                        .append("pageSize", String.valueOf(paramInt2)), false);
    }

    public static CProtocol loginRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "login", paramParam, false);
    }

    public static CProtocol orderRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "order", paramParam, false);
    }

    public static CProtocol orderRequestUpdate(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "orderupdate", paramParam,
                false);
    }

    public static CProtocol payRequest(Context paramContext, Param paramParam,
            boolean paramBoolean) {
        return new CProtocol(paramContext, null, "yinglianpay", paramParam,
                false);
    }

    public static Protocol postRequest(String paramString) {
        return new CProtocol(ActivityManager.getCurrent(), null, paramString,
                null, false);
    }

    public static ByteArrayOutputStream readToByteArray(
            InputStream paramInputStream) throws IOException {
        return readToByteArray(paramInputStream, 1024);
    }

    public static ByteArrayOutputStream readToByteArray(
            InputStream paramInputStream, int paramInt) throws IOException {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte = new byte[paramInt];
        for (int i = paramInputStream.read(arrayOfByte); i > 0; i = paramInputStream
                .read(arrayOfByte)) {
            localByteArrayOutputStream.write(arrayOfByte, 0, i);
            arrayOfByte = new byte[paramInt];
        }
        return localByteArrayOutputStream;
    }

    public static Protocol recommendByCity(Context paramContext, int paramInt1,
            int paramInt2, int paramInt3, String paramString1,
            String paramString2, double paramDouble1, double paramDouble2,
            int paramInt4) {
        return recommendByCity(paramContext, paramInt1, paramInt2, paramInt3,
                paramString1, paramString2, paramDouble1, paramDouble2,
                paramInt4, 0, -1);
    }

    public static Protocol recommendByCity(Context paramContext, int paramInt1,
            int paramInt2, int paramInt3, String paramString1,
            String paramString2, double paramDouble1, double paramDouble2,
            int paramInt4, int paramInt5, int paramInt6) {
        String str1 = MapUtil.getCityId();
        String str2 = null;
        Object localObject = str1.substring(0, 2);
        str1 = str1.substring(2, 4);
        if (!StringUtils.isEmpty(paramString1))
            str2 = "keyword:" + paramString1;
        else
            str2 = "";
        if (paramString2 != null)
            str2 = str2 + ";address:" + paramString2;
        str2 = str2 + ";lat:" + paramDouble1 + ";lon:" + paramDouble2;
        localObject = new Param().append("pn", String.valueOf(paramInt2))
                .append("ps", String.valueOf(paramInt3))
                .append("lpi", (String) localObject).append("q", str2)
                .append("method", String.valueOf(paramInt4))
                .append("customize", "1").append("sort", "3");
        if (!str1.equals("00"))
            ((Param) localObject).append("lci", str1);
        if (paramInt5 > 0)
            ((Param) localObject).append("shopid", String.valueOf(paramInt5));
        if (paramInt6 >= 0)
            ((Param) localObject).append("cat", String.valueOf(paramInt6));
        return (Protocol) new CProtocol(paramContext, null, "coupon",
                (Param) localObject);
    }

    public static CProtocol registerRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "register", paramParam, false);
    }

    public static CProtocol registerWeiboRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "weiboregister", paramParam,
                false);
    }

    public static Protocol requestDetail(Context paramContext,
            String paramString1, boolean paramBoolean, String paramString2) {
        Param localParam = new Param().append("id", paramString1);
        String str;
        if (!paramBoolean)
            str = MapUtil.getCity();
        else
            str = MapUtil.getGpsCity();
        return new CProtocol(paramContext, null, paramString2,
                localParam.append("city", str));
    }

    public static Protocol requestShopDetail(Context paramContext,
            String paramString) {
        return new CProtocol(paramContext, null, "search_detail", new Param()
                .append("id", paramString).append("type", "2"));
    }

    public static CProtocol resetPswRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "reset", paramParam, false);
    }

    public static Protocol storeByCity(Context paramContext, int paramInt1,
            int paramInt2, int paramInt3, String paramString) {
        String str = MapUtil.getCityId();
        Object localObject = str.substring(0, 2);
        str = str.substring(2, 4);
        localObject = new Param().append("q", paramString)
                .append("ps", String.valueOf(paramInt3))
                .append("pn", String.valueOf(paramInt2))
                .append("lpi", (String) localObject);
        if (!str.equals("00"))
            ((Param) localObject).append("lci", str);
        return (Protocol) new CProtocol(paramContext, null, "city",
                (Param) localObject);
    }

    public static Protocol storeSearch(Context paramContext, int paramInt1,
            String paramString1, String paramString2, int paramInt2) {
        String str1 = MapUtil.getCityId();
        Object localObject = str1.substring(0, 2);
        str1 = str1.substring(2, 4);
        String str2 = "category:" + paramInt2;
        if (!StringUtils.isEmpty(paramString1))
            str2 = str2 + ";keyword:" + paramString1;
        if (paramString2 != null)
            str2 = str2 + ";address:" + paramString2;
        localObject = new Param().append("pn", String.valueOf(paramInt1))
                .append("ps", "10").append("lpi", (String) localObject)
                .append("q", str2).append("customize", "1");
        if (!str1.equals("00"))
            ((Param) localObject).append("lci", str1);
        return (Protocol) new CProtocol(paramContext, null, "store",
                (Param) localObject);
    }

    public static CProtocol storeUpRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "fav", paramParam, false);
    }

    public static CProtocol synchroFavRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "syn_fav", paramParam, false);
    }

    public static CProtocol zhifubaoPayRequest(Context paramContext,
            Param paramParam, boolean paramBoolean) {
        return new CProtocol(paramContext, null, "zhifubao", paramParam, false);
    }
}
