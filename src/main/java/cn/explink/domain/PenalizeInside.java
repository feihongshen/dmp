package cn.explink.domain;

import java.math.BigDecimal;

//对内扣罚
public class PenalizeInside {
	private long id;//主键id
	private String punishNo;//扣罚单号
	private long createBySource;//创建扣罚单的来源
	private String  sourceNo;//来源单号
	private String cwb;//订单号
	private long dutybranchid;//责任机构
	private long dutypersonid;//责任人
	private long cwbstate;//订单状态
	private BigDecimal cwbPrice;//订单价格
	private BigDecimal punishInsideprice;//对内扣罚金额
	private long punishbigsort;//扣罚大类
	private long punishsmallsort;//扣罚小类
	private long createuserid;//创建人
	private String creDate;//创建日期
	private int punishcwbstate;//扣罚单状态
	private String punishdescribe;//扣罚描述
	private String fileposition;//上传附件的存储的位置
	
	
	public String getFileposition() {
		return fileposition;
	}
	public void setFileposition(String fileposition) {
		this.fileposition = fileposition;
	}
	public String getPunishdescribe() {
		return punishdescribe;
	}
	public void setPunishdescribe(String punishdescribe) {
		this.punishdescribe = punishdescribe;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPunishNo() {
		return punishNo;
	}
	public void setPunishNo(String punishNo) {
		this.punishNo = punishNo;
	}
	public long getCreateBySource() {
		return createBySource;
	}
	public void setCreateBySource(long createBySource) {
		this.createBySource = createBySource;
	}
	public String getSourceNo() {
		return sourceNo;
	}
	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public long getDutybranchid() {
		return dutybranchid;
	}
	public void setDutybranchid(long dutybranchid) {
		this.dutybranchid = dutybranchid;
	}
	public long getDutypersonid() {
		return dutypersonid;
	}
	public void setDutypersonid(long dutypersonid) {
		this.dutypersonid = dutypersonid;
	}
	public long getCwbstate() {
		return cwbstate;
	}
	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}
	public BigDecimal getCwbPrice() {
		return cwbPrice;
	}
	public void setCwbPrice(BigDecimal cwbPrice) {
		this.cwbPrice = cwbPrice;
	}
	public BigDecimal getPunishInsideprice() {
		return punishInsideprice;
	}
	public void setPunishInsideprice(BigDecimal punishInsideprice) {
		this.punishInsideprice = punishInsideprice;
	}
	public long getPunishbigsort() {
		return punishbigsort;
	}
	public void setPunishbigsort(long punishbigsort) {
		this.punishbigsort = punishbigsort;
	}
	public long getPunishsmallsort() {
		return punishsmallsort;
	}
	public void setPunishsmallsort(long punishsmallsort) {
		this.punishsmallsort = punishsmallsort;
	}
	public long getCreateuserid() {
		return createuserid;
	}
	public void setCreateuserid(long createuserid) {
		this.createuserid = createuserid;
	}
	public String getCreDate() {
		return creDate;
	}
	public void setCreDate(String creDate) {
		this.creDate = creDate;
	}
	public int getPunishcwbstate() {
		return punishcwbstate;
	}
	public void setPunishcwbstate(int punishcwbstate) {
		this.punishcwbstate = punishcwbstate;
	}

	
}
