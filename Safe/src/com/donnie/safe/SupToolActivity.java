package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.SmsInfo;
import com.donnie.safe.service.BackupSmsService;
import com.donnie.safe.service.SmsInfoService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SupToolActivity extends Activity {
	
	private TextView tools_address_query,sms_backup,sms_restore;
	private ProgressDialog progress;
	private SmsInfoService smsinfoService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sup_tool);
		smsinfoService = new SmsInfoService(this);
		tools_address_query = (TextView)findViewById(R.id.tools_address_query);
		sms_backup = (TextView)findViewById(R.id.sms_backup);
		sms_restore = (TextView)findViewById(R.id.sms_restore);
		
		sms_restore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progress = new ProgressDialog(SupToolActivity.this);
				progress.setTitle("正在删除原来短信....");
				progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress.show();
				new Thread(){
					public void run(){
						try {
							SupToolActivity.this.getContentResolver().delete(
									Uri.parse("content://sms"), null, null);
							progress.setTitle("正在还原短信....");
							List<SmsInfo> smsInfos = smsinfoService
									.getSmsinfosFromXml();
							progress.setMax(smsInfos.size());
							for (SmsInfo smsInfo : smsInfos) {
								ContentValues values = new ContentValues();
								values.put("address", smsInfo.getAddress());
								values.put("date", smsInfo.getDate());
								values.put("type", smsInfo.getType());
								values.put("body", smsInfo.getBody());
								SupToolActivity.this.getContentResolver().insert(Uri.parse("content://sms"), values);
								progress.incrementProgressBy(1);
							}
							progress.dismiss();
							Looper.prepare();
							Toast.makeText(getApplicationContext(), "短信还原成功", 0).show();
							Looper.loop();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							progress.dismiss();
							Looper.prepare();
							Toast.makeText(getApplicationContext(), "短信还原失败", 0).show();
							Looper.loop();
						}
					};
				}.start();
			}
		});
		
		sms_backup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), BackupSmsService.class);
				startService(intent);
			}
		});
		tools_address_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SupToolActivity.this, AddressQueryActivity.class);
				startActivity(intent);
			}
		});
	}
}
