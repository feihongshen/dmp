package cn.explink.controller;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ComplaintView {
	private String cwb = "";// 订单号
	private String delivername = ""; // 投诉小件员的名称
	private String cwbdelivername = ""; // 当前小件员的名称
	private String customername = "";// 供货商的名称
	private String emaildate = "";// 邮件时间
	private String instoreroomtime = "";// 入库时间
	private String outstoreroomtime = "";// 出库时间
	private String inSitetime = "";// 到站时间
	private String goclasstime = "";// 归班时间
	private long deliverystate; // 配送状态（配送成功、上门退成功、上门换成功、全部退货、部分退货、分站滞留、上门拒退、货物丢失）
	private long orderflowtype; // 订单当前状态
	private String consigneename = "";// 收件人
	private String consigneeaddress = "";// 收件人地址
	private String consigneephone;// 收件人电话
	private String consigneemobile;// 收件人手机
	private String branchname = "";// 投诉站点

	private long id; // 投诉类型，按枚举类型保存
	private long type; // 投诉类型，按枚举类型保存
	private long auditType;// 审核结果，-1 未审核，0 审核成失败，1审核成成功
	private String content;// 投诉内容
	private String auditRemark;// 审核备注
	private String createTime;// 投诉创建时间
	private String auditTime;// 投诉审核时间

	private long branchid;// 投诉站点
	private long deliveryid;// 投诉小件员
	private String createUser;// 投诉创建人
	private long auditUser;// 投诉审核人

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getDelivername() {
		return delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getInstoreroomtime() {
		return instoreroomtime;
	}

	public void setInstoreroomtime(String instoreroomtime) {
		this.instoreroomtime = instoreroomtime;
	}

	public String getOutstoreroomtime() {
		return outstoreroomtime;
	}

	public void setOutstoreroomtime(String outstoreroomtime) {
		this.outstoreroomtime = outstoreroomtime;
	}

	public String getInSitetime() {
		return inSitetime;
	}

	public void setInSitetime(String inSitetime) {
		this.inSitetime = inSitetime;
	}

	public String getGoclasstime() {
		return goclasstime;
	}

	public void setGoclasstime(String goclasstime) {
		this.goclasstime = goclasstime;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public String getCwbdelivername() {
		return cwbdelivername;
	}

	public void setCwbdelivername(String cwbdelivername) {
		this.cwbdelivername = cwbdelivername;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public long getAuditType() {
		return auditType;
	}

	public void setAuditType(long auditType) {
		this.auditType = auditType;
	}

	public long getOrderflowtype() {
		return orderflowtype;
	}

	public void setOrderflowtype(long orderflowtype) {
		this.orderflowtype = orderflowtype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(long deliveryid) {
		this.deliveryid = deliveryid;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public long getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(long auditUser) {
		this.auditUser = auditUser;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

}
