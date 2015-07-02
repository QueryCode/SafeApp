package com.donnie.safe.service;

import com.donnie.safe.LostProtectActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**  
 * @Title: PhoneReceiver.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月1日 下午2:13:49   
 * @version V1.0  
 */
public class PhoneReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String data = getResultData();
		if ("20150701".equals(data)) {
			setResultData(null);
			
			intent = new Intent(context, LostProtectActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

}
