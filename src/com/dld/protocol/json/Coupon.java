package com.dld.protocol.json;

import com.dld.android.util.ReflectionFactory;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Coupon extends JsonBean {
    public List<CouponDetail> details = new ArrayList();
    public PageInfo pageInfo = new PageInfo();

    public List getDetails() {
        return this.details;
    }

    public int getTotal() {
        return this.pageInfo.total;
    }

    public JsonBean parse(JSONObject paramJSONObject) throws Exception {
        this.pageInfo = ((PageInfo) ReflectionFactory.create(
                paramJSONObject.getJSONObject("paging"), PageInfo.class));
        JSONArray localJSONArray = paramJSONObject.getJSONArray("results");
        for (int i = 0; i < localJSONArray.length(); i++)
            this.details.add((CouponDetail) ReflectionFactory.create(
                    localJSONArray.getJSONObject(i), CouponDetail.class));
        return this;
    }

    public JsonBean parseXml(Element paramElement) throws Exception {
        this.pageInfo = new PageInfo();
        Element localElement = (Element) paramElement.getElementsByTagName(
                "total").item(0);
        this.pageInfo.total = Integer.parseInt(localElement.getFirstChild()
                .getNodeValue().trim());
        NodeList localNodeList = paramElement.getElementsByTagName("result");
        int j = localNodeList.getLength();
        for (int i = 0; i < j; i++) {
            CouponDetail localCouponDetail = (CouponDetail) ReflectionFactory
                    .create((Element) localNodeList.item(i), CouponDetail.class);
            this.details.add(localCouponDetail);
        }
        return this;
    }

    public void setDetails(List paramList) {
        this.details = paramList;
    }

    public void setTotal(int paramInt) {
        this.pageInfo.total = paramInt;
    }
}
