package cn.explink.b2c.huitongtx.addressmatch;

public class HttxEditBranch {
	private long id;
	private String taskcode; // 快行线唯一标识
	private String receiver_city; // 城市
	private String receiver_address; //
	private String cretime; // 产生时间
	private String dealtime; // 处理时间
	private long sendflag; // 推送标识 （0未推送，id推送成功，2推送失败）
	private int matchtype;// 匹配类型(1自动匹配，2人工匹配，0未匹配)
	private String remark; // 备注

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String matchbranch; // 匹配到的站点
	private long branchid; // 匹配到站点的id

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getMatchbranch() {
		return matchbranch;
	}

	public void setMatchbranch(String matchbranch) {
		this.matchbranch = matchbranch;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTaskcode() {
		return taskcode;
	}

	public void setTaskcode(String taskcode) {
		this.taskcode = taskcode;
	}

	public String getReceiver_city() {
		return receiver_city;
	}

	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public String getDealtime() {
		return dealtime;
	}

	public void setDealtime(String dealtime) {
		this.dealtime = dealtime;
	}

	public long getSendflag() {
		return sendflag;
	}

	public void setSendflag(long sendflag) {
		this.sendflag = sendflag;
	}

	public int getMatchtype() {
		return matchtype;
	}

	public void setMatchtype(int matchtype) {
		this.matchtype = matchtype;
	}

}
