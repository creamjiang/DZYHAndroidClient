package com.dld.protocol.json;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Response extends JsonBean {
    public int code;
    public String message;
    public User user;

    public JsonBean parseXml(Element paramElement) throws Exception {
        Object localObject = (Element) paramElement
                .getElementsByTagName("code").item(0);
        if (localObject != null)
            this.code = Integer.parseInt(((Element) localObject)
                    .getFirstChild().getNodeValue());
        localObject = (Element) ((Element) paramElement.getElementsByTagName(
                "head").item(0)).getElementsByTagName("msg").item(0);
        if (localObject != null)
            this.message = ((Element) localObject).getFirstChild()
                    .getNodeValue();
        localObject = paramElement.getElementsByTagName("customer");
        if ((localObject != null) && (((NodeList) localObject).getLength() > 0)) {
            this.user = new User();
            localObject = ((NodeList) localObject).item(0).getChildNodes();
            for (int i = 0; i < ((NodeList) localObject).getLength(); i++) {
                if (((NodeList) localObject).item(i).getNodeType() != 1)
                    continue;
                Node localNode = ((Element) ((NodeList) localObject).item(i))
                        .getFirstChild();
                if (localNode == null)
                    continue;
                if ("id".equals(((NodeList) localObject).item(i).getNodeName()))
                    this.user.id = localNode.getNodeValue();
                if ("email".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.email = localNode.getNodeValue();
                if ("mobile".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.mobile = localNode.getNodeValue();
                if ("customername".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.customername = localNode.getNodeValue();
                if ("realname".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.realname = localNode.getNodeValue();
                if ("gender".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.gender = Integer.parseInt(localNode
                            .getNodeValue());
                if ("qqcode".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.qqcode = localNode.getNodeValue();
                if ("msn"
                        .equals(((NodeList) localObject).item(i).getNodeName()))
                    this.user.msn = localNode.getNodeValue();
                if ("weibo".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.weibo = localNode.getNodeValue();
                if ("idcard".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.idcard = localNode.getNodeValue();
                if ("cityname".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.idcard = localNode.getNodeValue();
                if ("lastLoginTime".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.lastLoginTime = localNode.getNodeValue();
                if ("registerDate".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.registerDate = localNode.getNodeValue();
                if ("mark".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.mark = new Integer(localNode.getNodeValue());
                if ("level".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.level = localNode.getNodeValue();
                if ("registerDate".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.registerDate = localNode.getNodeValue();
                if ("birthday".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.birthday = localNode.getNodeValue();
                if ("intrests".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.intrests = localNode.getNodeValue();
                if ("cards".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    this.user.cards = localNode.getNodeValue();
                if (!"profile".equals(((NodeList) localObject).item(i)
                        .getNodeName()))
                    continue;
                this.user.profile = localNode.getNodeValue();
            }
        }
        return (JsonBean) this;
    }
}
