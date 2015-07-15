package com.donnie.safe.service;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**  
 * @Title: BootCompleteReceiver.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月2日 上午10:24:03   
 * @version V1.0  
 */
public class BootCompleteReceiver extends BroadcastReceiver{

	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent service = new Intent(context, AutoClearService.class);
		context.startService(service);
		
		tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		Boolean isprotected = SafePreference.getBoo(context, Const.ISPROTECTED);
		if (isprotected) {
			String sim_serial = tm.getSimSerialNumber();
			Toast.makeText(context, "您的号码是:"+sim_serial, Toast.LENGTH_SHORT).show();
		}
	}

}
