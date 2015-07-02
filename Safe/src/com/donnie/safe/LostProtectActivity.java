package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.utils.MD5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LostProtectActivity extends Activity {
	
	private EditText et_frist,et_frist_agagin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_lost_protect);
		
		if (TextUtils.isEmpty(SafePreference.getStr(getApplicationContext(), Const.SAFEPASSWORD))) {
			showFirstDialog();
		}else {
			showNormalDialog();
		}
	}

	private void showNormalDialog() {
		// TODO Auto-generated method stub
		final View views = LayoutInflater.from(this).inflate(R.layout.normal_dialog, null);
		final EditText et_frist_one = (EditText)views.findViewById(R.id.et_first_one);
		
		AlertDialog.Builder builder = new Builder(this)
		.setIcon(R.drawable.safe)
		.setTitle("输入您的安全密码")
		.setView(views)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String first = et_frist_one.getText().toString();
				String pwd = MD5.getData(first);
				//String old_pwd = SafePreference.getStr(getApplicationContext(), Const.SAFEPASSWORD);
				if (pwd.equals(SafePreference.getStr(getApplicationContext(), Const.SAFEPASSWORD))) {
					loadGuideUI();
					dialog.dismiss();
					finish();
				}else {
					Toast.makeText(getApplicationContext(), "输入有误，请重新输入！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		
		builder.create().show();
	}

	private void showFirstDialog() {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(this).inflate(R.layout.first_dialog, null);
		et_frist = (EditText)view.findViewById(R.id.et_first);
		et_frist_agagin = (EditText)view.findViewById(R.id.ed_first_again);
		AlertDialog.Builder builder = new Builder(this)
		.setIcon(R.drawable.safe)
		.setTitle("设置安全密码")
		.setView(view)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override 
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String first = et_frist.getText().toString();
				String next = et_frist_agagin.getText().toString();
				if (!TextUtils.isEmpty(first) && !TextUtils.isEmpty(next) && first.equals(next)) {
					String md5_pwd = MD5.getData(first);
					SafePreference.save(getApplicationContext(), Const.SAFEPASSWORD, md5_pwd);
					dialog.dismiss();
					finish();
				}else {
					Toast.makeText(getApplicationContext(), "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		builder.create().show();
	}
	
	//进入向导界面
	private void loadGuideUI(){
		boolean isSetup = SafePreference.getBoo(getApplicationContext(), Const.ISSETUP);
		if (isSetup) {
			Intent intent = new Intent(getApplicationContext(), LostProtectSettingActivity.class);
			startActivity(intent);
		}else {
			Intent intent = new Intent(this, Setup1ConfigActivity.class);
			startActivity(intent); 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lost_protect, menu);
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
