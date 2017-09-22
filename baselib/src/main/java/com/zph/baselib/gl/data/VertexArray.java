package com.zph.baselib.gl.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by zph on 2017/9/21.
 */

public class VertexArray {
    private  FloatBuffer floatBuffer=null;
    private static final int BYTES_PER_FLOAT = 4;
    public VertexArray(float[] vertexData){
        floatBuffer= ByteBuffer.allocate(vertexData.length*BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer(int dataOffSet,int attributeLocation,int componentCount,int stride){
        floatBuffer.position(dataOffSet);
        GLES20.glVertexAttribPointer(attributeLocation,componentCount,GLES20.GL_FLOAT,false,stride,floatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }

}
