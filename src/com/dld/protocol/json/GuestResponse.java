package com.dld.protocol.json;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GuestResponse extends JsonBean {
    public String id;

    public JsonBean parseXml(Element paramElement) throws Exception {
        Element localElement = (Element) paramElement.getElementsByTagName(
                "customer").item(0);
        if (localElement != null)
            this.id = localElement.getElementsByTagName("id").item(0)
                    .getFirstChild().getNodeValue();
        else
            this.id = null;
        return this;
    }
}
