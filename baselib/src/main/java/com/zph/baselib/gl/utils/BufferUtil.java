package com.zph.baselib.gl.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by zph on 2017/9/28.
 */

public class BufferUtil {
    public static FloatBuffer mBuffer;
    public static FloatBuffer floatToBuffer(float[] a){
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        mbb.order(ByteOrder.nativeOrder());
        mBuffer = mbb.asFloatBuffer();
        mBuffer.put(a);
        mBuffer.position(0);
        return mBuffer;
    }

    public static IntBuffer intToBuffer(int[] a){

        IntBuffer intBuffer;
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        mbb.order(ByteOrder.nativeOrder());
        intBuffer = mbb.asIntBuffer();
        intBuffer.put(a);
        intBuffer.position(0);
        return intBuffer;
    }
}
