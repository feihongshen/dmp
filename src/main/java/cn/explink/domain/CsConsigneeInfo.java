package cn.explink.domain;

import java.util.Date;

/**
 * 
 * @author wzy
 *id	主键	√	√	bigint(11)
name	姓名			varchar(64)
sex	性别(0：女 1：男)			int(2)
phone_one	电话1			varchar(20)
phone_two	电话2			varchar(20)
mail_box 	邮箱			varchar(64)
province	省份			varchar(64)
city	城市			varchar(64)
consignee_type	客户分类（数据字典）			int(8)
contact_last_time	最近时间联系时间			
contact_num	联系次数			int(8)
remark	备注			varchar(200)
 */
public class CsConsigneeInfo {
	
		private int id;
		private String name;
		private int sex;
		private String phoneonOne;
		private String phoneonTwo;
		private String mailBox;
		private String province;
		private String city;
		private int consigneeType;
		private String contactLastTime;
		private int contactNum;
		private String remark;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getSex() {
			return sex;
		}
		public void setSex(int sex) {
			this.sex = sex;
		}
		public String getPhoneonOne() {
			return phoneonOne;
		}
		public void setPhoneonOne(String phoneonOne) {
			this.phoneonOne = phoneonOne;
		}
		public String getPhoneonTwo() {
			return phoneonTwo;
		}
		public void setPhoneonTwo(String phoneonTwo) {
			this.phoneonTwo = phoneonTwo;
		}
		public String getMailBox() {
			return mailBox;
		}
		public void setMailBox(String mailBox) {
			this.mailBox = mailBox;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public int getConsigneeType() {
			return consigneeType;
		}
		public void setConsigneeType(int consigneeType) {
			this.consigneeType = consigneeType;
		}
		public String getContactLastTime() {
			return contactLastTime;
		}
		public void setContactLastTime(String contactLastTime) {
			this.contactLastTime = contactLastTime;
		}
		public int getContactNum() {
			return contactNum;
		}
		public void setContactNum(int contactNum) {
			this.contactNum = contactNum;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		
		

}
