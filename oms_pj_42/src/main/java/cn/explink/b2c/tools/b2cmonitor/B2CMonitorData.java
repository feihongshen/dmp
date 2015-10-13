package cn.explink.b2c.tools.b2cmonitor;

public class B2CMonitorData {

	private long b2cid;
	private String cwb;
	private String posttime;
	private long flowordertype;// 当当只存入库，小件员领货，投递反馈
	private long send_b2c_flag;// 推送状态，0未推送，1.推送成功，2推送失败)
	private long customerid;
	private String expt_reason; // 异常原因
	private String remark; // 备注信息
	private int hand_deal_flag; // 客服处理备注 是否处理 0,未处理，1已处理

	public long getB2cid() {
		return b2cid;
	}

	public void setB2cid(long b2cid) {
		this.b2cid = b2cid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getSend_b2c_flag() {
		return send_b2c_flag;
	}

	public void setSend_b2c_flag(long send_b2c_flag) {
		this.send_b2c_flag = send_b2c_flag;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getExpt_reason() {
		return expt_reason;
	}

	public void setExpt_reason(String expt_reason) {
		this.expt_reason = expt_reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getHand_deal_flag() {
		return hand_deal_flag;
	}

	public void setHand_deal_flag(int hand_deal_flag) {
		this.hand_deal_flag = hand_deal_flag;
	}

}
