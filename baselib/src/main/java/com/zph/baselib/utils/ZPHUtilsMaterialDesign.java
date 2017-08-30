package com.zph.baselib.utils;

import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zph.baselib.R;

/**
 * Created by zph on 2017/8/30.
 * MaterialDesign some setting
 */

public class ZPHUtilsMaterialDesign {

    /**
     * 改变 Sncakbar的背景色和字体颜色
     */
    public static void setSnackbarColor(Snackbar snackbar, int backgroundColor, int messageColor) {
        if (null == snackbar) {
            return;
        }
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
    }

    /**
     * Snackbar添加 布局文件
     * */
    public static void SnackbarAddView(Snackbar snackbar, int layoutId, int index) {
        View snackbarview = snackbar.getView();//获取snackbar的View(其实就是SnackbarLayout)
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarview;//将获取的View转换成SnackbarLayout
        View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId, null);//加载布局文件新建
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);//设置新建布局参数
        p.gravity = Gravity.CENTER_VERTICAL;//设置新建布局在Snackbar内垂直居中显示
        snackbarLayout.addView(add_view, index, p);//将新建布局添加进snackbarLayout相应位置
    }
}
