package com.donnie.safe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.donnie.safe.bean.TrafficInfo;
import com.donnie.safe.service.TrafficManagerService;
import com.donnie.safe.utils.TextFormat;

import android.app.Activity;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {
	
	protected static final int SUCCESS_GET_TRAFFICINFO = 0;
	protected static final int UPDATE_DISPLAY = 1;
	private TextView tv_traffic_manager_mobile,tv_traffic_manager_wifi;
	private ListView lv_traffic_manager_content;
	private TrafficManagerService trafficManagerService;
	private List<TrafficInfo> trafficInfos;
	private List<TrafficInfo> realTrafficInfos;
	private TrafficManagerAdapter mAdapter;
	private Timer timer = null;
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = UPDATE_DISPLAY;
			mHandler.sendMessage(msg);
		}
	};
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_TRAFFICINFO:
				mAdapter = new TrafficManagerAdapter(getApplicationContext());
				lv_traffic_manager_content.setAdapter(mAdapter);
				timer = new Timer();
				timer.schedule(task, 0,2000);
				break;
			case UPDATE_DISPLAY:
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		
		tv_traffic_manager_mobile = (TextView)findViewById(R.id.tv_traffic_manager_mobile);
		tv_traffic_manager_wifi = (TextView)findViewById(R.id.tv_traffic_manager_wifi);
		lv_traffic_manager_content = (ListView)findViewById(R.id.lv_traffic_manager_content);
		
		tv_traffic_manager_mobile.setText(TextFormat.formatByte(getMobileTotal()));
		tv_traffic_manager_wifi.setText(TextFormat.formatByte(getWifiTotal()));
		
		trafficManagerService = new TrafficManagerService(this);
		new Thread(){
			public void run() {
				trafficInfos = trafficManagerService.getLauncherTrafficInfos();
				realTrafficInfos = new ArrayList<TrafficInfo>();
				for (TrafficInfo info : trafficInfos) {
					if (TrafficStats.getUidRxBytes(info.getUid()) == -1 && TrafficStats.getUidTxBytes(info.getUid()) == -1) {
						
					}else {
						realTrafficInfos.add(info);
					}
				}
				Message msg = new Message();
				msg.what = SUCCESS_GET_TRAFFICINFO;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	private long getMobileTotal(){
		long mobile_tx = TrafficStats.getMobileTxBytes();
		long mobile_rx = TrafficStats.getMobileRxBytes();
		return mobile_rx+mobile_tx;
	}
	
	private long getTotal(){
		return TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
	}
	
	private long getWifiTotal(){
		return getTotal() - getMobileTotal();
	}

	private final class TrafficManagerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public TrafficManagerAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return realTrafficInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return realTrafficInfos.get(position);
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
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}else {
				view = mInflater.inflate(R.layout.traffic_manager_item, null);
				holder = new ViewHolder();
				holder.iv_appicon = (ImageView)view.findViewById(R.id.iv_appicon);
				holder.tv_appname = (TextView)view.findViewById(R.id.tv_appname);
				holder.tv_apptraffic = (TextView)view.findViewById(R.id.tv_apptraffic);
				holder.tv_apptx = (TextView)view.findViewById(R.id.tv_apptx);
				holder.tv_apprx = (TextView)view.findViewById(R.id.tv_apprx);
				view.setTag(holder);
			}
			
			TrafficInfo trafficInfo = realTrafficInfos.get(position);
			
			holder.iv_appicon.setImageDrawable(trafficInfo.getAppicon());
			holder.tv_appname.setText(trafficInfo.getAppname());
			int uid = trafficInfo.getUid();
			long tx = TrafficStats.getUidTxBytes(uid);
			if (tx == -1) {
				tx = 0;
			}
			long rx = TrafficStats.getUidRxBytes(uid);
			if (rx == -1) {
				rx = 0;
			}
			long total = tx+rx;
			holder.tv_apptraffic.setText(TextFormat.formatByte(total));
			holder.tv_apptx.setText("上传:"+TextFormat.formatByte(tx));
			holder.tv_apprx.setText("下载:"+TextFormat.formatByte(rx));
			
			return view;
		}
		
	}
	
	static class ViewHolder{
		ImageView iv_appicon;
		TextView tv_appname,tv_apptraffic,tv_apptx,tv_apprx;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			task = null;
		}
	}
	
}
