package com.donnie.safe.utils;

import java.text.DecimalFormat;

/**  
 * @Title: TextFormat.java
 * @Package com.donnie.safe.utils
 * @Description: TODO(值转化)
 * @author donnieSky
 * @date 2015年7月13日 下午2:57:13   
 * @version V1.0  
 */
public class TextFormat {
	
	public static String formatByte(long data){
		DecimalFormat format = new DecimalFormat("##.##");
		if (data < 1024) {
			return data + "bytes";
		}else if (data < (1024*1024)) {
			return format.format(data/1024f) + "KB";
		}else if (data < (1024*1024*1024)) {
			return format.format(data/1024f/1024f) + "MB";
		}else if (data < (1024*1024*1024*1024)) {
			return format.format(data/1024f/1024f/1024f/1024f) + "GB";
		}else {
			return "data too big!";
		}
	}

}
