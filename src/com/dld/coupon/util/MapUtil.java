package com.dld.coupon.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.activity.CityActivity;
import com.dld.coupon.db.GenericDAO;
import com.dld.coupon.view.DialogHelper;
import com.dld.protocol.CommonProtocolHelper;
import com.dld.protocol.json.AddressResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class MapUtil {
    private static final String KEY = "4EE0F29E7EB1FC29EAC2B755CE57511E9535CF98";
    private static String address;
    public static boolean cityCanceled = false;
    private static String cityId;
    private static String cityName;
    public static String cityOfRange = new String();
    static AlertDialog d;
    private static LatAndLon location;
    private static LocationListener locationListener;
    public static BMapManager manager;
    public static String range;

    public static void destory() {
        manager.destroy();
    }

    public static String getCity() {
        String str = SharePersistent.get("city_name");
        if (StringUtils.isEmpty(str))
            str = "北京";
        return str;
    }

    public static String getCityId() {
        String str = SharePersistent.get("city_id");
        if (StringUtils.isEmpty(str))
            str = "1000";
        return str;
    }

    public static String getGpsCity() {
        String str;
        if (StringUtils.isEmpty(cityName)) {
            cityName = SharePersistent.get("gps_city");
            if (StringUtils.isEmpty(cityName))
                str = getCity();
            else
                str = cityName;
        } else {
            str = cityName;
        }
        return str;
    }

    public static String getGpsCityId() {
        String str;
        if (StringUtils.isEmpty(cityId)) {
            cityId = SharePersistent.get("gps_city_id");
            if (StringUtils.isEmpty(cityId))
                str = getCityId();
            else
                str = cityId;
        } else {
            str = cityId;
        }
        return str;
    }

    public static boolean isInited() {
        boolean i;
        if (location == null)
            i = false;
        else
            i = true;
        return i;
    }

    public static void openMap(double paramDouble1, double paramDouble2,
            String paramString) {
        if ((paramDouble1 <= 0.0D) || (paramDouble2 <= 0.0D))
            return;
        while (true) {
            try {
                DialogHelper.showInvalidLatLon();
                // break label190;
                Object localObject = paramString.replace("(", "（")
                        .replace(")", "）").replace("\"", " ");
                localObject = "(\"" + Uri.encode((String) localObject, "utf-8")
                        + "\")";
                localObject = new Intent("android.intent.action.VIEW",
                        Uri.parse(new StringBuilder("geo:")
                                .append(paramDouble1).append(",")
                                .append(paramDouble2).append("?q=")
                                .append(paramDouble1).append(",")
                                .append(paramDouble2).toString()
                                + (String) localObject));
                ((Intent) localObject).setFlags(0);
                ((Intent) localObject).setClassName(
                        "com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                ActivityManager.getCurrent()
                        .startActivity((Intent) localObject);
            } catch (Exception localException) {
                LogUtils.e("test", "", localException);
            }
            // label190: ;
            if (paramDouble1 <= paramDouble2)
                continue;
            double d1 = paramDouble1;
            paramDouble1 = paramDouble2;
            paramDouble2 = d1;
        }
    }

    public static void regist(LocationObserver paramLocationObserver,
            Handler paramHandler, Activity paramActivity) {
        final LocationObserver observer = paramLocationObserver;
        final Handler handler = paramHandler;
        final Activity activity = paramActivity;
        new Thread() {
            public void run() {
                Looper.prepare();
                try {
                    if (MapUtil.manager == null) {
                        if (MapUtil.manager == null) {
                            MapUtil.manager = new BMapManager(
                                    ActivityManager.getCurrent());
//                            MapUtil.manager.init(
//                                    "4EE0F29E7EB1FC29EAC2B755CE57511E9535CF98",
//                                    new MapUtil.MyGeneralListener());
                        }
                        MapUtil.manager.start();
                        LocationListener local1 = new LocationListener() {
                            public void onLocationChanged(Location paramLocation) {
                                if (paramLocation == null)
                                    return;
                                try {
                                    observer.onFailed();
                                    if (MapUtil.location == null)
                                        MapUtil.location = new MapUtil.LatAndLon();
                                    MapUtil.location.lat = paramLocation
                                            .getLatitude();
                                    MapUtil.location.lon = paramLocation
                                            .getLongitude();
                                    observer.onLocationChanged(MapUtil.location);
                                    MapUtil.requestRange();
                                    MapUtil.reverseGeoCode(handler, activity);
                                } catch (Exception localException) {
                                    LogUtils.e("test", localException);
                                }
                            }
                        };
                        MapUtil.manager.getLocationManager()
                                .requestLocationUpdates(local1);
                        Looper.loop();
                    }
                } catch (Exception localException) {
                    while (true)
                        LogUtils.e("test", localException);
                }
            }
        }.start();
    }

    public static void requestRange() {
        try {
            Protocol localProtocol = CommonProtocolHelper.searchAddress();
            localProtocol.startTrans(new Protocol.OnJsonProtocolResult(
                    AddressResponse.class) {
                public void onException(String paramString,
                        Exception paramException) {
                    LogUtils.e("test", "", paramException);
                    MapUtil.range = null;
                    MapUtil.cityOfRange = new String();
                }

                public void onResult(String paramString, Object paramObject) {
                    AddressResponse localAddressResponse = null;
                    try {
                        if (!paramString.equals(paramString))
                            return;
                        localAddressResponse = (AddressResponse) paramObject;
                        if (localAddressResponse.code != 0) {
                            MapUtil.range = null;
                            MapUtil.cityOfRange = new String();
                        }
                    } catch (Exception localException) {
                        onException(paramString, localException);
                    }
                    if (localAddressResponse.isDistrict)
                        ;
                    for (String str = "全" + localAddressResponse.name;; str = localAddressResponse.name) {
                        MapUtil.range = str;
                        MapUtil.cityOfRange = localAddressResponse.city;
                        break;
                    }
                }
            });
            return;
        } catch (Exception localException) {
            while (true)
                LogUtils.e("error", "", localException);
        }
    }

    public static void reverseGeoCode(Handler paramHandler,
            Activity paramActivity) {
        try {
            String str2 = EntityUtils
                    .toString(HttpClientFactory
                            .create()
                            .execute(
                                    new HttpGet(
                                            "http://ditu.google.cn/maps/geo?output=csv&q="
                                                    + location.lat + ","
                                                    + location.lon))
                            .getEntity());
            if (!StringUtils.isEmpty(str2)) {
                int i = str2.indexOf("省");
                if (i < 0)
                    i = str2.indexOf("国");
                int j = str2.indexOf(" ");
                if (j < 0)
                    j = -1 + str2.length();
                String str1 = str2.substring(i + 1, j);
                address = str1;
                cityName = str1.substring(0, str1.indexOf("市"));
                LogUtils.log("test", cityName);
                cityId = GenericDAO.getInstance(ActivityManager.getCurrent())
                        .getCityIdByName(cityName);
                updateAddress(paramActivity, paramHandler);
                Cache.put("gps_address", str1);
                Cache.put("gps_city", cityName);
                Cache.put("gps_city_id", cityId);
                SharePersistent.set("gps_city", cityName);
                SharePersistent.set("gps_city_id", cityId);
                if ((!cityCanceled)
                        && (!StringUtils.equals(cityName,
                                SharePersistent.get("city_name"))))
                    paramHandler.post(new Runnable() {
                        public void run() {
                            MapUtil.showCityChanged(MapUtil.cityName);
                        }
                    });
            }
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    public static void showCityChanged(String paramString) {
        if (d == null) {
            d = new AlertDialog.Builder(ActivityManager.getCurrent())
                    .setTitle("切换城市")
                    .setMessage("GPS定位您当前在" + paramString + "，需要切换吗？")
                    .setPositiveButton("切换",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    SharePersistent.set("city_id",
                                            MapUtil.cityId);
                                    SharePersistent.set("city_name",
                                            MapUtil.cityName);
                                    CityActivity.notifyChange(MapUtil.cityName,
                                            MapUtil.cityId);
                                    MapUtil.d = null;
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface paramDialogInterface,
                                        int paramInt) {
                                    MapUtil.cityCanceled = true;
                                    MapUtil.d = null;
                                }
                            }).create();
            d.setCanceledOnTouchOutside(true);
            d.show();
        }
    }

    public static void unregist() {
        if (locationListener != null) {
            manager.getLocationManager().removeUpdates(locationListener);
            manager = null;
        }
    }

    public static void updateAddress(Activity paramActivity,
            Handler paramHandler) {
        updateAddress(paramActivity, paramHandler, 0);
    }

    private static void updateAddress(final Activity paramActivity,
            final Handler paramHandler, final int paramInt) {
        try {
            TextView localTextView = (TextView) paramActivity
                    .findViewById(R.id.location);
            if (localTextView == null) {
                if (paramInt < 2)
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            MapUtil.updateAddress(paramActivity, paramHandler,
                                    paramInt);
                        }
                    }, 3000L);
            } else
                paramHandler.post(new Runnable() {
                    public void run() {
                        if (!StringUtils.isEmpty(MapUtil.address))
                            MapUtil.range = "我的位置：" + MapUtil.address;
                        else
                            MapUtil.range = "十分抱歉，未能确定您所在的位置。";
                    }
                });
        } catch (Exception localException) {
            LogUtils.e("test", "", localException);
        }
    }

    public static class LatAndLon {
        public double lat;
        public double lon;

        public double getLatitude() {
            return this.lat;
        }

        public double getLongitude() {
            return this.lon;
        }
    }

    public static abstract interface LocationObserver {
        public abstract void onFailed();

        public abstract void onLocationChanged(MapUtil.LatAndLon paramLatAndLon);
    }

    static class MyGeneralListener implements MKGeneralListener {
        public void onGetNetworkState(int paramInt) {
        }

        public void onGetPermissionState(int paramInt) {
        }
    }
}
