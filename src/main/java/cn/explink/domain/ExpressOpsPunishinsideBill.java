package cn.explink.domain;

import java.math.BigDecimal;

/**
 * express_ops_punishinside_bill 实体类 Sat Jun 27 16:48:16 CST 2015 Li ChongChong
 */

public class ExpressOpsPunishinsideBill {
	private int id;
	private String billBatch;
	private int billState;
	private int dutybranchid;
	private int dutypersonid;
	private BigDecimal sumPrice;
	private int creator;
	private String createDate;
	private int shenHePerson;
	private String shenHeDate;
	private int cheXiaoPerson;
	private String cheXiaoDate;
	private int heXiaoPerson;
	private String heXiaoDate;
	private int quXiaoHeXiaoPerson;
	private String quXiaoHeXiaoDate;
	private int punishbigsort;
	private int punishsmallsort;
	private String punishInsideRemark;
	private String punishNoCreateBeginDate;
	private String punishNoCreateEndDate;

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

}
