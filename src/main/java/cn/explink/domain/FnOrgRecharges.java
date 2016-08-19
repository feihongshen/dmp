package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class FnOrgRecharges implements java.io.Serializable {

	private Long id;

	private Long orgId;

	private Integer paymethod;

	private String accountNo;

	private BigDecimal amount;

	private BigDecimal surplus;

	private String remark = "";

	private Date createTime;

	private String creator;

	private Long biId;
	
	private Integer handleStatus;

	private Date importTime;
	
	private String picker;
	
	private String billNo;
	
	private Long billDetailId;
	
	private String billType;
	
	private Integer payinType;
	
	private String rechargeNo;
	
	private Long pickerId;
	
	private Integer rechargeSource;
	
	private String cwb;
	
	private Long vpalRecordId;
		
	private Date updateTime;
	
	
	public Integer getRechargeSource() {
		return rechargeSource;
	}


	public void setRechargeSource(Integer rechargeSource) {
		this.rechargeSource = rechargeSource;
	}


	public String getCwb() {
		return cwb;
	}


	public void setCwb(String cwb) {
		this.cwb = cwb;
	}


	public Long getVpalRecordId() {
		return vpalRecordId;
	}


	public void setVpalRecordId(Long vpalRecordId) {
		this.vpalRecordId = vpalRecordId;
	}


	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(Integer paymethod) {
		this.paymethod = paymethod;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getSurplus() {
		return surplus;
	}

	public void setSurplus(BigDecimal surplus) {
		this.surplus = surplus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getBiId() {
		return biId;
	}

	public void setBiId(Long biId) {
		this.biId = biId;
	}

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public String getPicker() {
		return picker;
	}

	public void setPicker(String picker) {
		this.picker = picker;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Long getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(Long billDetailId) {
		this.billDetailId = billDetailId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public Integer getPayinType() {
		return payinType;
	}

	public void setPayinType(Integer payinType) {
		this.payinType = payinType;
	}

	public String getRechargeNo() {
		return rechargeNo;
	}

	public void setRechargeNo(String rechargeNo) {
		this.rechargeNo = rechargeNo;
	}

	public Long getPickerId() {
		return pickerId;
	}

	public void setPickerId(Long pickerId) {
		this.pickerId = pickerId;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
