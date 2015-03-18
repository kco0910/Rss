package com.fly.rss.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.mcsoxford.rss.RSSItem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fly.rss.R;
import com.fly.rss.interfaces.IOnItemClickListener;

public class RssRecleContentAdapter extends RecyclerView.Adapter<RssRecleContentAdapter.ViewHolder>  implements OnClickListener{
	private Context mContext = null;
	private List<RSSItem> rssItems  = null;
	private IOnItemClickListener mLis = null;
	public RssRecleContentAdapter(Context context) {
		// TODO Auto-generated constructor stub
		super();
		mContext = context;
		rssItems = new ArrayList<RSSItem>();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder{
		TextView tvTitle,tvContent,tvTime;
		public ViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			if(itemView != null){
				tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
				tvContent = (TextView)itemView.findViewById(R.id.tv_content);
				tvTime = (TextView)itemView.findViewById(R.id.tv_time);
			}
		}
		
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return rssItems.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int i) {
		// TODO Auto-generated method stub
		RSSItem rssItem = rssItems.get(i);
		if(rssItem != null){
			viewHolder.tvTitle.setText(rssItem.getTitle());
			viewHolder.tvContent.setText(Html.fromHtml(rssItem.getDescription()));
			viewHolder.tvTime.setText(dateToStr(rssItem.getPubDate()));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		// TODO Auto-generated method stub
		View convertView = View.inflate(mContext, R.layout.rss_content_item, null);
		convertView.setTag(i);
		convertView.setOnClickListener(this);
		ViewHolder holder = new ViewHolder(convertView);
		return holder;
	}

	public void setData(List<RSSItem> rssItems){
		if(rssItems != null){
			this.rssItems = rssItems;
		}
	}
	
	public List<RSSItem> getData(){
		return rssItems;
	}
	
	public void setOnItemClickListener(IOnItemClickListener lis){
		mLis = lis;
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int p = (Integer)v.getTag();
		if(mLis != null){
			mLis.onItemClick(p);
		}
	}
}


