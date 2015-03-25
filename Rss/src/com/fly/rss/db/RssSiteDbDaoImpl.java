package com.fly.rss.db;

import android.content.Context;

import com.ab.db.orm.AbSDSQLiteOpenHelper;
import com.ab.db.orm.dao.AbDBDaoImpl;
import com.fly.rss.model.RssSite;

public class RssSiteDbDaoImpl extends AbDBDaoImpl<RssSite> {
	
	public RssSiteDbDaoImpl(Context context){
		super(new RssDBHelper(context), RssSite.class);
		
	}
	
	/**
	 * 添加站点到数据库
	 * @param rssSite
	 * @param classId
	 */
	public void addRssSite(RssSite rssSite,int classId){
		
		startReadableDatabase();
		//不添加重复的站点
		int size = queryList("rssLink = ? and rssClassId = ?", new String[]{rssSite.getRssLink(),String.valueOf(classId)}).size();
		if(size == 0){
			insert(rssSite);
		}
		closeDatabase();
	}
	/**
	 * 删除站点
	 * @param rssSite
	 */
	public void delRssSite(RssSite rssSite){
		startReadableDatabase();
		delete(rssSite.getId());
		closeDatabase();
	}
	
	
}
