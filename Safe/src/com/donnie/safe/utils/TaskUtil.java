package com.donnie.safe.utils;

import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.R;
import com.donnie.safe.bean.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**  
 * @Title: TaskUtil.java
 * @Package com.donnie.safe.utils
 * @Description: TODO(进程管理工具)
 * @author donnieSky
 * @date 2015年7月13日 下午3:08:28   
 * @version V1.0  
 */
public class TaskUtil {
	
	/**
	 * 
	 * @Title: getRunningAppProccessInfoSize
	 * @Description: TODO(获取当前正在运行的进程数量) 
	 * @param @param context
	 * @return int
	 */
	public static int getRunningAppProccessInfoSize(Context context){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return manager.getRunningAppProcesses().size();
	}
	
	/**
	 * 
	 * @Title: getAvailMemory
	 * @Description: TODO(获取当前系统的可用内存) 
	 * @param @param context
	 * @return String
	 */
	public static long getAvailMemory(Context context){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		manager.getMemoryInfo(outInfo);
		long availMem = outInfo.availMem;
		return availMem;
	}
	
	/**
	 * 
	 * @Title: getTaskInfos
	 * @Description: TODO(获得进程所有的信息) 
	 * @param @param context
	 * @return List<TaskInfo>
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		PackageManager packageManager = context.getPackageManager();
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfos = manager.getRunningAppProcesses();
		for (RunningAppProcessInfo info : runningAppProcessInfos) {
			TaskInfo taskInfo = new TaskInfo();
			int pid = info.pid;
			taskInfo.setPid(pid);
			String packageName = info.processName;
			taskInfo.setPackageName(packageName);
			try {
				ApplicationInfo appinfo = packageManager.getApplicationInfo(packageName, 0);
				Drawable task_icon = appinfo.loadIcon(packageManager);
				String task_name = appinfo.loadLabel(packageManager).toString();
				taskInfo.setTask_name(task_name);
				if (task_icon == null) {
					taskInfo.setTask_icon(context.getResources().getDrawable(R.drawable.ic_launcher));
				}else {
					taskInfo.setTask_icon(task_icon);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			android.os.Debug.MemoryInfo[] memoryInfos = manager.getProcessMemoryInfo(new int[]{pid});
			android.os.Debug.MemoryInfo memoryInfo = memoryInfos[0];
			long task_memory = memoryInfo.getTotalPrivateDirty();
			taskInfo.setTask_memory(task_memory);
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}

}
