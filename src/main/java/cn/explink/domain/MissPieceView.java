package cn.explink.domain;

public class MissPieceView {
	private long id;//丢失订单id
	private String cwb;//订单号
	private String customername;//供货商
	private String ordertype;//订单类型
	private String flowordertypeName;//当时状态
	private String callbackbranchname;//找回机构
	private String createtime;//创建时间
	private String creusername;//创建人
	private String questionno;//问题件号
	private String describe;//丢失件说明
	
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
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	public String getFlowordertypeName() {
		return flowordertypeName;
	}
	public void setFlowordertypeName(String flowordertypeName) {
		this.flowordertypeName = flowordertypeName;
	}
	public String getCallbackbranchname() {
		return callbackbranchname;
	}
	public void setCallbackbranchname(String callbackbranchname) {
		this.callbackbranchname = callbackbranchname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreusername() {
		return creusername;
	}
	public void setCreusername(String creusername) {
		this.creusername = creusername;
	}
	public String getQuestionno() {
		return questionno;
	}
	public void setQuestionno(String questionno) {
		this.questionno = questionno;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	
}
