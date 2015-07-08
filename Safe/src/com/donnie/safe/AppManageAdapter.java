package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**  
 * @Title: AppManageAdapter.java
 * @Package com.donnie.safe
 * @Description: TODO(程序列表显示adapter)
 * @author donnieSky
 * @date 2015年7月7日 下午5:29:26   
 * @version V1.0  
 */
public class AppManageAdapter extends BaseAdapter{

	private Context context;
	private List<AppInfo> appInfos;
	private LayoutInflater mInflater;
	
	public void setAppInfos(List<AppInfo> appInfos) {
		this.appInfos = appInfos;
	}

	public AppManageAdapter(Context context,List<AppInfo> appInfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.appInfos = appInfos;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appInfos.get(position);
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
		if (convertView!=null) {
			view = convertView;
		}else {
			view = mInflater.inflate(R.layout.app_item, null);
		}
		
		ImageView appicon = (ImageView) view.findViewById(R.id.appicon);
		TextView appname = (TextView) view.findViewById(R.id.appname);
		TextView appversion = (TextView) view.findViewById(R.id.appversion);
		
		AppInfo appInfo = appInfos.get(position);
		appicon.setImageDrawable(appInfo.getApp_icon());
		appname.setText(appInfo.getApp_name());
		appversion.setText(appInfo.getApp_version());
		return view;
	}

}
