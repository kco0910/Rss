package com.fly.rss.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fly.rss.R;
import com.fly.rss.model.RssClass;
import com.fly.rss.model.RssSite;

public class ExpandableAdapter extends BaseExpandableListAdapter{
	private List<RssClass> rssClasses = null;
	private Context mContext = null;
	private OnClickListener mClickListener = null;
	private OnChildClickListener mClildClick = null;
	public ExpandableAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		rssClasses = new ArrayList<RssClass>();
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return rssClasses.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		int count = 0;
		List<RssSite> sites  = rssClasses.get(groupPosition).getSites();
		if(sites!= null){
			count = sites.size();
		}
		return count;
	}

	@Override
	public RssClass getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return rssClasses.get(groupPosition);
	}

	@Override
	public RssSite getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		RssSite rssSite = null;
		List<RssSite> rssSites = getGroup(groupPosition).getSites();
		if(rssSites != null && !rssSites.isEmpty())
			rssSite = rssSites.get(childPosition);
		return rssSite;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = View.inflate(mContext, android.R.layout.simple_list_item_1, null);
			convertView.setPadding(100, 0, 0, 0);
		}
		TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
		RssClass rssClass = getGroup(groupPosition);
		if(rssClass != null){
			tv.setText(rssClass.getClassName());
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild,View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.content_del_item, null);
			convertView.setPadding(100, 0, 0, 0);
		}
		RssSite rssSite = getChild(groupPosition, childPosition);
		TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
		Button ibDel = (Button)convertView.findViewById(R.id.ib_del);
		if(mClickListener != null){
			ibDel.setTag(rssSite);
			ibDel.setOnClickListener(mClickListener);
		}
		if(rssSite != null){
			tv.setText(rssSite.getRssTitle());
		}
		ibDel.setFocusable(false);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mClildClick != null){
					mClildClick.onChildClick((ExpandableListView)parent,
							null, groupPosition, childPosition, 0);
				}
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void setData(List<RssClass> classes){
		if(classes != null){
			this.rssClasses = classes;
			notifyDataSetChanged();
		}
	}
	/**
	 * 必须写在setAdapter前
	 * @param clickListener
	 */
	public void setOnContentDel(OnClickListener clickListener){
		mClickListener = clickListener;
	}
		
	public void setOnChildItemClick(OnChildClickListener childClick){
		mClildClick = childClick;
	}
	
}
