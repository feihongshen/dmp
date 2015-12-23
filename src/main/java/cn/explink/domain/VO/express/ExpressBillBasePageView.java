package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 表格显示的基本类
 * @author jiangyu 2015年8月11日
 *
 */
public class ExpressBillBasePageView {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 账单编号
	 */
	private String billNo;
	/**
	 * 运费
	 */
	private BigDecimal freight;
	/**
	 * 代收货款
	 */
	private BigDecimal cod;
	/**
	 * 创建者
	 */
	private String creatorName;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 审核人
	 */
	private String auditorName;
	/**
	 * 审核时间
	 */
	private String auditTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 站点
	 */
	private Long branchId;
	/**
	 * 站点名称
	 */
	private String branchName;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 账单状态text
	 */
	private String billStateStr;
	/**
	 * 账单状态
	 */
	private Integer billState;
	/**
	 * 核销人
	 */
	private String cavName;
	/**
	 * 核销时间
	 */
	private String cavTime;
	/**
	 * 截止时间
	 */
	private String dealLineTime;
	/**
	 * 截止时间对应的名称
	 */
	private String dealLineTimeName;
	/**
	 * 付款方式
	 */
	private String payMethod;
	/**
	 * 付款方式字符串
	 */
	private String payMethodStr;
	
	/**
	 * 应收省份
	 */
	private String receProvinceName;
	
	/**
	 * 应付省份
	 */
	private String payProvinceName;
	
	public ExpressBillBasePageView() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getCod() {
		return cod;
	}

	public void setCod(BigDecimal cod) {
		this.cod = cod;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBillStateStr() {
		return billStateStr;
	}

	public void setBillStateStr(String billStateStr) {
		this.billStateStr = billStateStr;
	}

	public Integer getBillState() {
		return billState;
	}

	public void setBillState(Integer billState) {
		this.billState = billState;
	}

	public String getCavName() {
		return cavName;
	}

	public void setCavName(String cavName) {
		this.cavName = cavName;
	}

	public String getCavTime() {
		return cavTime;
	}

	public void setCavTime(String cavTime) {
		this.cavTime = cavTime;
	}

	public String getDealLineTime() {
		return dealLineTime;
	}

	public void setDealLineTime(String dealLineTime) {
		this.dealLineTime = dealLineTime;
	}

	public String getDealLineTimeName() {
		return dealLineTimeName;
	}

	public void setDealLineTimeName(String dealLineTimeName) {
		this.dealLineTimeName = dealLineTimeName;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getReceProvinceName() {
		return receProvinceName;
	}

	public void setReceProvinceName(String receProvinceName) {
		this.receProvinceName = receProvinceName;
	}

	public String getPayProvinceName() {
		return payProvinceName;
	}

	public void setPayProvinceName(String payProvinceName) {
		this.payProvinceName = payProvinceName;
	}

	public String getPayMethodStr() {
		return payMethodStr;
	}

	public void setPayMethodStr(String payMethodStr) {
		this.payMethodStr = payMethodStr;
	}
}