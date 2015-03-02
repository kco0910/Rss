package com.fly.rss.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fly.rss.R;


public class LoadDialog implements OnDismissListener{
	private PopupWindow mPopupWindow = null;
	private Context mContext = null;
	private AsyncTask mDelegateTask = null;
	
	public LoadDialog(Context context,String loadDescribe){
		mContext = context;
		Drawable bgDrawable = context.getResources().getDrawable(R.color.c_333333);
		mPopupWindow = new PopupWindow(200,200);
		mPopupWindow.setBackgroundDrawable(bgDrawable);
		mPopupWindow.setOnDismissListener(this);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		View view = View.inflate(context, R.layout.load_item, null);
		TextView tvLoad = (TextView)view.findViewById(R.id.tv_load);
		if(!TextUtils.isEmpty(loadDescribe)){
			tvLoad.setText(loadDescribe);
		}else{
			tvLoad.setVisibility(View.GONE);
		}
		mPopupWindow.setContentView(view);
	}
	
	
	public void show(){
		if(mPopupWindow != null && !mPopupWindow.isShowing()){
			Activity act = (Activity)mContext;
			mPopupWindow.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		}
	}
	
	public void dismiss(){
		if(mPopupWindow != null){
			mPopupWindow.dismiss();
		}
	}

	
	public void setDelegateTask(AsyncTask task){
		mDelegateTask = task;
	}
	
	
	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		if(mDelegateTask != null){
			mDelegateTask.cancel(true);
		}
	}

	
}
