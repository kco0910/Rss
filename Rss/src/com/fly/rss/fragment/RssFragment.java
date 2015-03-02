package com.fly.rss.fragment;

import java.util.List;

import org.mcsoxford.rss.RSSItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fly.rss.R;
import com.fly.rss.adapter.RssContentAdapter;

public class RssFragment extends Fragment implements OnItemClickListener{
	private List<RSSItem> mRssItems = null;
	
	public RssFragment(List<RSSItem> rssItems){
		mRssItems = rssItems;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.frg_rss_content, container,false);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		Context context = getActivity();
		if(view != null && context != null){
			ListView listView  = (ListView)view.findViewById(R.id.listView);
			RssContentAdapter rssContentAdapter = new RssContentAdapter(getActivity());
			rssContentAdapter.setData(mRssItems);
			listView.setAdapter(rssContentAdapter);
			listView.setOnItemClickListener(this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		RSSItem rssItem = (RSSItem)parent.getItemAtPosition(position);
		if(rssItem != null){
			System.out.println("跳转到rss的详情页");
			List<String> cateGories = rssItem.getCategories();
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
			System.out.println(rssItem.getLink());
		}
	}
}
