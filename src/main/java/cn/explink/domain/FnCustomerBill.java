package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fn_customer_bill", schema = "")
@SuppressWarnings("serial")
/**
 * 客户账单entity
 * 
 * @author gaoll
 *
 */
public class FnCustomerBill implements java.io.Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name="bill_no", nullable=false, length=30)
	private String billNo;

	@Column(name="customer_id")
	private Long customerId;

	@Column(name="bill_type")
	private Integer billType;

	private Integer status;

	@Column(name="settle_time")
	private String settleTime;

	private String creator;

	@Column(name="check_time")
	private String checkTime;

	@Column(name="check_user")
	private String checkUser;

	@Column(name="confirm_time")
	private Date confirmTime;

	@Column(name="confirm_user")
	private String confirmUser;

	@Column(name="verify_time")
	private String verifyTime;

	@Column(name="verify_user")
	private String verifyUser;

	@Column(name="deliver_org")
	private String deliverOrg;

	@Column(name="bill_count")
	private Integer billCount;

	@Column(name="bill_amount")
	private BigDecimal billAmount;

	@Column(name="actual_amount")
	private BigDecimal actualAmount;

	private String remark;

	@Column(name="date_type")
	private long dateType;

	@Column(name="start_time")
	private String startTime;

	@Column(name="end_time")
	private String endTime;

	@Column(name="create_type")
	private String createType;

	@Column(name="import_remark")
	private String importRemark;

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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(String settleTime) {
		this.settleTime = settleTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getConfirmUser() {
		return confirmUser;
	}

	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}

	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getDeliverOrg() {
		return deliverOrg;
	}

	public void setDeliverOrg(String deliverOrg) {
		this.deliverOrg = deliverOrg;
	}

	public Integer getBillCount() {
		return billCount;
	}

	public void setBillCount(Integer billCount) {
		this.billCount = billCount;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getDateType() {
		return dateType;
	}

	public void setDateType(long dateType) {
		this.dateType = dateType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public String getImportRemark() {
		return importRemark;
	}

	public void setImportRemark(String importRemark) {
		this.importRemark = importRemark;
	}
}
