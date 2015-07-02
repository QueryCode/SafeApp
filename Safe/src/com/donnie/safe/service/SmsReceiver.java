package com.donnie.safe.service;

import com.donnie.safe.R;
import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;

/**  
 * @Title: SmsReceiver.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月1日 下午3:20:07   
 * @version V1.0  
 */
public class SmsReceiver extends BroadcastReceiver{

	private DevicePolicyManager devicePolicyManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Boolean isprotected = SafePreference.getBoo(context, Const.ISPROTECTED);
		if (isprotected) {
			devicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] pdus = (Object[])intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu);
				String body = smsMessage.getDisplayMessageBody();
				String safe_number = SafePreference.getStr(context, Const.SAFE_NUMBER);
				if ("#*location*#".equals(body)) {
					GPSInfoService service = GPSInfoService.getInstance(context);
					service.registerLocationChangeListener();
					String location = service.getLastLocation();
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(SafePreference.getStr(context, Const.SAFE_NUMBER), null, "your phone's location:"+location, null, null);
				}else if ("#*lockscreen*#".equals(body)) {
					devicePolicyManager.lockNow();
					devicePolicyManager.resetPassword("9392", 0);
					abortBroadcast();
				}else if ("#*alarm*#".equals(body)) {
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
					mediaPlayer.setVolume(1.0f, 1.0f);
					mediaPlayer.start();
					abortBroadcast();
				}else if (body.contains("6+1")||body.contains("cctv")) {
					abortBroadcast();
				}
			}
		}
	}

}
