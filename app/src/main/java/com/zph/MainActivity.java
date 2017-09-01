package com.zph;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.zph.base.ActRootMap;
import com.zph.baselib.view.ZPHMapTileView;

public class MainActivity extends ActRootMap implements SlidingDrawer.OnDrawerOpenListener, SlidingDrawer.OnDrawerCloseListener {
    MapView mapView;
    MapOptions opt= new  MapOptions(MapOptions.MapType.STREETS,33.666354, -117.903557,13);
    ZPHMapTileView slidingDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setmActionBarType(NAVBTNRIGHT_TYPE_NOTITLE);
        iniview();

//        setContentView(R.layout.zph_arcgis_map);
//        mapView = new MapView(this,opt);
//
//        mapView.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
    }

    private void iniview() {
        slidingDrawer= (ZPHMapTileView) findViewById(R.id.slidingDrawer);
        slidingDrawer.setOnDrawerOpenListener(this);
        slidingDrawer.setOnDrawerCloseListener(this);

    }


    @Override
    public void onDrawerOpened() {
        if(slidingDrawer.isOpened()){
            View view=slidingDrawer.getHandle();
//            view.setBackgroundColor(Color.RED);
            view.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDrawerClosed() {
        View view=slidingDrawer.getHandle();
        view.setBackgroundColor(Color.WHITE);
    }


}
