package com.dld.protocol.json;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OrderResponse extends JsonBean {
    public int code;
    public String message;
    public Order order;

    public JsonBean parseXml(Element paramElement) throws Exception {
        Object localObject = (Element) paramElement
                .getElementsByTagName("code").item(0);
        if (localObject != null)
            this.code = Integer.parseInt(((Element) localObject)
                    .getFirstChild().getNodeValue());
        localObject = (Element) ((Element) paramElement.getElementsByTagName(
                "head").item(0)).getElementsByTagName("msg").item(0);
        if (localObject != null) {
            localObject = ((Element) localObject).getFirstChild();
            if (localObject != null)
                this.message = ((Node) localObject).getNodeValue();
        }
        localObject = paramElement.getElementsByTagName("order");
        if ((localObject != null) && (((NodeList) localObject).getLength() > 0)) {
            this.order = new Order();
            NodeList localNodeList = ((NodeList) localObject).item(0)
                    .getChildNodes();
            for (int i = 0; i < localNodeList.getLength(); i++) {
                if (localNodeList.item(i).getNodeType() != 1)
                    continue;
                Node localNode = ((Element) localNodeList.item(i))
                        .getFirstChild();
                if (localNode == null)
                    continue;
                if ("id".equals(localNodeList.item(i).getNodeName()))
                    this.order.id = new Integer(localNode.getNodeValue());
                if ("price".equals(localNodeList.item(i).getNodeName()))
                    this.order.price = Float.parseFloat(localNode
                            .getNodeValue());
                if ("quantity".equals(localNodeList.item(i).getNodeName()))
                    this.order.quantity = Integer.parseInt(localNode
                            .getNodeValue());
                if ("freight".equals(localNodeList.item(i).getNodeName()))
                    this.order.freight = Float.parseFloat(localNode
                            .getNodeValue());
                if ("discount".equals(localNodeList.item(i).getNodeName()))
                    this.order.discount = Float.parseFloat(localNode
                            .getNodeValue());
                if ("total".equals(localNodeList.item(i).getNodeName()))
                    this.order.total = Float.parseFloat(localNode
                            .getNodeValue());
                if (!"date".equals(localNodeList.item(i).getNodeName()))
                    continue;
                this.order.date = localNode.getNodeValue();
            }
        }
        return (JsonBean) this;
    }

    public String toString() {
        return "OrderResponse [code=" + this.code + ", message=" + this.message
                + ", order=" + this.order + "]";
    }
}
