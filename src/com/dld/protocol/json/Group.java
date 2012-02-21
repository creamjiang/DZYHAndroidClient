package com.dld.protocol.json;

import com.dld.android.util.ReflectionFactory;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Group extends JsonBean {
    public List<GroupDetail> details = new ArrayList();
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
        NodeList localNodeList1 = paramElement.getElementsByTagName("result");
        int j = localNodeList1.getLength();
        for (int i = 0; i < j; i++) {
            Element localElement = (Element) localNodeList1.item(i);
            GroupDetail localGroupDetail = (GroupDetail) ReflectionFactory
                    .create(localElement, GroupDetail.class);
            NodeList localNodeList2 = localElement.getElementsByTagName("shop");
            int k = localNodeList2.getLength();
            for (int m = 0; m < k; m++)
                localGroupDetail.shops.add((Shop) ReflectionFactory.create(
                        localNodeList2.item(m), Shop.class));
            this.details.add(localGroupDetail);
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
