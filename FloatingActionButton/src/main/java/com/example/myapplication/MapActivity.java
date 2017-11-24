package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.myapplication.bean.Info;
import com.example.myapplication.mapapi.overlayutil.WalkingRouteOverlay;
import com.example.myapplication.sensor.MyOrientationListener;
import com.example.myapplication.tool.ActivityCollector;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    Toolbar toolbar;
    MapView mapView;
    BaiduMap baiduMap;
    Context context;
    //定位
    LocationClient locationClient;
    MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    private double mLatitude, mLongitude;
    //自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private MyLocationConfiguration.LocationMode locationMode;
    //自定义marker图标
    private BitmapDescriptor mIconMarker;
    private RelativeLayout mMarkerLy;
    //路线规划
    private RoutePlanSearch mRoutePlan;
    private RouteLine route;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //SDK初始化
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        this.context = this;
        ActivityCollector.addActivity(this);
        initView();
        //初始化定位
        initLocation();
        initMarker();

    }

    private void initMarker() {
        mIconMarker = BitmapDescriptorFactory.fromResource(R.mipmap.hong);
        mMarkerLy = (RelativeLayout) findViewById(R.id.marker_ly);
    }

    private void initLocation() {
        locationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        //初始化图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.jiantou);

        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置actionbar返回，第一个是左上角小图标能否点击，第二是是否有小箭头,第三个隐藏标题
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("地图");
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mapView = (MapView) findViewById(R.id.map);
        //普通地图 ,baiduMap是地图控制器对象
        baiduMap = mapView.getMap();
        //放大倍数
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(13.0f);
        baiduMap.setMapStatus(mapStatusUpdate);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item1://普通地图
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.item2://卫星地图
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.item3://实时交通
                if (baiduMap.isTrafficEnabled()) {
                    baiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通(off)");
                } else {
                    baiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通(on)");
                }
                break;
            case R.id.item4://我的位置
                centerToMyLocation(mLatitude, mLongitude);
                break;
            case R.id.item5://普通模式
                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case R.id.item6://罗盘模式
                locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;
            case R.id.item7://跟随模式
                locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;
            case R.id.item8://添加覆盖物
                addOverlays(Info.infos);
                break;
            case R.id.item9:
                addRoutePlan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //路线规划
    private void addRoutePlan() {
        mRoutePlan = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                //步行
                if(walkingRouteResult==null||walkingRouteResult.error!= SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(getApplicationContext(), "未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (walkingRouteResult.error==SearchResult.ERRORNO.NO_ERROR){
                    route=walkingRouteResult.getRouteLines().get(0);
                    WalkingRouteOverlay overlay=new WalkingRouteOverlay(baiduMap);
                    baiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    Toast.makeText(getApplicationContext(), route.getDistance()+","+route.getDuration()
                            +","+route.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                //乘车路线，包括公交，铁路，飞机，步行等
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                //自驾路线
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                //自行车
            }
        };
        mRoutePlan.setOnGetRoutePlanResultListener(listener);
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("铜仁", "铜仁大峡谷");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("铜仁", "孔圣园");
        mRoutePlan.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));

    }

    private void addOverlays(List<Info> infos) {
        baiduMap.clear();
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        for (Info info : infos) {
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            options = new MarkerOptions().icon(mIconMarker).position(latLng).zIndex(5);
            marker = (Marker) baiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);


        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                Info info = (Info) bundle.getSerializable("info");
                TextView markerName = (TextView) mMarkerLy.findViewById(R.id.marker_name);
                markerName.setText(info.getName());

                InfoWindow infoWindow;
                TextView tv = new TextView(context);
                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                tv.setText(info.getName());
                tv.setPadding(10, 10, 10, 10);

                LatLng latLng = marker.getPosition();
//                Point p=baiduMap.getProjection().toScreenLocation(latLng);
//                p.y-=47;
//                LatLng ll=baiduMap.getProjection().fromScreenLocation(p);
                infoWindow = new InfoWindow(tv, latLng, -47);
                baiduMap.showInfoWindow(infoWindow);
                mMarkerLy.setVisibility(View.VISIBLE);
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMarkerLy.setVisibility(View.GONE);
                baiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /**
     * 定位到我的位置
     *
     * @param mLatitude
     * @param mLongitude
     */
    private void centerToMyLocation(double mLatitude, double mLongitude) {
        LatLng latlng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
        baiduMap.animateMapStatus(msu);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            baiduMap.setMyLocationData(data);
            //设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(locationMode, true, mIconLocation);
            baiduMap.setMyLocationConfiguration(config);

            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            if (isFirstIn) {
                centerToMyLocation(bdLocation.getLatitude(), bdLocation.getLongitude());
                isFirstIn = false;
                Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        baiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted()) {
            locationClient.start();
            //开启方向传感器
            myOrientationListener.start();
        }
    }

    @Override
    protected void onStop() {
        //关闭定位
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        //停止方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRoutePlan != null) {
            mRoutePlan.destroy();
        }
        mapView.onDestroy();
        mapView = null;
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
