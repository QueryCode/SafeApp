package com.donnie.safe.service;

import com.donnie.safe.MainActivity;
import com.donnie.safe.R;
import com.donnie.safe.TaskManagerActivity;
import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.utils.TaskUtil;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**  
 *
 * @Description: TODO(锁屏自动清理服务)
 * @author donnieSky
 * @date 2015年7月15日 下午3:04:58
 */
public class AutoClearService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Notification notification = new Notification(R.drawable.cleaner,"有通知到来",System.currentTimeMillis());
		int icon_noti = R.drawable.cleaner;
		icon_noti = notification.icon;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, TaskManagerActivity.class), 0);
		notification.setLatestEventInfo(this, "锁屏自动清理通知", "锁屏后将自动清理进程", pendingIntent);
		startForeground(1, notification);//设置为前台进程
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		LockScreenReceiver receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Boolean isAutoClear = SafePreference.getBoo(context, Const.ISAUTOCLEAR);
			if (isAutoClear) {
				TaskUtil.killAllProcess(context);
			}
		}
		
	}

}
