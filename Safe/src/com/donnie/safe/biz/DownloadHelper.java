package com.donnie.safe.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;

/**  
 * @Title: DownloadHelper.java
 * @Package com.donnie.safe.biz
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年6月22日 下午4:29:49   
 * @version V1.0  
 */
public class DownloadHelper {

	public static File getApkFile(String url,ProgressDialog pd){
		int last = url.lastIndexOf("/");
		File file = new File(Environment.getExternalStorageDirectory(), url.substring(last)+1);
		try {
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection)u.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			if (con.getResponseCode()==200) {
				pd.setMax(con.getContentLength());
				int num = 0;
				InputStream is = con.getInputStream();
				FileOutputStream os =new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len;
				while((len=is.read(buffer))!=-1){
					os.write(buffer,0,len);
					num+=len;
					Thread.sleep(10);
					pd.setProgress(num);
				}
				os.flush();
				os.close();
				is.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return file;
	}
	
}
