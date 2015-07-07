package com.donnie.safe.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import com.donnie.safe.bean.SmsInfo;

/**  
 * @Title: SmsInfoService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(短信获取方法)
 * @author donnieSky
 * @date 2015年7月7日 上午10:35:12   
 * @version V1.0  
 */
public class SmsInfoService {
	
	private Context context;
	
	public SmsInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	//得到所有短信
	public List<SmsInfo> getSmsInfos(){
		List<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
		Uri uri = Uri.parse("content://sms");
		Cursor cursor = context.getContentResolver().query(uri, new String[]{"address","date","type","body"}, null, null, null);
		while(cursor.moveToNext()){
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			String body = cursor.getString(cursor.getColumnIndex("body"));
			
			SmsInfo smsInfo = new SmsInfo(address, date, type, body);

		}
		
		return smsInfos;
	}
	
	//把短信数据写入到xml文件中
	public void createXml(List<SmsInfo> smsInfos) throws Exception{
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(Environment.getExternalStorageDirectory(), "smsbackup.xml");
		OutputStream os = new FileOutputStream(file);
		serializer.setOutput(os,"UTF-8");
		
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "smsinfos");
		for (SmsInfo smsInfo : smsInfos) {
			serializer.startTag(null, "smsinfo");
			
			serializer.startTag(null, "address");
			serializer.text(smsInfo.getAddress());
			serializer.endTag(null, "address");
			
			serializer.startTag(null, "date");
			serializer.text(smsInfo.getDate());
			serializer.endTag(null, "date");
			
			serializer.startTag(null, "type");
			serializer.text(smsInfo.getType());
			serializer.endTag(null, "type");
			
			serializer.startTag(null, "body");
			serializer.text(smsInfo.getBody());
			serializer.endTag(null, "body");
			
			serializer.endTag(null, "smsinfo");
		}
		serializer.endTag(null, "smsinfos");
		serializer.endDocument();
		os.close();
	}
	
	//解析备份的短信xml文件
	public List<SmsInfo> getSmsinfosFromXml() throws Exception{
		List<SmsInfo> smsInfos = null;
		SmsInfo smsInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		File file = new File(Environment.getExternalStorageDirectory(), "smsbackup.xml");
		FileInputStream inputStream = new FileInputStream(file);
		parser.setInput(inputStream,"UTF-8");
		int eventType = parser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("smsinfos".equals(parser.getName())) {
					smsInfos = new ArrayList<SmsInfo>();
				}else if ("smsinfo".equals(parser.getName())) {
					smsInfo = new SmsInfo();
				}else if ("address".equals(parser.getName())) {
					String address = parser.nextText();
					smsInfo.setAddress(address);
				}else if ("date".equals(parser.getName())) {
					String date = parser.nextText();
					smsInfo.setDate(date);
				}else if ("type".equals(parser.getName())) {
					String type = parser.nextText();
					smsInfo.setType(type);
				}else if ("body".equals(parser.getName())) {
					String body = parser.nextText();
					smsInfo.setBody(body);
				}
				break;
			case XmlPullParser.END_TAG:
				if ("smsinfo".equals(parser.getName())) {
					smsInfos.add(smsInfo);
					smsInfo = null;
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		
		return smsInfos;
	}

}
