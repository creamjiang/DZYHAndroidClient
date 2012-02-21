package com.dld.protocol.json;

import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.XmlUtil;

import org.w3c.dom.Element;

public class CommonResponse extends JsonBean {
    public String message;

    public JsonBean parseXml(Element paramElement) throws Exception {
        if (!StringUtils.equals("0", XmlUtil.getString(paramElement, "code")))
            this.message = XmlUtil.getString(paramElement, "msg");
        return this;
    }
}
