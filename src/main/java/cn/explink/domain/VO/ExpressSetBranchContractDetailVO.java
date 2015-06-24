package cn.explink.domain.VO;

import java.math.BigDecimal;

public class ExpressSetBranchContractDetailVO {
	private int id;
	private int branchId;
	private String depositReturnTime;
	private BigDecimal depositReturnAmount;
	private String depositReturnPerson;
	private String depositCollector;
	private String remark;
	private int creator;
	private String createTime;
	private int modifyPerson;
	private String modifyTime;

	public ExpressSetBranchContractDetailVO() {

	}

	public ExpressSetBranchContractDetailVO(int id, int branchId,
			String depositReturnTime, BigDecimal depositReturnAmount,
			String depositReturnPerson, String depositCollector, String remark,
			int creator, String createTime, int modifyPerson,
			String modifyTime) {
		super();
		this.id = id;
		this.branchId = branchId;
		this.depositReturnTime = depositReturnTime;
		this.depositReturnAmount = depositReturnAmount;
		this.depositReturnPerson = depositReturnPerson;
		this.depositCollector = depositCollector;
		this.remark = remark;
		this.creator = creator;
		this.createTime = createTime;
		this.modifyPerson = modifyPerson;
		this.modifyTime = modifyTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setDepositReturnTime(String depositReturnTime) {
		this.depositReturnTime = depositReturnTime;
	}

	public String getDepositReturnTime() {
		return depositReturnTime;
	}

	public void setDepositReturnAmount(BigDecimal depositReturnAmount) {
		this.depositReturnAmount = depositReturnAmount;
	}

	public BigDecimal getDepositReturnAmount() {
		return depositReturnAmount;
	}

	public void setDepositReturnPerson(String depositReturnPerson) {
		this.depositReturnPerson = depositReturnPerson;
	}

	public String getDepositReturnPerson() {
		return depositReturnPerson;
	}

	public void setDepositCollector(String depositCollector) {
		this.depositCollector = depositCollector;
	}

	public String getDepositCollector() {
		return depositCollector;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setModifyPerson(int modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	public int getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}
}
