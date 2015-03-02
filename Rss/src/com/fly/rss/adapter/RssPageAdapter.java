package com.fly.rss.adapter;

import java.util.ArrayList;
import java.util.List;

import org.mcsoxford.rss.RSSItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fly.rss.fragment.RssFragment;

public class RssPageAdapter extends FragmentPagerAdapter {
	/** 单页可以显示的行*/
	private int mPageCount = 0;
	private List<RSSItem> mRssItems = null;
	public RssPageAdapter(FragmentManager fm,int pageCount,List<RSSItem> rssItems) {
		super(fm);
		// TODO Auto-generated constructor stub
		mPageCount = pageCount;
		mRssItems = rssItems;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		int size = mRssItems.size();
		int start = position*mPageCount;
		int end = (position +1)*mPageCount;
		List<RSSItem> childRss = new ArrayList<RSSItem>();
		if(end <= size ){
			childRss.addAll(mRssItems.subList(start, end));
		}
		return new RssFragment(childRss);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		int size = mRssItems.size();
		count = size / mPageCount;
		if(size % mPageCount >0){
			count++;
		}
		return count;
	}

	
	
}
