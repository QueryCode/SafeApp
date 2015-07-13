package com.donnie.safe;

import com.donnie.safe.application.SafeApplication;
import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.service.AppLockService;
import com.donnie.safe.service.IService;
import com.donnie.safe.utils.MD5;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPasswordActivity extends Activity {

	private Button click_ok;
	private ImageView iv_appicon;
	private TextView tv_appname;
	private EditText et_pwd;
	private PackageManager manager;
	private MyServiceConnection conn;
	private IService ibinder;
	private String packageName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_password);
		
		click_ok = (Button)findViewById(R.id.click_ok);
		iv_appicon = (ImageView)findViewById(R.id.iv_appicon);
		tv_appname = (TextView)findViewById(R.id.tv_appname);
		et_pwd = (EditText)findViewById(R.id.et_pwd);
		manager = getPackageManager();
		SafeApplication application = (SafeApplication) getApplication();
		packageName = application.getPackname();
		try {
			ApplicationInfo applicationInfo = manager.getApplicationInfo(packageName, 0);
			Drawable icon = applicationInfo.loadIcon(manager);
			String appname = applicationInfo.loadLabel(manager).toString();
			iv_appicon.setImageDrawable(icon);
			tv_appname.setText(appname);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = new MyServiceConnection();
		Intent service = new Intent(this, AppLockService.class);
		bindService(service, conn, BIND_AUTO_CREATE);
		
		click_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pwd = et_pwd.getText().toString();
				String md5_pwd = MD5.getData(pwd);
				String old_pwd = SafePreference.getStr(getApplicationContext(), Const.SAFEPASSWORD);
				if (TextUtils.isEmpty(pwd)) {
					Toast.makeText(getApplicationContext(), "密码不能为空!", 0).show();
				}else if (old_pwd.equals(md5_pwd)) {
					ibinder.addTemp(packageName);
					finish();
				}else {
					Toast.makeText(getApplicationContext(), "密码输入不正确,请重新输入!", 0).show();
				}
			}
		});
	}
	
	private final class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ibinder = (IService)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			conn = null;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(conn);
	}
}
