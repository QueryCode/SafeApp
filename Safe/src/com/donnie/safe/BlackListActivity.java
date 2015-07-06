package com.donnie.safe;

import java.util.List;

import com.donnie.safe.db.BlackNumberOperator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BlackListActivity extends Activity {

	private Button add_blacknumber;
	private ListView lv_blacknumber;
	private TextView empty,input_blacknumber;
	private BlackNumberOperator operator;
	private BlackAdapter adapter;
	private View view;
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_list);
		
		add_blacknumber = (Button)findViewById(R.id.tv_add_black);
		lv_blacknumber = (ListView)findViewById(R.id.lv_blacknumber);
		empty = (TextView)findViewById(R.id.empty);
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = LayoutInflater.from(BlackListActivity.this).inflate(R.layout.add_black, null);
		input_blacknumber = (TextView)view.findViewById(R.id.input_blacknumber);
		lv_blacknumber.setEmptyView(empty);
		operator = new BlackNumberOperator(this);
		List<String> all = operator.findAll();
		adapter = new BlackAdapter(this, all);
		lv_blacknumber.setAdapter(adapter);
		registerForContextMenu(lv_blacknumber);
		add_blacknumber.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewGroup parent = (ViewGroup)view.getParent();
				if (parent!=null) {
					parent.removeAllViews();
				}
				AlertDialog.Builder builder = new Builder(BlackListActivity.this)
				.setIcon(R.drawable.black)
				.setTitle("添加黑名单")
				.setView(view)
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						String number = input_blacknumber.getText().toString();
						if ("".equals(number)) {
							Toast.makeText(getApplicationContext(), "黑名单号码不能为空!", Toast.LENGTH_SHORT).show();
						}else {
							boolean isBlackNumber = operator.isBlackNumber(number);
							if (isBlackNumber) {
								Toast.makeText(getApplicationContext(), "该号码存在黑名单中!!", Toast.LENGTH_SHORT).show();
							}else {
								operator.add(number);
								Toast.makeText(getApplicationContext(), "黑名单添加成功", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								List<String> list = operator.findAll();
								adapter.setNumbers(list);
								adapter.notifyDataSetChanged();
							}
						}
						
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, 0, 0, "更新黑名单号码");
		menu.add(0,1,0,"删除黑名单号码");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = menuInfo.position;
		String number = (String)adapter.getItem(position);
		int id = item.getItemId();
		switch (id) {
		case 0:
		ViewGroup parent = (ViewGroup)view.getParent();
			if (parent!=null) {
				parent.removeAllViews();
			}
			
			input_blacknumber.setText(number);
			AlertDialog.Builder builder = new Builder(this)
			.setTitle("更新黑名单")
			.setView(view)
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String number = input_blacknumber.getText().toString();
					if ("".equals(number)) {
						Toast.makeText(getApplicationContext(), "黑名单号码不能为空!", Toast.LENGTH_SHORT).show();
					}else {
						boolean isBlackNumber = operator.isBlackNumber(number);
						if (isBlackNumber) {
							Toast.makeText(getApplicationContext(), "该号码存在黑名单中!!", Toast.LENGTH_SHORT).show();
						}else {
							int _id = operator.queryId(number);
							operator.update(_id, number);
							Toast.makeText(getApplicationContext(), "黑名单更新成功", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							List<String> list = operator.findAll();
							adapter.setNumbers(list);
							adapter.notifyDataSetChanged();
						}
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			builder.create().show();
			break;
		case 1:
			
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.black_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
