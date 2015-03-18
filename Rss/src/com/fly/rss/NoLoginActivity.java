package com.fly.rss;

import java.util.List;

import org.mcsoxford.rss.RSSException;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.ResponseDeliver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ab.fragment.AbAlertDialogFragment;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbToastUtil;
import com.fly.rss.adapter.RssContentAdapter;
import com.fly.rss.db.RssClassDbDaoImpl;
import com.fly.rss.dialog.LoadDialog;
import com.fly.rss.model.RssConstant;
import com.fly.rss.utils.SharedPreferencesUtil;
import com.fly.rss.widget.EditDropSelect;

public class NoLoginActivity extends ActionBarActivity implements OnClickListener,OnRefreshListener,OnItemClickListener{
	private SwipeRefreshLayout refreshLayout = null;
	private RssContentAdapter contentAdapter = null;
	private GetRssContentTask mGetRssContentTask = null;
	private RssClassDbDaoImpl mRssClassDbDaoImpl = null;
	/** 是否第一次显示*/
	private boolean isFirstShow = true;
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		// TODO Auto-generated method stub
		if(android.os.Build.VERSION.SDK_INT >18){
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//设置虚拟按钮透明(导航栏)
			//window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		super.onCreate(saveInstanceState);
		setContentView(R.layout.act_no_login);
		//RssUtil.setStateBarColor(this, "#0099CC");
		initUI();
	}
	
