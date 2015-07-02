package com.donnie.safe.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

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
		
		public List<ContactInfo> getContacts(Context context){
			Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			List<ContactInfo> info = new ArrayList<ContactInfo>();
			while(cursor.moveToNext()){
				ContactInfo contact = new ContactInfo();
				int id = cursor.getInt((cursor.getColumnIndex(ContactsContract.Contacts._ID)));
				contact.setId(id);
				contact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				Cursor cursor2 = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?", new String[]{String.valueOf(id)}, null);
				String number = null;
				for(cursor2.moveToFirst();!cursor2.isAfterLast();cursor2.moveToNext()){
					number = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
				contact.setNumber(number);
				info.add(contact);
				cursor2.close();
			}
			cursor.close();
			return info;
		}
}
