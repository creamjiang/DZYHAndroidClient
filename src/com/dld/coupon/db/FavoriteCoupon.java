package com.dld.coupon.db;

public class FavoriteCoupon {
    public static final String ADDRESS = "address";
    public static final String BIZCODE = "bizcode";
    public static final String[] COLOUMNS;
    public static final String DETAIL = "detail";
    public static final String DISCOUNT = "wl_discount";
    public static final String IMAGE_PATH = "image_path";
    public static final String NAME = "name";
    public static final String SAVE_DATE = "save_date";
    public static final String TABLE_NAME = "FavoriteCoupon";
    public static final String TEL_NO = "telno";
    public static final String TRADE_NAME = "trade_name";
    public static final String VALIDATE_END = "validate_end";
    public static final String VALIDATE_START = "validate_start";
    public static final String X = "x";
    public static final String Y = "y";
    public String address;
    public String bizcode;
    public String detail;
    public String imagePath;
    public String name;
    public String save_date;
    public String telno;
    public String trade_name;
    public String validate_end;
    public String validate_start;
    public String wl_discount;
    public String x;
    public String y;

    static {
        String[] arrayOfString = new String[13];
        arrayOfString[0] = "name";
        arrayOfString[1] = "save_date";
        arrayOfString[2] = "validate_start";
        arrayOfString[3] = "validate_end";
        arrayOfString[4] = "detail";
        arrayOfString[5] = "address";
        arrayOfString[6] = "trade_name";
        arrayOfString[7] = "bizcode";
        arrayOfString[8] = "wl_discount";
        arrayOfString[9] = "telno";
        arrayOfString[10] = "x";
        arrayOfString[11] = "y";
        arrayOfString[12] = "image_path";
        COLOUMNS = arrayOfString;
    }
}
