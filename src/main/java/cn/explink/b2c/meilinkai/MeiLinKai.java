package cn.explink.b2c.meilinkai;

public class MeiLinKai {
	
	private String usrename;//用户名
	private String pwd;//密码
	private String customerid;//电商在配送系统id
	private String hdtolipsUrl;//下载订单路径
	private String maxCount;//回传单次查询最大量
	private String shipJDECarrierNum;//承运商编码
	private String lipstohdUrl;//回传货态信息路径
	private int warehouseid;//数据将要导入的库房id
	private String checkUsermethod;//用户校验方法名
	private String hdtolipsmethod;//货态回传方法名
	
	public String getCheckUsermethod() {
		return checkUsermethod;
	}
	public void setCheckUsermethod(String checkUsermethod) {
		this.checkUsermethod = checkUsermethod;
	}
	public String getHdtolipsmethod() {
		return hdtolipsmethod;
	}
	public void setHdtolipsmethod(String hdtolipsmethod) {
		this.hdtolipsmethod = hdtolipsmethod;
	}
	public String getShipJDECarrierNum() {
		return shipJDECarrierNum;
	}
	public void setShipJDECarrierNum(String shipJDECarrierNum) {
		this.shipJDECarrierNum = shipJDECarrierNum;
	}
	public String getUsrename() {
		return usrename;
	}
	public void setUsrename(String usrename) {
		this.usrename = usrename;
	}
	
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getHdtolipsUrl() {
		return hdtolipsUrl;
	}
	public void setHdtolipsUrl(String hdtolipsUrl) {
		this.hdtolipsUrl = hdtolipsUrl;
	}
	
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount;
	}
	public String getLipstohdUrl() {
		return lipstohdUrl;
	}
	public void setLipstohdUrl(String lipstohdUrl) {
		this.lipstohdUrl = lipstohdUrl;
	}
	public int getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(int warehouseid) {
		this.warehouseid = warehouseid;
	}
	
	
}
