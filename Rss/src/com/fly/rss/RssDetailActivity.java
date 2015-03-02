package com.fly.rss;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fly.rss.utils.RssUtil;

public class RssDetailActivity extends ActionBarActivity{
	public static final String RSS_TITLE = "rss_title";
	public static final String RSS_CONTENT = "rss_content";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(android.os.Build.VERSION.SDK_INT >18){
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_rss_detail);
		
		Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		initUI();
		RssUtil.setStateBarColor(this, "#0099CC");
	}

	private void initUI() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		TextView tvDetail = (TextView)findViewById(R.id.tv_detail);
		ActionBar actionBar  = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		/** 返回键生效 ,在manifest中描述*/
		actionBar.setDisplayHomeAsUpEnabled(true);
		if(bundle != null){
			String title = bundle.getString(RSS_TITLE);
			String content = bundle.getString(RSS_CONTENT);
			if(!TextUtils.isEmpty(content)){
				tvDetail.setText(Html.fromHtml(content));
			}
			if(!TextUtils.isEmpty(title)){
				actionBar.setTitle(title);
			}
		}
	}
	
}
