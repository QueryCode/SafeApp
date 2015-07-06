package com.donnie.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**  
 * @Title: MyDBHelper.java
 * @Package com.donnie.safe.db
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月6日 下午2:40:48   
 * @version V1.0  
 */
public class MyDBHelper extends SQLiteOpenHelper{
	
	private static SQLiteOpenHelper mInstance;
	
	private final static String DATABASE_NAME = "blacknumber.db";
	
	public static SQLiteOpenHelper getInstance(Context context){
		if (mInstance == null) {
			mInstance = new MyDBHelper(context, DATABASE_NAME, null, 1);
		}
		return mInstance;
	}

	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table blacknumber(_id integer primary key autoincrement,number integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
