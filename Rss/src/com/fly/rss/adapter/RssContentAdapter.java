package com.fly.rss.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.mcsoxford.rss.RSSItem;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fly.rss.R;
import com.fly.rss.utils.AbViewUtil;

public class RssContentAdapter extends BaseAdapter{
	private Context mContext = null;
	private List<RSSItem> rssItems  = new ArrayList<RSSItem>();
	public RssContentAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rssItems.size();
	}

	@Override
	public RSSItem getItem(int position) {
		// TODO Auto-generated method stub
		return rssItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.rss_content_item, null);
			AbViewUtil.scaleContentView((ViewGroup)convertView);
			int minHeight = AbViewUtil.scale(mContext,mContext.getResources().getDimension(R.dimen.content_item_height));
			convertView.setMinimumHeight(minHeight);
		}
		TextView tvTitle = ViewHolder.get(convertView, R.id.tv_title);
		TextView tvContent = ViewHolder.get(convertView, R.id.tv_content);
		TextView tvTime = ViewHolder.get(convertView, R.id.tv_time);
		RSSItem rssItem  = rssItems.get(position);
		if(rssItem != null){
			tvTitle.setText(rssItem.getTitle());
			tvContent.setText(Html.fromHtml(rssItem.getDescription()));
			tvTime.setText(dateToStr(rssItem.getPubDate()));
		}
		return convertView;
	}

	public void setData(List<RSSItem> rssItems){
		if(rssItems != null){
			this.rssItems = rssItems;
		}
	}
		
	 static String dateToStr(Date date){
		if(date == null){
			return "";
		}
		Calendar curC = Calendar.getInstance();
		long curTime = curC.getTimeInMillis();
		Calendar setC = Calendar.getInstance();
		setC.setTime(date);
		String describeTime = "";
		long setTime = setC.getTimeInMillis();
		int hour = curC.get(Calendar.HOUR_OF_DAY);
		int min =  curC.get(Calendar.MINUTE);
		int sec = curC.get(Calendar.SECOND);
		long today = (hour*60*60+min*60+sec)*1000;
		long oneDay = 24*60*60*1000;
		long gapT = Math.abs(curTime-setTime);
		int curDay = curC.get(Calendar.DAY_OF_MONTH);
		int curMonth = curC.get(Calendar.MONTH);
		int setDay = setC.get(Calendar.DAY_OF_MONTH);
		int setMonth = setC.get(Calendar.MONTH);
		if(setMonth == curMonth){
			int week = curC.get(Calendar.DAY_OF_WEEK);
			long weekStartDay = curTime - (week -1)*oneDay;
			if(gapT <=today){
				describeTime = LToHM(setTime);
			}else if(gapT < today+oneDay){
				describeTime = "昨天";
			}else if(setTime>= weekStartDay && setTime <= curTime){
				int setWeek = setC.get(Calendar.DAY_OF_WEEK)-1;
				if(setWeek <0){
					setWeek = 0;
				}
				String[] weekDay = new String[] { "周日", "周一", "周二", "周三",
						"周四", "周五", "周六" };
				describeTime = weekDay[setWeek];
			}else if(setTime > curTime){
				describeTime = (setMonth+1)+"月"+setDay+"日";
			}else{
				describeTime =Math.abs(curDay - setDay)+"天前";
			}
		}else{
			describeTime =(setMonth+1)+"月"+setDay+"日";
		}
		return describeTime;
	}
	
	static String LToHM(long time){
		String str = "";
		SimpleDateFormat format = new SimpleDateFormat("HH:mm",Locale.CHINA);
		str = format.format(new Date(time));
		return str;
	}

}
