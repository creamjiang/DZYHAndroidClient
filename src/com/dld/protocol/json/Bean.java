package com.dld.protocol.json;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

public class Bean extends JsonBean {
    private List<DetailRef> details = new ArrayList();
    private int total;

    public List getDetails() {
        return this.details;
    }

    public int getTotal() {
        return this.total;
    }

    public JsonBean parseXml(Element paramElement) throws Exception {
        return this;
    }

    public void setDetails(List paramList) {
        this.details = paramList;
    }

    public void setTotal(int paramInt) {
        this.total = paramInt;
    }
}
