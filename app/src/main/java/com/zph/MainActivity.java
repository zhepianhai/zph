package com.zph;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.zph.base.ActRootMap;
import com.zph.view.ZPHMapTileView;

public class MainActivity extends ActRootMap {
    MapView mapView;
    MapOptions opt= new  MapOptions(MapOptions.MapType.STREETS,33.666354, -117.903557,13);
    ZPHMapTileView slidingDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.zph_map_title_view);
        setmActionBarType(NAVBTNRIGHT_TYPE_NOTITLE);
        iniview();
        ZPHMapTileView layShow = (ZPHMapTileView) LinearLayout.inflate(this, R.layout.zph_map_title_view, null);
        mViewMain.addView(layShow, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

//        ZPHMapTileView zphMapTileView=new ZPHMapTileView(this);

//        mViewMain.addView(zphMapTileView);

//        setContentView(R.layout.zph_arcgis_map);
//        mapView = new MapView(this,opt);
//
//        mapView.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
    }

    private void iniview() {

    }




}
