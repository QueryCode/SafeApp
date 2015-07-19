package com.donnie.safe.service;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**  
 *
 * @Description: TODO(内存清理widget)
 * @author donnieSky
 * @date 2015年7月16日 上午10:36:21
 */
public class ProcessReceiver extends AppWidgetProvider{
	
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		
		//启动服务
		Intent intent = new Intent(context, ProcessWidgetService.class);
		context.startService(intent);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		
		//通知服务
		Intent intent = new Intent(context, ProcessWidgetService.class);
		context.stopService(intent);
	}

}
