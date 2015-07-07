package com.donnie.safe.bean;

import android.graphics.drawable.Drawable;

/**  
 * @Title: AppInfo.java
 * @Package com.donnie.safe.bean
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月7日 下午4:40:07   
 * @version V1.0  
 */
public class AppInfo {
	
	private Drawable app_icon;
	
	private String app_name;
	
	private String app_version;
	
	private String packagename;

	public AppInfo(Drawable app_icon, String app_name, String app_version) {
		super();
		this.app_icon = app_icon;
		this.app_name = app_name;
		this.app_version = app_version;
	}

	public AppInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Drawable getApp_icon() {
		return app_icon;
	}

	public void setApp_icon(Drawable app_icon) {
		this.app_icon = app_icon;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	

}
