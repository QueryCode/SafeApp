package com.donnie.safe.biz;

import android.content.Context;
import android.content.SharedPreferences;

/**  
 * @Title: SafePreference.java
 * @Package com.donnie.safe.biz
 * @Description: TODO(SharePreference的存取)
 * @author donnieSky
 * @date 2015年6月23日 下午3:19:46   
 * @version V1.0  
 */
public class SafePreference {
	
	public static void save(Context context,String key,Object value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Const.PREFERENCE_NAME, 0);
		//判断数据存储类型
		if (value instanceof String) {
			sharedPreferences.edit().putString(key, (String)value).commit();
		}else if (value instanceof Boolean) {
			sharedPreferences.edit().putBoolean(key, (Boolean)value).commit();
		}
	}
	
	public static String getStr(Context context,String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Const.PREFERENCE_NAME, 0);
		return sharedPreferences.getString(key, "");
	}
	
	public static Boolean getBoo(Context context,String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Const.PREFERENCE_NAME, 0);
		return sharedPreferences.getBoolean(key, false);
	}

}
