package cn.explink.b2c.ems;

/**
 * EMS轨迹信息
 * @author huan.zhou
 */
public class EMSFlowObjInitial{
	private long id;
	private String emailnum;
	private int state;
	private String flowContent;
	private String remark;
	private int handleCount;
	private String credate;
	private String action;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getHandleCount() {
		return handleCount;
	}
	public void setHandleCount(int handleCount) {
		this.handleCount = handleCount;
	}
	public String getCredate() {
		return credate;
	}
	public void setCredate(String credate) {
		this.credate = credate;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}

