package com.fly.db;

import android.content.Context;

import com.ab.db.orm.AbDBHelper;
import com.fly.rss.model.RssClass;
import com.fly.rss.model.RssSite;

public class RssDBHelper extends AbDBHelper {
	public static final String DB_NAME = "rss.db";
	public static final int VERSION = 1;
	private static final Class<?>[] classes = {RssClass.class,RssSite.class};
	public RssDBHelper(Context context) {
		super(context, DB_NAME, null, VERSION, classes);
		// TODO Auto-generated constructor stub
	}

}
