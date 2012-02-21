package com.dld.protocol.json;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommentResponse extends JsonBean {
    public int avgStar;
    public int code;
    public List<Comment> commentList = new ArrayList();
    public String message;
    public int total;

    public CommentResponse parseXml(Element paramElement) throws Exception {
        Object localObject1 = (Element) paramElement.getElementsByTagName(
                "code").item(0);
        if (localObject1 != null)
            this.code = Integer.parseInt(((Element) localObject1)
                    .getFirstChild().getNodeValue());
        localObject1 = (Element) paramElement.getElementsByTagName("msg").item(
                0);
        if (localObject1 != null)
            this.message = ((Element) localObject1).getFirstChild()
                    .getNodeValue();
        localObject1 = (Element) paramElement.getElementsByTagName("star")
                .item(0);
        if (localObject1 != null)
            this.avgStar = Integer.parseInt(((Element) localObject1)
                    .getFirstChild().getNodeValue());
        localObject1 = paramElement.getElementsByTagName("comments");
        if ((localObject1 != null)
                && (((NodeList) localObject1).getLength() > 0)) {
            localObject1 = (Element) ((NodeList) localObject1).item(0);
            this.total = Integer.parseInt(((Element) localObject1)
                    .getAttribute("total"));
            NodeList localNodeList1 = ((Element) localObject1)
                    .getElementsByTagName("comment");
            for (int i = 0; i < localNodeList1.getLength(); i++) {
                Object localObject2 = (Element) localNodeList1.item(i);
                localObject1 = new Comment();
                NodeList localNodeList2 = ((Element) localObject2)
                        .getChildNodes();
                for (int j = 0; j < localNodeList2.getLength(); j++) {
                    if (localNodeList2.item(j).getNodeType() != 1)
                        continue;
                    localObject2 = localNodeList2.item(j);
                    Node localNode = ((Node) localObject2).getFirstChild();
                    if (localNode == null)
                        continue;
                    if ("id".equals(((Node) localObject2).getNodeName()))
                        ((Comment) localObject1).id = localNode.getNodeValue();
                    if ("name".equals(((Node) localObject2).getNodeName()))
                        ((Comment) localObject1).name = localNode
                                .getNodeValue();
                    if ("star".equals(((Node) localObject2).getNodeName()))
                        ((Comment) localObject1).starNum = Integer
                                .parseInt(localNode.getNodeValue());
                    if ("date".equals(((Node) localObject2).getNodeName()))
                        ((Comment) localObject1).date = localNode
                                .getNodeValue();
                    if (!"content".equals(((Node) localObject2).getNodeName()))
                        continue;
                    ((Comment) localObject1).content = localNode.getNodeValue();
                }
                this.commentList.add((Comment) localObject1);
            }
        }
        return (CommentResponse) (CommentResponse) this;
    }
}
