package com.donnie.safe.db;

import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.R.id;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**  
 * @Title: BlackNumberOperator.java
 * @Package com.donnie.safe.db
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月6日 下午2:48:22   
 * @version V1.0  
 */
public class BlackNumberOperator {
	
	private SQLiteOpenHelper mOpenHelper;
	
	public BlackNumberOperator(Context context) {
		// TODO Auto-generated constructor stub
		mOpenHelper = MyDBHelper.getInstance(context);
	}
	
	//添加黑名单
	public void add(String number){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.insert("blacknumber", "_id", values);
			db.close();
		}
	}
	
	//判断号码是否是黑名单
	public boolean isBlackNumber(String number){
		boolean isExist = false;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
			if (cursor.moveToFirst()) {
				isExist = true;
			}
			cursor.close();
			db.close();
		}
		return isExist;
	}
	
	//删除黑名单
	public void delete(String number){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete("blacknumber"," number = ? ", new String[]{number});
			db.close();
		}
	}
	
	//更新黑名单
	public void update(int id,String number){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.update("blacknumber", values, "_id = ?", new String[]{id+""});
			db.close();
		}
	}
	
	//查询所有的黑名单
	public List<String> findAll(){
		List<String> numbers = new ArrayList<String>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("blacknumber", new String[]{"number"}, null, null, null, null, null);
			while(cursor.moveToNext()){
				String number = cursor.getString(0);
				numbers.add(number);
			}
			cursor.close();
			db.close();
		}
		return numbers;
	}
	
	public int queryId(String number){
		int _id = 0;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.query("blacknumber", new String[]{"_id"}, "number=?", new String[]{number}, null, null, null);
			if (cursor.moveToFirst()) {
				_id = cursor.getInt(0);
			}
			cursor.close();
			db.close();
	}
	return _id;	
	}

}
