package com.fly.db;

import java.util.ArrayList;
import java.util.List;

import org.mcsoxford.rss.RSSFeed;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.fly.rss.model.RssClass;
import com.fly.rss.model.RssSite;

public class RssClassDbDaoImpl extends AbDBDaoImpl<RssClass> {
	private Context mContext = null;
	public RssClassDbDaoImpl(Context context) {
		super(new RssDBHelper(context),RssClass.class);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public void addNewRss(RSSFeed feed,String className){
		startReadableDatabase();
		int id = 0;
		boolean isNew = true;
		List<RssClass> rssClasses = null;
		if(className == null){
			className = "";
		}
		//判断分类名称是否存在
		rssClasses = queryList("className = ?",new String[]{className});
		if(rssClasses != null && !rssClasses.isEmpty()){
			id = rssClasses.get(0).get_id();
			isNew = false;
		}else{//分类名称不存在则新建
			rssClasses = queryList(new String[]{"max(_id) as _id"}, null, null, null, null, null, null);
			if(rssClasses != null && !rssClasses.isEmpty()){
				id = rssClasses.get(0).get_id();
				id++;
			}
		}
		List<RssSite> rssSites = new ArrayList<RssSite>();
		RssSite rssSite = new RssSite();
		rssSite.setRssClassId(id);
		rssSite.setRssLink(feed.getLink().getHost());
		rssSite.setRssTitle(feed.getTitle());
		rssSites.add(rssSite);
		
		if(isNew){
			//插入
			RssClass rssClass = new RssClass();
			rssClass.setClassId(id);
			rssClass.setClassName(className);
			rssClass.setSites(rssSites);
			insert(rssClass);	
		}else{
			new RssSiteDbDaoImpl(mContext).addRssSite(rssSite,id);
		}
			
		
		closeDatabase();
	}
	
	
	public List<String> getRssClass(){
		List<String> classes = new ArrayList<String>();
		startReadableDatabase();
		List<RssClass> rssClasses = queryList();
		for(RssClass rssClass : rssClasses){
			classes.add(rssClass.getClassName());
		}
		closeDatabase();
		return classes;
	}
	
	
}
