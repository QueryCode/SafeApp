package com.donnie.safe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**  
 * @Title: MD5.java
 * @Package com.donnie.safe.utils
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年6月24日 上午10:30:25   
 * @version V1.0  
 */
public class MD5 {
	
	public static String getData(String str){
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ; i <data.length ; i++){
				String result = Integer.toHexString(data[i]&0xff);
				String temp = null;
				if (result.length() == 1) {
					temp = "0"+result;
				}else {
					temp = result;
				}
				sb.append(temp);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
