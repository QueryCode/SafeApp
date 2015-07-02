package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetupConfig3Activity extends Activity {

	private EditText safe_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_config3);
		
		safe_number = (EditText)findViewById(R.id.safe_number);
		String safe_numbers = SafePreference.getStr(getApplicationContext(), Const.SAFE_NUMBER);
		if ("".equals(safe_numbers)) {
			
		}else {
			safe_number.setText(safe_numbers);
		}
	}

	public void selectContacts(View v){
		Intent intent = new Intent(this, ContactListActivity.class);
		startActivityForResult(intent, 100);
		//Toast.makeText(getApplicationContext(), "准备查看联系人", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (data!=null) {
				String number = data.getStringExtra("number");
				safe_number.setText(number);
			}
		}
	}
	
	public void up(View view){
		Intent intent = new Intent(this, Setup2ConfigActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	
	public void next(View view){
		if (safe_number.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "安全号码不能为空！", Toast.LENGTH_SHORT).show();
		}else {
			SafePreference.save(getApplicationContext(), Const.SAFE_NUMBER, safe_number.getText().toString());
			Intent intent = new Intent(this, Setup4ConfigActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
		}
	}
}
