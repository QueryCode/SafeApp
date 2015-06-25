package com.donnie.safe;


import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Setup2ConfigActivity extends Activity {

	private ImageView bind_sim;
	private TelephonyManager tm;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2_config);
		
		bind_sim = (ImageView)findViewById(R.id.bind_sim);
		tm = (TelephonyManager) getSystemService(Context.TELECOM_SERVICE);
		String sim_serial = SafePreference.getStr(getApplicationContext(), Const.SIM_SERIAL);
		if ("".equals(sim_serial)) {
			bind_sim.setImageResource(R.drawable.switch_off_normal);
		}else {
			bind_sim.setImageResource(R.drawable.switch_on_normal);
		}
		bind_sim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sim_serial = tm.getSimSerialNumber();
				SafePreference.save(getApplicationContext(), Const.SIM_SERIAL, sim_serial);
				bind_sim.setImageResource(R.drawable.switch_on_normal);
			}
		});
		
	}
	
	public void up(View v){
		Intent intent = new Intent(this, Setup1ConfigActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	} 
	
	public void next(View v){
		Intent intent = new Intent(this, SetupConfig3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}

}
