package com.dld.protocol.json;

import org.json.JSONObject;

public class CheckClinetVer extends JsonBean {
    public String results;

    public JsonBean parse(JSONObject paramJSONObject) throws Exception {
        this.results = paramJSONObject.getString("results");
        return super.parse(paramJSONObject);
    }
}
