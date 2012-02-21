package com.dld.android.net;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Param implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, String> map = new HashMap();

    public Param append(Param paramParam) {
        Iterator localIterator = paramParam.toMap().entrySet().iterator();
        while (localIterator.hasNext()) {
            Object localObject = (Map.Entry) localIterator.next();
            String str = (String) ((Map.Entry) localObject).getKey();
            localObject = (String) ((Map.Entry) localObject).getValue();
            this.map.put(str, (String) localObject);
        }
        return (Param) this;
    }

    public Param append(String paramString1, String paramString2) {
        this.map.put(paramString1, paramString2);
        return this;
    }

    public String get(String paramString) {
        return (String) this.map.get(paramString);
    }

    public HashMap<String, String> toMap() {
        return this.map;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder();
        Iterator localIterator = this.map.entrySet().iterator();
        while (true) {
            if (!localIterator.hasNext()) {
                localStringBuilder.append("&ver=4&cv=A22");
                return localStringBuilder.toString();
            }
            Object localObject = (Map.Entry) localIterator.next();
            String str = (String) ((Map.Entry) localObject).getKey();
            localObject = (String) ((Map.Entry) localObject).getValue();
            if ((localObject == null) || (((String) localObject).equals("")))
                continue;
            try {
                localStringBuilder.append(str + "="
                        + URLEncoder.encode((String) localObject, "UTF-8")
                        + "&");
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
                localUnsupportedEncodingException.printStackTrace();
            }
        }
    }

    public String toStringfrome() {
        StringBuilder localStringBuilder = new StringBuilder();
        Iterator localIterator = this.map.entrySet().iterator();
        while (true) {
            if (!localIterator.hasNext())
                return localStringBuilder.toString();
            Object localObject = (Map.Entry) localIterator.next();
            String str = (String) ((Map.Entry) localObject).getKey();
            localObject = (String) ((Map.Entry) localObject).getValue();
            if ((localObject == null) || (((String) localObject).equals("")))
                continue;
            try {
                localStringBuilder.append(str + "=" + (String) localObject
                        + "&");
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
    }
}
