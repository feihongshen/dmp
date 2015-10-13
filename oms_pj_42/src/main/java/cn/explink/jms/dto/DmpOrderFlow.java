package cn.explink.jms.dto;

import java.lang.reflect.Method;
import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.explink.enumutil.FlowOrderTypeEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DmpOrderFlow {
	protected long floworderid;
	protected String cwb;
	protected long branchid;
	protected Timestamp credate;
	protected long userid;
	protected int flowordertype;
	protected int isnow;
	protected String floworderdetail;
	protected String username;
	protected String branchname;

	protected String flowordertypeMethod;
	String comment = "";

	public DmpOrderFlow() {
	}

	public DmpOrderFlow(long floworderid, String cwb, long branchid, Timestamp credate, long userid, String floworderdetail, int flowordertype, String comment) {
		super();
		this.floworderid = floworderid;
		this.cwb = cwb;
		this.branchid = branchid;
		this.credate = credate;
		this.userid = userid;
		this.floworderdetail = floworderdetail;
		this.flowordertype = flowordertype;
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return 获得flowordertype的值对应的文字
	 */
	public String getFlowordertypeText() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getText();
		}
		return "";
	}

	/**
	 * @return 获得flowordertype的值对应的解析JSON方法
	 */
	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getMethod();
		}
		return "";
	}

	public String toHtml() throws Exception {

		Method m = this.getClass().getMethod("get" + getFlowordertypeMethod());
		String html = (String) m.invoke(this);
		return html;
	}

	public long getFloworderid() {
		return floworderid;
	}

	public void setFloworderid(long floworderid) {
		this.floworderid = floworderid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public Timestamp getCredate() {
		return credate;
	}

	public void setCredate(Timestamp credate) {
		this.credate = credate;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getFloworderdetail() {
		return floworderdetail;
	}

	public void setFloworderdetail(String floworderdetail) {
		this.floworderdetail = floworderdetail;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public int getIsnow() {
		return isnow;
	}

	public void setIsnow(int isnow) {
		this.isnow = isnow;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public void setFlowordertypeMethod(String flowordertypeMethod) {
		this.flowordertypeMethod = flowordertypeMethod;
	}

}
