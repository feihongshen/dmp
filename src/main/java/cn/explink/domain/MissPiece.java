package cn.explink.domain;

public class MissPiece {
	private long id;//主键
	private String cwb;//订单号
	private long customerid;//供货商
	private long cwbordertypeid;//订单类型
	private long flowordertype;//操作流程
	private String createtime;//创建时间
	private long creuserid;//创建人
	private String questionno;//问题件号
	private long callbackbranchid;//找回机构
	private String describeinfo;//描述
	private String filepath;//上传的文件路径
	private String state;//是否有效1.有效。0.无效
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	public long getCwbordertypeid() {
		return cwbordertypeid;
	}
	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}
	public long getFlowordertype() {
		return flowordertype;
	}
	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public long getCreuserid() {
		return creuserid;
	}
	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}
	public String getQuestionno() {
		return questionno;
	}
	public void setQuestionno(String questionno) {
		this.questionno = questionno;
	}

	public long getCallbackbranchid() {
		return callbackbranchid;
	}
	public void setCallbackbranchid(long callbackbranchid) {
		this.callbackbranchid = callbackbranchid;
	}
	public String getDescribeinfo() {
		return describeinfo;
	}
	public void setDescribeinfo(String describeinfo) {
		this.describeinfo = describeinfo;
	}

	
}
