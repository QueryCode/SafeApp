package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.AppInfo;
import com.donnie.safe.db.AppLockOperator;
import com.donnie.safe.service.AppInfoService;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private AppLockOperator operator;
	private List<String> appLocks;//程序锁应用集合
	
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICATION:
				appLocks = operator.findAll();
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
		
		operator = new AppLockOperator(this);
		lv_applockmanage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ImageView iv_lock = ((ViewHolder)view.getTag()).iv_lock;
				AppInfo info = (AppInfo) mAdapter.getItem(position);
				//boolean isLockApp = operator.isLockApp(info.getPackagename());
				boolean isLockApp = appLocks.contains(info.getPackagename());
				if (isLockApp) {
					//直接操作数据库
					//operator.delete(info.getPackagename());
					Uri uri = Uri.parse("content://applock/applock");
					String where = " packageName = ?";
					String[] selectionArgs = new String[]{info.getPackagename()};
					//操作内容提供者来操作数据库
					getContentResolver().delete(uri, where, selectionArgs);
					appLocks.remove(info.getPackagename());
					iv_lock.setImageResource(R.drawable.lock_open);
				}else {
					//operator.add(info.getPackagename());
					Uri uri = Uri.parse("content://applock/applock");
					ContentValues values = new ContentValues();
					values.put("packageName", info.getPackagename());
					getContentResolver().insert(uri,values);
					//appLocks.add(info.getPackagename());
					iv_lock.setImageResource(R.drawable.lock_close);
				}
				TranslateAnimation animation = new TranslateAnimation(0, 1000, 0, 0);
				animation.setDuration(500);
				view.startAnimation(animation);
			}
		});
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
			ViewHolder holder = null;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else {
				view = mInflater.inflate(R.layout.app_lock_item, null);
				holder = new ViewHolder();
				holder.iv_appicon = (ImageView) view.findViewById(R.id.applockicon);
				holder.tv_appname = (TextView) view.findViewById(R.id.applockname);
				holder.iv_lock = (ImageView) view.findViewById(R.id.isapplock);
				view.setTag(holder);
			}
			AppInfo appInfo = appInfos.get(position);

			ImageView iv_appicon = holder.iv_appicon;
			TextView tv_appname = holder.tv_appname;
			ImageView iv_lock = holder.iv_lock;
			
			iv_appicon.setImageDrawable(appInfo.getApp_icon());
			tv_appname.setText(appInfo.getApp_name());
			
			//boolean isLockApp = operator.isLockApp(appInfo.getPackagename());
			boolean isLockApp = appLocks.contains(appInfo.getPackagename());
			if (isLockApp) {
				iv_lock.setImageResource(R.drawable.lock_close);
			}else {
				iv_lock.setImageResource(R.drawable.lock_open);
			}
			
			return view;
		}
		
	}
	
	static class ViewHolder{
		ImageView iv_appicon;
		TextView tv_appname;
		ImageView iv_lock;
	}

}
