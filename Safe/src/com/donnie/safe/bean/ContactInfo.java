package com.donnie.safe.bean;
/**  
 * @Title: ContactInfo.java
 * @Package com.donnie.safe.entity
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年6月24日 下午3:45:13   
 * @version V1.0  
 */
public class ContactInfo {
	
	private String name;
	private String number;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public ContactInfo(String name, String number) {
		super();
		this.name = name;
		this.number = number;
	}
	public ContactInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
