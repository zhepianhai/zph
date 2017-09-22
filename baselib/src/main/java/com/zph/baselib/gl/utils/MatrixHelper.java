package com.zph.baselib.gl.utils;

/**
 * Created by zph on 2017/9/20.
 * 矩阵辅助类
 */

public class MatrixHelper {
    /*
     * 解决添加透视矩阵问题
     * */
    public static void perspectiveM(float[]m,float yFovInDegrees,float aspect,float n,float f){
        //计算焦距 得到弧度
        final float angleInRadiancs=(float)(yFovInDegrees*Math.PI/180.0);
        final float a=(float) (1/Math.tan(angleInRadiancs/2.0));
        //建立矩阵
        m[0]=a/aspect;
        m[1]=0f;
        m[2]=0f;
        m[3]=0f;
        m[4]=0f;
        m[5]=a;
        m[6]=0f;
        m[7]=0f;
        m[8]=0f;
        m[9]=0f;
        m[10]=-((f+n)/(f-n));
        m[11]=-1f;
        m[12]=0f;
        m[13]=0f;
        m[14]=-((2f*f*n)/(f-n));
        m[15]=0f;

    }
}
