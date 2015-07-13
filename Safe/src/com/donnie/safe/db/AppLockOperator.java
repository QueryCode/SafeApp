package com.donnie.safe.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**  
 * @Title: AppLockOperator.java
 * @Package com.donnie.safe.db
 * @Description: TODO(加锁程序数据库操作)
 * @author donnieSky
 * @date 2015年7月10日 下午2:32:45   
 * @version V1.0  
 */
public class AppLockOperator {
	
	private SQLiteOpenHelper mOpenHelper;
	
	public AppLockOperator(Context context) {
		// TODO Auto-generated constructor stub
		mOpenHelper = AppLockDBHelper.getInstance(context);
	}
	
	//增加程序锁
	public void add(String packageName){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("packageName", packageName);
			db.insert("applock", "_id", values);
			db.close();
		}
	}
	
	//删除程序锁
	public void delete(String packageName){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete("applock", "packageName = ?", new String[]{packageName});
			db.close();
		}
	}

	//判断程序是否加锁
	public boolean isLockApp(String packageName){
		boolean isLockApp = false;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("applock", null, "packageName = ?", new String[]{packageName}, null, null, null);
			if (cursor.moveToNext()) {
				isLockApp = true;
			}
			cursor.close();
			db.close();
		}
		return isLockApp;
	}
	
	//查询所有的加锁程序
	public List<String> findAll(){
		List<String> applocks = new ArrayList<String>();
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("applock", new String[]{"packageName"}, null, null, null, null, null);
			while(cursor.moveToNext()){
				String packageName = cursor.getString(0);
				applocks.add(packageName);
			}
			cursor.close();
			db.close();
		}
		return applocks;
	}
	
}
