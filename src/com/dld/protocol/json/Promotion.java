package com.dld.protocol.json;

public class Promotion extends JsonBean {
    public String content;
    public String id;
    public String image;
    public String type;

    public String toString() {
        return "Promotion [Content=" + this.content + ", id=" + this.id
                + ", image=" + this.image + ", type=" + this.type + "]";
    }
}
