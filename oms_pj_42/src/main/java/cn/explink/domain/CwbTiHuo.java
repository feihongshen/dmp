package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 提货统计表
 *
 */
public class CwbTiHuo {
	private long id;
	private String cwb;
	private String emaildate;
	private long currentbranchid;
	private long userid;
	private long cwbordertypeid;
	private long customerid;
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额
	private String tihuotime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getCurrentbranchid() {
		return currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getTihuotime() {
		return tihuotime;
	}

	public void setTihuotime(String tihuotime) {
		this.tihuotime = tihuotime;
	}

}
