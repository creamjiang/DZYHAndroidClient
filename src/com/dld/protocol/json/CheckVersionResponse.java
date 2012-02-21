package com.dld.protocol.json;

import com.dld.android.util.ReflectionFactory;

import org.w3c.dom.Element;

public class CheckVersionResponse extends JsonBean {
    public String desc;
    public String title;
    public String url;
    public int ver;

    public JsonBean parseXml(Element paramElement) throws Exception {
        ReflectionFactory
                .attach(this, paramElement, CheckVersionResponse.class);
        return this;
    }
}
