package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class SettingActivity extends Activity {

	private CheckBox is_update;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		is_update = (CheckBox)findViewById(R.id.is_update);
		
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
