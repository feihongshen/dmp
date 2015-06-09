package cn.explink.domain;

public class AbnormalOrder {
	private long id;
	private long opscwbid;
	private long customerid;
	private long ishandle;
	private String describe;
	private long abnormaltypeid;
	private long creuserid;
	private String credatetime;
	private long branchid;
	private long isnow;
	private long flowordertype;
	private long deliverybranchid;
	private String emaildate;
	private String cwb;
	private long handleBranch;
	private String fileposition;//上传文件所在名称
	private String questionno;//创建问题件时自动创建的问题件号
	private String cwbordertypeid;//创建的问题件的订单号的类型
	private String dealresult;//处理结果（1，问题成立 2.问题不成立）
	private long dutybrachid;//责任机构
	private long dutypersonid;//责任人id
	private long isfine;//是否罚款（1，未罚款 2.已罚款）
	private long losebackid;//该问题件是不是丢失返回的，是的话为丢失返回表里面的主键id
	private String resultdealcontent;//结案处理说明内容
	private long isfind;//是否已经找回的字段，创建问题件的时候为0 未找回 ，在丢失找回表里面出现的话为1
	private long lastdutybranchid;//最终审判机构
	private long lastdutyuserid;//最终审判人
	public long getLastdutybranchid() {
		return lastdutybranchid;
	}

	public void setLastdutybranchid(long lastdutybranchid) {
		this.lastdutybranchid = lastdutybranchid;
	}

	public long getLastdutyuserid() {
		return lastdutyuserid;
	}

	public void setLastdutyuserid(long lastdutyuserid) {
		this.lastdutyuserid = lastdutyuserid;
	}

	public long getIsfind() {
		return isfind;
	}

	public void setIsfind(long isfind) {
		this.isfind = isfind;
	}

	public String getQuestionno() {
		return questionno;
	}

	public void setQuestionno(String questionno) {
		this.questionno = questionno;
	}

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getDealresult() {
		return dealresult;
	}

	public void setDealresult(String dealresult) {
		this.dealresult = dealresult;
	}

	public long getDutybrachid() {
		return dutybrachid;
	}

	public void setDutybrachid(long dutybrachid) {
		this.dutybrachid = dutybrachid;
	}



	public long getDutypersonid() {
		return dutypersonid;
	}

	public void setDutypersonid(long dutypersonid) {
		this.dutypersonid = dutypersonid;
	}

	public long getIsfine() {
		return isfine;
	}

	public void setIsfine(long isfine) {
		this.isfine = isfine;
	}

	public long getLosebackid() {
		return losebackid;
	}

	public void setLosebackid(long losebackid) {
		this.losebackid = losebackid;
	}

	public String getResultdealcontent() {
		return resultdealcontent;
	}

	public void setResultdealcontent(String resultdealcontent) {
		this.resultdealcontent = resultdealcontent;
	}

	public String getFileposition() {
		return fileposition;
	}

	public void setFileposition(String fileposition) {
		this.fileposition = fileposition;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getAbnormaltypeid() {
		return this.abnormaltypeid;
	}

	public void setAbnormaltypeid(long abnormaltypeid) {
		this.abnormaltypeid = abnormaltypeid;
	}

	public long getCreuserid() {
		return this.creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getCredatetime() {
		return this.credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getIshandle() {
		return this.ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getIsnow() {
		return this.isnow;
	}

	public void setIsnow(long isnow) {
		this.isnow = isnow;
	}


	public long getFlowordertype() {
		return this.flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getHandleBranch() {
		return this.handleBranch;
	}

	public void setHandleBranch(long handleBranch) {
		this.handleBranch = handleBranch;
	}

}
