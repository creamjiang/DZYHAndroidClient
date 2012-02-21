package com.dld.protocol.json;

public class Ticket implements Detail {
    public String address;
    public String branch_id;
    public String branch_name = "";
    private int dbid;
    public String description;
    public long end_time;
    public int id;
    public String sms;
    public String source;
    public long start_time;
    public String telno;
    public String title;
    public double x;
    public double y;

    public boolean equals(Object paramObject) {
        boolean i = false;
        if ((paramObject != null) && ((paramObject instanceof Ticket))) {
            Ticket localTicket = (Ticket) paramObject;
            if ((this.id == localTicket.id)
                    && (this.title.equals(localTicket.title))
                    && (this.end_time == localTicket.end_time))
                i = true;
        }
        return i;
    }

    public int getDBId() {
        return this.dbid;
    }

    public String getId() {
        return String.valueOf(this.id);
    }

    public void setDBId(int paramInt) {
        this.dbid = paramInt;
    }
}
