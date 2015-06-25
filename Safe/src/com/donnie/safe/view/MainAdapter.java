package com.donnie.safe.view;

import com.donnie.safe.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**  
 * @Title: MainAdapter.java
 * @Package com.donnie.safe.view
 * @Description: TODO(Adapter自定义)
 * @author donnieSky
 * @date 2015年6月22日 下午7:06:41   
 * @version V1.0  
 */
public class MainAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private String[] textArray = new String[]{"手机防盗","通讯卫士","软件管理","任务管理","流量管理","手机杀毒","系统优化","高级工具","设置中心"};
	private int[] iconArray = new int[]{R.drawable.widget01,R.drawable.widget02,R.drawable.widget03,R.drawable.widget04,R.drawable.widget05,R.drawable.widget06,R.drawable.widget07,R.drawable.widget08,R.drawable.widget09};
	
	public MainAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return textArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/*LinearLayout view = new LinearLayout(context);
		view.setOrientation(LinearLayout.VERTICAL);
		view.setGravity(Gravity.CENTER);
		view.setPadding(0, 40, 0, 40);
		ImageView iv = new ImageView(context);
		iv.setImageResource(iconArray[position]);
		iv.setLayoutParams(new LinearLayout.LayoutParams(180, 180));
		view.addView(iv);
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT	, LayoutParams.WRAP_CONTENT));
		tv.setText(textArray[position]);
		tv.setTextColor(0xffC0C0C0);
		tv.setTextSize(16);
		view.addView(tv);*/
		
		View view = inflater.inflate(R.layout.main_item, null);
		
		ImageView iv_main = (ImageView)view.findViewById(R.id.iv_main);
		TextView tv_main = (TextView)view.findViewById(R.id.tv_main);
		iv_main.setImageResource(iconArray[position]);
		tv_main.setText(textArray[position]);
		
		return view;
	}

}
