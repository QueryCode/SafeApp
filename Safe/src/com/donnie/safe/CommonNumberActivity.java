package com.donnie.safe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.donnie.safe.service.CommonNumberService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;

public class CommonNumberActivity extends Activity {

	private ExpandableListView elv_common_number;
	private CommonNumberService service;
	private SimpleExpandableListAdapter adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);
		
		elv_common_number = (ExpandableListView) findViewById(R.id.elv_common_number);
		service = new CommonNumberService(this);
		try {
			//获得常用号码数据库
			File file = new File(getFilesDir(), "commonnum.db");
			if (!file.exists()) {
				FileOutputStream outputStream = new FileOutputStream(file);
				InputStream inputStream = getAssets().open("commonnum.db");
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
				}
				inputStream.close();
				outputStream.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		List<Map<String,String>> groupData = service.getGroupData();
		List<List<Map<String,String>>> childData = service.getChildData();
		adapter = new SimpleExpandableListAdapter(this, groupData, 
				android.R.layout.simple_expandable_list_item_1, 
				new String[]{"name"}, 
				new int[]{android.R.id.text1}, 
				childData, 
				android.R.layout.simple_expandable_list_item_2, 
				new String[]{"name","number"}, 
				new int[]{android.R.id.text1,android.R.id.text2});
		
		elv_common_number.setAdapter(adapter);
		
		elv_common_number.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Map<String, String> map = (Map<String, String>) adapter.getChild(groupPosition, childPosition);
				String number = map.get("number");
				
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				return false;
			}
		});
	}
}