	private void initUI(){
		Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
		mToolbar.setTitle(R.string.app_name);
		//mToolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);
		/**必须写在set之后 */
		ActionBar actionBar = getSupportActionBar();
		/* 和Toolbar一样
		actionBar.setTitle("主标题");
		actionBar.setSubtitle("副标题");
		actionBar.setLogo(R.drawable.icon); */
		actionBar.setDisplayHomeAsUpEnabled(true);
		DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, 
				mDrawerLayout, mToolbar,R.string.open, R.string.close);
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		Button btnLogin = (Button)findViewById(R.id.btn_login);
		Button btnAddContent = (Button)findViewById(R.id.btn_add_content);
		btnAddContent.setOnClickListener(this);
		refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
		refreshLayout.setOnRefreshListener(this);
		/*refreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,  
                android.R.color.holo_green_light,  
                android.R.color.holo_orange_light,  
                android.R.color.holo_red_light);*/
		ListView listview = (ListView)findViewById(R.id.listview);
		contentAdapter = new RssContentAdapter(this);
		listview.setAdapter(contentAdapter);
		listview.setOnItemClickListener(this);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && isFirstShow){
			initRss();
			isFirstShow = false;
		}
	}
	
	/**
	 * 初始化Rss显示
	 */
	private void initRss(){
		String lastReadLink = SharedPreferencesUtil.getString(this, SharedPreferencesUtil.LAST_READ_RSS_link);
		if(!TextUtils.isEmpty(lastReadLink)){
			System.out.println("initRss:"+lastReadLink);
			mGetRssContentTask = new GetRssContentTask(false);
			mGetRssContentTask.execute(lastReadLink);
		}else{
			System.out.println("没有打开RSS记录!");
		}
	}
	
	private RssClassDbDaoImpl getRssClassDbDaoImpl(){
		return mRssClassDbDaoImpl == null ? new RssClassDbDaoImpl(this) : mRssClassDbDaoImpl;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {  
	        case R.id.add_rss:  
	        	addRss();
	            break;  
	        case R.id.barcode:  
	        	
	        	
	            break;  
	        default:  
	            break;  
	     }  
	     return true; 
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_add_content:
			
			break;
		default:
			break;
		}
	}
	
	private void addRss(){
		View view = View.inflate(this, R.layout.add_content, null);
		final EditText etRssLink = (EditText)view.findViewById(R.id.et_rss_link);
		//String url = "http://news.qq.com/china_index.shtml";
		String url = "http://coolshell.cn/feed";
		etRssLink.setText(url);
		AbAlertDialogFragment.newInstance(0,getString(R.string.addRss) , null, view, new AbDialogOnClickListener(){
			@Override
			public void onPositiveClick() {
				// TODO Auto-generated method stub
				String link = etRssLink.getText().toString().trim();
				if(!TextUtils.isEmpty(link)){
					//判断是否符合URL正则
					if(link.matches(RssConstant.RSS_LINK_REGEX)){
						if(mGetRssContentTask != null && !mGetRssContentTask.isCancelled()){
							mGetRssContentTask.cancel(true);
						}
						mGetRssContentTask = new GetRssContentTask();
						mGetRssContentTask.execute(link);
					}else{
						AbToastUtil.showToast(getBaseContext(), R.string.rss_link_no_regex);
					}
				}else{
					AbToastUtil.showToast(getBaseContext(), R.string.rss_link_is_null);
				}
			}
			@Override
			public void onNegativeClick() {
				// TODO Auto-generated method stub
				
			}
		}).show(getFragmentManager(), "addRss");;
	}
	/** 加载框*/
	private LoadDialog mLoadDialog = null;
	
	
	/**
	 * 从网络上获取RSS的任务
	 * @author jian.fu
	 *
	 */
	private class GetRssContentTask extends AsyncTask<String, Void, RSSFeed>{
		
		private boolean mIsSave = false;
		public GetRssContentTask(){
			this(true);
		}
		/**
		 * 是否进行保存操作
		 * @param isSave
		 */
		public GetRssContentTask(boolean isSave){
			mIsSave = isSave;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(mLoadDialog == null){
				mLoadDialog = new LoadDialog(NoLoginActivity.this, getString(R.string.load_data));
				mLoadDialog.setDelegateTask(this);
			}
			mLoadDialog.show();
		}
		
		@Override
		protected RSSFeed doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			if(url == null){
				return null;
			}
			RSSReader reader = null;
			RSSFeed feed = null;
			try {
				reader = new RSSReader(new RssReponseDeliver());
				feed = reader.load(url);
			} catch (Exception e) {
			}finally{
				if(reader != null) reader.close();
			}
			return feed;
		}
		
		@Override
		protected void onPostExecute(RSSFeed feed) {
			// TODO Auto-generated method stub
			super.onPostExecute(feed);
			if(mLoadDialog != null){
				mLoadDialog.dismiss();
			}
			if(feed != null){
				if(mIsSave){
					//弹出分类窗口
					saveRssClass(feed);
				}else{
					//显示Rss内容
					contentAdapter.setData(feed.getItems());
					contentAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	
	private void saveRssClass(final RSSFeed feed){
		if(feed != null){
			View view = View.inflate(this, R.layout.rss_class_item, null);
			final EditDropSelect editDropSelect = (EditDropSelect)view.findViewById(R.id.edit_drop);
			RssClassDbDaoImpl rssClassDbDaoImpl = new RssClassDbDaoImpl(this);
			List<String> rssClasses =  rssClassDbDaoImpl.getRssClassNames();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rssClasses);
			editDropSelect.setAdapter(adapter);
			AbAlertDialogFragment.newInstance(0, getString(R.string.add_rss), null, view, new AbDialogOnClickListener() {
				@Override
				public void onPositiveClick() {
					// TODO Auto-generated method stub
					String className = editDropSelect.getEditTextStr();
					if(!TextUtils.isEmpty(className)){
						RssClassDbDaoImpl rssClassDbDaoImpl = getRssClassDbDaoImpl();
						rssClassDbDaoImpl.addNewRss(feed, className);
						//记录打开的Rss
						SharedPreferencesUtil.putString(NoLoginActivity.this, SharedPreferencesUtil.LAST_READ_RSS_link
								, feed.getRssLink());
						//显示Rss内容
						contentAdapter.setData(feed.getItems());
						contentAdapter.notifyDataSetChanged();
					}else{
						AbToastUtil.showToast(NoLoginActivity.this, R.string.class_name_not_null);
					}
				}
				
				@Override
				public void onNegativeClick() {
					// TODO Auto-generated method stub
					
				}
			}).show(getFragmentManager(), "saveRssClass");
		}
	}
	
	
	
	private class RssReponseDeliver implements ResponseDeliver{

		@Override
		public void deliverError(RSSException rssException) {
			// TODO Auto-generated method stub
			System.out.println("RSSException :"+rssException.toString());
		}
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshLayout.setRefreshing(false);
			}
		}, 1000);
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		RSSItem rssItem = (RSSItem)parent.getItemAtPosition(position);
		if(rssItem != null){
			Intent intent = new Intent(this, RssDetailActivity.class);
			intent.putExtra(RssDetailActivity.RSS_CONTENT, rssItem.getContent());
			intent.putExtra(RssDetailActivity.RSS_TITLE, rssItem.getTitle());
			startActivity(intent);
			
			/*List<String> cateGories = rssItem.getCategories();
			System.out.println("-------------categories------------------");
			for(String str : cateGories){
				System.out.print("categories :"+str);
			}
			System.out.println("-------------content------------------");
			System.out.println(rssItem.getContent());
			
			System.out.println("-------------title------------------");
			System.out.println(rssItem.getTitle());

			System.out.println("-------------description------------------");
			System.out.println(rssItem.getDescription());
			
			System.out.println("-------------link------------------");
			System.out.println(rssItem.getLink());*/
		}
	}
	
}
