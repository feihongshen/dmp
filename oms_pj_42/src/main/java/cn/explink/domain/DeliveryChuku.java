package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 妥投订单汇总表
 * 
 * @author Administrator
 *
 */
public class DeliveryChuku {
	private long id;
	private String cwb;
	private String emaildate;
	private long startbranchid;
	private long nextbranchid;
	private String cwbordertypeid;
	private String outstoreroomtime;
	private long customerid;
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额
	private long outstoreroomtimeuserid;// 出库操作人

	public long getOutstoreroomtimeuserid() {
		return outstoreroomtimeuserid;
	}

	public void setOutstoreroomtimeuserid(long outstoreroomtimeuserid) {
		this.outstoreroomtimeuserid = outstoreroomtimeuserid;
	}

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

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
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

	public long getStartbranchid() {
		return startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public String getOutstoreroomtime() {
		return outstoreroomtime;
	}

	public void setOutstoreroomtime(String outstoreroomtime) {
		this.outstoreroomtime = outstoreroomtime;
	}

}
