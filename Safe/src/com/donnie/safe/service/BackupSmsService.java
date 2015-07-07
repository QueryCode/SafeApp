package com.donnie.safe.service;

import java.util.List;

import com.donnie.safe.MainActivity;
import com.donnie.safe.R;
import com.donnie.safe.bean.SmsInfo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

/**  
 * @Title: BackupSmsService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(短信备份后台服务)
 * @author donnieSky
 * @date 2015年7月7日 上午10:32:05   
 * @version V1.0  
 */
public class BackupSmsService extends Service{

	private SmsInfoService smsInfoService;
	private NotificationManager manager;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		smsInfoService = new SmsInfoService(this);
		manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				List<SmsInfo> smsInfos = smsInfoService.getSmsInfos();
				
				try {
					smsInfoService.createXml(smsInfos);
					Notification notification = new Notification(R.drawable.backup, "短信备份完毕", System.currentTimeMillis());
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, 0);
					notification.setLatestEventInfo(getApplicationContext(), "提示信息", "短信备份完毕", contentIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					manager.notify(100, notification);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Looper.prepare();//从消息队列里抽取消息,交给handler处理
					Toast.makeText(getApplicationContext(), "短信备份失败", 0).show();
					Looper.loop();
				}
				//停止服务
				stopSelf();
			}
		}.start();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
