package com.donnie.safe.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.bean.PhoneInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**  
 * @Title: AddressQueryService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月2日 下午1:51:15   
 * @version V1.0  
 */
public class AddressQueryService {
	
	public boolean isExist(){
		File file = new File(Environment.getExternalStorageDirectory(), "address.db");
		return file.exists();
	}
	
	public List<PhoneInfo> query(String number){
		List<PhoneInfo> list = new ArrayList<PhoneInfo>();
		File file = new File(Environment.getExternalStorageDirectory(), "address.db");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			String regularExpression = "^1[358]\\d{9}$";
			boolean isphone = number.matches(regularExpression);
			String prefix_number = number.substring(0,7);
			Cursor cursor = db.query("info", new String[]{"city","cardtype"}, "mobileprefix=?", new String[]{prefix_number}, null, null, null);
			if (cursor.moveToFirst()) {
				PhoneInfo info = new PhoneInfo();
				info.setCity(cursor.getString(0));
				info.setCardtype(cursor.getString(1));
				list.add(info);
			}
			cursor.close();
			db.close();
		}
		if (list.size()==0) {
			PhoneInfo info = new PhoneInfo();
			info.setCity("未知城市");
			info.setCardtype("未知号码");
			list.add(info);
		}
		return list;
	}

}
