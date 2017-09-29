package com.zph.baselib.gl.utils;

import java.nio.ByteBuffer;

import android.opengl.Matrix;

/**
 * 控制三维图旋转的Matrix类
 */
public class MatrixState {
	//4*4矩阵，投影用
	private static float[] mProjMatrix = new float[16];
	//摄像机位置方向，9个参数的矩阵
	private static float[] mVMatrix = new float[16];
	//当前的变化矩阵
	private static float[] currMatrix;
	// 保护变换矩阵的栈
	static float[][] mStack = new float[10][16];
	static int stackTop = -1;
	
	/**
	 * 初始化矩阵
	 * */
	public static void setInitStack(){
		currMatrix = new float[16];
		Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
	}
	/**
	 * 存储变换矩阵
	 * */
	public static void pushMatrix(){
		stackTop++;
		for (int i = 0; i < 16; i++) {
			mStack[stackTop][i] = currMatrix[i];
		}
	}
	/**
	 * 恢复变换矩阵
	 * */
	public static void popMatrix(){
		for (int i = 0; i < 16; i++) {
			currMatrix[i] = mStack[stackTop][i];
		}
		stackTop--;
	}
	
	
	/**
	 * 设置沿着XYZ轴移动 
	 * */
	public static void translate(float x, float y, float z){
		Matrix.translateM(currMatrix, 0, x, y, z);
	}
	
	/**
	 * 绕XYZ轴移动
	 * */
	public static void rotate(float angle, float x, float y, float z){
		Matrix.rotateM(currMatrix, 0, angle, x, y, z);
	}

	//设置相机
	static ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
	//相机的位置
	static float[] cameraLocation = new float[3];

	public static void setCamera(float cx, // 摄像机位置x
			float cy, // 摄像机位置y
			float cz, // 摄像机位置z
			float tx, // 摄像机目标点x
			float ty, // 摄像机目标点y
			float tz, // 摄像机目标点z
			float upx, // 摄像机UP向量X分量
			float upy, // 摄像机UP向量Y分量
			float upz // 摄像机UP向量Z分量
	) {
		Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
	}

	// 设置透视投影参数
	public static void setProjectFrustum(float left, // near面的left
			float right, // near面的right
			float bottom, // near面的bottom
			float top, // near面的top
			float near, // near面距离
			float far // far面距离
	) {
		Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}

	// 设置正交投影参数
	public static void setProjectOrtho(float left, // near面的left
			float right, // near面的right
			float bottom, // near面的bottom
			float top, // near面的top
			float near, // near面距离
			float far // far面距离
	) {
		Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}

	// 获取具体物体的总变换矩阵
	static float[] mMVPMatrix = new float[16];

	public static float[] getFinalMatrix() {
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

	// 获取具体物体的变换矩阵
	public static float[] getMMatrix() {
		return currMatrix;
	}
}
