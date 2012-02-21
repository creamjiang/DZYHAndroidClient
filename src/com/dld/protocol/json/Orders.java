package com.dld.protocol.json;

import com.dld.android.util.LogUtils;
import com.dld.android.util.ReflectionFactory;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Orders extends JsonBean {
    public List<OrdersItem> list = new ArrayList();
    public int total;

    public JsonBean parseXml(Element paramElement) throws Exception {
        int i;
        int j;
        do
            try {
                Object localObject = (Element) paramElement
                        .getElementsByTagName("orders").item(0);
                this.total = Integer.parseInt(((Element) localObject)
                        .getAttribute("total"));
                localObject = ((Element) localObject)
                        .getElementsByTagName("order");
                i = 0;
                j = ((NodeList) localObject).getLength();
                // continue;
                OrdersItem localOrdersItem = (OrdersItem) ReflectionFactory
                        .create(((NodeList) localObject).item(i),
                                OrdersItem.class);
                this.list.add(localOrdersItem);
                i++;
            } catch (Exception localException) {
                LogUtils.e("test", "", localException);
                break;
            }
        while (i < j);
        return (JsonBean) this;
    }
}
