package com.donnie.safe.utils;

import android.content.Context;

/**  
 *
 * @Description: TODO(px转化为dip)
 * @author donnieSky
 * @date 2015年7月18日 下午4:19:46
 */
public class DensityUtil {
	
	public static int px2dip(Context context,float px){
		//密度
		float density = context.getResources().getDisplayMetrics().density;
		int dip = (int)(px*density);
		return dip;
	}

}
