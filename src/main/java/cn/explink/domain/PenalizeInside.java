package cn.explink.domain;

import java.math.BigDecimal;

//对内扣罚
public class PenalizeInside {
	private long id;//主键id
	private String punishNo;//扣罚单号
	private long createBySource;//创建扣罚单的来源
	private String createBysourcename;
	private String  sourceNo;//来源单号
	private String cwb;//订单号
	private long dutybranchid;//责任机构
	private String dutybranchname;
	private long dutypersonid;//责任人
	private String dutypersonname;
	private long cwbstate;//订单状态
	private String cwbstatename;
	private BigDecimal cwbPrice;//订单价格
	private BigDecimal punishInsideprice;//对内扣罚金额
	private long punishbigsort;//扣罚大类
	private long punishsmallsort;//扣罚小类
	private long createuserid;//创建人
	private String creUserName;
	private String creDate;//创建日期
	private int punishcwbstate;//扣罚单状态
	private String punishcwbstatename;
	private String punishdescribe;//扣罚描述
	private String fileposition;//上传附件的存储的位置
	private long shensutype;//申诉类型
	private String shensudescribe;//申诉描述
	private String shensufileposition;//申诉上传的证据的地址
	private long shensuuserid;//申诉人的userid
	private BigDecimal shenhepunishprice;//申诉罚款
	private long shenhetype;//审核类型
	private String shenhedescribe;//审核描述
	private String shenhefileposition;//审核的附件上传的附件名
	private long shenheuserid;//审核人
	private String shenhedate;//审核时间
	private String shensudate;//申诉时间
	private String punishbigsortname;//扣罚大类中文名
	private String punishsmallsortname;//扣罚小类中文名
	private BigDecimal creategoodpunishprice;//创建时的货物扣罚金额
	private BigDecimal createqitapunishprice;//创建时的其它扣罚金额
	private BigDecimal lastgoodpunishprice;//审核时确定的货物扣罚金额
	private BigDecimal lastqitapunishprice;//审核时的确定的其他扣罚金额
	private String goodpriceremark;//货物金额为汉字时存放的remark
	private String qitapriceremark;//其它金额为汉字的时候存放的remark
	
	
	public String getGoodpriceremark() {
		return goodpriceremark;
	}
	public void setGoodpriceremark(String goodpriceremark) {
		this.goodpriceremark = goodpriceremark;
	}
	public String getQitapriceremark() {
		return qitapriceremark;
	}
	public void setQitapriceremark(String qitapriceremark) {
		this.qitapriceremark = qitapriceremark;
	}
	public BigDecimal getCreategoodpunishprice() {
		return creategoodpunishprice;
	}
	public void setCreategoodpunishprice(BigDecimal creategoodpunishprice) {
		this.creategoodpunishprice = creategoodpunishprice;
	}
	public BigDecimal getCreateqitapunishprice() {
		return createqitapunishprice;
	}
	public void setCreateqitapunishprice(BigDecimal createqitapunishprice) {
		this.createqitapunishprice = createqitapunishprice;
	}
	public BigDecimal getLastgoodpunishprice() {
		return lastgoodpunishprice;
	}
	public void setLastgoodpunishprice(BigDecimal lastgoodpunishprice) {
		this.lastgoodpunishprice = lastgoodpunishprice;
	}
	public BigDecimal getLastqitapunishprice() {
		return lastqitapunishprice;
	}
	public void setLastqitapunishprice(BigDecimal lastqitapunishprice) {
		this.lastqitapunishprice = lastqitapunishprice;
	}
	public String getCreUserName() {
		return creUserName;
	}
	public void setCreUserName(String creUserName) {
		this.creUserName = creUserName;
	}
	public String getCreateBysourcename() {
		return createBysourcename;
	}
	public void setCreateBysourcename(String createBysourcename) {
		this.createBysourcename = createBysourcename;
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
	public String getCwbstatename() {
		return cwbstatename;
	}
	public void setCwbstatename(String cwbstatename) {
		this.cwbstatename = cwbstatename;
	}
	public String getPunishcwbstatename() {
		return punishcwbstatename;
	}
	public void setPunishcwbstatename(String punishcwbstatename) {
		this.punishcwbstatename = punishcwbstatename;
	}
	public String getPunishbigsortname() {
		return punishbigsortname;
	}
	public void setPunishbigsortname(String punishbigsortname) {
		this.punishbigsortname = punishbigsortname;
	}
	public String getPunishsmallsortname() {
		return punishsmallsortname;
	}
	public void setPunishsmallsortname(String punishsmallsortname) {
		this.punishsmallsortname = punishsmallsortname;
	}
	public String getShenhedate() {
		return shenhedate;
	}
	public void setShenhedate(String shenhedate) {
		this.shenhedate = shenhedate;
	}
	public String getShensudate() {
		return shensudate;
	}
	public void setShensudate(String shensudate) {
		this.shensudate = shensudate;
	}
	public long getShenheuserid() {
		return shenheuserid;
	}
	public void setShenheuserid(long shenheuserid) {
		this.shenheuserid = shenheuserid;
	}
	public BigDecimal getShenhepunishprice() {
		return shenhepunishprice;
	}
	public void setShenhepunishprice(BigDecimal shenhepunishprice) {
		this.shenhepunishprice = shenhepunishprice;
	}
	
	public long getShenhetype() {
		return shenhetype;
	}
	public void setShenhetype(long shenhetype) {
		this.shenhetype = shenhetype;
	}
	public String getShenhedescribe() {
		return shenhedescribe;
	}
	public void setShenhedescribe(String shenhedescribe) {
		this.shenhedescribe = shenhedescribe;
	}
	public String getShenhefileposition() {
		return shenhefileposition;
	}
	public void setShenhefileposition(String shenhefileposition) {
		this.shenhefileposition = shenhefileposition;
	}
	public long getShensuuserid() {
		return shensuuserid;
	}
	public void setShensuuserid(long shensuuserid) {
		this.shensuuserid = shensuuserid;
	}
	public long getShensutype() {
		return shensutype;
	}
	public void setShensutype(long shensutype) {
		this.shensutype = shensutype;
	}
	public String getShensudescribe() {
		return shensudescribe;
	}
	public void setShensudescribe(String shensudescribe) {
		this.shensudescribe = shensudescribe;
	}
	public String getShensufileposition() {
		return shensufileposition;
	}
	public void setShensufileposition(String shensufileposition) {
		this.shensufileposition = shensufileposition;
	}
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
