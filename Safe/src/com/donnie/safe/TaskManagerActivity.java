package com.donnie.safe;

import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.bean.TaskInfo;
import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;
import com.donnie.safe.utils.TaskUtil;
import com.donnie.safe.utils.TextFormat;
import com.donnie.safe.utils.ToastUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {
	
	protected static final int SUCCESS_GET_TASKINFO = 0;
	private static final int MENU_ALL_SELECTED_ID = 1;
	private static final int MENU_CANCEL_SELECTED_ID = 0;
	private TextView tv_task_count,tv_task_memory;
	private ActivityManager manager;//系统任务管理器
	private List<TaskInfo> taskInfos;
	private TaskManagerAdapter mAdapter;
	private RelativeLayout rl_loading;
	private ListView lv_task_manager;
	private Button btn_clear,btn_setting;
	
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	
	private long total;
	
	private Handler mHanlder = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_TASKINFO:
				total = TaskUtil.getAvailMemory(getApplicationContext());
				for (TaskInfo taskInfo : taskInfos) {
					total = total + taskInfo.getTask_memory()*1024;
				}
				//可用内存
				String availMem = TextFormat.formatByte(TaskUtil.getAvailMemory(getApplicationContext()));
				//总内存
				String totalMem = TextFormat.formatByte(total);
				
				tv_task_memory.setText("可用/总内存:"+availMem+"/"+totalMem);
				
				mAdapter = new TaskManagerAdapter();
				mAdapter.setInfos(taskInfos);
				rl_loading.setVisibility(View.GONE);
				lv_task_manager.setAdapter(mAdapter);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		
		tv_task_count = (TextView)findViewById(R.id.tv_task_count);
		tv_task_memory = (TextView)findViewById(R.id.tv_task_memery);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		lv_task_manager = (ListView) findViewById(R.id.lv_taskmanager);
		btn_clear = (Button)findViewById(R.id.btn_clear);
		btn_setting = (Button)findViewById(R.id.btn_setting);
		manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		//得到进程数量
		int size = TaskUtil.getRunningAppProccessInfoSize(this);
		tv_task_count.setText("进程数:"+size);
		
		//得到可用内存
		new Thread(){
			public void run() {
				taskInfos = TaskUtil.getTaskInfos(getApplicationContext());
				
				//用户与系统进程分离
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : taskInfos) {
					if (taskInfo.isUserTask()) {
						userTaskInfos.add(taskInfo);
					}else {
						systemTaskInfos.add(taskInfo);
					}
				}
				Message msg = new Message();
				msg.what = SUCCESS_GET_TASKINFO;
				mHanlder.sendMessage(msg);
			};
		}.start();
		
		btn_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*//存放没有杀死的进程
				List<TaskInfo> newTaskInfos = new ArrayList<TaskInfo>();
				for(TaskInfo taskInfo:taskInfos){
					if (taskInfo.isChecked()) {
						manager.killBackgroundProcesses(taskInfo.getPackageName());
					}else {
						newTaskInfos.add(taskInfo);
					}
				}
				mAdapter.setInfos(newTaskInfos);
				mAdapter.notifyDataSetChanged();*/
				
				List<TaskInfo> killTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : userTaskInfos) {
					if (taskInfo.isChecked()) {
						manager.killBackgroundProcesses(taskInfo.getPackageName());
						killTaskInfos.add(taskInfo);
					}
				}
				
				for (TaskInfo taskInfo : systemTaskInfos) {
					if (taskInfo.isChecked()) {
						manager.killBackgroundProcesses(taskInfo.getPackageName());
						killTaskInfos.add(taskInfo);
					}
				}
				long totalMemory = 0;
				for (TaskInfo taskInfo : killTaskInfos) {
					totalMemory = totalMemory + taskInfo.getTask_memory();
					if (taskInfo.isUserTask()) {
						userTaskInfos.remove(taskInfo);
					}else {
						systemTaskInfos.remove(taskInfo);
					}
				}
				
				mAdapter.notifyDataSetChanged();
				tv_task_count.setText("进程数:"+(userTaskInfos.size()+systemTaskInfos.size()));
				//可用内存
				String availMem = TextFormat.formatByte(TaskUtil.getAvailMemory(getApplicationContext()));
				//总内存
				String totalMem = TextFormat.formatByte(total);
				tv_task_memory.setText("可用/总内存:"+availMem+"/"+totalMem);
				String msg = "成功清理了"+killTaskInfos.size()+"进程,已释放"+TextFormat.formatByte(totalMemory*1024)+"内存!";
				ToastUtil.show(getApplicationContext(), msg);
			}
		});
		
		btn_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), TaskManagerSettingActivity.class);
				startActivityForResult(intent, 200);
			}
		});
		
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				CheckBox cb_task_manager_selected = ((ViewHolder)view.getTag()).cb_task_manager_selected;
				TaskInfo taskInfo = (TaskInfo) mAdapter.getItem(position);
				if (taskInfo.getPackageName().equals(getPackageName())) {
					return;
				}
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);
					cb_task_manager_selected.setChecked(false);
				}else {
					taskInfo.setChecked(true);
					cb_task_manager_selected.setChecked(true);
				}
			}
		});
	}
	
	static class ViewHolder{
		ImageView iv_task_manager_icon;
		TextView tv_task_manager_name;
		TextView tv_task_manager_memory;
		CheckBox cb_task_manager_selected;
	}
	
	private final class TaskManagerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		private List<TaskInfo> infos;
		
		public void setInfos(List<TaskInfo> infos) {
			this.infos = infos;
		}

		public TaskManagerAdapter() {
			// TODO Auto-generated constructor stub
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//return infos.size();
			Boolean isdisplaysystem = SafePreference.getBoo(getApplicationContext(), Const.ISDISPLAYSYSTEM);
			if (isdisplaysystem) {
				return userTaskInfos.size()+systemTaskInfos.size()+2;
			}else {
				return userTaskInfos.size() + 1;
			}
			
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return false;
			}else if (position == userTaskInfos.size() + 1) {
				return false;
			}
			return super.isEnabled(position);
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return null;
			}else if (position <= userTaskInfos.size()) {
				return userTaskInfos.get(position - 1);
			}else if (position == userTaskInfos.size() + 1) {
				return null;
			}else {
				return systemTaskInfos.get(position - userTaskInfos.size() - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			ViewHolder holder = null;
			
			if (position == 0) {
				TextView tv_userView = new TextView(getApplicationContext());
				tv_userView.setText("用户进程("+userTaskInfos.size()+")");
				tv_userView.setBackgroundColor(Color.GRAY);
				tv_userView.setTextSize(15);
				return tv_userView;
			}else if (position <= userTaskInfos.size()) {
				if (convertView !=null && !(convertView instanceof TextView)) {
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}else {
					view = mInflater.inflate(R.layout.task_manager_item, null);
					holder = new ViewHolder();
					holder.iv_task_manager_icon = (ImageView) view.findViewById(R.id.iv_task_manager_icon);
					holder.tv_task_manager_name = (TextView) view.findViewById(R.id.tv_task_manager_name);
					holder.tv_task_manager_memory = (TextView) view.findViewById(R.id.tv_task_manager_memory);
					holder.cb_task_manager_selected = (CheckBox)view.findViewById(R.id.cb_task_manager_selected);
					view.setTag(holder);
				}
				TaskInfo taskInfo = userTaskInfos.get(position - 1);
				holder.iv_task_manager_icon.setImageDrawable(taskInfo.getTask_icon());
				holder.tv_task_manager_name.setText(taskInfo.getTask_name());
				holder.tv_task_manager_memory.setText("占用内存:"+TextFormat.formatByte(taskInfo.getTask_memory()*1024));
				
				String packageName = taskInfo.getPackageName();
				if (packageName.equals(getPackageName())) {
					holder.cb_task_manager_selected.setVisibility(View.GONE);
				}else {
					holder.cb_task_manager_selected.setVisibility(View.VISIBLE);
				}
				
				boolean isChecked = taskInfo.isChecked();
				if (isChecked) {
					holder.cb_task_manager_selected.setChecked(true);
				}else {
					holder.cb_task_manager_selected.setChecked(false);
				}
				return view;
			}else if (position == userTaskInfos.size() + 1) {
				TextView tv_sysView = new TextView(getApplicationContext());
				tv_sysView.setText("系统进程("+systemTaskInfos.size()+")");
				tv_sysView.setBackgroundColor(Color.GRAY);
				tv_sysView.setTextSize(15);
				return tv_sysView;
			}else {
				if (convertView !=null && !(convertView instanceof TextView)) {
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}else {
					view = mInflater.inflate(R.layout.task_manager_item, null);
					holder = new ViewHolder();
					holder.iv_task_manager_icon = (ImageView) view.findViewById(R.id.iv_task_manager_icon);
					holder.tv_task_manager_name = (TextView) view.findViewById(R.id.tv_task_manager_name);
					holder.tv_task_manager_memory = (TextView) view.findViewById(R.id.tv_task_manager_memory);
					holder.cb_task_manager_selected = (CheckBox)view.findViewById(R.id.cb_task_manager_selected);
					view.setTag(holder);
				}
				TaskInfo taskInfo = systemTaskInfos.get(position - userTaskInfos.size() - 2);
				holder.iv_task_manager_icon.setImageDrawable(taskInfo.getTask_icon());
				holder.tv_task_manager_name.setText(taskInfo.getTask_name());
				holder.tv_task_manager_memory.setText("占用内存:"+TextFormat.formatByte(taskInfo.getTask_memory()*1024));
				
				String packageName = taskInfo.getPackageName();
				if (packageName.equals(getPackageName())) {
					holder.cb_task_manager_selected.setVisibility(View.GONE);
				}else {
					holder.cb_task_manager_selected.setVisibility(View.VISIBLE);
				}
				
				boolean isChecked = taskInfo.isChecked();
				if (isChecked) {
					holder.cb_task_manager_selected.setChecked(true);
				}else {
					holder.cb_task_manager_selected.setChecked(false);
				}
				return view;
			}
			
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_ALL_SELECTED_ID, 0, "全选");
		menu.add(0, MENU_CANCEL_SELECTED_ID, 0, "取消");
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		switch (itemId) {
		case MENU_ALL_SELECTED_ID:
			for(TaskInfo taskInfo:taskInfos){
				if (!taskInfo.getPackageName().equals(getPackageName())) {
					taskInfo.setChecked(true);
				}
			}
			mAdapter.notifyDataSetChanged();
			break;
		case MENU_CANCEL_SELECTED_ID:
			for(TaskInfo taskInfo:taskInfos){
				taskInfo.setChecked(false);
			}
			mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 200) {
			mAdapter.notifyDataSetChanged();
		}
	}

}
