package com.fly.rss.db;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.ab.db.orm.AbDBHelper;
import com.ab.db.orm.AbSDDBHelper;
import com.fly.rss.model.RssClass;
import com.fly.rss.model.RssSite;

public class RssDBHelper extends AbDBHelper {
	public static final String DB_NAME = "rss.db";
	public static final int VERSION = 1;
	private static final Class<?>[] classes = {RssClass.class,RssSite.class};
	public static final String PATH = Environment.getExternalStorageDirectory()+File.separator+"阅读器";
	public RssDBHelper(Context context) {
		//super(context,PATH, DB_NAME, null, VERSION, classes);
		super(context,DB_NAME, null, VERSION, classes);
		// TODO Auto-generated constructor stub
	}

}
