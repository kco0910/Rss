package com.fly.rss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		
		MyHandle mHandle = new MyHandle();
		mHandle.postDelayed(new MyRunnable(), 2*1000);
		
	}
	
	private static class MyHandle extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			
		}
	}
	
	private class MyRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if(isLogin()){
				intent.setClass(SplashActivity.this, LoginActivity.class);
			}else{
				intent.setClass(SplashActivity.this, NoLoginActivity.class);
			}
			startActivity(intent);
			finish();
		}
	}
	
	
	
	/**
	 *  是否登录
	 * @return
	 */
	private boolean isLogin(){
		return false;
	}
	
}
