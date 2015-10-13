package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 中转订单统计表
 * 
 * @author Administrator
 *
 */
public class ZhongZhuan {
	private long id;
	private String cwb;
	private long customerid;
	private String zhongzhuanoutstoreroomtime;
	private long startbranchid;
	private long nextbranchid;
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额
	private long insitebranchid;
	private String inSitetime;
	private long cwbordertypeid;

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

	public String getZhongzhuanoutstoreroomtime() {
		return zhongzhuanoutstoreroomtime;
	}

	public void setZhongzhuanoutstoreroomtime(String zhongzhuanoutstoreroomtime) {
		this.zhongzhuanoutstoreroomtime = zhongzhuanoutstoreroomtime;
	}

	public long getInsitebranchid() {
		return insitebranchid;
	}

	public void setInsitebranchid(long insitebranchid) {
		this.insitebranchid = insitebranchid;
	}

	public String getInSitetime() {
		return inSitetime;
	}

	public void setInSitetime(String inSitetime) {
		this.inSitetime = inSitetime;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

}
