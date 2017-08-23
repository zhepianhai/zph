package com.zph.base;

import android.os.Bundle;

import com.zph.baselib.map.ZPHArcGisMapView;

/**
 * Created by 3119 on 2017/8/23.
 */

public class ActRootMap extends ActRoot implements ZPHArcGisMapView.ZPHArcGisMapViewPopClickListener{
    protected ZPHArcGisMapView mMapView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        try {
            if (mMapView != null) {
                mMapView.getMapView().pause();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        try {
            if (mMapView != null) {
                mMapView.getMapView().destroyDrawingCache();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void onMapViewPopClick(ZPHArcGisMapView var1, Object var2) {

    }


}
