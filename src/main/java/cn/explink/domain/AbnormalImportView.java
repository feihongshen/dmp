package cn.explink.domain;

public class AbnormalImportView {
	private CwbOrder cwbOrder;
	private  User user;
	private String nowtime;
	private long abnormaltypeid;
	private long action;
	private long handleBranch;
	private String filepath;
	private String abnormalinfo;
	private String questionNo;
	private long isfind;
	private long ishandle;
	private long systemtime;
	
	public long getSystemtime() {
		return systemtime;
	}
	public void setSystemtime(long systemtime) {
		this.systemtime = systemtime;
	}
	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}
	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getNowtime() {
		return nowtime;
	}
	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}
	public long getAbnormaltypeid() {
		return abnormaltypeid;
	}
	public void setAbnormaltypeid(long abnormaltypeid) {
		this.abnormaltypeid = abnormaltypeid;
	}
	public long getAction() {
		return action;
	}
	public void setAction(long action) {
		this.action = action;
	}
	public long getHandleBranch() {
		return handleBranch;
	}
	public void setHandleBranch(long handleBranch) {
		this.handleBranch = handleBranch;
	}
	
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getAbnormalinfo() {
		return abnormalinfo;
	}
	public void setAbnormalinfo(String abnormalinfo) {
		this.abnormalinfo = abnormalinfo;
	}
	public String getQuestionNo() {
		return questionNo;
	}
	public void setQuestionNo(String questionNo) {
		this.questionNo = questionNo;
	}
	public long getIsfind() {
		return isfind;
	}
	public void setIsfind(long isfind) {
		this.isfind = isfind;
	}
	public long getIshandle() {
		return ishandle;
	}
	public void setIshandle(long ishandle) {
		this.ishandle = ishandle;
	}
	
}
