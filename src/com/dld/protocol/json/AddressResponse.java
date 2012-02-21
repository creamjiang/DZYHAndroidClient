package com.dld.protocol.json;

import com.dld.coupon.util.XmlUtil;

import org.w3c.dom.Element;

public class AddressResponse extends JsonBean {
    public String city;
    public int code;
    public boolean isDistrict;
    public String name;

    public JsonBean parseXml(Element paramElement) throws Exception {
        int i = 1;
        this.code = XmlUtil.getInt(paramElement, "code");
        if (this.code == 0) {
            this.city = XmlUtil.getString(paramElement, "city");
            this.name = XmlUtil.getString(paramElement, "name");
            if (XmlUtil.getInt(paramElement, "is_district") != i)
                i = 0;
            this.isDistrict = i == 0 ? false : true;
        }
        return this;
    }
}
