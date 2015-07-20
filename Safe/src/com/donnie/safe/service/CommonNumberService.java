package com.donnie.safe.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**  
 *
 * @Description: TODO(常用号码数据库操作)
 * @author donnieSky
 * @date 2015年7月20日 上午10:52:10
 */
public class CommonNumberService {
	
	private Context context;
	
	public CommonNumberService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public List<Map<String, String>> getGroupData(){
		List<Map<String, String>> groupData = new ArrayList<Map<String,String>>();
		File file = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor cursor = db.query("classlist", new String[]{"name","idx"}, null, null, null, null, null);
			while(cursor.moveToNext()){
				Map<String, String> map = new HashMap<String, String>();
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String idx = cursor.getString(cursor.getColumnIndex("idx"));
				map.put("name", name);
				map.put("idx", idx);
				groupData.add(map);
			}
			cursor.close();
			db.close();
		}
		return groupData;
	}
	
	public List<List<Map<String, String>>> getChildData(){
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String,String>>>();
		List<Map<String, String>> parent = getGroupData();
		
		for(int i = 0;i<parent.size();i++){
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			File file = new File(context.getFilesDir(), "commonnum.db");
			SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
			if (db.isOpen()) {
				String idx = parent.get(i).get("idx");
				Cursor cursor = db.query("table"+idx, new String[]{"number","name"}, null, null, null, null, null);
				while(cursor.moveToNext()){
					Map<String, String> map = new HashMap<String, String>();
					String name = cursor.getString(cursor.getColumnIndex("name"));
					String number = cursor.getString(cursor.getColumnIndex("number"));
					map.put("name", name);
					map.put("number", number);
					list.add(map);
				}
				cursor.close();
				db.close();
			}
			childData.add(list);
		}
		return childData;
	}

}
