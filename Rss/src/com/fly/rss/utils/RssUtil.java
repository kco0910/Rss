package com.fly.rss.utils;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RssUtil {
	/**
	 * 设置状态栏的颜色
	 * @param act
	 * @param color
	 */
	public static void setStateBarColor(Activity act,int color){
		//设置状态栏的颜色
		TextView textView = new TextView(act);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getStatusBarHeight(act));
        textView.setBackgroundColor(color);
        textView.setLayoutParams(lParams);
        // 获得根视图并把TextView加进去。
        ViewGroup view = (ViewGroup) act.getWindow().getDecorView();
        view.addView(textView);
	}
	
	/**
	 * 获取ActionBar的高度
	 * @param context
	 * @return
	 */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
    
	 /**
	  * 获取手机状态栏高度
	  * @param context
	  * @return
	  */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
	
}
