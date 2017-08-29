package com.zph.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.event.OnStatusChangedListener;
import com.zph.R;
import com.zph.base.ActRoot;
import com.zph.base.ActRootMap;
import com.zph.baselib.map.ZPHArcGisMapView;

public class ActMain extends ActRootMap  {
    private LocationDisplayManager locationDisplayManager;
    private static final int BAIDU_READ_PHONE_STATE =100;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            initVar();//init为定位方法
        }
    }
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(ActMain.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            initVar();
        }
    }
    private void initVar() {
//        this.setNavBtnRightType(ActRoot.NAVBTNRIGHT_TYPE_HOME);
        mMapView=new ZPHArcGisMapView(this);
        mNavLay.setVisibility(View.VISIBLE);
        mViewMain.addView(mMapView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        locationDisplayManager=  mMapView.getMapView().getLocationDisplayManager();//获取定位类
        locationDisplayManager.setShowLocation(true);
        locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
        locationDisplayManager.setShowPings(true);
        locationDisplayManager.start();//开始定位
        locationDisplayManager.setLocationListener(new listener());

    }

//    @Override
//    public void onStatusChanged(Object o, STATUS status) {
//        Log.i("TAG","o"+o.toString());
//        Log.i("TAG","status"+status.getError().toString());
//        Log.i("TAG","status"+status.getValue());
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initVar();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    class listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("TAG","onLocationChanged:"+location.toString());
            double lat=location.getLatitude();
            double log=location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i("TAG","onStatusChanged:"+s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i("TAG","onProviderEnabled:"+s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i("TAG","onProviderDisabled:"+s);
        }
    }
}
