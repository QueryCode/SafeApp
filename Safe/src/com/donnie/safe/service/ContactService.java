package com.donnie.safe.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.donnie.safe.ContactListActivity;
import com.donnie.safe.bean.ContactInfo;

/**  
 * @Title: ContactService.java
 * @Package com.donnie.safe.service
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年6月24日 下午3:44:21   
 * @version V1.0  
 */
public class ContactService {
	
	private Context context;
	
	public ContactService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
		public List<ContactInfo> getContacts(){
			List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
			Cursor c = cr.query(uri, new String[]{"_id","display_name"}, null, null, null);
			while(c.moveToNext()){
				ContactInfo info = new ContactInfo();
				String _id = c.getString(c.getColumnIndex("_id"));
				String name = c.getString(c.getColumnIndex("display_name"));
				info.setName(name);
				
				uri = Uri.parse("content://com.android.contacts/raw_contacts/" + _id + "/data");
				Cursor c1 = cr.query(uri, new String[]{"data1","mimetype"}, null, null, null);
	            while(c1.moveToNext()){
	            	String data1 = c1.getString(c1.getColumnIndex("data1"));
	            	String mimetype = c1.getString(c1.getColumnIndex("mimetype"));
	            	if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
	            		info.setNumber(data1);
	            		contactInfos.add(info);
	            	}
	            }
	            c1.close();
			}
			c.close();
			return contactInfos;
		}
}
