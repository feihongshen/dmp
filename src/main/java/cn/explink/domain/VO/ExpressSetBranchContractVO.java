package cn.explink.domain.VO;

import java.math.BigDecimal;
import java.util.List;

public class ExpressSetBranchContractVO {
	private int id;
	private String contractNo;
	private int contractState;
	private String contractBeginDate;
	private String contractBeginDateFrom;
	private String contractBeginDateTo;
	private String contractEndDate;
	private String contractEndDateFrom;
	private String contractEndDateTo;
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
	private String branchContractDetailVOStr;
	private List<ExpressSetBranchContractDetailVO> branchContractDetailVOList;
	private String contractColumn;
	private String contractColumnOrder;
	private String qualityControlClause;

	public ExpressSetBranchContractVO() {

	}

	public ExpressSetBranchContractVO(int id, String contractNo,
			int contractState, String contractBeginDate,
			String contractEndDate, String branchName, String siteChief,
			String chiefIdentity, String areaManager, Integer isDeposit,
			String depositCollectDate, BigDecimal depositCollectAmount,
			String depositCollector, String depositPayor,
			String contractDescription, String contractAttachment,
			String qualityControlClause, int creator,
			String createTime, int modifyPerson, String modifyTime,
			List<ExpressSetBranchContractDetailVO> branchContractDetailVOList) {
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
		this.branchContractDetailVOList = branchContractDetailVOList;
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

	public List<ExpressSetBranchContractDetailVO> getBranchContractDetailVOList() {
		return branchContractDetailVOList;
	}

	public void setBranchContractDetailVOList(
			List<ExpressSetBranchContractDetailVO> branchContractDetailVOList) {
		this.branchContractDetailVOList = branchContractDetailVOList;
	}

	public String getBranchContractDetailVOStr() {
		return branchContractDetailVOStr;
	}

	public void setBranchContractDetailVOStr(String branchContractDetailVOStr) {
		this.branchContractDetailVOStr = branchContractDetailVOStr;
	}

	public String getContractBeginDateFrom() {
		return contractBeginDateFrom;
	}

	public void setContractBeginDateFrom(String contractBeginDateFrom) {
		this.contractBeginDateFrom = contractBeginDateFrom;
	}

	public String getContractBeginDateTo() {
		return contractBeginDateTo;
	}

	public void setContractBeginDateTo(String contractBeginDateTo) {
		this.contractBeginDateTo = contractBeginDateTo;
	}

	public String getContractEndDateFrom() {
		return contractEndDateFrom;
	}

	public void setContractEndDateFrom(String contractEndDateFrom) {
		this.contractEndDateFrom = contractEndDateFrom;
	}

	public String getContractEndDateTo() {
		return contractEndDateTo;
	}

	public void setContractEndDateTo(String contractEndDateTo) {
		this.contractEndDateTo = contractEndDateTo;
	}

	public String getContractColumn() {
		return contractColumn;
	}

	public void setContractColumn(String contractColumn) {
		this.contractColumn = contractColumn;
	}

	public String getContractColumnOrder() {
		return contractColumnOrder;
	}

	public void setContractColumnOrder(String contractColumnOrder) {
		this.contractColumnOrder = contractColumnOrder;
	}

	public String getQualityControlClause() {
		return qualityControlClause;
	}

	public void setQualityControlClause(String qualityControlClause) {
		this.qualityControlClause = qualityControlClause;
	}

}
