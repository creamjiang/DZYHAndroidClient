package com.dld.protocol.json;

import com.dld.android.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HuoDongResponse extends JsonBean {
    public int code;
    public List<Promotion> promList = new ArrayList();

    public JsonBean parseXml(Element paramElement) throws Exception {
        Element localElement = (Element) paramElement.getElementsByTagName(
                "code").item(0);
        if (localElement != null)
            this.code = Integer.parseInt(localElement.getFirstChild()
                    .getNodeValue());
        NodeList localNodeList1 = paramElement
                .getElementsByTagName("promotion");
        LogUtils.log("main", "before pNodesList!=null");
        if ((localNodeList1 != null) && (localNodeList1.getLength() > 0)) {
            LogUtils.log("main", "after pNodesList!=null");
            for (int j = 0; j < localNodeList1.getLength(); j++) {
                NodeList localNodeList2 = ((Element) localNodeList1.item(j))
                        .getChildNodes();
                Promotion localPromotion = new Promotion();
                for (int i = 0; i < localNodeList2.getLength(); i++) {
                    if (localNodeList2.item(i).getNodeType() != 1)
                        continue;
                    Node localNode = ((Element) localNodeList2.item(i))
                            .getFirstChild();
                    if (localNode == null)
                        continue;
                    if ("id".equals(localNodeList2.item(i).getNodeName()))
                        localPromotion.id = localNode.getNodeValue();
                    if ("image".equals(localNodeList2.item(i).getNodeName()))
                        localPromotion.image = localNode.getNodeValue();
                    if ("content".equals(localNodeList2.item(i).getNodeName()))
                        localPromotion.content = localNode.getNodeValue();
                    if (!"type".equals(localNodeList2.item(i).getNodeName()))
                        continue;
                    localPromotion.type = localNode.getNodeValue();
                }
                this.promList.add(localPromotion);
            }
        }
        return this;
    }
}
