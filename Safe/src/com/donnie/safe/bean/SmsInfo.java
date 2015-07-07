package com.donnie.safe.bean;
/**  
 * @Title: SmsInfo.java
 * @Package com.donnie.safe.bean
 * @Description: TODO(短信实体类)
 * @author donnieSky
 * @date 2015年7月7日 上午10:36:00   
 * @version V1.0  
 */
public class SmsInfo {
	
	private String address;
	
	private String date;
	
	private String type;
	
	private String body;
	

	public SmsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SmsInfo(String address, String date, String type, String body) {
		super();
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
