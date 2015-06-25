package com.donnie.safe;

import java.util.List;

import com.donnie.safe.bean.ContactInfo;
import com.donnie.safe.service.ContactService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends Activity {

	private ListView lv_contact;
	
	private ContactService service;
	
	private ContactInfoAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		lv_contact = (ListView)super.findViewById(R.id.lv_contact);
		
		service = new ContactService(this);
		List<ContactInfo> contacts = service.getContacts();
		
		adapter = new ContactInfoAdapter(this, contacts);
		lv_contact.setAdapter(adapter);
		
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ContactInfo info = (ContactInfo)adapter.getItem(position);
				String number = info.getNumber();
				Intent data = new Intent();
				data.putExtra("number", number);
				setResult(100, data);
				finish();
			}
		});
	}
}
