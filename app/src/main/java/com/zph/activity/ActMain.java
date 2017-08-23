package com.zph.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.event.OnStatusChangedListener;
import com.zph.R;
import com.zph.base.ActRoot;
import com.zph.base.ActRootMap;
import com.zph.baselib.map.ZPHArcGisMapView;

public class ActMain extends ActRootMap  {
    LocationDisplayManager locationDisplayManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initVar();
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

    }

//    @Override
//    public void onStatusChanged(Object o, STATUS status) {
//        Log.i("TAG","o"+o.toString());
//        Log.i("TAG","status"+status.getError().toString());
//        Log.i("TAG","status"+status.getValue());
//    }
}
