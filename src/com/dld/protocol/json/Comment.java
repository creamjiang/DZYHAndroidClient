package com.dld.protocol.json;

public class Comment {
    public String content;
    public String date;
    public String id;
    public String name;
    public int starNum;

    public boolean equals(Object paramObject) {
        boolean bool = false;
        try {
            bool = ((Comment) paramObject).id.equals(this.id);
            // bool = bool;
        } catch (Exception localException) {
            // while (true)
            bool = false;
        }
        return bool;
    }
}
