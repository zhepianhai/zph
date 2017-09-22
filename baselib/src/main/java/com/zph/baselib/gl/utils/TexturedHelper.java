package com.zph.baselib.gl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by zph on 2017/9/21.
 * 纹理辅助类
 */

public class TexturedHelper {
    private static final String TAG=TexturedHelper.class.getName();
    /*
     * @return 纹理ID
     * */
    public static  int loadTexture(Context context,int resourceId){
        int[] texturedObjectIds=new int[1];
        //创建
        GLES20.glGenTextures(1,texturedObjectIds,0);
        if(texturedObjectIds[0]==0){
            Log.w(TAG,"create texture id error 0.0");
            return 0;
        }
        //加载位图数据
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;//使用原始图像
        Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),resourceId,options);
        if(null==bitmap){
            Log.w(TAG,"resource can not bu decode");
            GLES20.glDeleteTextures(1,texturedObjectIds,0);
            return 0;
        }
        //绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texturedObjectIds[0]);
        //设置过滤器
        //对于缩小情况我们使用三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //对于放大情况我们使用双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //读入位图数据
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        //生成MIP贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return texturedObjectIds[0];

    }

}
