package cn.explink.domain.VO;

import java.math.BigDecimal;
import java.util.List;

import cn.explink.domain.PenalizeInside;

/**
 * express_ops_punishinside_bill VO Sat Jun 27 16:48:16 CST 2015 Li ChongChong
 */

public class ExpressOpsPunishinsideBillVO {
	private int id;
	private String billBatch;
	private int billState;
	private int dutybranchid;
	private String dutybranchname;
	private int dutypersonid;
	private String dutypersonname;
	private BigDecimal sumPrice;
	private int creator;
	private String creatorName;
	private String createDate;
	private String createDateFrom;
	private String createDateTo;
	private int shenHePerson;
	private String shenHePersonName;
	private String shenHeDate;
	private int cheXiaoPerson;
	private String cheXiaoPersonName;
	private String cheXiaoDate;
	private int heXiaoPerson;
	private String heXiaoPersonName;
	private String heXiaoDate;
	private String heXiaoDateFrom;
	private String heXiaoDateTo;
	private int quXiaoHeXiaoPerson;
	private String quXiaoHeXiaoPersonName;
	private String quXiaoHeXiaoDate;
	private int punishbigsort;
	private int punishsmallsort;
	private String punishInsideRemark;
	private String contractColumn;
	private String contractColumnOrder;
	private String punishNoCreateBeginDate;
	private String punishNoCreateEndDate;
	private List<PenalizeInside> penalizeInsideList;
	private String cwbs;
	private String punishNos;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setBillBatch(String billBatch) {
		this.billBatch = billBatch;
	}

	public String getBillBatch() {
		return billBatch;
	}

	public void setBillState(int billState) {
		this.billState = billState;
	}

	public int getBillState() {
		return billState;
	}

	public void setDutybranchid(int dutybranchid) {
		this.dutybranchid = dutybranchid;
	}

	public int getDutybranchid() {
		return dutybranchid;
	}

	public void setDutypersonid(int dutypersonid) {
		this.dutypersonid = dutypersonid;
	}

	public int getDutypersonid() {
		return dutypersonid;
	}

	public void setSumPrice(BigDecimal sumPrice) {
		this.sumPrice = sumPrice;
	}

	public BigDecimal getSumPrice() {
		return sumPrice;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setShenHePerson(int shenHePerson) {
		this.shenHePerson = shenHePerson;
	}

	public int getShenHePerson() {
		return shenHePerson;
	}

	public void setShenHeDate(String shenHeDate) {
		this.shenHeDate = shenHeDate;
	}

	public String getShenHeDate() {
		return shenHeDate;
	}

	public void setCheXiaoPerson(int cheXiaoPerson) {
		this.cheXiaoPerson = cheXiaoPerson;
	}

	public int getCheXiaoPerson() {
		return cheXiaoPerson;
	}

	public void setCheXiaoDate(String cheXiaoDate) {
		this.cheXiaoDate = cheXiaoDate;
	}

	public String getCheXiaoDate() {
		return cheXiaoDate;
	}

	public void setHeXiaoPerson(int heXiaoPerson) {
		this.heXiaoPerson = heXiaoPerson;
	}

	public int getHeXiaoPerson() {
		return heXiaoPerson;
	}

	public void setHeXiaoDate(String heXiaoDate) {
		this.heXiaoDate = heXiaoDate;
	}

	public String getHeXiaoDate() {
		return heXiaoDate;
	}

	public void setQuXiaoHeXiaoPerson(int quXiaoHeXiaoPerson) {
		this.quXiaoHeXiaoPerson = quXiaoHeXiaoPerson;
	}

	public int getQuXiaoHeXiaoPerson() {
		return quXiaoHeXiaoPerson;
	}

	public void setQuXiaoHeXiaoDate(String quXiaoHeXiaoDate) {
		this.quXiaoHeXiaoDate = quXiaoHeXiaoDate;
	}

	public String getQuXiaoHeXiaoDate() {
		return quXiaoHeXiaoDate;
	}

	public void setPunishbigsort(int punishbigsort) {
		this.punishbigsort = punishbigsort;
	}

	public int getPunishbigsort() {
		return punishbigsort;
	}

	public void setPunishsmallsort(int punishsmallsort) {
		this.punishsmallsort = punishsmallsort;
	}

	public int getPunishsmallsort() {
		return punishsmallsort;
	}

	public void setPunishInsideRemark(String punishInsideRemark) {
		this.punishInsideRemark = punishInsideRemark;
	}

	public String getPunishInsideRemark() {
		return punishInsideRemark;
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

	public String getCreateDateFrom() {
		return createDateFrom;
	}

	public void setCreateDateFrom(String createDateFrom) {
		this.createDateFrom = createDateFrom;
	}

	public String getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(String createDateTo) {
		this.createDateTo = createDateTo;
	}

	public String getHeXiaoDateFrom() {
		return heXiaoDateFrom;
	}

	public void setHeXiaoDateFrom(String heXiaoDateFrom) {
		this.heXiaoDateFrom = heXiaoDateFrom;
	}

	public String getHeXiaoDateTo() {
		return heXiaoDateTo;
	}

	public void setHeXiaoDateTo(String heXiaoDateTo) {
		this.heXiaoDateTo = heXiaoDateTo;
	}

	public String getDutybranchname() {
		return dutybranchname;
	}

	public void setDutybranchname(String dutybranchname) {
		this.dutybranchname = dutybranchname;
	}

	public String getDutypersonname() {
		return dutypersonname;
	}

	public void setDutypersonname(String dutypersonname) {
		this.dutypersonname = dutypersonname;
	}

	public String getPunishNoCreateBeginDate() {
		return punishNoCreateBeginDate;
	}

	public void setPunishNoCreateBeginDate(String punishNoCreateBeginDate) {
		this.punishNoCreateBeginDate = punishNoCreateBeginDate;
	}

	public String getPunishNoCreateEndDate() {
		return punishNoCreateEndDate;
	}

	public void setPunishNoCreateEndDate(String punishNoCreateEndDate) {
		this.punishNoCreateEndDate = punishNoCreateEndDate;
	}

	public List<PenalizeInside> getPenalizeInsideList() {
		return penalizeInsideList;
	}

	public void setPenalizeInsideList(List<PenalizeInside> penalizeInsideList) {
		this.penalizeInsideList = penalizeInsideList;
	}

	public String getCwbs() {
		return cwbs;
	}

	public void setCwbs(String cwbs) {
		this.cwbs = cwbs;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getShenHePersonName() {
		return shenHePersonName;
	}

	public void setShenHePersonName(String shenHePersonName) {
		this.shenHePersonName = shenHePersonName;
	}

	public String getCheXiaoPersonName() {
		return cheXiaoPersonName;
	}

	public void setCheXiaoPersonName(String cheXiaoPersonName) {
		this.cheXiaoPersonName = cheXiaoPersonName;
	}

	public String getHeXiaoPersonName() {
		return heXiaoPersonName;
	}

	public void setHeXiaoPersonName(String heXiaoPersonName) {
		this.heXiaoPersonName = heXiaoPersonName;
	}

	public String getQuXiaoHeXiaoPersonName() {
		return quXiaoHeXiaoPersonName;
	}

	public void setQuXiaoHeXiaoPersonName(String quXiaoHeXiaoPersonName) {
		this.quXiaoHeXiaoPersonName = quXiaoHeXiaoPersonName;
	}

	public String getPunishNos() {
		return punishNos;
	}

	public void setPunishNos(String punishNos) {
		this.punishNos = punishNos;
	}

}
