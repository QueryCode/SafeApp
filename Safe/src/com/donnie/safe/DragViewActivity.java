package com.donnie.safe;

import com.donnie.safe.biz.Const;
import com.donnie.safe.biz.SafePreference;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DragViewActivity extends Activity {
	
	private RelativeLayout rl_change_location;
	private TextView tv_info;
	private int initX,initY;//归属地初始位置
	private int widthPixels,heightPixels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		widthPixels = metrics.widthPixels;
		heightPixels = metrics.heightPixels;
		
		initX = widthPixels/2;
		initY = heightPixels/2;
		tv_info = (TextView)findViewById(R.id.tv_info);
		rl_change_location = (RelativeLayout)findViewById(R.id.rl_change_location);
		
		rl_change_location.setOnTouchListener(new OnTouchListener() {
			
				private int startX,startY;
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						startX = (int)event.getRawX();
						startY = (int)event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						int dx = (int) (event.getRawX()-startX);
						int dy = (int) (event.getRawY()-startY);
						
						int l = rl_change_location.getLeft()+dx;
						int t = rl_change_location.getTop()+dy;
						int r = rl_change_location.getRight()+dx;
						int b = rl_change_location.getBottom()+dy;
						if(l<16||r>widthPixels+16||t<16||b>heightPixels+16){
							//rl_change_location.layout(l, t, r, b);
						}else {
							rl_change_location.layout(l, t, r, b);
						}
						
						int centerY = (rl_change_location.getTop()+rl_change_location.getBottom())/2;
						if (centerY>initY) {
							tv_info.layout(tv_info.getLeft(), 36, tv_info.getRight(), tv_info.getHeight()+36);
						}else {
							tv_info.layout(tv_info.getLeft(), heightPixels-tv_info.getHeight()-36, tv_info.getRight(), heightPixels-36);
						}
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						int endX = (rl_change_location.getLeft()+rl_change_location.getRight())/2;
						int endY = (rl_change_location.getTop()+rl_change_location.getBottom())/2;
						
						int x = endX - initX;
						int y = endY - initY;
						SafePreference.save(DragViewActivity.this, Const.X, x);
						SafePreference.save(DragViewActivity.this, Const.Y, y);
						break;

					default:
						break;
					}
					return true;
			}
		});	
	}
}
