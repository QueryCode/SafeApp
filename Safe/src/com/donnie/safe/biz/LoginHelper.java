package com.donnie.safe.biz;
import java.net.HttpURLConnection;
import java.net.URL;

import com.donnie.safe.R;
import com.donnie.safe.bean.UpdateBean;
import com.donnie.safe.utils.XmlParseUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**  
 * @Title: LoginHelper.java
 * @Package com.donnie.safe.biz
 * @Description: TODO(登录逻辑)
 * @author donnieSky
 * @date 2015年6月13日 下午4:32:55   
 * @version V1.0  
 */
public class LoginHelper {
		private static LoginHelper login;
		private Activity context;
		private final int UPDATA = 11;
		private UpdateBean bean;
		
		private LoginHelper(Activity context){
			this.context = context;
		}
		
		public static LoginHelper getInstance(Activity context){
			if (login == null) {
				login = new LoginHelper(context);
			}
			return login;
		}
		
		/**
		 * 
		 * @Title: loginConnect
		 * @Description: TODO(连接服务器) 
		 * @return void
		 */
		public void loginConnect() {
			// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						connect();
					}
				}).start();
		}
		
		protected void connect() {
			Message msg = new Message();
			try {
				URL url = new URL(context.getResources().getString(R.string.apkurl));
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setConnectTimeout(5000);
				
				if (con.getResponseCode()==200) {
					//连接成功
				bean = XmlParseUtil.getUpdateInfo(con.getInputStream());
				if (bean!=null) {
					if (bean.getVersion()==VersionHelper.getVersion(context)) {
						enterMain();
					}else {
						msg.what = UPDATA;
						handler.sendMessage(msg);
					}
				}
				} else {
					//连接失败	
					Toast.makeText(context, "连接服务器失败", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
		/**
		 * 
		 * @Title: enterMain
		 * @Description: TODO(进入主页面) 
		 * @param 
		 * @return void
		 */
		private void enterMain(){
			
		}
		
		private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case UPDATA:
						updateTipDialog();
						break;

					default:
						break;
					}
					super.handleMessage(msg);
				}
		};
		
		
		/**
		 * 
		 * @Title: updateTipDialog
		 * @Description: TODO(提示用户升级) 
		 * @param 
		 * @return void
		 */
		protected void updateTipDialog() {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("升级提示");
			builder.setMessage(bean.getDescription());
			builder.setPositiveButton("升级", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.create().show();
		}
		
		
}
