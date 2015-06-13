package com.donnie.safe.biz;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @ClassName: VersionHelper
 * @Description: TODO(版本辅助类)
 * @author donnieSky
 * @date 2015年6月13日 下午3:41:28
 *
 */
public class VersionHelper {
	
	/**
	 * 
	 * @Title: getVersion
	 * @Description: TODO(获取版本号) 
	 * @param context
	 * @return String 版本号
	 */
	public static String getVersion(Context context) {
		// TODO Auto-generated method stub
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
