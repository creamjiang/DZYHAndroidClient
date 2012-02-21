package com.dld.protocol.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupDetail implements Detail, Serializable {
    private static final long serialVersionUID = 1L;
    public String address = "";
    public int bought;
    public int category;
    public String city = "";
    public String cpsUrl = "";
    public int dbId;
    public String dealUrl = "";
    public int delivery;
    public int distance;
    public long endTime;
    public float freight;
    public int freightFree;
    public int id;
    public String image = "";
    public int maxPerOrder;
    public int minPerOrder;
    public double price;
    public String range = "";
    public double rebate;
    public List<Shop> shops = new ArrayList();
    public long startTime;
    public String tip = "";
    public String title = "";
    public double value;
    public String website = "";

    public boolean equals(Object paramObject) {
        boolean bool = false;
        try {
            bool = ((GroupDetail) paramObject).dealUrl.equals(this.dealUrl);
            // bool = bool;
            return bool;
        } catch (Exception localException) {
            // while (true)
            bool = false;
        }
        return bool;
    }

    public int getDBId() {
        return this.dbId;
    }

    public String getId() {
        String str;
        if (this.id > 0)
            str = String.valueOf(this.id);
        else
            str = this.dealUrl;
        return str;
    }

    public void setDBId(int paramInt) {
        this.dbId = paramInt;
    }

    public String toString() {
        return "GroupDetail [address=" + this.address + ", bought="
                + this.bought + ", category=" + this.category + ", city="
                + this.city + ", dealUrl=" + this.dealUrl + ", delivery="
                + this.delivery + ", distance=" + this.distance + ", endTime="
                + this.endTime + ", freight=" + this.freight + ", freightFree="
                + this.freightFree + ", id=" + this.id + ", image="
                + this.image + ", maxPerOrder=" + this.maxPerOrder
                + ", minPerOrder=" + this.minPerOrder + ", price=" + this.price
                + ", range=" + this.range + ", rebate=" + this.rebate
                + ", shops=" + this.shops + ", startTime=" + this.startTime
                + ", tip=" + this.tip + ", title=" + this.title + ", value="
                + this.value + ", website=" + this.website + "]";
    }
}
