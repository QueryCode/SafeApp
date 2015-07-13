package com.donnie.safe.service;

import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.EnterPasswordActivity;
import com.donnie.safe.application.SafeApplication;
import com.donnie.safe.db.AppLockOperator;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

/**  
 * @Title: AppLockService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(监视后台是否有加锁的程序)
 * @author donnieSky
 * @date 2015年7月12日 下午5:02:04   
 * @version V1.0  
 */
public class AppLockService extends Service{

	private ActivityManager manager;
	private AppLockOperator operator;
	private List<String> appLocks;
	private List<String> tempAppLocks;//放置临时解锁的程序
	private MyContentObserver observer;
	private KeyguardManager keyguardManager;//手机锁屏和解锁
	private LockScreenReceiver receiver;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		operator = new AppLockOperator(this);
		appLocks = operator.findAll();
		tempAppLocks = new ArrayList<String>();
		
		//注册内容观察者
		observer = new MyContentObserver(new Handler());
		Uri uri = Uri.parse("content://applock/applock");
		getContentResolver().registerContentObserver(uri, true, observer);
		
		//注册锁屏广播接受者
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
		new Thread(){
			public void run(){
				while (true) {
					boolean isLockScreen = keyguardManager.inKeyguardRestrictedInputMode();
					if (isLockScreen) {
						SystemClock.sleep(500);
					}else {
						//得到当前正在运行的任务栈
						List<RunningTaskInfo> tasks = manager.getRunningTasks(1);
						//得到前台显示的任务栈
						RunningTaskInfo taskInfo = tasks.get(0);
						ComponentName component = taskInfo.topActivity;
						String packageName = component.getPackageName();
						//判断当前应用程序是否在程序锁里面
						if (appLocks.contains(packageName)) {
							if (!tempAppLocks.contains(packageName)) {
								SafeApplication application = (SafeApplication) getApplication();
								application.setPackname(packageName);
								Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						}
						SystemClock.sleep(500);
					}
				}	
			};
		}.start();
	}
	
	private final class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			appLocks = operator.findAll();
			tempAppLocks.clear();
		}
		
	}
	
	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			tempAppLocks.clear();
		}
		
	}
	
	private MyBinder ibinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return ibinder;
	}

	private final class MyBinder extends Binder implements IService{

		@Override
		public void addTemp(String packageName) {
			// TODO Auto-generated method stub
			addTempAppLock(packageName);
		}
		
	}
	
	private void addTempAppLock(String packageName){
		tempAppLocks.add(packageName);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消监听
		getContentResolver().unregisterContentObserver(observer);
		
		//取消锁屏广播监听
		unregisterReceiver(receiver);
	}
	
}
