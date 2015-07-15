package com.donnie.safe.utils;

import com.donnie.safe.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**  
 * @Title: ToastUtil.java
 * @Package com.donnie.safe.utils
 * @Description: TODO(土司工具类)
 * @author donnieSky
 * @date 2015年7月15日 下午1:28:14   
 * @version V1.0  
 */
public class ToastUtil {
	
	public static void show(Context context,String msg){
		Toast toast = new Toast(context);
		View view = View.inflate(context, R.layout.task_toast, null);
		TextView tv_msg = (TextView)view.findViewById(R.id.tv_task_clean);
		tv_msg.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

}
