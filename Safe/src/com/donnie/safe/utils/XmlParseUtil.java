package com.donnie.safe.utils;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.donnie.safe.bean.UpdateBean;

/**  
 * @Title: XmlParseUtil.java
 * @Package com.donnie.safe.utils
 * @Description: TODO(解析工具类)
 * @author donnieSky
 * @date 2015年6月13日 下午4:48:59   
 * @version V1.0  
 */
public class XmlParseUtil {

	/**
	 * 
	 * @Title: getUpdateInfo
	 * @Description: TODO(解析Xml) 
	 * @param @param inputStream
	 * @param @return
	 * @return UpdateBean
	 */
	public static UpdateBean getUpdateInfo(InputStream inputStream) {
		// TODO Auto-generated method stub
		XmlPullParser parser = Xml.newPullParser();
		UpdateBean bean = new UpdateBean();
		try {
			parser.setInput(inputStream, "UTF-8");
			int type = parser.getEventType();
			while(type!=XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("version".equals(parser.getName())) {
						bean.setVersion(parser.nextText());
					}else if ("description".equals(parser.getName())) {
						bean.setDescription(parser.nextText());
					}else if ("apkurl".equals(parser.getName())) {
						bean.setApkUrl(parser.nextText());
					}
					break;
				default:
					break;
				}
				type = parser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
}
