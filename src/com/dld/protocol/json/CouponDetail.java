package com.dld.protocol.json;

import com.dld.android.util.LogUtils;
import com.dld.android.util.ReflectionFactory;
import com.dld.coupon.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CouponDetail extends JsonBean implements Detail {
    public String address;
    public String bank;
    public List<BankCouponDetail> banks = new ArrayList();
    public String bizcode;
    public String branch_name;
    public String bus_route;
    public String business_hours;
    public String charge_type;
    public String cityName;
    public int cost;
    public int dbId;
    public int distance = -1;
    public String district;
    public String district_name;
    public String feature;
    public List<GroupDetail> groups = new ArrayList();
    public String id;
    public String introduction;
    public String landmark;
    public int likes;
    public String name = "";
    public int parking;
    public ArrayList<Phone> phones = new ArrayList();
    public double poix;
    public double poiy;
    public int private_room;
    public String province_name;
    public String recommendation;
    public int reservation;
    public int seqnum;
    public String service_mask;
    public int shop_id;
    public String short_name;
    public String special_offer;
    public String style;
    public String telno;
    public String ticket_price;
    public List<Ticket> tickets = new ArrayList();
    public String trade_name;
    public String wl_discount;
    public double x;
    public double y;

    public boolean equals(Object paramObject) {
        boolean bool = false;
        try {
            bool = ((CouponDetail) paramObject).id.equals(this.id);
            bool = bool;
            return bool;
        } catch (Exception i) {
            // while (true)
            {
                LogUtils.e("test", "", i);
                // int i = 0;
            }
            return bool;
        }
    }

    public String getBank() {
        String str;
        if (!StringUtils.isEmpty(this.bank)) {
            if (!this.bank.equals("CMB"))
                str = "其他";
            else
                str = "招商银行";
        } else
            str = "其他";
        return str;
    }

    public int getDBId() {
        return this.dbId;
    }

    public String getId() {
        return this.id;
    }

    public String getTels() {
        Object localObject;
        if (!StringUtils.isEmpty(this.telno)) {
            String[] arrayOfString = this.telno.split(" ");
            if ((arrayOfString == null) || (arrayOfString.length <= 0)) {
                localObject = this.telno;
            } else {
                localObject = new StringBuilder();
                for (int i = 0; i < arrayOfString.length; i++) {
                    ((StringBuilder) localObject).append(arrayOfString[i]);
                    if (i >= -1 + arrayOfString.length)
                        continue;
                    ((StringBuilder) localObject).append(";");
                }
                localObject = ((StringBuilder) localObject).toString();
            }
        } else {
            localObject = this.telno;
        }
        return (String) localObject;
    }

    public boolean isOnlyShop() {
        return false;
    }

    public JsonBean parseXml(Element paramElement) throws Exception {
        Element localElement = (Element) paramElement.getElementsByTagName(
                "result").item(0);
        ReflectionFactory.attach(this, localElement, CouponDetail.class);
        NodeList localNodeList1 = localElement.getElementsByTagName("coupon");
        int m = localNodeList1.getLength();
        for (int k = 0; k < m; k++) {
            Ticket localTicket = (Ticket) ReflectionFactory.create(
                    localNodeList1.item(k), Ticket.class);
            localTicket.branch_id = this.id;
            localTicket.address = this.address;
            localTicket.telno = this.telno;
            localTicket.x = this.x;
            localTicket.y = this.y;
            localTicket.branch_name = this.name;
            this.tickets.add(localTicket);
        }
        NodeList localNodeList2 = localElement.getElementsByTagName("group");
        m = localNodeList2.getLength();
        for (int j = 0; j < m; j++)
            this.groups.add((GroupDetail) ReflectionFactory.create(
                    localNodeList2.item(j), GroupDetail.class));
        localNodeList2 = localElement.getElementsByTagName("bankCoupon");
        int i = localNodeList2.getLength();
        for (int j = 0; j < i; j++)
            this.banks.add((BankCouponDetail) ReflectionFactory.create(
                    localNodeList2.item(j), BankCouponDetail.class));
        return this;
    }

    public void setDBId(int paramInt) {
        this.dbId = paramInt;
    }

    public static class Phone {
        public String forDial;
        public String forShow;

        public Phone(String paramString1, String paramString2) {
            this.forShow = paramString1;
            this.forDial = paramString2;
        }
    }
}
