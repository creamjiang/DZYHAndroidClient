package com.dld.protocol.json;

import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    public String date;
    public float discount;
    public float freight;
    public Integer id;
    public int oid;
    public float price;
    public int quantity;
    public float total;

    public String toString() {
        return "Order [date=" + this.date + ", discount=" + this.discount
                + ", freight=" + this.freight + ", id=" + this.id + ", price="
                + this.price + ", quantity=" + this.quantity + ", total="
                + this.total + "]";
    }
}
