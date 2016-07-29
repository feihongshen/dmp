package cn.explink.domain;

/**
 * 客户管理-工单导入页面展示VO
 * @author vic.liang 2016-07-13
 */

public class CsComplaintAcceptViewVo extends CsComplaintAccept {
	private boolean isCorrect;// 页面导入工单的结果标识，决定导入是否会保存该记录
	private String errorMsg;// 页面导入工单错误信息提示
	private String customerName; //客户名称
	private String complaintOneLevelName;	//一级分类
	private String complaintTwoLevelName;	//二级分类
	private String codOrgIdName; //被投诉机构
	private String contact;//联系人
	private String email;//联系邮箱

	public CsComplaintAcceptViewVo() {

	}

	public CsComplaintAcceptViewVo(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getComplaintOneLevelName() {
		return complaintOneLevelName;
	}

	public void setComplaintOneLevelName(String complaintOneLevelName) {
		this.complaintOneLevelName = complaintOneLevelName;
	}

	public String getComplaintTwoLevelName() {
		return complaintTwoLevelName;
	}

	public void setComplaintTwoLevelName(String complaintTwoLevelName) {
		this.complaintTwoLevelName = complaintTwoLevelName;
	}

	public String getCodOrgIdName() {
		return codOrgIdName;
	}

	public void setCodOrgIdName(String codOrgIdName) {
		this.codOrgIdName = codOrgIdName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
