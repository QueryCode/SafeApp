package com.donnie.safe;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**  
 * @Title: BlackAdapter.java
 * @Package com.donnie.safe
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月6日 下午3:45:09   
 * @version V1.0  
 */
public class BlackAdapter extends BaseAdapter{

	private Context context;
	
	private List<String> numbers;
	
	private LayoutInflater inflater;
	
	private TextView number;
	
	public BlackAdapter(Context context,List<String> numbers) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.numbers = numbers;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return numbers.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return numbers.get(position);
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
		if (convertView != null) {
			view = convertView;
		}else {
			view = inflater.inflate(R.layout.black_item, null);
		}
		number = (TextView)view.findViewById(R.id.number);
		String blacknumber = numbers.get(position);
		number.setText(blacknumber);
		return view;
	}

	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}

	
	
}
