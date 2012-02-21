package com.dld.protocol.json;

import com.dld.android.util.LogUtils;
import com.dld.android.util.ReflectionFactory;
import com.dld.coupon.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AllResult extends JsonBean {
    public List<AllResultDetail> details = new ArrayList();
    public boolean noresult;
    public int total;

    public List getDetails() {
        return this.details;
    }

    public int getTotal() {
        return this.total;
    }

    public JsonBean parseXml(Element paramElement) throws Exception {
        this.total = XmlUtil.getInt(paramElement, "total");
        this.noresult = XmlUtil.getBoolean(paramElement, "noresult");
        NodeList localNodeList = paramElement.getElementsByTagName("result");
        int i = 0;
        int j = localNodeList.getLength();
        while (true) {
            if (i >= j)
                return this;
            try {
                Element localElement = (Element) localNodeList.item(i);
                this.details.add((AllResultDetail) ReflectionFactory.create(
                        localElement, AllResultDetail.class));
                i++;
            } catch (Exception localException) {
                while (true)
                    LogUtils.e("test", "", localException);
            }
        }
    }

    public void setDetails(List paramList) {
        this.details = paramList;
    }

    public void setTotal(int paramInt) {
        this.total = paramInt;
    }
}
