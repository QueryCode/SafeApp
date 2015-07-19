package com.donnie.safe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.donnie.safe.utils.MD5;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class KillVirusActivity extends Activity {

	private final static int SCANING = 0;
	private final static int FINISH_SCAN = 1;
	private Button btn_kill_virus;
	private ImageView iv_scan;
	private ScrollView scrollView;
	private ProgressBar pb;
	private LinearLayout ll_info;
	private AnimationDrawable ad;
	private List<String> allvirus;
	private List<String> existvirus;
	private File file;
	private PackageManager manager;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				View view = (View) msg.obj;
				ll_info.addView(view);
				scrollView.scrollBy(0, 120);
				break;
			case FINISH_SCAN:
				int size = (int) msg.obj;
				TextView tv = new TextView(getApplicationContext());
				if (existvirus.size()>0) {
					tv.setText("扫描了"+size+"个应用程序,发现了"+existvirus.size()+"个病毒!");
				}else {
					tv.setText("扫描了"+size+"个应用程序,发现了"+existvirus.size()+"个病毒!");
				}
				ll_info.addView(tv);
				ad.stop();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kill_virus);
		
		btn_kill_virus = (Button)findViewById(R.id.btn_kill_virus);
		iv_scan = (ImageView)findViewById(R.id.iv_scan);
		scrollView = (ScrollView)findViewById(R.id.scrollview);
		pb = (ProgressBar)findViewById(R.id.pb);
		ll_info = (LinearLayout)findViewById(R.id.ll_info);
		
		manager = getPackageManager();
		//拷贝病毒库
		try {
			file = new File(getFilesDir(), "antivirus.db");
			InputStream inputStream = getAssets().open("antivirus.db");
			FileOutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buffer)) != -1){
				outputStream.write(buffer,0,len);
			}
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allvirus = new ArrayList<String>();
		existvirus = new ArrayList<String>();
		btn_kill_virus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ad = (AnimationDrawable)iv_scan.getBackground();
				ad.start();
				
				//杀毒
				new Thread(){
					public void run() {
						Message msg = new Message();
						msg.what = SCANING;
						TextView tv = new TextView(getApplicationContext());
						tv.setText("正在扫描.....");
						msg.obj = tv;
						mHandler.sendMessage(msg);
						SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
						if (db.isOpen()) {
							//查询病毒库信息
							Cursor cursor = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
							while(cursor.moveToNext()){
								String md5 = cursor.getString(0);
								allvirus.add(md5);
							}
							cursor.close();
							db.close();
						}
						List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES|PackageManager.GET_SIGNATURES);
						pb.setMax(packageInfos.size());
						for (PackageInfo packageInfo : packageInfos) {
							String packageName = packageInfo.packageName;
							ApplicationInfo appinfo = packageInfo.applicationInfo;
							Drawable icon = appinfo.loadIcon(manager);
							CharSequence name = appinfo.loadLabel(manager).toString();
							View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.app_about_virus, null);
							ImageView iv_virus_icon = (ImageView)view.findViewById(R.id.iv_virus_icon);
							TextView tv_virus_name = (TextView)view.findViewById(R.id.tv_virus_name);
							iv_virus_icon.setImageDrawable(icon);
							tv_virus_name.setText(name);
							
							Message msgs = new Message();
							msgs.what = SCANING;
							msgs.obj = view;
							mHandler.sendMessage(msgs);
							
							Signature[] signatures = packageInfo.signatures;
							StringBuffer stringBuffer = new StringBuffer();
							for (Signature signature : signatures) {
								stringBuffer.append(signature.toCharsString());
							}
							String signature = MD5.getData(stringBuffer.toString());
							if (allvirus.contains(signature)) {
								existvirus.add(packageName);
							}
							pb.incrementProgressBy(1);
							SystemClock.sleep(100);
						}
						Message message = new Message();
						
						message.what = FINISH_SCAN;
						message.obj = packageInfos.size();
						mHandler.sendMessage(message);
					};
				}.start();
			}
		});
	}

}
