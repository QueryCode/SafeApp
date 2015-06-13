package com.donnie.safe.bean;
/**  
 * @Title: UpdateBean.java
 * @Package com.donnie.safe.bean
 * @Description: TODO(更新信息的实体类)
 * @author donnieSky
 * @date 2015年6月13日 下午4:46:30   
 * @version V1.0  
 */
public class UpdateBean {
		private String version;
		private String description;
		private String apkUrl;
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getApkUrl() {
			return apkUrl;
		}
		public void setApkUrl(String apkUrl) {
			this.apkUrl = apkUrl;
		}
		
}
