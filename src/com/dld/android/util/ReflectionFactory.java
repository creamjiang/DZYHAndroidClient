package com.dld.android.util;

import com.dld.coupon.util.XmlUtil;
import com.dld.protocol.json.BankCouponDetail;
import com.dld.protocol.json.GroupDetail;
import com.dld.protocol.json.Shop;
import com.dld.protocol.json.Ticket;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ReflectionFactory {
    public static <T> void attach(T paramT, Node paramNode, Class<?> paramClass)
            throws Exception {
        Element localElement = (Element) paramNode;
        Field[] arrayOfField = paramClass.getFields();
        int i = arrayOfField.length;
        int j = 0;
        while (true) {
            if (j >= i)
                return;
            Field localField = arrayOfField[j];
            try {
                String str2 = localField.getType().getName();
                String str1 = XmlUtil.getString(localElement,
                        localField.getName());
                if (!str1.equals(""))
                    if (str2.equals(String.class.getName()))
                        localField.set(paramT, str1);
                    else if (str2.equals("int"))
                        localField.set(paramT,
                                Integer.valueOf(Integer.parseInt(str1)));
                    else if (str2.equals("float"))
                        localField.set(paramT,
                                Float.valueOf(Float.parseFloat(str1)));
                    else if (str2.equals("double"))
                        localField.set(paramT,
                                Double.valueOf(Double.parseDouble(str1)));
                    else if (str2.equals("boolean"))
                        localField.set(paramT,
                                Boolean.valueOf(Boolean.parseBoolean(str1)));
                    else if (str2.equals("long"))
                        localField.set(paramT,
                                Long.valueOf(Long.parseLong(str1)));
                label224: j++;
            } catch (Exception localException) {
                // break label224;
                j++;
            }
        }
    }

    public static <T> T create(JSONObject paramJSONObject, Class<?> paramClass)
    // throws Exception
    {
        Object localObject1 = null;
        try {
            localObject1 = paramClass.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Field[] arrayOfField = paramClass.getFields();
        int j = arrayOfField.length;
        return (T) localObject1;
        // for (int i = 0; ; i++)
        // {
        // if (i >= j)
        // return (T) localObject1;
        // Object localObject2 = arrayOfField[i];
        // JSONArray localJSONArray4;
        // JSONArray localJSONArray2;
        // do
        // try
        // {
        // String str = ((Field)localObject2).getName();
        // int k;
        // int n;
        // if (str.equals("shops"))
        // {
        // localObject2 = (List)((Field)localObject2).get(localObject1);
        // JSONArray localJSONArray3 = paramJSONObject.getJSONArray(str);
        // k = 0;
        // n = localJSONArray3.length();
        // continue;
        // ((List)localObject2).add((Shop)create(localJSONArray3.getJSONObject(k),
        // Shop.class));
        // k++;
        // }
        // else
        // {
        // JSONArray localJSONArray1 = null;
        // int m;
        // // if (k.equals("tickets"))
        // {
        // localObject2 = (List)((Field)localObject2).get(localObject1);
        // // localJSONArray1 = paramJSONObject.getJSONArray(k);
        // n = 0;
        // m = localJSONArray1.length();
        // while (n < m)
        // {
        // ((List)localObject2).add((Ticket)create(localJSONArray1.getJSONObject(n),
        // Ticket.class));
        // n++;
        // }
        // }
        // if (localJSONArray1.equals("groups"))
        // {
        // localObject2 = (List)((Field)localObject2).get(localObject1);
        // // localJSONArray1 = paramJSONObject.getJSONArray(localJSONArray1);
        // n = 0;
        // m = localJSONArray1.length();
        // while (n < m)
        // {
        // ((List)localObject2).add((GroupDetail)create(localJSONArray1.getJSONObject(n),
        // GroupDetail.class));
        // n++;
        // }
        // }
        // if (localJSONArray1.equals("banks"))
        // {
        // localObject2 = (List)((Field)localObject2).get(localObject1);
        // // localJSONArray4 = paramJSONObject.getJSONArray(localJSONArray1);
        // m = 0;
        // // localJSONArray2 = localJSONArray4.length();
        // // while (m < localJSONArray2)
        // {
        // ((List)localObject2).add((BankCouponDetail)create(localJSONArray4.getJSONObject(m),
        // BankCouponDetail.class));
        // m++;
        // }
        // }
        // // ((Field)localObject2).set(localObject1,
        // paramJSONObject.get(localJSONArray2));
        // }
        // }
        // catch (Exception localException)
        // {
        // }
        // while (localJSONArray2.length() < localJSONArray4.length());
        // }
    }

    public static <T> T create(Node paramNode, Class<?> paramClass)
            throws Exception {
        Object localObject = paramClass.newInstance();
        attach(localObject, paramNode, paramClass);
        return (T) localObject;
    }

    public static JSONObject toJSON(Object paramObject) {
        JSONObject localJSONObject = new JSONObject();
        Field[] arrayOfField = paramObject.getClass().getFields();
        int j = arrayOfField.length;
        int i = 0;
        while (true) {
            if (i >= j)
                return localJSONObject;
            Object localObject = arrayOfField[i];
            try {
                String str = ((Field) localObject).getName();
                JSONArray localJSONArray;
                if (str.equals("shops")) {
                    localJSONArray = new JSONArray();
                    localObject = ((List) ((Field) localObject)
                            .get(paramObject)).iterator();
                    while (true) {
                        if (!((Iterator) localObject).hasNext()) {
                            localJSONObject.put(str, localJSONArray);
                            break;
                        }
                        localJSONArray
                                .put(toJSON((Shop) ((Iterator) localObject)
                                        .next()));
                    }
                }
                if (str.equals("tickets")) {
                    localJSONArray = new JSONArray();
                    localObject = ((List) ((Field) localObject)
                            .get(paramObject)).iterator();
                    while (true) {
                        if (!((Iterator) localObject).hasNext()) {
                            localJSONObject.put(str, localJSONArray);
                            break;
                        }
                        localJSONArray
                                .put(toJSON((Ticket) ((Iterator) localObject)
                                        .next()));
                    }
                }
                if (str.equals("groups")) {
                    localJSONArray = new JSONArray();
                    localObject = ((List) ((Field) localObject)
                            .get(paramObject)).iterator();
                    while (true) {
                        if (!((Iterator) localObject).hasNext()) {
                            localJSONObject.put(str, localJSONArray);
                            break;
                        }
                        localJSONArray
                                .put(toJSON((GroupDetail) ((Iterator) localObject)
                                        .next()));
                    }
                }
                if (str.equals("banks")) {
                    localJSONArray = new JSONArray();
                    localObject = ((List) ((Field) localObject)
                            .get(paramObject)).iterator();
                    while (true) {
                        if (!((Iterator) localObject).hasNext()) {
                            localJSONObject.put(str, localJSONArray);
                            break;
                        }
                        localJSONArray
                                .put(toJSON((BankCouponDetail) ((Iterator) localObject)
                                        .next()));
                    }
                }
                localJSONObject
                        .put(str, ((Field) localObject).get(paramObject));
                label364: i++;
            } catch (Exception localException) {
                // break label364;
                i++;
            }
        }
    }
}
