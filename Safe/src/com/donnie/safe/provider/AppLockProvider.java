package com.donnie.safe.provider;

import com.donnie.safe.db.AppLockDBHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**  
 * @Title: AppLockProvider.java
 * @Package com.donnie.safe.provider
 * @Description: TODO(加锁程序内容提供者)
 * @author donnieSky
 * @date 2015年7月13日 上午11:07:22   
 * @version V1.0  
 */
public class AppLockProvider extends ContentProvider{

	private SQLiteOpenHelper mOpenHelper;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private final static String AUTHORITY = "applock";
	private final static int APPLOCK = 10;
	static{
		matcher.addURI(AUTHORITY, "applock", APPLOCK);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = AppLockDBHelper.getInstance(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int code = matcher.match(uri);
		switch (code) {
		case APPLOCK:
			db.insert("applock", "_id", values);
			getContext().getContentResolver().notifyChange(uri, null);
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int code = matcher.match(uri);
		switch (code) {
		case APPLOCK:
			db.delete("applock", selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
			break;

		default:
			break;
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
