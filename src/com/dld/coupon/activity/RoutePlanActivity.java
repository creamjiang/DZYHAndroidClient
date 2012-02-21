package com.dld.coupon.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoutePlan;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.MapUtil;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.List;

public class RoutePlanActivity extends MapActivity {
    MKPlanNode endNode;
    Button mBtnDrive = null;
    Button mBtnTransit = null;
    Button mBtnWalk = null;
    MapView mMapView = null;
    MKSearch mSearch = null;
    MKPlanNode startNode;

    private void hasStartPosition() {
        View.OnClickListener local2 = new View.OnClickListener() {
            public void onClick(View paramView) {
                RoutePlanActivity.this.searchButtonProcess(paramView);
            }
        };
        this.mBtnDrive.setOnClickListener(local2);
        this.mBtnTransit.setOnClickListener(local2);
        this.mBtnWalk.setOnClickListener(local2);
        searchButtonProcess(this.mBtnWalk);
    }

    private void noStartPostion() {
        if ((this.endNode.pt != null) && (this.endNode.pt.getLatitudeE6() > 0)
                && (this.endNode.pt.getLongitudeE6() > 0)) {
            showPosition();
        } else {
            String str = MapUtil.getGpsCity();
            this.mSearch.geocode(str, this.endNode.name);
        }
    }

