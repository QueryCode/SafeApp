package com.donnie.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**  
 * @Title: AppLockDBHelper.java
 * @Package com.donnie.safe.db
 * @Description: TODO(程序锁数据库)
 * @author donnieSky
 * @date 2015年7月8日 下午4:58:56   
 * @version V1.0  
 */
public class AppLockDBHelper extends SQLiteOpenHelper{

	private static SQLiteOpenHelper mInstance;
	private final static String DATABASE_NAME = "applock.db";
	
	public static  synchronized SQLiteOpenHelper  getInstance(Context context){
			if (mInstance == null) {
				mInstance = new AppLockDBHelper(context, DATABASE_NAME, null, 1);
			}
			return mInstance;
	}
	
	public AppLockDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table applock(_id integer primary key autoincrement,"
				+ "packageName text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
