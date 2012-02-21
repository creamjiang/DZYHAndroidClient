package com.dld.protocol.json;

import android.content.Context;

import com.dld.android.net.Callback;
import com.dld.android.net.Param;
import com.dld.android.util.LogUtils;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CProtocol extends Protocol {
    private static final String ADDRESS = "http://www.dld.com/vlife/address?";
    private static final String ADD_NEWCOMMENT = "http://www.dld.com/customer/action/shr/add/v1";
    private static final String ALL_ORDERS = "http://www.dld.com/customer/action/order/all/v1/";
    private static final String ALL_SEARCH = "http://www.dld.com/search/all?";
    private static final String BANK_SEARCH = "http://www.dld.com/search/bank?";
    private static final String BIND_PHONE = "http://www.dld.com/customer/action/customer/bm/v1";
    private static final String BOUGHT = "http://www.dld.com/customer/action/order/bought/v1/";
    private static final String CAIFUTONGPAY = "http://www.dld.com/trade/action/pay/ten/v1?";
    private static final String CANCELL = "http://www.dld.com/trade/action/buy/tuan/v1/1/?";
    public static final String CANCELLZHIFUBAO = "http://www.dld.com/trade/action/buy/tuan/v1/2/";
    private static final String COMMENT = "http://www.dld.com/tuan/comment?";
    private static final String COUPON_SEARCH = "http://www.dld.com/search/coupon?";
    private static final String DELETE_FAV = "http://www.dld.com/web/api/collect/remove?";
    private static final String DETAIL = "http://www.dld.com/vlife/detail?";
    private static final String EMAIL = "http://www.dld.com/vlife/email?";
    private static final String FAV = "http://www.dld.com/web/api/collect/add?";
    private static final String FEEDBACK = "http://www.dld.com/account/feedback?";
    private static final String GET_COMMONTLIST = "http://www.dld.com/customer/action/shr/list/v1";
    private static final String GET_COMMONTNEW = "http://www.dld.com/customer/action/shr/brief/v1/";
    private static final String GROUP_SEARCH = "http://www.dld.com/search/deal?";
    private static final String GUEST_LOGIN = "http://www.dld.com/customer/action/guest/login/v1";
    private static final String GUEST_REGISTER = "http://www.dld.com/customer/action/guest/register/v1";
    private static final String HUODONG = "http://www.dld.com/customer/action/promotion/get/v2?";
    private static final String INVITE = "http://www.dld.com/customer/action/invite/do/v1";
    private static final String LIKE = "http://www.dld.com/vlife/like?";
    private static final String LOGIN = "http://www.dld.com/customer/action/account/login/v1?";
    private static final String ORDER = "http://www.dld.com/trade/action/order/new/v1?";
    private static final String ORDERUPDATE = "http://www.dld.com/trade/action/order/upd/v1?";
    private static final String PERSONAL = "http://www.dld.com/customer/action/uc/home/v1/";
    private static final String REFUND = "http://www.dld.com/customer/action/order/refund/v1/";
    private static final String REGISTER = "http://www.dld.com/customer/action/customer/register/v1?";
    private static final String REGISTERED_CONTACTS = "http://www.dld.com/customer/action/invite/contacts/v1";
    private static final String RESET = "http://www.dld.com/trade/action/reset?";
    public static final String SEARCH_DETAIL = "http://www.dld.com/search/detail?";
    private static final String STORE_SEARCH = "http://www.dld.com/vlife/coupon?";
    public static final String SYNCHRON_FAV = "http://www.dld.com/web/api/collect/sync?";
    private static final String TIP = "http://www.dld.com/search/tip?";
    private static final String UNPAID_ORDERS = "http://www.dld.com/customer/action/order/unpaid/v1/";
    private static final String UPLOAD = "http://www.dld.com/vlife/upload?";
    private static final String VCODE = "http://www.dld.com/customer/action/vcode/get/v1?";
    private static final String VEERSION = "http://www.dld.com/vlife/version?";
    private static final String WEIBO = "http://www.dld.com/vlife/share?";
    private static final String WEIBOREGISTER = "http://www.dld.com/customer/action/account/register/v1?";
    private static final String YINGLIANPAY = "http://www.dld.com/trade/action/pay/dna/v1?";
    private static final String ZHIFUBAO = "http://www.dld.com/trade/action/pay/ali/v1?";
    
    private static final String DISCOUNT = "http://shenzhen.dld.com/www/?r=www/api/list&userAgent=dld_android&module=discount&&";

    public CProtocol(Context paramContext, String paramString1,
            String paramString2, Param paramParam) {
        this(paramContext, null, paramString1, paramString2, paramParam, true,
                true);
    }

    public CProtocol(Context paramContext, String paramString1,
            String paramString2, Param paramParam, boolean paramBoolean) {
        this(paramContext, null, paramString1, paramString2, paramParam, true,
                paramBoolean);
    }

    public CProtocol(Context paramContext, String paramString1,
            String paramString2, String paramString3, Param paramParam,
            boolean paramBoolean1, boolean paramBoolean2) {
        super(paramContext, paramString1, paramString2, paramString3,
                paramParam, paramBoolean1, paramBoolean2);
    }

    public String getAbsoluteUrl() {
        return getUrl() + getParam().toString();
    }

    public Callback getCallback() {
        Callback localCallback = super.getCallback();
        localCallback.custom = true;
        return localCallback;
    }

    public Param getParam() {
        if (this.param == null) {
            this.param = new Param();
            LogUtils.log("Protocol", "parame是空的");
        }
        return this.param;
    }

    public String getUrl() {
        String str;
        if (!this.method.equals("coupon")) {
            if (!this.method.equals("group")) {
                if (!this.method.equals("bank")) {
                    if (!this.method.equals("store")) {
                        if (!this.method.equals("weibo")) {
                            if (!this.method.equals("comment")) {
                                if (!this.method.equals("email")) {
                                    if (!this.method.equals("upload")) {
                                        if (!this.method.equals("version")) {
                                            if (!this.method
                                                    .equals("yinglianpay")) {
                                                if (!this.method
                                                        .equals("caifutongpay")) {
                                                    if (!this.method
                                                            .equals("like")) {
                                                        if (!this.method
                                                                .equals("feedback")) {
                                                            if (!this.method
                                                                    .equals("detail")) {
                                                                if (!this.method
                                                                        .equals("search_detail")) {
                                                                    if (!this.method
                                                                            .equals("all_search")) {
                                                                        if (!this.method
                                                                                .equals("register")) {
                                                                            if (!this.method
                                                                                    .equals("login")) {
                                                                                if (!this.method
                                                                                        .equals("reset")) {
                                                                                    if (!this.method
                                                                                            .equals("guest_register")) {
                                                                                        if (!this.method
                                                                                                .equals("guest_login")) {
                                                                                            if (!this.method
                                                                                                    .equals("personal")) {
                                                                                                if (!this.method
                                                                                                        .equals("all_orders")) {
                                                                                                    if (!this.method
                                                                                                            .equals("unpaid_orders")) {
                                                                                                        if (!this.method
                                                                                                                .equals("bought")) {
                                                                                                            if (!this.method
                                                                                                                    .equals("refund")) {
                                                                                                                if (!this.method
                                                                                                                        .equals("vcode")) {
                                                                                                                    if (!this.method
                                                                                                                            .equals("bind_phone")) {
                                                                                                                        if (!this.method
                                                                                                                                .equals("registered_contacts")) {
                                                                                                                            if (!this.method
                                                                                                                                    .equals("invite")) {
                                                                                                                                if (!this.method
                                                                                                                                        .equals("fav")) {
                                                                                                                                    if (!this.method
                                                                                                                                            .equals("order")) {
                                                                                                                                        if (!this.method
                                                                                                                                                .equals("orderupdate")) {
                                                                                                                                            if (!this.method
                                                                                                                                                    .equals("cancellpay")) {
                                                                                                                                                if (!this.method
                                                                                                                                                        .equals("weiboregister")) {
                                                                                                                                                    if (!this.method
                                                                                                                                                            .equals("zhifubao")) {
                                                                                                                                                        if (!this.method
                                                                                                                                                                .equals("cancellzhifubao")) {
                                                                                                                                                            if (!this.method
                                                                                                                                                                    .equals("address")) {
                                                                                                                                                                if (!this.method
                                                                                                                                                                        .equals("tip")) {
                                                                                                                                                                    if (!this.method
                                                                                                                                                                            .equals("huodong")) {
                                                                                                                                                                        if (!this.method
                                                                                                                                                                                .equals("delete_fav")) {
                                                                                                                                                                            if (!this.method
                                                                                                                                                                                    .equals("syn_fav")) {
                                                                                                                                                                                if (!this.method
                                                                                                                                                                                        .equals("add_newcomment")) {
                                                                                                                                                                                    if (!this.method
                                                                                                                                                                                            .equals("get_commentlist")) {
                                                                                                                                                                                        if (!this.method
                                                                                                                                                                                                .equals("get_commentnew"))
                                                                                                                                                                                            str = "http://www.dld.com/search/coupon?";
                                                                                                                                                                                        else
                                                                                                                                                                                            str = "http://www.dld.com/customer/action/shr/brief/v1//"
                                                                                                                                                                                                    + this.type
                                                                                                                                                                                                    + "?";
                                                                                                                                                                                    } else
                                                                                                                                                                                        str = "http://www.dld.com/customer/action/shr/list/v1/"
                                                                                                                                                                                                + this.type
                                                                                                                                                                                                + "?";
                                                                                                                                                                                } else
                                                                                                                                                                                    str = "http://www.dld.com/customer/action/shr/add/v1/"
                                                                                                                                                                                            + this.type
                                                                                                                                                                                            + "?";
                                                                                                                                                                            } else
                                                                                                                                                                                str = "http://www.dld.com/web/api/collect/sync?";
                                                                                                                                                                        } else
                                                                                                                                                                            str = "http://www.dld.com/web/api/collect/remove?";
                                                                                                                                                                    } else
                                                                                                                                                                        str = "http://www.dld.com/customer/action/promotion/get/v2?";
                                                                                                                                                                } else
                                                                                                                                                                    str = "http://www.dld.com/search/tip?";
                                                                                                                                                            } else
                                                                                                                                                                str = "http://www.dld.com/vlife/address?";
                                                                                                                                                        } else
                                                                                                                                                            str = "http://www.dld.com/trade/action/buy/tuan/v1/2/"
                                                                                                                                                                    + this.type
                                                                                                                                                                    + "?";
                                                                                                                                                    } else
                                                                                                                                                        str = "http://www.dld.com/trade/action/pay/ali/v1?";
                                                                                                                                                } else
                                                                                                                                                    str = "http://www.dld.com/customer/action/account/register/v1?";
                                                                                                                                            } else
                                                                                                                                                str = "http://www.dld.com/trade/action/buy/tuan/v1/1/?";
                                                                                                                                        } else
                                                                                                                                            str = "http://www.dld.com/trade/action/order/upd/v1?";
                                                                                                                                    } else
                                                                                                                                        str = "http://www.dld.com/trade/action/order/new/v1?";
                                                                                                                                } else
                                                                                                                                    str = "http://www.dld.com/web/api/collect/add?";
                                                                                                                            } else
                                                                                                                                str = "http://www.dld.com/customer/action/invite/do/v1";
                                                                                                                        } else
                                                                                                                            str = "http://www.dld.com/customer/action/invite/contacts/v1";
                                                                                                                    } else
                                                                                                                        str = "http://www.dld.com/customer/action/customer/bm/v1";
                                                                                                                } else
                                                                                                                    str = "http://www.dld.com/customer/action/vcode/get/v1?";
                                                                                                            } else
                                                                                                                str = "http://www.dld.com/customer/action/order/refund/v1/"
                                                                                                                        + this.type
                                                                                                                        + "?";
                                                                                                        } else
                                                                                                            str = "http://www.dld.com/customer/action/order/bought/v1/"
                                                                                                                    + this.type
                                                                                                                    + "?";
                                                                                                    } else
                                                                                                        str = "http://www.dld.com/customer/action/order/unpaid/v1/"
                                                                                                                + this.type
                                                                                                                + "?";
                                                                                                } else
                                                                                                    str = "http://www.dld.com/customer/action/order/all/v1/"
                                                                                                            + this.type
                                                                                                            + "?";
                                                                                            } else
                                                                                                str = "http://www.dld.com/customer/action/uc/home/v1/"
                                                                                                        + this.type;
                                                                                        } else
                                                                                            str = "http://www.dld.com/customer/action/guest/login/v1";
                                                                                    } else
                                                                                        str = "http://www.dld.com/customer/action/guest/register/v1";
                                                                                } else
                                                                                    str = "http://www.dld.com/trade/action/reset?";
                                                                            } else
                                                                                str = "http://www.dld.com/customer/action/account/login/v1?";
                                                                        } else
                                                                            str = "http://www.dld.com/customer/action/customer/register/v1?";
                                                                    } else
                                                                        str = "http://www.dld.com/search/all?";
                                                                } else
                                                                    str = "http://www.dld.com/search/detail?";
                                                            } else
                                                                str = "http://www.dld.com/vlife/detail?";
                                                        } else
                                                            str = "http://www.dld.com/account/feedback?";
                                                    } else
                                                        str = "http://www.dld.com/vlife/like?";
                                                } else
                                                    str = "http://www.dld.com/trade/action/pay/ten/v1?";
                                            } else
                                                str = "http://www.dld.com/trade/action/pay/dna/v1?";
                                        } else
                                            str = "http://www.dld.com/vlife/version?";
                                    } else
                                        str = "http://www.dld.com/vlife/upload?";
                                } else
                                    str = "http://www.dld.com/vlife/email?";
                            } else
                                str = "http://www.dld.com/tuan/comment?";
                        } else
                            str = "http://www.dld.com/vlife/share?";
                    } else
                        str = "http://www.dld.com/vlife/coupon?";
                } else
                    str = "http://www.dld.com/search/bank?";
            } else
                str = "http://www.dld.com/search/deal?";
        } else
            str = "http://www.dld.com/search/coupon?";
        return str;
    }

    protected Object parse(String paramString) throws Exception {
        LogUtils.log("CProtocol", "parse执行了 " + paramString);
        Object localObject2 = null;
        Object localObject1 = this.onProtocolResult.getJsonBeanClass()
                .newInstance();
        try {
            Element localElement = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(paramString
                            .getBytes("UTF-8"))).getDocumentElement();
            // localObject2 = localElement;
            return ((JsonBean) localObject1).parseXml(localElement);
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }
}
