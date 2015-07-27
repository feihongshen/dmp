package cn.explink.domain;

import java.math.BigDecimal;

public class Paybusinessbenefits {
	private long id;
	private long customerid;
	private String lower;//下限
	private String upper;//上限 
	private BigDecimal kpifee;//业务补助
 	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	private String customername;
	//业务KPI补助规则各个阶段范围
	private String paybusinessbenefits;
	//其他补助
	private BigDecimal othersubsidies;
	private String remark;
	public BigDecimal getKpifee() {
		return kpifee;
	}
	public void setKpifee(BigDecimal kpifee) {
		this.kpifee = kpifee;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getPaybusinessbenefits() {
		return paybusinessbenefits;
	}
	public void setPaybusinessbenefits(String paybusinessbenefits) {
		this.paybusinessbenefits = paybusinessbenefits;
	}
	public BigDecimal getOthersubsidies() {
		return othersubsidies;
	}
	public void setOthersubsidies(BigDecimal othersubsidies) {
		this.othersubsidies = othersubsidies;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
