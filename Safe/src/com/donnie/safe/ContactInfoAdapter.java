package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.ContactInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**  
 * @Title: ContactInfoAdapter.java
 * @Package com.donnie.safe
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年6月24日 下午4:16:10   
 * @version V1.0  
 */
public class ContactInfoAdapter extends BaseAdapter{

	private Context context;
	private List<ContactInfo> contacts;
	private LayoutInflater inflater;
	
	public ContactInfoAdapter(Context context,List<ContactInfo> contacts) {
		// TODO Auto-generated constructor stub
		this.contacts = contacts;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.contact_item, null);
		TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
		TextView tv_number = (TextView)view.findViewById(R.id.tv_number);
		
		ContactInfo info = contacts.get(position);
		tv_name.setText(info.getName());
		tv_number.setText(info.getNumber());
		
		return view;
	}

}
