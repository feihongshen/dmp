package cn.explink.jms.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DmpDeliveryState {
	private long id;
	private String cwb;
	private long deliveryid;
	private BigDecimal receivedfee = BigDecimal.ZERO;
	private BigDecimal returnedfee = BigDecimal.ZERO;
	private BigDecimal businessfee = BigDecimal.ZERO;
	private String cwbordertypeid;
	private long deliverystate;
	private BigDecimal cash = BigDecimal.ZERO;
	private BigDecimal pos = BigDecimal.ZERO;
	private BigDecimal codpos = BigDecimal.ZERO;
	private String posremark;
	// private Date mobilepodtime;
	private BigDecimal checkfee = BigDecimal.ZERO;
	private String checkremark;
	private long receivedfeeuser;
	private String createtime;
	private BigDecimal otherfee = BigDecimal.ZERO;
	private long podremarkid;
	private String deliverstateremark;
	private long isout;
	private long pos_feedback_flag;
	private long userid;
	private String consigneename;
	private String consigneephone;
	private String consigneemobile;
	private long gcaid;
	private int sign_typeid; // 是否签收 0未签收，1已签收
	private String sign_man; // 签收人
	private String sign_time; // 签收时间

	private long deliverybranchid;// 配送站点
	private String deliverystateStr;
	private String deliverytime;// 反馈时间
	private String auditingtime;// 归班时间
	private String pushtime;// 推送时间
	private long pushstate;// 推送状态
	private String pushremarks;// 推送备注

	// 附加的功能性字段
	private long customerid;// 供货商编号
	private String consigneeaddress;// 送货地址
	private String emaildate;
	private long sendcarnum;// 发货数量
	private String sendcarname;// 发货商品名

	private long backcarnum;// 取货货数量
	private String backcarname;// 取回货物商品名

	private String deliverealname;// 小件员姓名
	private long flowordertype;// 环节类型
	private String backreason;// 退货原因
	private String leavedreason;// 滞留原因

	private String shangmenlanshoutime;// 上门揽收时间

	public String getShangmenlanshoutime() {
		return shangmenlanshoutime;
	}

	public void setShangmenlanshoutime(String shangmenlanshoutime) {
		this.shangmenlanshoutime = shangmenlanshoutime;
	}

	public String getCwb() {
		return cwb;
	}

	public int getSign_typeid() {
		return sign_typeid;
	}

	public void setSign_typeid(int sign_typeid) {
		this.sign_typeid = sign_typeid;
	}

	public String getSign_man() {
		return sign_man;
	}

	public void setSign_man(String sign_man) {
		this.sign_man = sign_man;
	}

	public String getSign_time() {
		return sign_time;
	}

	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(long deliveryid) {
		this.deliveryid = deliveryid;
	}

	public BigDecimal getReceivedfee() {
		return receivedfee;
	}

	public void setReceivedfee(BigDecimal receivedfee) {
		this.receivedfee = receivedfee;
	}

	public BigDecimal getReturnedfee() {
		return returnedfee;
	}

	public void setReturnedfee(BigDecimal returnedfee) {
		this.returnedfee = returnedfee;
	}

	public BigDecimal getBusinessfee() {
		return businessfee;
	}

	public void setBusinessfee(BigDecimal businessfee) {
		this.businessfee = businessfee;
	}

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getPos() {
		return pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public String getPosremark() {
		return posremark;
	}

	public void setPosremark(String posremark) {
		this.posremark = posremark;
	}

	public BigDecimal getCheckfee() {
		return checkfee;
	}

	public void setCheckfee(BigDecimal checkfee) {
		this.checkfee = checkfee;
	}

	public String getCheckremark() {
		return checkremark;
	}

	public void setCheckremark(String checkremark) {
		this.checkremark = checkremark;
	}

	public long getReceivedfeeuser() {
		return receivedfeeuser;
	}

	public void setReceivedfeeuser(long receivedfeeuser) {
		this.receivedfeeuser = receivedfeeuser;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public BigDecimal getOtherfee() {
		return otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public long getPodremarkid() {
		return podremarkid;
	}

	public void setPodremarkid(long podremarkid) {
		this.podremarkid = podremarkid;
	}

	public String getDeliverstateremark() {
		return deliverstateremark;
	}

	public void setDeliverstateremark(String deliverstateremark) {
		this.deliverstateremark = deliverstateremark;
	}

	public String getBackreason() {
		return backreason;
	}

	public void setBackreason(String backreason) {
		this.backreason = backreason;
	}

	public String getLeavedreason() {
		return leavedreason;
	}

	public void setLeavedreason(String leavedreason) {
		this.leavedreason = leavedreason;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public String getSendcarname() {
		return sendcarname;
	}

	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}

	public long getBackcarnum() {
		return backcarnum;
	}

	public void setBackcarnum(long backcarnum) {
		this.backcarnum = backcarnum;
	}

	public String getBackcarname() {
		return backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public String getDeliverealname() {
		return deliverealname;
	}

	public void setDeliverealname(String deliverealname) {
		this.deliverealname = deliverealname;
	}

	public long getIsout() {
		return isout;
	}

	public void setIsout(long isout) {
		this.isout = isout;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getPos_feedback_flag() {
		return pos_feedback_flag;
	}

	public void setPos_feedback_flag(long pos_feedback_flag) {
		this.pos_feedback_flag = pos_feedback_flag;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
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

	/**
	 * @return 返回差异金额
	 */
	public double getDifference() {
		return receivedfee.compareTo(returnedfee) > 0 ? receivedfee.doubleValue() : (returnedfee.doubleValue() * -1);
	}

	/**
	 * @return 返回反馈记录中的交款方式
	 */
	public String getPaymentPattern() {
		String reStr = "";
		if (this.cash.compareTo(BigDecimal.ZERO) > 0) {
			reStr += "现金";
		}
		if (this.pos.compareTo(BigDecimal.ZERO) > 0) {
			reStr += reStr.length() > 0 ? "/" : "";
			reStr += "POS机";
		}
		if (this.checkfee.compareTo(BigDecimal.ZERO) > 0) {
			reStr += reStr.length() > 0 ? "/" : "";
			reStr += "支票";
		}
		if (this.otherfee.compareTo(BigDecimal.ZERO) > 0) {
			reStr += reStr.length() > 0 ? "/" : "";
			reStr += "其他方式";
		}
		if (reStr.length() == 0) {
			reStr += reStr.length() > 0 ? "/" : "";
			reStr += "现金";
		}
		return reStr;
	}

	/**
	 * @return 返回相应收款方式的备注
	 */
	public String getRemarks() {
		String reStr = "";
		if (!this.pos.equals(BigDecimal.ZERO)) {
			reStr += "　POS:" + this.posremark;
		}
		if (!this.checkfee.equals(BigDecimal.ZERO)) {
			reStr += "　支票号：" + this.checkremark;
		}
		reStr += "　自定义：" + this.deliverstateremark;
		return reStr;
	}

	public long getGcaid() {
		return gcaid;
	}

	public void setGcaid(long gcaid) {
		this.gcaid = gcaid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getDeliverystateStr() {
		return deliverystateStr;
	}

	public void setDeliverystateStr(String deliverystateStr) {
		this.deliverystateStr = deliverystateStr;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getAuditingtime() {
		return auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public String getPushtime() {
		return pushtime;
	}

	public void setPushtime(String pushtime) {
		this.pushtime = pushtime;
	}

	public long getPushstate() {
		return pushstate;
	}

	public void setPushstate(long pushstate) {
		this.pushstate = pushstate;
	}

	public String getPushremarks() {
		return pushremarks;
	}

	public void setPushremarks(String pushremarks) {
		this.pushremarks = pushremarks;
	}

	public BigDecimal getCodpos() {
		return codpos;
	}

	public void setCodpos(BigDecimal codpos) {
		this.codpos = codpos;
	}

}
