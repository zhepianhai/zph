package com.zph.baselib.map;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
/**
 * Created by zph on 2017/8/23.
 * encapsulation the ArcGisMapView
 */

public class ZPHArcGisMapView  extends LinearLayout {

    private Context a;
    private MapView f;
    private MapOptions o;
    private RelativeLayout searcL;
    private FloatingActionButton fBtn;

    private ZPHArcGisMapViewPopClickListener z;
    public ZPHArcGisMapView(Context context) {
        super(context,null);
        this.a(context);
    }

    public ZPHArcGisMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        this.a(context);
    }

    public ZPHArcGisMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.a(context);
    }

    private void a(Context context) {
        this.a=context;
        LinearLayout var2;
        (var2 = (LinearLayout)LinearLayout.inflate(this.a, this.a.getResources().
                getIdentifier("zph_arcgis_map","layout", this.a.getPackageName()),null)).
                setLayoutParams(new LayoutParams(-1, -1));
        this.addView(var2);
        this.f = var2.findViewById(this.a.getResources().getIdentifier("mapview", "id", this.a.getPackageName()));
        this.searcL=var2.findViewById(this.a.getResources().getIdentifier("root_searchView_layout","id",this.a.getPackageName()));
        this.fBtn=var2.findViewById(this.a.getResources().getIdentifier("root_floatingActionButton","id",this.a.getPackageName()));
        this.setShadow();

    }

    private void setShadow() {
//        this.searcL.setElevation();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            this.searcL.setElevation(8.0f);
//        }
    }

    public void setZPHMapOptions(double t,double g,int l){
        o=new  MapOptions(MapOptions.MapType.STREETS,t,g,l);
        if(null==f){
            return;
        }
        f=new MapView(this.a,o);

    }
    public MapView getMapView() {
        return this.f;
    }


    public interface ZPHArcGisMapViewPopClickListener {
        void onMapViewPopClick(ZPHArcGisMapView var1, Object var2);
    }
}
