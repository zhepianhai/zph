package com.zph.activity;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huitu.app.engine.HTMainNet;
import com.huitu.app.util.HTUtilData;
import com.huitu.app.util.HTUtilNet;
import com.zph.R;
import com.zph.baselib.gl.render.RainISORenderer;
import com.zph.baselib.gl.view.OpenGLView;

public class ActRain extends AppCompatActivity {
    private OpenGLView openGLView;
    private ProgressDialog myDialog;
    private int mDataCol;
    private int mDataRow;
    private static final String DATA_SPLIT = "-9999";
    private float[][] mDataGrid;
    private float mDataMax;
    private RainISORenderer mRender;
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            try {
                switch(msg.what){
                    case HTMainNet.MSG_WEB_DOWNLOAD_UPDATE:
                        ActRain.this.showData();
                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        break;

                }
            } catch (Exception ignored) {
            }
        }
    };

    private void showData() {
        new Thread() {
            public void run() {
                mRender.showData(mDataGrid, mDataMax, mDataCol, mDataRow);
                openGLView.requestRender();
            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_act_rain);
        initVar();
        initView();
        setContentView(openGLView);
        loadData();
    }

    private void initVar() {
        mDataGrid = new float[0][0];
    }

    private void loadData() {
        myDialog=new ProgressDialog(this);
        myDialog.setTitle("Loading....");
        new Thread(){
            @Override
            public void run() {
                try {
                    String strUrlGL = "http://47.94.237.158:31700/RainISO/RainISO.aspx?stm=1506556800000&etm=1506567600000&kind=3";
                    String str_data = HTUtilNet.GetWebCont(strUrlGL, "UTF-8");
                    // Head
                    String str_data_head = str_data.substring(0, str_data.indexOf(DATA_SPLIT));
                    str_data_head = str_data_head.replace("ncols", "").replace("nrows", "").replace("xllcorner", "").replace("yllcorner", "")
                            .replace("cellsize", "").replace("NODATA_value", "").trim();
                    String[] arrDataHead = str_data_head.split("\t");

                    mDataCol = HTUtilData.GetDataInt(arrDataHead[0]);
                    mDataRow = HTUtilData.GetDataInt(arrDataHead[1]);
                    // Data
                    String str_data_body = str_data.substring(str_data.indexOf(DATA_SPLIT) + DATA_SPLIT.length());
                    String[] arrData = str_data_body.split(" ");
                    mDataGrid = new float[mDataCol][mDataRow];
                    mDataMax = 0;
                    for (int j = 0; j < mDataRow; j++) {
                        for (int i = 0; i < mDataCol; i++) {
                            mDataGrid[i][j] = HTUtilData.GetDataFloat(arrData[j * mDataCol + i]);
                            if (mDataMax < mDataGrid[i][j]) {
                                mDataMax = mDataGrid[i][j];
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                myHandler.sendEmptyMessage(HTMainNet.MSG_WEB_DOWNLOAD_UPDATE);
            }
        }.start();
    }

    private void initView() {
//        openGLView= (OpenGLView) findViewById(R.id.openglview);
//
        openGLView=new OpenGLView(this);
        openGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        openGLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        openGLView.setZOrderOnTop(true);
        mRender = new RainISORenderer(this);
        openGLView.setRenderer(mRender);
        openGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


    }
}
