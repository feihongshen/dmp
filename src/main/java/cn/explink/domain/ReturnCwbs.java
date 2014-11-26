package cn.explink.domain;

public class ReturnCwbs {

	private long id;// 主键id
	private long opscwbid; // cwb对应的id
	private String cwb;// 订单号
	private long userid;// 操作人id
	private long branchid; // 操作机构id
	private String createtime;// 操作时间
	private long type; // 操作类型，具体对应枚举类ReturnCwbsTypeEnum
	private long customerid;// 供货商id
	private long tobranchid;// 去往的站点id
	private String isnow;// 是否是当前状态0为否

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOpscwbid() {
		return opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getTobranchid() {
		return tobranchid;
	}

	public void setTobranchid(long tobranchid) {
		this.tobranchid = tobranchid;
	}

	public String getIsnow() {
		return isnow;
	}

	public void setIsnow(String isnow) {
		this.isnow = isnow;
	}

}
