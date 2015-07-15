package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.service.AutoClearService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class TaskManagerSettingActivity extends Activity {

	private CheckBox cb_task_choose_clean,cb_task_lock_clean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager_setting);
		
		cb_task_choose_clean = (CheckBox)findViewById(R.id.choose_clean);
		cb_task_lock_clean = (CheckBox)findViewById(R.id.lock_clean);
		
		Boolean isdisplaysystem = SafePreference.getBoo(getApplicationContext(), Const.ISDISPLAYSYSTEM);
		if (isdisplaysystem) {
			cb_task_choose_clean.setChecked(true);
			cb_task_choose_clean.setText("显示系统进程");
		}else {
			cb_task_choose_clean.setChecked(false);
			cb_task_choose_clean.setText("隐藏系统进程");
		}
		
		cb_task_choose_clean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean isdisplaysystem = SafePreference.getBoo(getApplicationContext(), Const.ISDISPLAYSYSTEM);
				if (isdisplaysystem) {
					cb_task_choose_clean.setChecked(false);
					cb_task_choose_clean.setText("隐藏系统进程");
					SafePreference.save(getApplicationContext(), Const.ISDISPLAYSYSTEM, false);
				}else {
					cb_task_choose_clean.setChecked(true);
					cb_task_choose_clean.setText("显示系统进程");
					SafePreference.save(getApplicationContext(), Const.ISDISPLAYSYSTEM, true);
				}
				setResult(200);
			}
		});
		
		Boolean isAutoClear = SafePreference.getBoo(this, Const.ISAUTOCLEAR);
		if (isAutoClear) {
			cb_task_lock_clean.setChecked(true);
			cb_task_lock_clean.setText("锁屏清理进程");
			Intent service = new Intent(getApplicationContext(), AutoClearService.class);
			startService(service);
		}else {
			cb_task_lock_clean.setChecked(false);
			cb_task_lock_clean.setText("锁屏不清理进程");
			Intent service = new Intent(getApplicationContext(), AutoClearService.class);
			stopService(service);
		}
		
		cb_task_lock_clean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean isAutoClear = SafePreference.getBoo(getApplicationContext(), Const.ISAUTOCLEAR);
				if (isAutoClear) {
					cb_task_lock_clean.setChecked(false);
					cb_task_lock_clean.setText("锁屏不清理进程");
					SafePreference.save(getApplicationContext(), Const.ISAUTOCLEAR, false);
					Intent service = new Intent(getApplicationContext(), AutoClearService.class);
					stopService(service);
				}else {
					cb_task_lock_clean.setChecked(true);
					cb_task_lock_clean.setText("锁屏清理进程");
					SafePreference.save(getApplicationContext(), Const.ISAUTOCLEAR, true);
					Intent service = new Intent(getApplicationContext(), AutoClearService.class);
					startService(service);
				}
			}
		});
	}

}
