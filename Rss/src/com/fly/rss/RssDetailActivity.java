package com.fly.rss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fly.rss.utils.RssUtil;

public class RssDetailActivity extends ActionBarActivity{
	public static final String RSS_TITLE = "rss_title";
	public static final String RSS_CONTENT = "rss_content";
	private WebView webView = null;
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
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		setSupportActionBar(mToolbar);
		initUI();
		RssUtil.setStateBarColor(this, getResources().getColor(R.color.c_status_bar));
	}

	private void initUI() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		ActionBar actionBar  = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		/** 返回键生效 ,在manifest中描述*/
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
		//Webview显示 
		webView = (WebView)findViewById(R.id.webview);
		webView.setVerticalScrollBarEnabled(false);
		webView.setWebChromeClient(new IntenalWebChromeClient());
		webView.setWebViewClient(new InWebViewClient());
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBlockNetworkImage(false);//显示网络图片
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setSaveFormData(true);
		settings.setAppCacheEnabled(true);
		settings.setAppCachePath(
				getApplicationContext().getCacheDir().getPath());
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		if(bundle != null){
			String title = bundle.getString(RSS_TITLE);
			String content = bundle.getString(RSS_CONTENT);
			System.out.println("rss内容:"+content);
			StringBuffer buffer = new StringBuffer();
			buffer.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
			buffer.append("<html>");
			buffer.append("<head>");
			buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
			buffer.append("</head>");
			buffer.append("<body style=\"color:#666666;font-size:38px;\">");
			buffer.append(content);
			buffer.append("</body>");
			buffer.append("</html>");
			webView.loadDataWithBaseURL("http://null",buffer.toString(),"text/html","UTF-8","http://null");
			if(!TextUtils.isEmpty(title)){
				actionBar.setTitle(title);
			}
		}
	}
	
	private class IntenalWebChromeClient extends WebChromeClient{
		
	}
	
	private class InWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse(url);//使用外部浏览器打开超链接
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
