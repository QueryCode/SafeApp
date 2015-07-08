package com.donnie.safe;

import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.bean.AppInfo;
import com.donnie.safe.service.AppInfoService;

import android.R.integer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity {

	protected static final int SUCCESS_GET_APPLICATION = 0;
	private PackageManager manager;
	private AppInfoService appinfoService;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private RelativeLayout rl_loading;
	private ListView lv_appmanage;
	private AppManageAdapter mAdapter;
	private TextView tv_title;
	private boolean isAllApp = true;
	private PopupWindow mPopupWindow;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICATION:
				if (mAdapter != null) {
					if (isAllApp) {
						mAdapter.setAppInfos(appInfos);
					}else {
						mAdapter.setAppInfos(userAppInfos);
					}
					mAdapter.notifyDataSetChanged();
					rl_loading.setVisibility(View.GONE);
				}else {
					mAdapter = new AppManageAdapter(getApplicationContext(), appInfos);
					lv_appmanage.setAdapter(mAdapter);
					rl_loading.setVisibility(View.GONE);
				}
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
		tv_title = (TextView)findViewById(R.id.tv_title);
		
		lv_appmanage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				View contentView = View.inflate(getApplicationContext(), R.layout.app_popup, null);
				LinearLayout pop_window = (LinearLayout) contentView.findViewById(R.id.pop_window);
				ScaleAnimation animation = new ScaleAnimation(0, 1.0f, 0, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(150);
				pop_window.startAnimation(animation);
				LinearLayout uninstall = (LinearLayout) contentView.findViewById(R.id.unistall_app);
				LinearLayout start_app = (LinearLayout) contentView.findViewById(R.id.start_app);
				LinearLayout share_app = (LinearLayout) contentView.findViewById(R.id.share_app);
				final AppInfo appInfo = (AppInfo) mAdapter.getItem(position);
				final String packagename = appInfo.getPackagename();
				
				uninstall.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!appInfo.isUserApp()) {
							Toast.makeText(getApplicationContext(), "抱歉!系统应用不能被卸载", 0).show();
						}else {
							if (packagename.equals(getPackageName())) {
								Toast.makeText(getApplicationContext(), "自身应用不能被卸载哟!", 0).show();
							}else {
								Intent uninstal_intent = new Intent();
								uninstal_intent.setAction(Intent.ACTION_DELETE);
								uninstal_intent.setData(Uri.parse("package:"+packagename));
//								startActivity(uninstal_intent);
								startActivityForResult(uninstal_intent, 100);
								//finish();
							}
						}
						mPopupWindow.dismiss();
					}
				});
				
				start_app.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							PackageInfo packageInfo = manager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
							ActivityInfo[] activities = packageInfo.activities;
							if (activities == null || activities.length == 0) {
								Toast.makeText(getApplicationContext(), "该应用程序不能被启动", 0).show();
							}else {
								ActivityInfo activityInfo = activities[0];
								ComponentName componentName = new ComponentName(packagename, activityInfo.name);
								Intent start_intent = new Intent();
								start_intent.setComponent(componentName);
								startActivity(start_intent);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "该应用程序不能被启动", 0).show();
						}
						mPopupWindow.dismiss();
					}
				});
				
				share_app.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent share_intent = new Intent();
						share_intent.setAction(Intent.ACTION_SEND);
						share_intent.setType("text/plain");
						share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
						share_intent.putExtra(Intent.EXTRA_TEXT, "Hello ! share with my favourite app to you,it's name is:"+appInfo.getApp_name());
						share_intent = Intent.createChooser(share_intent, "分享");
						startActivity(share_intent);
						mPopupWindow.dismiss();
					}
				});
				mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				int[] arrayOfInt = new int[2];
				view.getLocationInWindow(arrayOfInt);
				int x = arrayOfInt[0]+350;
				int y = arrayOfInt[1];
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow.setFocusable(true);
				mPopupWindow.showAtLocation(view, Gravity.LEFT|Gravity.TOP, x, y);
			}
		});
		
		tv_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isAllApp) {
					mAdapter.setAppInfos(userAppInfos);
					mAdapter.notifyDataSetChanged();
					tv_title.setText("用 户 程 序");
					isAllApp = false;
				}else {
					mAdapter.setAppInfos(appInfos);
					mAdapter.notifyDataSetChanged();
					tv_title.setText("全 部 程 序");
					isAllApp = true;
				}
			}
		});
		
		appinfoService = new AppInfoService(this);
		manager = getPackageManager();
		initData();
	}

	private void initData() {
		rl_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
				appInfos = appinfoService.getAppInfos();
				userAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					}
				}
				Message msg = new Message();
				msg.what = SUCCESS_GET_APPLICATION;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			initData();
		}
	}

}
