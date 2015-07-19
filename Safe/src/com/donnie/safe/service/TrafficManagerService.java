package com.donnie.safe.service;

import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.bean.TrafficInfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**  
 *
 * @Description: TODO(程序流量统计)
 * @author donnieSky
 * @date 2015年7月17日 下午3:52:21
 */
public class TrafficManagerService {
	
	private Context context;
	private PackageManager manager;
	
	public TrafficManagerService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		manager = context.getPackageManager();
	}
	
	public List<TrafficInfo> getLauncherTrafficInfos(){
		List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();
		//可运行的程序
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo info : resolveInfos) {
			ApplicationInfo appInfo = info.activityInfo.applicationInfo;
			Drawable appicon = appInfo.loadIcon(manager);
			String appname = appInfo.loadLabel(manager).toString();
			
			String packageName = appInfo.packageName;
			int uid = appInfo.uid;
			
			trafficInfos.add(new TrafficInfo(appicon, appname, packageName, uid));
		}
		return trafficInfos;
	}

}
