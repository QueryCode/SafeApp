package com.donnie.safe.application;

import android.app.Application;

/**  
 * @Title: SafeApplication.java
 * @Package com.donnie.safe.application
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月13日 上午9:54:54   
 * @version V1.0  
 */
public class SafeApplication extends Application{
	
	private String packname;
	
	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

}
