package com.zph.activity;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;


import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOptions;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.zph.R;
import com.zph.base.ActRootMap;
import com.zph.baselib.map.ArcGisMapSetting;
import com.zph.baselib.map.ZPHArcGisMapView;
import com.zph.baselib.utils.ZPHUtilsMaterialDesign;
import com.zph.view.ZPHMapTileView;

public class ActMain extends ActRootMap implements ZPHMapTileView.MapTileScrollListener {
    private LocationDisplayManager locationDisplayManager;
    private MapOptions o;
    private ActionBar mActionBar;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private ZPHMapTileView layShow;
    private boolean isViewInTop;
    private Location mLocation = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();

        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            initMap();//init为定位方法
        }
        initMyActionBar();

    }

    private void initVar() {
        layShow = (ZPHMapTileView) LinearLayout.inflate(this, R.layout.zph_map_title_view, null);
        mViewMain1.addView(layShow, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        layShow.setMapTileScrollListener(this);
    }

    private void initMyActionBar() {
        setmActionBarType(NAVBTNRIGHT_TYPE_NOTITLE);
    }

    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            Snackbar snackbar = Snackbar.make(mViewMain,R.string.promiss_location_error, Snackbar.LENGTH_SHORT);
            ZPHUtilsMaterialDesign.setSnackbarColor(snackbar, Color.BLACK, Color.WHITE);

            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(ActMain.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        } else {
            initMap();
        }
    }

    private void initMap() {
//        this.setNavBtnRightType(ActRoot.NAVBTNRIGHT_TYPE_HOME);
        mMapView = new ZPHArcGisMapView(this);
        mViewMain.addView(mMapView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMapView.setZphMapType(1);

        mMapView.getMapView().addLayer(new ArcGISTiledMapServiceLayer(ArcGisMapSetting.ChinaOnlineStreetPurplishBlue));
//                "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer"));
//                "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer"));
//                "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));


        onStatusChangedListener = new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {


                    locationDisplayManager.setShowLocation(true);

                    locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    locationDisplayManager.setShowPings(true);
                    locationDisplayManager.setAllowNetworkLocation(false);
                    locationDisplayManager.setAccuracyCircleOn(false);
                    locationDisplayManager.setLocationListener(locationDisplayManager.getLocationListener());
                    locationDisplayManager.start();

                }
            }
        };
        mMapView.getMapView().setOnStatusChangedListener(onStatusChangedListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initMap();
                } else {
                    // 没有获取到权限，做特殊处理
                    Snackbar snackbar = Snackbar.make(mViewMain,R.string.location_error_open, Snackbar.LENGTH_SHORT);
                    ZPHUtilsMaterialDesign.setSnackbarColor(snackbar, Color.BLACK, Color.WHITE);

                }
                break;
            default:
                break;
        }
    }

    /**
     * buttom scroll listener
     * */
    @Override
    public void scrollStart() {

    }

    @Override
    public void scrollEnd() {

    }

    @Override
    public void scrollTop(boolean flag) {
        isViewInTop = flag;
    }

    @Override
    public void scrollRecover(boolean flag) {
        isViewInTop = flag;
    }

    @Override
    public void roadOnClickListener() {
        Snackbar snackbar = Snackbar.make(mViewMain, "check me", Snackbar.LENGTH_SHORT);
        ZPHUtilsMaterialDesign.setSnackbarColor(snackbar, Color.BLACK, Color.WHITE);
    }



    @Override
    public void onBackPressed() {
        if (isViewInTop) {
            layShow.RecoverAll();

        } else {
            Snackbar snackbar = Snackbar.make(mViewMain, "退出应用", Snackbar.LENGTH_SHORT);
            ZPHUtilsMaterialDesign.setSnackbarColor(snackbar, Color.BLACK, Color.WHITE);
            this.finish();
        }
    }


    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double lon = location.getLongitude();
                double lat = location.getLatitude();
                if (lon > 0 && lat > 0)
                    mLocation = location;
                o = new MapOptions(MapOptions.MapType.STREETS, lat, lon, 13);
                mMapView.getMapView().setMapOptions(o);
                locationDisplayManager.stop();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


}
