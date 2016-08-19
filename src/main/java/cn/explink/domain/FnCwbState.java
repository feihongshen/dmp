package cn.explink.domain;

import java.util.Date;

@SuppressWarnings("serial")
public class FnCwbState implements java.io.Serializable {

	private Long id;
	
	private String cwb; //订单号
	
	private int cwbordertypeid; //订单类型
	
	private long customerid; //客户id
	
	private int smtfreightflag;//上门退运费收款状态
	
	private Date smtfreightTime;//上门退运费收款时间
	
	private int receivablefeeflag; //代收货款收款状态
	
	private Date receivablefeeTime;//代收货款收款时间
	
	private int expressfreightflag;//快递运费收款状态
	
	private Date expressfreightTime;//快递运费收款时间
	
	private Date createdTime;//创建时间
	
	private Date updatedTime; //更新时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public int getSmtfreightflag() {
		return smtfreightflag;
	}

	public void setSmtfreightflag(int smtfreightflag) {
		this.smtfreightflag = smtfreightflag;
	}

	public Date getSmtfreightTime() {
		return smtfreightTime;
	}

	public void setSmtfreightTime(Date smtfreightTime) {
		this.smtfreightTime = smtfreightTime;
	}

	public int getReceivablefeeflag() {
		return receivablefeeflag;
	}

	public void setReceivablefeeflag(int receivablefeeflag) {
		this.receivablefeeflag = receivablefeeflag;
	}

	public Date getReceivablefeeTime() {
		return receivablefeeTime;
	}

	public void setReceivablefeeTime(Date receivablefeeTime) {
		this.receivablefeeTime = receivablefeeTime;
	}

	public int getExpressfreightflag() {
		return expressfreightflag;
	}

	public void setExpressfreightflag(int expressfreightflag) {
		this.expressfreightflag = expressfreightflag;
	}

	public Date getExpressfreightTime() {
		return expressfreightTime;
	}

	public void setExpressfreightTime(Date expressfreightTime) {
		this.expressfreightTime = expressfreightTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
	
	
}
