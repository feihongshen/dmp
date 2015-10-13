package cn.explink.domain;

public class B2CData {

	private long b2cid;
	private String cwb;
	private String posttime;
	private long flowordertype;// 当当只存入库，小件员领货，投递反馈
	private String jsoncontent;
	private long send_b2c_flag;// 推送状态，0未推送，1.推送成功，2推送失败 ,3(垃圾数据标识) 系统自动屏蔽
								// （通过系统自动过滤不推送状态产生20130723 add）
	private long customerid;
	private String shipcwb; // 供货商运单号 tmall暂时用到
	private String remark; // 备注信息 包括存储推送失败的原因
	private long timeout;// 推迟时间推送（单位分钟）
	private String timeoutdate;// 推迟时间
	private long delId;// 反馈表的id

	private long deliverystate;// 反馈结果
	private String sendTime;// 推送时间
	private String sendUser;// 推送操作人
	private long send_payamount_flag; // 支付反馈标示 0未推送，1已推送

	public long getSend_payamount_flag() {
		return send_payamount_flag;
	}

	public void setSend_payamount_flag(long send_payamount_flag) {
		this.send_payamount_flag = send_payamount_flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShipcwb() {
		return shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		this.shipcwb = shipcwb;
	}

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

	public String getJsoncontent() {
		return jsoncontent;
	}

	public void setJsoncontent(String jsoncontent) {
		this.jsoncontent = jsoncontent;
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

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getTimeoutdate() {
		return timeoutdate;
	}

	public void setTimeoutdate(String timeoutdate) {
		this.timeoutdate = timeoutdate;
	}

	public long getDelId() {
		return delId;
	}

	public void setDelId(long delId) {
		this.delId = delId;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

}
