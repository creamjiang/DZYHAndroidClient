package com.dld.protocol.json;

import com.dld.android.util.LogUtils;
import com.dld.android.util.ReflectionFactory;
import com.dld.coupon.util.XmlUtil;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UserCenterHome extends JsonBean {
    public String code;
    public Customer customer;
    public String orderTotal;
    public int orderUnpaid;

    public JsonBean parseXml(Element paramElement) throws Exception {
        try {
            this.code = XmlUtil.getString(paramElement, "code");
            this.orderTotal = XmlUtil.getString(paramElement, "total");
            this.orderUnpaid = XmlUtil.getInt(paramElement, "unpaid");
            this.customer = ((Customer) ReflectionFactory.create(paramElement
                    .getElementsByTagName("customer").item(0), Customer.class));
            return this;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", "", localException);
        }
    }
}
