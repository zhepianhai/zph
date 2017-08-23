package com.zph.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zph.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActRoot extends AppCompatActivity {
    public static final int NAVBTNRIGHT_TYPE_NROMAL = 0;
    public static final int NAVBTNRIGHT_TYPE_HOME = 1;
    public static final int NAVBTNRIGHT_TYPE_TIME = 2;
    public static final int NAVBTNRIGHT_TYPE_DATA = 3;
    public static final int NAVBTNRIGHT_TYPE_LOC = 4;
    public static final int NAVBTNRIGHT_TYPE_HISTPH = 5;
    public static final int NAVBTNRIGHT_TYPE_MSGFILE = 6;
    public static final int NAVBTNRIGHT_TYPE_MEDIAUP = 7;

    @BindView(R.id.nav_btn_left)
    protected Button mNavBtnLeft;
    @BindView(R.id.nav_btn_right)
    protected Button mNavBtnRight;
    @BindView(R.id.nav_txt_title)
    protected TextView mNavTxtTitle;
    @BindView(R.id.nav_lay_title)
    protected LinearLayout mNavLayTitle;
    @BindView(R.id.root_lay_show)
    protected LinearLayout mViewMain;
    @BindView(R.id.root_lay_nav)
    protected RelativeLayout mNavLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_root);
        ButterKnife.bind(this);
        this.initNav();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initNav() {
        mNavLayTitle.setVisibility(View.GONE);
    }
    public void setNavTitle(String title) {
        mNavTxtTitle.setText(title);
    }

    public void setNavBtnRightType(int type) {
        mNavBtnRight.setText("");
        if (type == NAVBTNRIGHT_TYPE_NROMAL) {
//            mNavBtnRight.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.frame_nav_btn_data_bg));
        }

    }

    @OnClick(R.id.nav_btn_left)
    public void OnLeftClickListener(View view){
        this.finish();
    }

    @OnClick(R.id.nav_btn_right)
    public void OnRightClickListener(View view){

    }

}
