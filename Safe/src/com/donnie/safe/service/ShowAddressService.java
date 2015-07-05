package com.donnie.safe.service;

import java.util.List;

import com.donnie.safe.R;
import com.donnie.safe.bean.PhoneInfo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**  
 * @Title: ShowAddressService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月2日 下午4:04:11   
 * @version V1.0  
 */
public class ShowAddressService extends Service{

	private TelephonyManager tm;
	private WindowManager windowManager;
	private View view;
	private LayoutInflater inflater;
	private boolean is_add = false;
	private TextView tv_number,tv_address;
	private AddressQueryService queryService;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		view = inflater.inflate(R.layout.show_address, null);
		tv_number = (TextView)view.findViewById(R.id.tv_number);
		tv_address = (TextView)view.findViewById(R.id.tv_address);
		tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		
		queryService = new AddressQueryService();
	}
	
	private final class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				Toast.makeText(getApplicationContext(), "显示归属地", Toast.LENGTH_SHORT).show();
				List<PhoneInfo> query = queryService.query(incomingNumber);
				for (PhoneInfo info : query) {
					tv_number.setText(incomingNumber);
					tv_address.setText(info.getCity());
				}
				
				final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | 
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				params.format = PixelFormat.TRANSLUCENT;
				params.type = WindowManager.LayoutParams.TYPE_TOAST;
				windowManager.addView(view, params);
				is_add = true;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (is_add) {
					windowManager.removeView(view);
				}
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
	}

}
