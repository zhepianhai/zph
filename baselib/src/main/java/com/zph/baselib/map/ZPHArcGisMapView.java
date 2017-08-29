package com.zph.baselib.map;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.AttributeSet;
import android.widget.LinearLayout;

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
