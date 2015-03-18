package com.fly.rss.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	/** 最后一次阅读Rss的链接地址*/
	public static final String LAST_READ_RSS_link = "lastReadRssLink";
	public static final String PRE_NAME = "rss";
	
	public static void putString(Context context,String preName,String value){
		SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, 0);
		Editor editor = preferences.edit();
		editor.putString(preName, value);
		editor.commit();
	}
	
	public static String getString(Context context,String preName){
		SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, 0);
		return preferences.getString(preName, "");
	}
}
