package com.donnie.safe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.donnie.safe.R;
import com.donnie.safe.utils.TaskUtil;
import com.donnie.safe.utils.TextFormat;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**  
 *
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月16日 下午1:59:52
 */
public class ProcessWidgetService extends Service{

	private Timer timer;
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
			ComponentName provider = new ComponentName(getApplicationContext(), ProcessReceiver.class);
			RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
			int process_count = TaskUtil.getRunningAppProccessInfoSize(getApplicationContext());
			views.setTextViewText(R.id.process_count, "正在运行的软件:" + process_count);
			String process_memory = TextFormat.formatByte(TaskUtil.getAvailMemory(getApplicationContext()));
			views.setTextViewText(R.id.process_memory, "可用内存:" + process_memory);
			Intent intent = new Intent(getApplicationContext(), KillProcessReceiver.class);
			intent.setAction("com.donnie.killprocess");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, 0);
			views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
			widgetManager.updateAppWidget(provider, views);
			
		}
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//不断对widget里面的数据进行更新
		timer = new Timer();
		timer.schedule(task, 0, 1000);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		task = null;
	}

}
