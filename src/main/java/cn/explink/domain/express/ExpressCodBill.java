package cn.explink.domain.express;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 *
 * @description 跨省代收货款表(express_ops_cod_bill);
 * @author 刘武强
 * @data 2015年8月12日
 */
public class ExpressCodBill {
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
	private Integer billState;
	/**
	 * 截止日期
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date closingDate;
	/**
	 * 订单数量
	 */
	private Integer orderCount;
	/**
	 * 账单类型（0：跨省应收货款，1：跨省应付货款）
	 */
	private Integer billType;
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
	@DateTimeFormat(iso = ISO.DATE)
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
	@DateTimeFormat(iso = ISO.DATE)
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
	@DateTimeFormat(iso = ISO.DATE)
	private Date cavTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 应收省id
	 */
	private Integer receivableProvinceId;
	/**
	 * 应收省
	 */
	private String receivableProvinceName;
	/**
	 * 应付省id
	 */
	private Integer payableProvinceId;
	/**
	 * 应付省
	 */
	private String payableProvinceName;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getBillState() {
		return this.billState;
	}

	public void setBillState(Integer billState) {
		this.billState = billState;
	}

	public Date getClosingDate() {
		return this.closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public Integer getOrderCount() {
		return this.orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getBillType() {
		return this.billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public BigDecimal getCod() {
		return this.cod;
	}

	public void setCod(BigDecimal cod) {
		this.cod = cod;
	}

	public Long getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return this.creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getAuditorId() {
		return this.auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorName() {
		return this.auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Date getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Long getCavId() {
		return this.cavId;
	}

	public void setCavId(Long cavId) {
		this.cavId = cavId;
	}

	public String getCavName() {
		return this.cavName;
	}

	public void setCavName(String cavName) {
		this.cavName = cavName;
	}

	public Date getCavTime() {
		return this.cavTime;
	}

	public void setCavTime(Date cavTime) {
		this.cavTime = cavTime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getReceivableProvinceId() {
		return this.receivableProvinceId;
	}

	public void setReceivableProvinceId(Integer receivableProvinceId) {
		this.receivableProvinceId = receivableProvinceId;
	}

	public String getReceivableProvinceName() {
		return this.receivableProvinceName;
	}

	public void setReceivableProvinceName(String receivableProvinceName) {
		this.receivableProvinceName = receivableProvinceName;
	}

	public Integer getPayableProvinceId() {
		return this.payableProvinceId;
	}

	public void setPayableProvinceId(Integer payableProvinceId) {
		this.payableProvinceId = payableProvinceId;
	}

	public String getPayableProvinceName() {
		return this.payableProvinceName;
	}

	public void setPayableProvinceName(String payableProvinceName) {
		this.payableProvinceName = payableProvinceName;
	}
}
