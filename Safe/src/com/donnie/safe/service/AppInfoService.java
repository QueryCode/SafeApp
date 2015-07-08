package com.donnie.safe.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.donnie.safe.bean.AppInfo;

/**  
 * @Title: AppInfoService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(获取手机上所有app的信息)
 * @author donnieSky
 * @date 2015年7月7日 下午4:39:05   
 * @version V1.0  
 */
public class AppInfoService {
	
	private Context context;
	private PackageManager manager;
	
	public AppInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		manager = context.getPackageManager();
	}
	
	//获取所有应用程序信息
	public List<AppInfo> getAppInfos(){
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		List<ApplicationInfo> applicationInfos = manager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (ApplicationInfo appInfo : applicationInfos) {
			AppInfo info = new AppInfo();
			Drawable app_icon = appInfo.loadIcon(manager);
			String app_name = appInfo.loadLabel(manager).toString();
			info.setApp_name(app_name);
			info.setApp_icon(app_icon);
			try {
				PackageInfo packageInfo = manager.getPackageInfo(appInfo.packageName, 0);
				String app_version = packageInfo.versionName;
				info.setApp_version(app_version);
				info.setPackagename(appInfo.packageName);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean isUserApp = filterApp(appInfo);
			info.setUserApp(isUserApp);
			appInfos.add(info);
		}
		return appInfos;
	}
	
	//判断是否是用户程序
	public boolean filterApp(ApplicationInfo info){
		if ((info.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0) {
			return true;
		}else if ((info.flags&ApplicationInfo.FLAG_SYSTEM)==0) {
			return true;
		}
		return false;
	}

}
