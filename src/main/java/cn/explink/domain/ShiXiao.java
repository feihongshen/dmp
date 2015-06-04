package cn.explink.domain;

public class ShiXiao {

	private long id; // 主键id
	private long opscwbid; // cwb主键id
	private long startbranchid; // 上一个机构id
	private long currentbranchid; // 当前机构
	private long nextbranchid; // 下一站目的机构id
	private long deliverybranchid; // 配送站点
	private long flowordertype; // 操作类型，具体对应枚举类FlowOrderTypeEnum
	private String cwb;// 订单号
	private long userid;// 操作人
	private String cretime;// 操作时间
	private long customerid;// 供货商id
	private long shixiaoreasonid;//失效原因id
	private long cwbstate;//订单状态
	private String emaildate;//发货批次时间

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getShixiaoreasonid() {
		return shixiaoreasonid;
	}

	public void setShixiaoreasonid(long shixiaoreasonid) {
		this.shixiaoreasonid = shixiaoreasonid;
	}

	public long getCwbstate() {
		return cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getOpscwbid() {
		return opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
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

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getCurrentbranchid() {
		return currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

}
