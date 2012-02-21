package com.dld.protocol.json;

import com.dld.android.util.ReflectionFactory;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GroupComment extends JsonBean {
    public List<GroupCommentDetail> details = new ArrayList();
    public int total = 0;

    public JsonBean parseXml(Element paramElement) throws Exception {
        this.total = Integer.parseInt(((Element) paramElement
                .getElementsByTagName("total").item(0)).getFirstChild()
                .getNodeValue().trim());
        NodeList localNodeList = paramElement.getElementsByTagName("result");
        int i = localNodeList.getLength();
        for (int j = 0; j < i; j++) {
            GroupCommentDetail localGroupCommentDetail = (GroupCommentDetail) ReflectionFactory
                    .create((Element) localNodeList.item(j),
                            GroupCommentDetail.class);
            this.details.add(localGroupCommentDetail);
        }
        return this;
    }
}
