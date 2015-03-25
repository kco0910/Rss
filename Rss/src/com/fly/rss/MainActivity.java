package com.fly.rss;

import java.util.List;

import org.mcsoxford.rss.RSSException;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.ResponseDeliver;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.ab.fragment.AbAlertDialogFragment;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbToastUtil;
import com.fly.rss.adapter.ExpandableAdapter;
import com.fly.rss.adapter.RssRecleContentAdapter;
import com.fly.rss.db.RssClassDbDaoImpl;
import com.fly.rss.db.RssSiteDbDaoImpl;
import com.fly.rss.dialog.LoadDialog;
import com.fly.rss.interfaces.IOnItemClickListener;
import com.fly.rss.model.RssClass;
import com.fly.rss.model.RssConstant;
import com.fly.rss.model.RssSite;
import com.fly.rss.utils.RssUtil;
import com.fly.rss.utils.SharedPreferencesUtil;
import com.fly.rss.widget.EditDropSelect;
import com.mining.app.zxing.view.MipcaActivityCapture;

public class MainActivity extends ActionBarActivity implements OnRefreshListener,IOnItemClickListener,
		OnChildClickListener{
	private SwipeRefreshLayout refreshLayout = null;
	//private RssContentAdapter contentAdapter = null;
	private RssRecleContentAdapter recleContentAdapter = null;
	private GetRssContentTask mGetRssContentTask = null;
	private RssClassDbDaoImpl mRssClassDbDaoImpl = null;
	private ExpandableAdapter expandableContentAdapter = null;
	private DrawerLayout mDrawerLayout = null;
	/** 加载框*/
	private LoadDialog mLoadDialog = null;
	/** 是否第一次显示*/
	private boolean isFirstShow = true;
	/** 二维码扫描的请求码*/
	private static final int REQUEST_CODE = 0X000001;
	/** 上一次按返回键的时间*/
	private long preBackTime = 0L;
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
		RssUtil.setStateBarColor(this,getResources().getColor(R.color.c_status_bar));
		initUI();
		
	}
	
	private void initUI(){
		Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
		mToolbar.setTitle("");//R.string.app_name
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		//mToolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);
		/**必须写在set之后 */
		ActionBar actionBar = getSupportActionBar();
		/* 和Toolbar一样
		actionBar.setTitle("主标题");
		actionBar.setSubtitle("副标题");
		actionBar.setLogo(R.drawable.icon); */
		actionBar.setDisplayHomeAsUpEnabled(true);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.open, R.string.close);
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
		refreshLayout.setOnRefreshListener(this);
		/*refreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,  
                android.R.color.holo_green_light,  
                android.R.color.holo_orange_light,  
                android.R.color.holo_red_light);*/
		RecyclerView listview = (RecyclerView)findViewById(R.id.recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		listview.setLayoutManager(layoutManager);
		recleContentAdapter = new RssRecleContentAdapter(this);
		recleContentAdapter.setOnItemClickListener(this);
		listview.setAdapter(recleContentAdapter);
		
		//侧边栏
		ExpandableListView expRss = (ExpandableListView)findViewById(R.id.content_list);
		expandableContentAdapter = new ExpandableAdapter(this);
		expandableContentAdapter.setOnContentDel(ContentDelClick);
		expandableContentAdapter.setOnChildItemClick(this);
		expRss.setAdapter(expandableContentAdapter);
		getDBContent();
	}
	
	/** 删除操作*/
	OnClickListener ContentDelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			RssSite rssSite = (RssSite)v.getTag();
			if(rssSite != null){
				System.out.println("id :"+rssSite.getId());
				delDialog(rssSite);
			}
		}
	};
	
	private void delDialog(final RssSite rssSite){
		new AlertDialog.Builder(this).setTitle(R.string.del_confirm).setMessage(R.string.ask_del_content)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				RssSiteDbDaoImpl  rssSiteDb = new RssSiteDbDaoImpl(MainActivity.this);
				rssSiteDb.delRssSite(rssSite);
				getDBContent();
			}
		})
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}
	
	
	/**
	 * 从数据库中获取分类信息
	 */
	private void getDBContent(){
		RssClassDbDaoImpl dbDaoImpl = getRssClassDbDaoImpl();
		List<RssClass>  rssClasses = dbDaoImpl.getRssClasses();
		expandableContentAdapter.setData(rssClasses);
		
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
	        	String url = "http://coolshell.cn/feed";
	        	addRss(url);
	            break;  
	        case R.id.barcode:  
	        	Intent intent = new Intent(MainActivity.this, MipcaActivityCapture.class);
	        	startActivityForResult(intent, REQUEST_CODE);
	            break;  
	        default:  
	            break;  
	     }  
	     return true; 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
			String str  = data.getStringExtra(MipcaActivityCapture.RESULT_STR);
			System.out.println("二维码扫描到的结果:"+str);
			if(!TextUtils.isEmpty(str)){
				addRss(str);
			}
		}
	}
	
	/**
	 * 添加RSS
	 * @param url
	 */
	private void addRss(String url){
		View view = View.inflate(this, R.layout.add_content, null);
		final EditText etRssLink = (EditText)view.findViewById(R.id.et_rss_link);
		etRssLink.setHint(R.string.hint_rss_link);
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
				mLoadDialog = new LoadDialog(MainActivity.this, getString(R.string.load_data));
				mLoadDialog.setDelegateTask(this);
			}
			mLoadDialog.show();
		}
		
		@Override
		protected RSSFeed doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			System.out.println("访问的链接:"+url);
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
					recleContentAdapter.setData(feed.getItems());
					recleContentAdapter.notifyDataSetChanged();
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
			String rssTitle = feed.getTitle();
			TextView tvRssTitle = (TextView)view.findViewById(R.id.tv_rss_title);
			if(!TextUtils.isEmpty(rssTitle)){
				tvRssTitle.setText(rssTitle);
			}else{
				Toast.makeText(this, R.string.toast_rss_no_full, Toast.LENGTH_LONG).show();
				return;
			}
			AbAlertDialogFragment.newInstance(0, getString(R.string.save_rss_classes), null, view, new AbDialogOnClickListener() {
				@Override
				public void onPositiveClick() {
					// TODO Auto-generated method stub
					String className = editDropSelect.getEditTextStr();
					if(!TextUtils.isEmpty(className)){
						RssClassDbDaoImpl rssClassDbDaoImpl = getRssClassDbDaoImpl();
						rssClassDbDaoImpl.addNewRss(feed, className);
						//记录打开的Rss
						SharedPreferencesUtil.putString(MainActivity.this, SharedPreferencesUtil.LAST_READ_RSS_link
								, feed.getRssLink());
						//显示Rss内容
						recleContentAdapter.setData(feed.getItems());
						recleContentAdapter.notifyDataSetChanged();
						getDBContent();
					}else{
						AbToastUtil.showToast(MainActivity.this, R.string.class_name_not_null);
					}
				}
				
				@Override
				public void onNegativeClick() {
					// TODO Auto-generated method stub
					
				}
			}).show(getFragmentManager(), "saveRssClass");
		}else{
			Toast.makeText(this, R.string.toast_rss_no_full, Toast.LENGTH_LONG).show();
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
	public void onItemClick(int position) {
		// TODO Auto-generated method stub
		RSSItem rssItem = recleContentAdapter.getData().get(position);
		if(rssItem != null){
			Intent intent = new Intent(this, RssDetailActivity.class);
			intent.putExtra(RssDetailActivity.RSS_CONTENT, rssItem.getContent());
			intent.putExtra(RssDetailActivity.RSS_TITLE, rssItem.getTitle());
			startActivity(intent);
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, 
			int childPosition, long id) {
		// TODO Auto-generated method stub
		if(expandableContentAdapter == null) return false;
		RssSite rssSite = expandableContentAdapter.getChild(groupPosition, childPosition);
		if(rssSite != null){
			String link = rssSite.getRssLink();
			if(!TextUtils.isEmpty(link)){
				mDrawerLayout.closeDrawers();
				SharedPreferencesUtil.putString(this, SharedPreferencesUtil.LAST_READ_RSS_link, link);
				mGetRssContentTask = new GetRssContentTask(false);
				mGetRssContentTask.execute(link);
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long curTime = System.currentTimeMillis();
		if(curTime - preBackTime >2000){
			Toast.makeText(this, R.string.again_back_exits, Toast.LENGTH_SHORT).show();
			preBackTime = curTime;
		}else{
			super.onBackPressed();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
