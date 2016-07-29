package cn.explink.b2c.ems;


public class EMSFlowEntity implements java.io.Serializable {
	private static final long serialVersionUID = -2988538739879650733L;
	private long id;
	private String transcwb;
	private String emailnum;
	private int state;
	private String flowContent;
	private String emsAction;
	private long emsFlowordertype;
	private String properdelivery;
	private String notproperdelivery;
	private int handleCount;
	private Long orderDirection;//订单去向  add by zhouhuan 2016-07-21
	
	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}
	public String getEmailnum() {
		return emailnum;
	}
	public void setEmailnum(String emailnum) {
		this.emailnum = emailnum;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getFlowContent() {
		return flowContent;
	}
	public void setFlowContent(String flowContent) {
		this.flowContent = flowContent;
	}
	public String getEmsAction() {
		return emsAction;
	}
	public void setEmsAction(String emsAction) {
		this.emsAction = emsAction;
	}
	public long getEmsFlowordertype() {
		return emsFlowordertype;
	}
	public void setEmsFlowordertype(long emsFlowordertype) {
		this.emsFlowordertype = emsFlowordertype;
	}
	public String getProperdelivery() {
		return properdelivery;
	}
	public void setProperdelivery(String properdelivery) {
		this.properdelivery = properdelivery;
	}
	public String getNotproperdelivery() {
		return notproperdelivery;
	}
	public void setNotproperdelivery(String notproperdelivery) {
		this.notproperdelivery = notproperdelivery;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getHandleCount() {
		return handleCount;
	}
	public void setHandleCount(int handleCount) {
		this.handleCount = handleCount;
	}
	public Long getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(Long orderDirection) {
		this.orderDirection = orderDirection;
	}
	
}
