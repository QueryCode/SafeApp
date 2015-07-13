package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.TaskInfo;
import com.donnie.safe.utils.TaskUtil;
import com.donnie.safe.utils.TextFormat;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {
	
	protected static final int SUCCESS_GET_TASKINFO = 0;
	private TextView tv_task_count,tv_task_memory;
	private ActivityManager manager;//系统任务管理器
	private List<TaskInfo> taskInfos;
	private TaskManagerAdapter mAdapter;
	private RelativeLayout rl_loading;
	private ListView lv_task_manager;
	
	private Handler mHanlder = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_TASKINFO:
				long total = TaskUtil.getAvailMemory(getApplicationContext());
				for (TaskInfo taskInfo : taskInfos) {
					total = total + taskInfo.getTask_memory()*1024;
				}
				//可用内存
				String availMem = TextFormat.formatByte(TaskUtil.getAvailMemory(getApplicationContext()));
				//总内存
				String totalMem = TextFormat.formatByte(total);
				
				tv_task_memory.setText("可用/总内存:"+availMem+"/"+totalMem);
				
				mAdapter = new TaskManagerAdapter();
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
		manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		//得到进程数量
		int size = TaskUtil.getRunningAppProccessInfoSize(this);
		tv_task_count.setText("进程数:"+size);
		
		//得到可用内存
		new Thread(){
			public void run() {
				taskInfos = TaskUtil.getTaskInfos(getApplicationContext());
				Message msg = new Message();
				msg.what = SUCCESS_GET_TASKINFO;
				mHanlder.sendMessage(msg);
			};
		}.start();
	}
	
	static class ViewHolder{
		ImageView iv_task_manager_icon;
		TextView tv_task_manager_name;
		TextView tv_task_manager_memory;
	}
	
	private final class TaskManagerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public TaskManagerAdapter() {
			// TODO Auto-generated constructor stub
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return taskInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return taskInfos.get(position);
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
			if (convertView !=null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else {
				view = mInflater.inflate(R.layout.task_manager_item, null);
				holder = new ViewHolder();
				holder.iv_task_manager_icon = (ImageView) view.findViewById(R.id.iv_task_manager_icon);
				holder.tv_task_manager_name = (TextView) view.findViewById(R.id.tv_task_manager_name);
				holder.tv_task_manager_memory = (TextView) view.findViewById(R.id.tv_task_manager_memory);
				view.setTag(holder);
			}
			TaskInfo taskInfo = taskInfos.get(position);
			if (taskInfo.getTask_icon() == null) {
				holder.iv_task_manager_icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_launcher));
			}else {
				holder.iv_task_manager_icon.setImageDrawable(taskInfo.getTask_icon());
			}
			holder.tv_task_manager_name.setText(taskInfo.getTask_name());
			holder.tv_task_manager_memory.setText("占用的内存:"+taskInfo.getTask_memory()+"KB");
			return view;
		}
		
	}

}
