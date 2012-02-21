package com.dld.protocol.json;

import com.dld.android.util.LogUtils;
import com.dld.android.util.ReflectionFactory;
import com.dld.coupon.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommonFav extends JsonBean {
    public List<DetailRef> details = new ArrayList();
    public List<DetailDelete> detailsDeleteList = new ArrayList();
    public int total = 0;

    public JsonBean parseXml(Element paramElement) throws Exception {
        NodeList localNodeList1 = paramElement
                .getElementsByTagName("no_result");
        int j = localNodeList1.getLength();
        for (int m = 0; m < j; m++) {
            DetailDelete localDetailDelete = new DetailDelete();
            Object localObject1 = (Element) localNodeList1.item(m);
            localDetailDelete.type = Integer
                    .parseInt(((Element) ((Element) localObject1)
                            .getElementsByTagName("type").item(0))
                            .getFirstChild().getNodeValue().trim());
            localDetailDelete.id = ((Element) ((Element) localObject1)
                    .getElementsByTagName("id").item(0)).getFirstChild()
                    .getNodeValue().trim();
            this.detailsDeleteList.add(localDetailDelete);
        }
        Object localObject1 = paramElement.getElementsByTagName("result");
        int i = ((NodeList) localObject1).getLength();
        this.total = i;
        for (j = 0; j < i; j++) {
            Object localObject3 = (Element) ((NodeList) localObject1).item(j);
            int k = Integer.parseInt(((Element) ((Element) localObject3)
                    .getElementsByTagName("type").item(0)).getFirstChild()
                    .getNodeValue().trim());
            LogUtils.log("main", "type==" + k);
            Object localObject2;
            if ((k != 3) && (k != 6)) {
                if (k != 7) {
                    if (k != 0) {
                        if (k != 2) {
                            if (k != 5)
                                continue;
                            localObject2 = (BankCouponDetail) ReflectionFactory
                                    .create((Node) localObject3,
                                            BankCouponDetail.class);
                            localObject3 = new DetailRef();
                            ((DetailRef) localObject3).type = 3;
                            ((DetailRef) localObject3).detail = ((Detail) localObject2);
                            this.details.add((DetailRef) localObject3);
                        } else {
                            localObject2 = (Ticket) ReflectionFactory.create(
                                    (Node) localObject3, Ticket.class);
                            ((Ticket) localObject2).branch_name = XmlUtil
                                    .getString((Element) localObject3,
                                            "shop_name");
                            localObject3 = new DetailRef();
                            ((DetailRef) localObject3).type = 4;
                            ((DetailRef) localObject3).detail = ((Detail) localObject2);
                            this.details.add((DetailRef) localObject3);
                        }
                    } else {
                        localObject3 = (CouponDetail) ReflectionFactory.create(
                                (Node) localObject3, CouponDetail.class);
                        localObject2 = new DetailRef();
                        ((DetailRef) localObject2).type = 1;
                        ((DetailRef) localObject2).detail = ((Detail) localObject3);
                        this.details.add((DetailRef) localObject2);
                    }
                } else {
                    localObject3 = (CouponDetail) ReflectionFactory.create(
                            (Node) localObject3, CouponDetail.class);
                    localObject2 = new DetailRef();
                    ((DetailRef) localObject2).type = 0;
                    ((DetailRef) localObject2).detail = ((Detail) localObject3);
                    this.details.add((DetailRef) localObject2);
                }
            } else {
                localObject2 = (GroupDetail) ReflectionFactory.create(
                        (Node) localObject3, GroupDetail.class);
                NodeList localNodeList2 = ((Element) localObject3)
                        .getElementsByTagName("shop");
                int i1 = localNodeList2.getLength();
                for (int n = 0; n < i1; n++)
                    ((GroupDetail) localObject2).shops
                            .add((Shop) ReflectionFactory.create(
                                    localNodeList2.item(n), Shop.class));
                DetailRef localDetailRef = new DetailRef();
                localDetailRef.type = 2;
                localDetailRef.detail = ((Detail) localObject2);
                this.details.add(localDetailRef);
            }
        }
        return (JsonBean) (JsonBean) (JsonBean) this;
    }
}
