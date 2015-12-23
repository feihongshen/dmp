package cn.explink.domain.VO.express;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运费账单表VO
 * @author wangzy 2015年8月11日
 *
 */
public class ExpressFreightAuditBillVO {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 账单编号
	 */
	private String billNo;
	/**
	 * 账单状态( 0:未审核，1：已审核，2：已核销)
	 */
	private String billState;
	/**
	 * 截止日期
	 */
	private Date closingDate;
	/**
	 * 付款方式（0：现付，1：到付，2：月结）
	 */
	private String payMethod;
	/**
	 * 订单数量
	 */
	private Integer orderCount;
	/**
	 * 账单类型（0：客户运费，1：站点运费，2：跨省应收运费，3：跨省应付运费）
	 */
	private Integer billType;
	/**
	 * 运费
	 */
	private BigDecimal freight;
	/**
	 * 代收货款
	 */
	private BigDecimal cod;
	/**
	 * 创建人id
	 */
	private Long creatorId;
	/**
	 * 创建人姓名
	 */
	private String creatorName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 审核人id
	 */
	private Long auditorId;
	/**
	 * 审核人姓名
	 */
	private String auditorName;
	/**
	 * 审核时间
	 */
	private Date auditTime;
	/**
	 * 核销人id
	 */
	private Long cavId;
	/**
	 * 核销人姓名
	 */
	private String cavName;
	/**
	 * 核销时间
	 */
	private Date cavTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 客户id
	 */
	private Long customreId;
	/**
	 * 客户姓名
	 */
	private String customerName;
	/**
	 * 所属站点id
	 */
	private Long branchId;
	/**
	 * 所属站点
	 */
	private String branchName;
	/**
	 * 应收省id
	 */
	private Long receivableProvinceId;
	/**
	 * 应收省
	 */
	private String receivableProvinceName;
	/**
	 * 应付省id
	 */
	private Long payableProvinceId;
	/**
	 * 应付省
	 */
	private String payableProvinceName;
	/**
	 * 揽件入站截止日期
	 */
	private Date intoStationDeadLineTime;
	/**
	 * 开始创建时间
	 */
	private String startCreatTime;
	/**
	 * 结束创建时间
	 */
	private String endCreatTime;
	/**
	 * 开始核销日期
	 */
	private String startAuditTime;
	/**
	 * 结束核销日期
	 */
	private String endAuditTime;
	/**
	 * 排序项
	 */
	private String sortOption;
	/**
	 * 排序规则
	 */
	private String sortRule;
	

	public ExpressFreightAuditBillVO() {
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

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
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

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Long getCavId() {
		return cavId;
	}

	public void setCavId(Long cavId) {
		this.cavId = cavId;
	}

	public String getCavName() {
		return cavName;
	}

	public void setCavName(String cavName) {
		this.cavName = cavName;
	}

	public Date getCavTime() {
		return cavTime;
	}

	public void setCavTime(Date cavTime) {
		this.cavTime = cavTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCustomreId() {
		return customreId;
	}

	public void setCustomreId(Long customreId) {
		this.customreId = customreId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public Long getReceivableProvinceId() {
		return receivableProvinceId;
	}

	public void setReceivableProvinceId(Long receivableProvinceId) {
		this.receivableProvinceId = receivableProvinceId;
	}

	public String getReceivableProvinceName() {
		return receivableProvinceName;
	}

	public void setReceivableProvinceName(String receivableProvinceName) {
		this.receivableProvinceName = receivableProvinceName;
	}

	public Long getPayableProvinceId() {
		return payableProvinceId;
	}

	public void setPayableProvinceId(Long payableProvinceId) {
		this.payableProvinceId = payableProvinceId;
	}

	public String getPayableProvinceName() {
		return payableProvinceName;
	}

	public void setPayableProvinceName(String payableProvinceName) {
		this.payableProvinceName = payableProvinceName;
	}

	public Date getIntoStationDeadLineTime() {
		return intoStationDeadLineTime;
	}

	public void setIntoStationDeadLineTime(Date intoStationDeadLineTime) {
		this.intoStationDeadLineTime = intoStationDeadLineTime;
	}
	
//新添加的账单的创建和核销开始时间和结束时间	
	public String getStartCreatTime() {
		return startCreatTime;
	}

	public void setStartCreatTime(String startCreatTime) {
		this.startCreatTime = startCreatTime;
	}

	public String getEndCreatTime() {
		return endCreatTime;
	}

	public void setEndCreatTime(String endCreatTime) {
		this.endCreatTime = endCreatTime;
	}

	public String getStartAuditTime() {
		return startAuditTime;
	}

	public void setStartAuditTime(String startAuditTime) {
		this.startAuditTime = startAuditTime;
	}

	public String getEndAuditTime() {
		return endAuditTime;
	}

	public void setEndAuditTime(String endAuditTime) {
		this.endAuditTime = endAuditTime;
	}
	public String getSortOption() {
		return sortOption;
	}

	public void setSortOption(String sortOption) {
		this.sortOption = sortOption;
	}

	public String getSortRule() {
		return sortRule;
	}

	public void setSortRule(String sortRule) {
		this.sortRule = sortRule;
	}

}
