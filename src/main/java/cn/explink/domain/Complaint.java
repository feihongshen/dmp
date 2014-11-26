package cn.explink.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Complaint {
	private long id; // 主键id
	private String cwb;// 订单号
	private long type; // 投诉类型，按枚举类型保存
	private long branchid;// 投诉站点
	private long deliveryid;// 投诉小件员
	private long auditType;// 审核结果，按枚举类型保存
	private String content;// 投诉内容
	private String auditRemark;// 审核备注
	private String createTime;// 投诉创建时间
	private String auditTime;// 投诉审核时间
	private long createUser;// 投诉创建人
	private long auditUser;// 投诉审核人
	private long servertreasonid;// 服务投诉类型对应的reasonid

	public Complaint() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		createTime = sdf.format(new Date());
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

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
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

	public long getAuditType() {
		return auditType;
	}

	public void setAuditType(long auditType) {
		this.auditType = auditType;
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

	public long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}

	public long getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(long auditUser) {
		this.auditUser = auditUser;
	}

	public long getServertreasonid() {
		return servertreasonid;
	}

	public void setServertreasonid(long servertreasonid) {
		this.servertreasonid = servertreasonid;
	}

}
