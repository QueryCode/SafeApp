package com.donnie.safe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1ConfigActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1_config);
	}
	
	public void next(View view){
		Intent intent = new Intent(this, Setup2ConfigActivity.class);
		startActivity(intent);
		finish();
		//页面切换动画
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}

}
