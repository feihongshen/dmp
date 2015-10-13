package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 妥投订单汇总表
 * 
 * @author Administrator
 *
 */
public class DeliveryDaohuo {
	private long id;
	private String cwb;
	private String emaildate;
	private String inSitetime;
	private long startbranchid;
	private long currentbranchid;
	private long nextbranchid;
	private String cwbordertypeid;
	private long customerid;
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额
	private long isnow;

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

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getInSitetime() {
		return inSitetime;
	}

	public void setInSitetime(String inSitetime) {
		this.inSitetime = inSitetime;
	}

	public long getStartbranchid() {
		return startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public long getCurrentbranchid() {
		return currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
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

	public long getIsnow() {
		return isnow;
	}

	public void setIsnow(long isnow) {
		this.isnow = isnow;
	}

}
