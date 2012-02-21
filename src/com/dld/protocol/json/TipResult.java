package com.dld.protocol.json;

import com.dld.android.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TipResult extends JsonBean {
    public List<String> tips = new ArrayList();

    public JsonBean parseXml(Element paramElement) throws Exception {
        int i;
        int j;
        do
            try {
                NodeList localNodeList = paramElement
                        .getElementsByTagName("result");
                i = 0;
                j = localNodeList.getLength();
                // continue;
                this.tips.add(localNodeList.item(i).getFirstChild()
                        .getNodeValue());
                i++;
            } catch (Exception localException) {
                LogUtils.e("test", "", localException);
                break;
            }
        while (i < j);
        return this;
    }
}
