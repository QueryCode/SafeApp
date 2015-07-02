package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.service.MyAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LostProtectSettingActivity extends Activity {

	private static final int MENU_CHANGE_NAME_ID = 0;
	private TextView setting_safe_number;
	private CheckBox setting_protected;
	private TextView setting_reset;
	private EditText change_name;
	private DevicePolicyManager devicePolicyManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

		setContentView(R.layout.activity_lost_protect_setting_main);
		
		devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		setting_safe_number = (TextView)findViewById(R.id.setting_safe_number);
		String safe_number = SafePreference.getStr(getApplicationContext(), Const.SAFE_NUMBER);
		setting_safe_number.setText(safe_number);
		
		setting_protected = (CheckBox)findViewById(R.id.setting_protected);
		boolean isprotected = SafePreference.getBoo(getApplicationContext(), Const.ISPROTECTED);
		if (isprotected) {
			setting_protected.setChecked(true);
			setting_protected.setText("防盗保护已经开启");
		}else {
			setting_protected.setChecked(false);
			setting_protected.setText("防盗保护没有开启");
		}
		setting_protected.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean isprotected = SafePreference.getBoo(getApplicationContext(), Const.ISPROTECTED);
				if (isprotected) {
					setting_protected.setChecked(false);
					setting_protected.setText("防盗保护没有开启");
					SafePreference.save(getApplicationContext(), Const.ISPROTECTED, false);
				}else {
					setting_protected.setChecked(true);
					setting_protected.setText("防盗保护已经开启");
					SafePreference.save(getApplicationContext(), Const.ISPROTECTED, true);
					
					activeAdmin();
				}
			}
		});
		
		setting_reset = (TextView)findViewById(R.id.setting_reset);
		setting_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					Intent intent = new Intent(getApplicationContext(), Setup1ConfigActivity.class);
					startActivity(intent);
					finish();
			}
		});
		
	}
	
	private void activeAdmin(){
		ComponentName componentName = new ComponentName(this, MyAdmin.class);
		boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
		if (!isAdminActive) {
			Intent intent = new Intent();
			intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, MENU_CHANGE_NAME_ID, 0, "修改名称");
		//getMenuInflater().inflate(R.menu.lost_protect_setting_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case MENU_CHANGE_NAME_ID:
			View view = LayoutInflater.from(this).inflate(R.layout.change_name, null);
			change_name = (EditText)view.findViewById(R.id.change_name);
			AlertDialog.Builder builder = new Builder(this)
			.setIcon(R.drawable.xiugai)
			.setTitle("修改手机防盗名称")
			.setView(view)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String name = change_name.getText().toString();
					if ("".equals(name)) {
						Toast.makeText(getApplicationContext(), "名字不能为空!", Toast.LENGTH_SHORT).show();
					}else {
						SafePreference.save(getApplicationContext(), Const.LOSTPROTECT_NAME, name);
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			builder.create().show();
			Toast.makeText(this, "进入修改页面", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
