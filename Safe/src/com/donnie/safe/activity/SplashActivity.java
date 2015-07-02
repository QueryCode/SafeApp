package com.donnie.safe.activity;

import com.donnie.safe.MainActivity;
import com.donnie.safe.R;
import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.LoginHelper;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.biz.VersionHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
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
		
		AlphaAnimation anim = new AlphaAnimation(0f, 1.0f);
		anim.setDuration(3000);
		RelativeLayout splash_bg = (RelativeLayout)findViewById(R.id.splash_bg);
		splash_bg.setAnimation(anim);
		
		/*AddressQueryService queryService = new AddressQueryService();
		
		boolean isExsit = queryService.isExist();
		if (isExsit) {
			Toast.makeText(SplashActivity.this, "数据库存在", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(SplashActivity.this, "数据库不存在", Toast.LENGTH_SHORT).show();
		}*/
		if (SafePreference.getBoo(this, Const.ISUPDATE)) {
			LoginHelper.getInstance(this).loginConnect();
		}else {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SplashActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent = new Intent(SplashActivity.this, MainActivity.class);
							SplashActivity.this.startActivity(intent);
							SplashActivity.this.finish();
						}
					});
				}
			}).start();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LoginHelper.getInstance(this).destory();
		super.onDestroy();
	}

}
