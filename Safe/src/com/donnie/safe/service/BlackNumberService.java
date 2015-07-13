package com.donnie.safe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.donnie.safe.BlackListActivity;
import com.donnie.safe.R;
import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.db.BlackNumberOperator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**  
 * @Title: BlackNumberService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(黑名单服务)
 * @author donnieSky
 * @date 2015年7月7日 上午9:38:14   
 * @version V1.0  
 */
public class BlackNumberService extends Service{
	
	private TelephonyManager manager;
	private BlackNumberOperator operator;
	private MyPhoneStateListener listener;
	private NotificationManager notificationManager;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		operator = new BlackNumberOperator(this);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	private final class MyPhoneStateListener extends PhoneStateListener{
		
		private long starttime = 0;
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				Boolean isopen = SafePreference.getBoo(BlackNumberService.this, Const.ISBLACKNUMBER);
				if (isopen) {
					boolean isblacknumber = operator.isBlackNumber(incomingNumber);
					if (isblacknumber) {
						cancelCall(incomingNumber);
						return;
					}
				}
				starttime = System.currentTimeMillis();
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				long endtime = System.currentTimeMillis();
				//来电一声响
				if ((endtime-starttime)<3000) {
					Notification notification = new Notification(R.drawable.voice, "拦截到来电一声响", System.currentTimeMillis());
					Intent intent = new Intent(getApplicationContext(), BlackListActivity.class);
					intent.putExtra("number", incomingNumber);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					notification.setLatestEventInfo(getApplicationContext(), "来电一声响", "拦截到来电一声响", contentIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					notificationManager.notify(100,notification);
				}
				break;
			default:
				break;
			}
		}
		
	}
	
	//挂断电话
	private void cancelCall(String incomingNumber){
		try {
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, Context.TELECOM_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			iTelephony.endCall();
			
			//删除通话记录
			getContentResolver().registerContentObserver(Calls.CONTENT_URI, true, new MyContentObserver(new Handler(),incomingNumber));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final class MyContentObserver extends ContentObserver{

		private String incomingNumber;
		
		public MyContentObserver(Handler handler, String incomingNumber) {
			super(handler);
			// TODO Auto-generated constructor stub
			this.incomingNumber = incomingNumber;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			String where = Calls.NUMBER + " =?";
			String[] selectionArgs = new String[]{incomingNumber};
			getContentResolver().delete(Calls.CONTENT_URI, where, selectionArgs);
			
			//解除监听
			getContentResolver().unregisterContentObserver(this);
		}
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
	
	
}
