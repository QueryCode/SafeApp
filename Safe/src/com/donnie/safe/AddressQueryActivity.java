package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.PhoneInfo;
import com.donnie.safe.service.AddressQueryService;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressQueryActivity extends Activity {

	private EditText et_number;
	private TextView place_info;
	private Button btn_sure;
	private AddressQueryService queryService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_query);
		queryService = new AddressQueryService();
		et_number = (EditText)findViewById(R.id.et_number);
		place_info = (TextView)findViewById(R.id.place_info);
		btn_sure = (Button)findViewById(R.id.btn_sure);
		
		
		btn_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = et_number.getText().toString();
				if ("".equals(number)) {
					Toast.makeText(AddressQueryActivity.this, "查询的号码不能为空!", Toast.LENGTH_SHORT).show();
				}else {
					List<PhoneInfo> list = queryService.query(number);
					for (PhoneInfo s : list) {
						place_info.setText("归属地:"+s.getCity()+"\n"+"运营商:"+s.getCardtype());
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.address_query, menu);
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
