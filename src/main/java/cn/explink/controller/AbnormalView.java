package cn.explink.controller;

public class AbnormalView {
	private long id;
	private String cwb;
	private String customerName;
	private String emaildate;
	private String flowordertype;//订单流程
	private String branchName;
	private String creuserName;
	private String abnormalType;
	private String describe;
	private long ishandle;
	private String credatetime;
	private String fileposition;//上传文件所在名称
	private String questionno;//创建问题件时自动创建的问题件号
	private String cwborderType;//订单类型(创建的问题件的订单号的类型)
	private long dealresult;//处理结果（1，问题成立 2.问题不成立）
	private String dealResultContent;//处理结果问题成立或者不成立
	private long dutybrachid;//责任机构
	private String dutybranchname;//责任机构名
	private long dutypersonid;//责任人
	private String dutyperson;//责任人姓名
	private String  isfinecontent;//是否罚款（1，未罚款 2.已罚款）
	private long losebackid;//该问题件是不是丢失返回的，是的话为丢失返回表里面的主键id
	private String losebackContent;//丢失件是否找回
	private String resultdealcontent;//结案处理说明内容
	private String isfindInfo;//问题件是否已经找回0对应未找回 1对应找回
	private String lastdutybranch;//最终审判人
	private String lastdutyuser;//最后审判机构
	private long creuserid;//创建人的id
	private String ishandleName;//问题件当前状态
	
	public String getIshandleName() {
		return ishandleName;
	}

	public void setIshandleName(String ishandleName) {
		this.ishandleName = ishandleName;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getCreuserid() {
		return creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getDutyperson() {
		return dutyperson;
	}

	public void setDutyperson(String dutyperson) {
		this.dutyperson = dutyperson;
	}

	public String getLastdutybranch() {
		return lastdutybranch;
	}

	public void setLastdutybranch(String lastdutybranch) {
		this.lastdutybranch = lastdutybranch;
	}

	public String getLastdutyuser() {
		return lastdutyuser;
	}

	public void setLastdutyuser(String lastdutyuser) {
		this.lastdutyuser = lastdutyuser;
	}

	public String getIsfindInfo() {
		return isfindInfo;
	}

	public void setIsfindInfo(String isfindInfo) {
		this.isfindInfo = isfindInfo;
	}

	public String getDealResultContent() {
		return dealResultContent;
	}

	public void setDealResultContent(String dealResultContent) {
		this.dealResultContent = dealResultContent;
	}

	public String getLosebackContent() {
		return losebackContent;
	}

	public void setLosebackContent(String losebackContent) {
		this.losebackContent = losebackContent;
	}

	public String getIsfinecontent() {
		return isfinecontent;
	}

	public void setIsfinecontent(String isfinecontent) {
		this.isfinecontent = isfinecontent;
	}

	public String getDutybranchname() {
		return dutybranchname;
	}

	public void setDutybranchname(String dutybranchname) {
		this.dutybranchname = dutybranchname;
	}

	public String getCwborderType() {
		return cwborderType;
	}

	public void setCwborderType(String cwborderType) {
		this.cwborderType = cwborderType;
	}

	public String getQuestionno() {
		return questionno;
	}

	public void setQuestionno(String questionno) {
		this.questionno = questionno;
	}




	public long getDealresult() {
		return dealresult;
	}

	public void setDealresult(long dealresult) {
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
	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmaildate() {
		return this.emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getFlowordertype() {
		return this.flowordertype;
	}

	public void setFlowordertype(String flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCreuserName() {
		return this.creuserName;
	}

	public void setCreuserName(String creuserName) {
		this.creuserName = creuserName;
	}

	public String getAbnormalType() {
		return this.abnormalType;
	}

	public void setAbnormalType(String abnormalType) {
		this.abnormalType = abnormalType;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getIshandle() {
		return this.ishandle;
	}

	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}

	public String getCredatetime() {
		return this.credatetime;
	}

	public void setCredatetime(String credatetime) {
		this.credatetime = credatetime;
	}

}
