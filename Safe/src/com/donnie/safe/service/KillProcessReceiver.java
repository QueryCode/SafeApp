package com.donnie.safe.service;

import com.donnie.safe.utils.TaskUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**  
 *
 * @Description: TODO(清理进程)
 * @author donnieSky
 * @date 2015年7月16日 下午2:46:39
 */
public class KillProcessReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		TaskUtil.killAllProcess(context);
	}

}
