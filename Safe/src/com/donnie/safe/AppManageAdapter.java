package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.AppInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**  
 * @Title: AppManageAdapter.java
 * @Package com.donnie.safe
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月7日 下午5:29:26   
 * @version V1.0  
 */
public class AppManageAdapter extends BaseAdapter{

	private Context context;
	private List<AppInfo> appInfos;
	
	public AppManageAdapter(Context context,List<AppInfo> appInfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.appInfos = appInfos;
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
		return null;
	}

}
