package com.zph.baselib.map;

import com.esri.android.map.MapOptions;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

/**
 * Created by zph on 2017/9/7.
 */

public class ArcGisMapSetting {
    public static  MapOptions mStreetBaseMap = new MapOptions(MapOptions.MapType.STREETS);//街道图
    public static  MapOptions mTopoBaseMap = new MapOptions(MapOptions.MapType.TOPO);//地形图
    public static  MapOptions mSatelliteBaseMap = new MapOptions(MapOptions.MapType.SATELLITE);//遥感图
    public static  MapOptions NATIONAL_GEOGRAPHIC = new MapOptions(MapOptions.MapType.NATIONAL_GEOGRAPHIC);//遥感图

    public static String ChinaOnlineStreetPurplishBlue=
            "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer";
//    http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer
    public static String ChinaOnlineCommunity_Mobile=
        "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity_Mobile/MapServer";
    public static String ChinaOnlineCommunityENG =
            "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunityENG/MapServer";
    public static String ChinaOnlineStreetCold =
            "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetCold/MapServer";
    public static String ChinaOnlineStreetColor=
            "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetColor/MapServer";
    public static String ChinaOnlineStreetGray=
            "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetGray/MapServer";
    public static String ChinaOnlineStreetWarm=
            "http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetWarm/MapServer";



    public static ArcGISTiledMapServiceLayer getGisMapLayout(){
        return new ArcGISTiledMapServiceLayer(ChinaOnlineStreetPurplishBlue);
    }

}
