package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 货物丢失表
 *
 */
public class CwbDiuShi {
	private long id;
	private String cwb;
	private String shenhetime;
	private long cwbordertypeid;
	private long customerid;
	private long userid;// 审核人
	private long branchid;// 货物丢失站点id
	private BigDecimal receivablefee = BigDecimal.ZERO;// 代收货款应收金额
	private BigDecimal paybackfee = BigDecimal.ZERO;// 上门退货应退金额
	private BigDecimal caramount = BigDecimal.ZERO;// 货物金额
	private BigDecimal payamount = BigDecimal.ZERO;// 赔偿金额
	private long ishandle;// 是否标记为已处理(0未处理，1已处理）
	private long isendstate;// 最终状态是否为丢失(0否 1是)
	private String handletime;// 处理时间
	private long handleuserid;// 处理人

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

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
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

	public String getShenhetime() {
		return shenhetime;
	}

	public void setShenhetime(String shenhetime) {
		this.shenhetime = shenhetime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public BigDecimal getCaramount() {
		return caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getIshandle() {
		return ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public long getIsendstate() {
		return isendstate;
	}

	public void setIsendstate(long isendstate) {
		this.isendstate = isendstate;
	}

	public BigDecimal getPayamount() {
		return payamount;
	}

	public void setPayamount(BigDecimal payamount) {
		this.payamount = payamount;
	}

	public String getHandletime() {
		return handletime;
	}

	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}

	public long getHandleuserid() {
		return handleuserid;
	}

	public void setHandleuserid(long handleuserid) {
		this.handleuserid = handleuserid;
	}

}
