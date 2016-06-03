package cn.explink.domain;

public class CustomerAccountPO {
	private Integer customerId;//客户Id
	private String customerNumber ; // 客户编号
	private String customerName ; // 客户名称
	private Integer settleType ; // 结算方式
	private String  settleTypeDesc ; // 结算方式描述
	private Integer settleTimeType ; // 结算时间类型
	private String  settleTimeTypeDesc ; // 结算时间类型描述
	private String creator;//创建人
	private String createTime;//创建时间
	private String updateUser ; // 更新人
	private String updateTime ; // 更新时间
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getSettleType() {
		return settleType;
	}
	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}
	public Integer getSettleTimeType() {
		return settleTimeType;
	}
	public void setSettleTimeType(Integer settleTimeType) {
		this.settleTimeType = settleTimeType;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getSettleTypeDesc() {
		return settleTypeDesc;
	}
	public void setSettleTypeDesc(String settleTypeDesc) {
		this.settleTypeDesc = settleTypeDesc;
	}
	public String getSettleTimeTypeDesc() {
		return settleTimeTypeDesc;
	}
	public void setSettleTimeTypeDesc(String settleTimeTypeDesc) {
		this.settleTimeTypeDesc = settleTimeTypeDesc;
	}
	
}