    private void showPosition() {
        this.mBtnDrive.setVisibility(8);
        this.mBtnTransit.setVisibility(8);
        this.mBtnWalk.setVisibility(8);
        this.mMapView.getController().animateTo(this.endNode.pt);
        Drawable localDrawable = getResources().getDrawable(
                R.drawable.iconmarka);
        localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(),
                localDrawable.getIntrinsicHeight());
        this.mMapView.getOverlays().clear();
        this.mMapView.getOverlays().add(
                new OverItemT(localDrawable, this, this.endNode.pt,
                        this.endNode.name));
    }

    protected boolean isRouteDisplayed() {
        return false;
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.routeplan);
        this.startNode = ((MKPlanNode) Cache.remove("start_node"));
        this.endNode = ((MKPlanNode) Cache.remove("end_node"));
        super.initMapActivity(MapUtil.manager);
        this.mMapView = ((MapView) findViewById(R.id.bmapView));
        this.mMapView.setDrawOverlayWhenZooming(true);
        this.mMapView.getController().setZoom(15);
        this.mSearch = new MKSearch();
        this.mSearch.init(MapUtil.manager, new MKSearchListener() {
            private void handlerError(int paramInt) {
                LogUtils.log("test", "路线寻找失败：" + paramInt + ","
                        + nodeToString(RoutePlanActivity.this.startNode) + ","
                        + nodeToString(RoutePlanActivity.this.endNode));
                Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", 0).show();
            }

            private String nodeToString(MKPlanNode paramMKPlanNode) {
                Object localObject;
                if (paramMKPlanNode != null) {
                    localObject = new StringBuilder();
                    if (paramMKPlanNode.pt != null)
                        ((StringBuilder) localObject)
                                .append(paramMKPlanNode.pt.getLatitudeE6())
                                .append(",")
                                .append(paramMKPlanNode.pt.getLongitudeE6())
                                .append(",");
                    ((StringBuilder) localObject).append(paramMKPlanNode.name);
                    localObject = ((StringBuilder) localObject).toString();
                } else {
                    localObject = "null";
                }
                return (String) localObject;
            }

            public void onGetAddrResult(MKAddrInfo paramMKAddrInfo, int paramInt) {
            }

            public void onGetDrivingRouteResult(
                    MKDrivingRouteResult paramMKDrivingRouteResult, int paramInt) {
                if ((paramInt == 0) && (paramMKDrivingRouteResult != null)) {
                    RouteOverlay localRouteOverlay = new RouteOverlay(
                            RoutePlanActivity.this,
                            RoutePlanActivity.this.mMapView);
                    localRouteOverlay.setData(paramMKDrivingRouteResult
                            .getPlan(0).getRoute(0));
                    RoutePlanActivity.this.mMapView.getOverlays().clear();
                    RoutePlanActivity.this.mMapView.getOverlays().add(
                            localRouteOverlay);
                    RoutePlanActivity.this.mMapView.invalidate();
                    RoutePlanActivity.this.mMapView.getController().animateTo(
                            paramMKDrivingRouteResult.getEnd().pt);
                } else {
                    handlerError(paramInt);
                }
            }

            public void onGetPoiResult(MKPoiResult paramMKPoiResult,
                    int paramInt1, int paramInt2) {
            }

            public void onGetTransitRouteResult(
                    MKTransitRouteResult paramMKTransitRouteResult, int paramInt) {
                if ((paramInt == 0) && (paramMKTransitRouteResult != null)) {
                    TransitOverlay localTransitOverlay = new TransitOverlay(
                            RoutePlanActivity.this,
                            RoutePlanActivity.this.mMapView);
                    localTransitOverlay.setData(paramMKTransitRouteResult
                            .getPlan(0));
                    RoutePlanActivity.this.mMapView.getOverlays().clear();
                    RoutePlanActivity.this.mMapView.getOverlays().add(
                            localTransitOverlay);
                    RoutePlanActivity.this.mMapView.invalidate();
                    RoutePlanActivity.this.mMapView.getController().animateTo(
                            paramMKTransitRouteResult.getEnd().pt);
                } else {
                    handlerError(paramInt);
                }
            }

            public void onGetWalkingRouteResult(
                    MKWalkingRouteResult paramMKWalkingRouteResult, int paramInt) {
                if ((paramInt == 0) && (paramMKWalkingRouteResult != null)) {
                    RouteOverlay localRouteOverlay = new RouteOverlay(
                            RoutePlanActivity.this,
                            RoutePlanActivity.this.mMapView);
                    localRouteOverlay.setData(paramMKWalkingRouteResult
                            .getPlan(0).getRoute(0));
                    RoutePlanActivity.this.mMapView.getOverlays().clear();
                    RoutePlanActivity.this.mMapView.getOverlays().add(
                            localRouteOverlay);
                    RoutePlanActivity.this.mMapView.invalidate();
                    RoutePlanActivity.this.mMapView.getController().animateTo(
                            paramMKWalkingRouteResult.getEnd().pt);
                } else {
                    handlerError(paramInt);
                }
            }
        });
        this.mBtnDrive = ((Button) findViewById(R.id.drive));
        this.mBtnTransit = ((Button) findViewById(R.id.transit));
        this.mBtnWalk = ((Button) findViewById(R.id.walk));
        if (this.startNode != null)
            hasStartPosition();
        else
            noStartPostion();
    }

    void searchButtonProcess(View paramView) {
        if (!this.mBtnDrive.equals(paramView)) {
            if (!this.mBtnTransit.equals(paramView)) {
                if (this.mBtnWalk.equals(paramView))
                    this.mSearch.walkingSearch("北京", this.startNode, "北京",
                            this.endNode);
            } else
                this.mSearch.transitSearch("北京", this.startNode, this.endNode);
        } else
            this.mSearch
                    .drivingSearch("北京", this.startNode, "上海", this.endNode);
    }

    class OverItemT extends ItemizedOverlay<OverlayItem> {
        private List<OverlayItem> mGeoList = new ArrayList();

        public OverItemT(Drawable paramContext, Context paramGeoPoint,
                GeoPoint paramString, String arg5) {
            super(paramContext);
            String str;
            this.mGeoList.add(new OverlayItem(paramString, arg5, null));
            populate();
        }

        protected OverlayItem createItem(int paramInt) {
            return (OverlayItem) this.mGeoList.get(paramInt);
        }

        public boolean onSnapToItem(int paramInt1, int paramInt2,
                Point paramPoint, MapView paramMapView) {
            return false;
        }

        public int size() {
            return this.mGeoList.size();
        }
    }
}
