package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

public class OrderbackRecord {
	private int id;//主键
	private String cwb;
	private long customerid;
	private int cwbordertypeid;
	private int shenhestate;//审核状态
	private String createtime;//退供货商出库时间
	private BigDecimal receivablefee;//应收金额
	private String emaildate;//发货时间
	
	private String auditname;//确认人
	private String audittime;//确认时间
	
	public String getAuditname() {
		return auditname;
	}
	public void setAuditname(String auditname) {
		this.auditname = auditname;
	}
	public String getAudittime() {
		return audittime;
	}
	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	
	public int getCwbordertypeid() {
		return cwbordertypeid;
	}
	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}
	public int getShenhestate() {
		return shenhestate;
	}
	public void setShenhestate(int shenhestate) {
		this.shenhestate = shenhestate;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public BigDecimal getReceivablefee() {
		return receivablefee;
	}
	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}
	public String getEmaildate() {
		return emaildate;
	}
	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}
	
}
