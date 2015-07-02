package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.service.ShowAddressService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingActivity extends Activity {

	private CheckBox is_update;
	private CheckBox open_address;
	private Boolean is_open_address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		is_update = (CheckBox)findViewById(R.id.is_update);
		open_address = (CheckBox)findViewById(R.id.open_address);
		is_open_address = SafePreference.getBoo(this, Const.IS_OPEN_ADDRESS);
		
		if (is_open_address) {
			open_address.setChecked(true);
			Intent intent = new Intent(SettingActivity.this, ShowAddressService.class);
			startService(intent);
		}else {
			open_address.setChecked(false);
			Intent intent = new Intent(SettingActivity.this, ShowAddressService.class);
			stopService(intent);
		}
		open_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_open_address) {
					open_address.setChecked(false);
					Toast.makeText(SettingActivity.this, "归属服务已关闭", Toast.LENGTH_SHORT).show();
					SafePreference.save(SettingActivity.this, Const.IS_OPEN_ADDRESS, false);
					
					Intent intent = new Intent(SettingActivity.this, ShowAddressService.class);
					stopService(intent);
				}else {
					open_address.setChecked(true);
					Toast.makeText(SettingActivity.this, "归属服务已开启", Toast.LENGTH_SHORT).show();
					SafePreference.save(SettingActivity.this, Const.IS_OPEN_ADDRESS, true);
					Intent intent = new Intent(SettingActivity.this, ShowAddressService.class);
					startService(intent);
				}
			}
		});
		
		Boolean isupdate = SafePreference.getBoo(getApplicationContext(), Const.ISUPDATE);
		if (isupdate) {
			is_update.setChecked(true);
		}else {
			is_update.setChecked(false);
		}
		
		is_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_update.isChecked()) {
					is_update.setChecked(true);
					SafePreference.save(getApplicationContext(), Const.ISUPDATE, true);
				}else {
					is_update.setChecked(false);
					SafePreference.save(getApplicationContext(), Const.ISUPDATE, false);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
