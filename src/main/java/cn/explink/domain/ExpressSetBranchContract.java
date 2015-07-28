package cn.explink.domain;

import java.math.BigDecimal;

/**
 * express_set_branch_contract 实体类 Tue Jun 16 18:26:46 CST 2015 LiChongChong
 * 
 */

public class ExpressSetBranchContract {
	private int id;
	private String contractNo;
	private int contractState;
	private String contractBeginDate;
	private String contractEndDate;
	private int branchId;
	private String branchName;
	private String siteChief;
	private String chiefIdentity;
	private String areaManager;
	private Integer isDeposit;
	private String depositCollectDate;
	private BigDecimal depositCollectAmount;
	private String depositCollector;
	private String depositPayor;
	private String contractDescription;
	private String contractAttachment;
	private int creator;
	private String createTime;
	private int modifyPerson;
	private String modifyTime;
	private String qualityControlClause;

	public ExpressSetBranchContract() {

	}

	public ExpressSetBranchContract(int id, String contractNo,
			int contractState, String contractBeginDate,
			String contractEndDate, String branchName, String siteChief,
			String chiefIdentity, String areaManager, Integer isDeposit,
			String depositCollectDate, BigDecimal depositCollectAmount,
			String depositCollector, String depositPayor,
			String contractDescription, String contractAttachment,
			String qualityControlClause, int creator, String createTime,
			int modifyPerson, String modifyTime) {
		super();
		this.id = id;
		this.contractNo = contractNo;
		this.contractState = contractState;
		this.contractBeginDate = contractBeginDate;
		this.contractEndDate = contractEndDate;
		this.branchName = branchName;
		this.siteChief = siteChief;
		this.chiefIdentity = chiefIdentity;
		this.areaManager = areaManager;
		this.isDeposit = isDeposit;
		this.depositCollectDate = depositCollectDate;
		this.depositCollectAmount = depositCollectAmount;
		this.depositCollector = depositCollector;
		this.depositPayor = depositPayor;
		this.contractDescription = contractDescription;
		this.contractAttachment = contractAttachment;
		this.qualityControlClause = qualityControlClause;
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

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractState(int contractState) {
		this.contractState = contractState;
	}

	public int getContractState() {
		return contractState;
	}

	public void setContractBeginDate(String contractBeginDate) {
		this.contractBeginDate = contractBeginDate;
	}

	public String getContractBeginDate() {
		return contractBeginDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setSiteChief(String siteChief) {
		this.siteChief = siteChief;
	}

	public String getSiteChief() {
		return siteChief;
	}

	public void setChiefIdentity(String chiefIdentity) {
		this.chiefIdentity = chiefIdentity;
	}

	public String getChiefIdentity() {
		return chiefIdentity;
	}

	public void setAreaManager(String areaManager) {
		this.areaManager = areaManager;
	}

	public String getAreaManager() {
		return areaManager;
	}

	public void setIsDeposit(Integer isDeposit) {
		this.isDeposit = isDeposit;
	}

	public Integer getIsDeposit() {
		return isDeposit;
	}

	public void setDepositCollectDate(String depositCollectDate) {
		this.depositCollectDate = depositCollectDate;
	}

	public String getDepositCollectDate() {
		return depositCollectDate;
	}

	public void setDepositCollectAmount(BigDecimal depositCollectAmount) {
		this.depositCollectAmount = depositCollectAmount;
	}

	public BigDecimal getDepositCollectAmount() {
		return depositCollectAmount;
	}

	public void setDepositCollector(String depositCollector) {
		this.depositCollector = depositCollector;
	}

	public String getDepositCollector() {
		return depositCollector;
	}

	public void setDepositPayor(String depositPayor) {
		this.depositPayor = depositPayor;
	}

	public String getDepositPayor() {
		return depositPayor;
	}

	public void setContractDescription(String contractDescription) {
		this.contractDescription = contractDescription;
	}

	public String getContractDescription() {
		return contractDescription;
	}

	public void setContractAttachment(String contractAttachment) {
		this.contractAttachment = contractAttachment;
	}

	public String getContractAttachment() {
		return contractAttachment;
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

	public String getQualityControlClause() {
		return qualityControlClause;
	}

	public void setQualityControlClause(String qualityControlClause) {
		this.qualityControlClause = qualityControlClause;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

}
