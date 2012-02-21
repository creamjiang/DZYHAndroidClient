package com.dld.protocol.json;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PayResponse extends JsonBean {
    public int code;
    public int id;
    public String merchant;
    public String message;
    public String orderInfo;
    public String token;

    public JsonBean parseXml(Element paramElement) throws Exception {
        Object localObject = (Element) paramElement
                .getElementsByTagName("code").item(0);
        if ((localObject != null)
                && (((Element) localObject).getFirstChild() != null))
            this.code = Integer.parseInt(((Element) localObject)
                    .getFirstChild().getNodeValue());
        localObject = (Element) paramElement.getElementsByTagName("msg")
                .item(0);
        if (localObject != null) {
            localObject = ((Element) localObject).getFirstChild();
            if (localObject != null)
                this.message = ((Node) localObject).getNodeValue();
        }
        localObject = (Element) paramElement.getElementsByTagName("token")
                .item(0);
        if (localObject != null) {
            localObject = ((Element) localObject).getFirstChild();
            if (localObject != null)
                this.token = ((Node) localObject).getNodeValue();
        }
        localObject = (Element) paramElement.getElementsByTagName("orderInfo")
                .item(0);
        if (localObject != null) {
            localObject = ((Element) localObject).getFirstChild();
            if (localObject != null)
                this.orderInfo = ((Node) localObject).getNodeValue();
        }
        localObject = (Element) paramElement.getElementsByTagName("merchant")
                .item(0);
        if (localObject != null) {
            localObject = ((Element) localObject).getFirstChild();
            if (localObject != null)
                this.merchant = ((Node) localObject).getNodeValue();
        }
        localObject = (Element) paramElement.getElementsByTagName("id").item(0);
        if (localObject != null) {
            localObject = ((Element) localObject).getFirstChild();
            if (localObject != null)
                this.id = Integer.parseInt(((Node) localObject).getNodeValue());
        }
        return (JsonBean) this;
    }

    public String toString() {
        return "PayResponse [code=" + this.code + ", id=" + this.id
                + ", merchant=" + this.merchant + ", message=" + this.message
                + ", token=" + this.token + "]";
    }
}
