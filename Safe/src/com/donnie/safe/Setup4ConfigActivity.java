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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class Setup4ConfigActivity extends Activity {
	
	private CheckBox start_protect;
	private DevicePolicyManager devicePolicyManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
		setContentView(R.layout.activity_setup4_config);
		
		start_protect = (CheckBox) findViewById(R.id.start_protect);
		
		Boolean isprotected = SafePreference.getBoo(getApplicationContext(), Const.ISPROTECTED);
		if (isprotected) {
			start_protect.setChecked(true);
			start_protect.setText("防盗保护已经开启");
		}else {
			start_protect.setChecked(false);
			start_protect.setText("防盗保护没有开启");
		}
		
		start_protect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				switch (id) {
				case R.id.start_protect:
					Boolean isprotected = SafePreference.getBoo(getApplicationContext(), Const.ISPROTECTED);
					if (isprotected) {
						start_protect.setChecked(false);
						start_protect.setText("防盗保护没有开启");
						SafePreference.save(getApplicationContext(), Const.ISPROTECTED, false);
					}else {
						start_protect.setChecked(true);
						start_protect.setText("防盗保护已经开启");
						SafePreference.save(getApplicationContext(), Const.ISPROTECTED, true);

						activeAdmin();
					}
					break;

				default:
					break;
				}
			}
	
		});
	}
	private void activeAdmin() {
		ComponentName componentName = new ComponentName(Setup4ConfigActivity.this, MyAdmin.class);
		boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
		if (!isAdminActive) {
			Intent intent = new Intent();
			intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
		}
	}

	public void up(View v){
		Intent intent = new Intent(this, SetupConfig3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	
	public void next(View v){
		boolean isprotected = SafePreference.getBoo(getApplicationContext(), Const.ISPROTECTED);
		if (isprotected) {
			SafePreference.save(getApplicationContext(), Const.ISSETUP, true);
			finish();
			overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
		}else {
			AlertDialog.Builder builder = new Builder(this)
			.setIcon(R.drawable.baohu)
			.setTitle("强烈建议")
			.setMessage("手机防盗极大的保护了手机的安全,\n请勾选开启防盗!")
			.setPositiveButton("开启", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					start_protect.setChecked(true);
					start_protect.setText("防盗保护已经开启");
					SafePreference.save(getApplicationContext(), Const.ISPROTECTED, true);
					SafePreference.save(getApplicationContext(), Const.ISSETUP, true);
					finish();
				}
			}).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SafePreference.save(getApplicationContext(), Const.ISSETUP, true);
					finish();
				}
			});
			builder.create().show();
		}
	}
}
