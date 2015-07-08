package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.AppInfo;
import com.donnie.safe.service.AppInfoService;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppLockManagerActivity extends Activity {

	protected static final int SUCCESS_GET_APPLICATION = 0;
	private ListView lv_applockmanage;
	private RelativeLayout rl_lockloading;
	private AppInfoService appInfoService;
	private List<AppInfo> appInfos;
	private AppLockManagerAdapter mAdapter;
	
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICATION:
				mAdapter = new AppLockManagerAdapter();
				lv_applockmanage.setAdapter(mAdapter);
				rl_lockloading.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock_manager);
		
		lv_applockmanage = (ListView)findViewById(R.id.applockmanage);
		rl_lockloading = (RelativeLayout)findViewById(R.id.rl_lockloading);
		appInfoService = new AppInfoService(this);
		new Thread(){
			public void run(){
				appInfos = appInfoService.getAppInfos();
				Message msg = new Message();
				msg.what = SUCCESS_GET_APPLICATION;
				mhandler.sendMessage(msg);
			}
		}.start();
	}
	
	private final class AppLockManagerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public AppLockManagerAdapter() {
			// TODO Auto-generated constructor stub
			mInflater = getLayoutInflater();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return appInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			if (convertView != null) {
				view = convertView;
			}else {
				view = mInflater.inflate(R.layout.app_lock_item, null);
			}
			AppInfo appInfo = appInfos.get(position);
			ImageView iv_appicon = (ImageView) view.findViewById(R.id.applockicon);
			TextView tv_appname = (TextView) view.findViewById(R.id.applockname);
			ImageView iv_lock = (ImageView)view.findViewById(R.id.isapplock);
			
			iv_appicon.setImageDrawable(appInfo.getApp_icon());
			tv_appname.setText(appInfo.getApp_name());
			
			return view;
		}
		
	}

}
