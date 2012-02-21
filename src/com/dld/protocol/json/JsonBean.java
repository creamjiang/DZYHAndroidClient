package com.dld.protocol.json;

import java.util.List;
import org.json.JSONObject;
import org.w3c.dom.Element;

public abstract class JsonBean {
    public int getDBId() {
        return 0;
    }

    public List getDetails() {
        return null;
    }

    public int getTotal() {
        return 0;
    }

    public JsonBean parse(JSONObject paramJSONObject) throws Exception {
        return this;
    }

    public JsonBean parseXml(Element paramElement) throws Exception {
        return this;
    }

    public void setDetails(List paramList) {
    }

    public void setTotal(int paramInt) {
    }
}
