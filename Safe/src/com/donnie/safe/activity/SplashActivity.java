package com.donnie.safe.activity;

import com.donnie.safe.R;
import com.donnie.safe.biz.LoginHelper;
import com.donnie.safe.biz.VersionHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 
 * @ClassName: SplashActivity
 * @Description: TODO(app欢迎页面)
 * @author donnieSky
 * @date 2015年6月13日 下午4:28:04
 *
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		TextView tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		
		tv_splash_version.setText("版本号:"+VersionHelper.getVersion(this));
		
		LoginHelper.getInstance(this).loginConnect();
	}

}
