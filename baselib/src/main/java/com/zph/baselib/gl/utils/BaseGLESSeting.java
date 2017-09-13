package com.zph.baselib.gl.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by zph on 2017/9/13.
 */

public class BaseGLESSeting {

    /**
     * 监测系统是否支持GL
     * 用它来获取设备的配置信息，然后,取出reqGlEsVersion变量检查OpenGL ES版本号。
     * 如果版本号为0*20000或后续版本，我们就可以使用OpenGL ES2.0的API了。
     * */
    public static boolean CheckSupposeGLES(Activity activity){
         ActivityManager activityManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
         ConfigurationInfo configurationInfo=activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion>=0x20000;
    }


    public static FloatBuffer mBuffer;
    public static FloatBuffer floatToBuffer(float[] a){
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        //数组排序用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        mBuffer = mbb.asFloatBuffer();
        mBuffer.put(a);
        mBuffer.position(0);
        return mBuffer;
    }

    public static IntBuffer intToBuffer(int[] a){

        IntBuffer intBuffer;
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        //数组排序用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        intBuffer = mbb.asIntBuffer();
        intBuffer.put(a);
        intBuffer.position(0);
        return intBuffer;
    }
}
