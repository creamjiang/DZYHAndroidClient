package com.dld.protocol.json;

import com.dld.android.util.ReflectionFactory;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RegisteredReponse extends CommonResponse {
    public List<RegisteredContact> contacts = new ArrayList();

    public JsonBean parseXml(Element paramElement) throws Exception {
        super.parseXml(paramElement);
        NodeList localNodeList = paramElement.getElementsByTagName("contact");
        int i = 0;
        int j = localNodeList.getLength();
        while (i < j) {
            this.contacts.add((RegisteredContact) ReflectionFactory.create(
                    localNodeList.item(i), RegisteredContact.class));
            i++;
        }
        return this;
    }
}
