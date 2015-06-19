package cn.explink.domain;

public class CsComplaintAcceptExportVO {
	
	private String acceptNo;	//工单号
	private String orderNo;		//订单号
	private String codOrgId;		//被投诉机构
	private String complaintType;		//工单类型
	private String complaintOneLevel;	//一级分类
	private String complaintTwoLevel;	//二级分类
	private String acceptTime;	//受理时间
	private String handleUser;  //工单受理人

	private String complaintResult;
	private String name;
	private String phoneOne;
	private String ifpunish;
	private int cuijianNum;
	private String customername;
	private String complaintState;
	
	
	public String getHandleUser() {
		return handleUser;
	}
	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}
	public String getAcceptNo() {
		return acceptNo;
	}
	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCodOrgId() {
		return codOrgId;
	}
	public void setCodOrgId(String codOrgId) {
		this.codOrgId = codOrgId;
	}
	public String getComplaintType() {
		return complaintType;
	}
	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}
	public String getComplaintOneLevel() {
		return complaintOneLevel;
	}
	public void setComplaintOneLevel(String complaintOneLevel) {
		this.complaintOneLevel = complaintOneLevel;
	}
	public String getComplaintTwoLevel() {
		return complaintTwoLevel;
	}
	public void setComplaintTwoLevel(String complaintTwoLevel) {
		this.complaintTwoLevel = complaintTwoLevel;
	}
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	public String getComplaintResult() {
		return complaintResult;
	}
	public void setComplaintResult(String complaintResult) {
		this.complaintResult = complaintResult;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneOne() {
		return phoneOne;
	}
	public void setPhoneOne(String phoneOne) {
		this.phoneOne = phoneOne;
	}
	
	public String getIfpunish() {
		return ifpunish;
	}
	public void setIfpunish(String ifpunish) {
		this.ifpunish = ifpunish;
	}
	public int getCuijianNum() {
		return cuijianNum;
	}
	public void setCuijianNum(int cuijianNum) {
		this.cuijianNum = cuijianNum;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getComplaintState() {
		return complaintState;
	}
	public void setComplaintState(String complaintState) {
		this.complaintState = complaintState;
	}
	
	
	
	

}
