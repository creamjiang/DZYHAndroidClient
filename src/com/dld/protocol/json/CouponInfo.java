package com.dld.protocol.json;

import org.json.JSONObject;
import org.w3c.dom.Element;

public class CouponInfo extends JsonBean {
    public String content;
    public String phone;
    public String storeCode;
    public String storeDiscount;
    public String storeName;
    public String type;
    public String validend;
    public String validstart;

    public String buildMessageContent() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(this.storeName);
        localStringBuilder.append("\n");
        localStringBuilder.append(this.content);
        return localStringBuilder.toString();
    }

    public String getMessageType() {
        String str;
        if (!this.type.equalsIgnoreCase("SMS")) {
            if (!this.type.equalsIgnoreCase("MMS")) {
                if (!this.type.equalsIgnoreCase("ALL"))
                    str = "SMS";
                else
                    str = "MMS";
            } else
                str = "MMS";
        } else
            str = "SMS";
        return str;
    }

    public CouponInfo parse(JSONObject paramJSONObject) throws Exception {
        JSONObject localJSONObject = paramJSONObject.getJSONObject("results");
        this.validend = localJSONObject.getString("validend");
        this.validstart = localJSONObject.getString("validstart");
        this.content = localJSONObject.getString("content");
        this.type = localJSONObject.getString("type");
        this.storeCode = localJSONObject.getString("storeCode");
        return this;
    }

    public CouponInfo parseXml(Element paramElement) throws Exception {
        return this;
    }
}
