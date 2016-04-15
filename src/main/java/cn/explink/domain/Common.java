package cn.explink.domain;

public class Common {

	private long id;
	private String commonname;
	private String commonnumber;
	private String orderprefix;
	private long commonstate;
	private long branchid;
	private long userid;
	private String private_key; //
	private long isopenflag; //
	private String sensitiveWord ;//敏感词 支持多个敏感词替换 格式：A|B|C|D
	
	public String getSensitiveWord() {
		return sensitiveWord;
	}

	public void setSensitiveWord(String sensitiveWord) {
		this.sensitiveWord = sensitiveWord;
	}

	private String feedback_url;// 上游回传下游URL
	private long isasynchronous; // 是否异步回传 0关闭，1开启

	public long getIsasynchronous() {
		return isasynchronous;
	}

	public void setIsasynchronous(long isasynchronous) {
		this.isasynchronous = isasynchronous;
	}

	public long getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(long loopcount) {
		this.loopcount = loopcount;
	}

	private String phone;// 查单电话
	private long loopcount; // 重发次数

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFeedback_url() {
		return feedback_url;
	}

	public void setFeedback_url(String feedback_url) {
		this.feedback_url = feedback_url;
	}

	public long getIsopenflag() {
		return isopenflag;
	}

	public void setIsopenflag(long isopenflag) {
		this.isopenflag = isopenflag;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	private long pageSize; // ÿ�β�ѯ���ֵ

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCommonname() {
		return commonname;
	}

	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}

	public String getCommonnumber() {
		return commonnumber;
	}

	public void setCommonnumber(String commonnumber) {
		this.commonnumber = commonnumber;
	}

	public String getOrderprefix() {
		return orderprefix;
	}

	public void setOrderprefix(String orderprefix) {
		this.orderprefix = orderprefix;
	}

	public long getCommonstate() {
		return commonstate;
	}

	public void setCommonstate(long commonstate) {
		this.commonstate = commonstate;
	}

}
