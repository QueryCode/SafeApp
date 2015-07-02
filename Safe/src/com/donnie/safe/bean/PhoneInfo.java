package com.donnie.safe.bean;
/**  
 * @Title: PhoneInfo.java
 * @Package com.donnie.safe.bean
 * @Description: TODO(添加描述)
 * @author donnieSky
 * @date 2015年7月2日 下午3:29:05   
 * @version V1.0  
 */
public class PhoneInfo {

	private String city;
	
	private String cardtype;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public PhoneInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhoneInfo(String city, String cardtype) {
		super();
		this.city = city;
		this.cardtype = cardtype;
	}
	
}
