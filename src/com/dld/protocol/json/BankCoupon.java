package com.dld.protocol.json;

import com.dld.android.util.ReflectionFactory;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BankCoupon extends JsonBean {
    public List<BankCouponDetail> details = new ArrayList();
    public int total = 0;

    public List getDetails() {
        return this.details;
    }

    public int getTotal() {
        return this.total;
    }

    public JsonBean parseXml(Element paramElement) throws Exception {
        this.total = Integer.parseInt(((Element) paramElement
                .getElementsByTagName("total").item(0)).getFirstChild()
                .getNodeValue().trim());
        NodeList localNodeList = paramElement.getElementsByTagName("result");
        int i = localNodeList.getLength();
        for (int j = 0; j < i; j++) {
            BankCouponDetail localBankCouponDetail = (BankCouponDetail) ReflectionFactory
                    .create((Element) localNodeList.item(j),
                            BankCouponDetail.class);
            this.details.add(localBankCouponDetail);
        }
        return this;
    }

    public void setDetails(List paramList) {
        this.details = paramList;
    }

    public void setTotal(int paramInt) {
        this.total = paramInt;
    }
}
