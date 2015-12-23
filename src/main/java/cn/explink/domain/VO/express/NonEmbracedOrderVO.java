package cn.explink.domain.VO.express;


public class NonEmbracedOrderVO {
	/*
	 * 订单主键
	 */
	private long opscwbid;
	/*
	 * 运单号
	 */
	private String cwb;
	/*
	 * 件数
	 */
	private long sendcarnum;
	/*
	 * 揽件员
	 */
	private String collectorname;
	/*
	 * 揽件时间（揽件录入时间）
	 */
	private String inputdatetime;

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getSendcarnum() {
		return this.sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public String getCollectorname() {
		return this.collectorname;
	}

	public void setCollectorname(String collectorname) {
		this.collectorname = collectorname;
	}

	public String getInputdatetime() {
		return this.inputdatetime;
	}

	public void setInputdatetime(String inputdatetime) {
		this.inputdatetime = inputdatetime;
	}
}
