package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.AppInfo;
import com.donnie.safe.service.AppInfoService;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class AppManagerActivity extends Activity {

	protected static final int SUCCESS_GET_APPLICATION = 0;
	private PackageManager manager;
	private AppInfoService appinfoService;
	private List<AppInfo> appInfos;
	private RelativeLayout rl_loading;
	private ListView lv_appmanage;
	private AppManageAdapter mAdapter;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICATION:
				mAdapter = new AppManageAdapter(getApplicationContext(), appInfos);
				lv_appmanage.setAdapter(mAdapter);
				rl_loading.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		
		rl_loading = (RelativeLayout)findViewById(R.id.rl_loading);
		lv_appmanage = (ListView)findViewById(R.id.appmanage);
		
		appinfoService = new AppInfoService(this);
		manager = getPackageManager();
		new Thread(){
			public void run(){
				appInfos = appinfoService.getAppInfos();
				Message msg = new Message();
				msg.what = SUCCESS_GET_APPLICATION;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

}
