package cn.explink.domain;

/**
 * 上游oms 订单反馈dmp 的临时bean
 * 
 * @author Administrator
 *
 */
public class CommenSendData {

	private long id;
	private String cwb;
	private long customerid;
	private long startbranchid;
	private String commencode; // 承运商编码
	private String datajson; // json格式
	private String posttime; // 插入时间
	private String statetime; // 下游回传的状态产生时间
	private int flowordertype;
	private int deliverystate;
	private String state; // 默认为'',成功则存储时间格式 ,2为失败
	private String reason; // 存储失败原因
	private String custid; // 唯一标识

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public long getStartbranchid() {
		return startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public String getCommencode() {
		return commencode;
	}

	public void setCommencode(String commencode) {
		this.commencode = commencode;
	}

	public String getDatajson() {
		return datajson;
	}

	public void setDatajson(String datajson) {
		this.datajson = datajson;
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}

	public String getStatetime() {
		return statetime;
	}

	public void setStatetime(String statetime) {
		this.statetime = statetime;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public int getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(int deliverystate) {
		this.deliverystate = deliverystate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
