package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.DateDeserializer;
import org.codehaus.jackson.map.ser.std.DateSerializer;

public class DeliveryState {
	private long id;
	private String cwb;
	private long deliveryid;
	private BigDecimal receivedfee = BigDecimal.ZERO;
	private BigDecimal returnedfee = BigDecimal.ZERO;
	private BigDecimal businessfee = BigDecimal.ZERO;
	private long deliverystate;
	private BigDecimal cash = BigDecimal.ZERO;
	private BigDecimal pos = BigDecimal.ZERO;
	private String posremark;
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private Date mobilepodtime;
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
	private long gcaid;
	private int sign_typeid; // 是否签收 0未签收，1已签收
	private String sign_man; // 签收人
	private String sign_time; // 签收时间
	private String backreason;// 退货原因
	private String leavedreason;// 滞留原因
	private long deliverybranchid;// 配送站点
	private String deliverystateStr;
	private String deliverytime;// 反馈时间
	private String auditingtime;// 归班时间
	private String pushtime;// 推送时间
	private long pushstate;// 推送状态
	private String pushremarks;// 推送备注
	private BigDecimal shouldfare = BigDecimal.ZERO;
	private BigDecimal infactfare = BigDecimal.ZERO;

	// 加字段
	private long customerid;// 供货商id

	private long payupid;// 上交款id
	private long issendcustomer;// 是否已推送给第三方
	private long isautolinghuo;// 是否滞留自动领货 0 否 1 是

	private BigDecimal codpos = BigDecimal.ZERO;// 支付宝COD扫码支付

	private String shangmenlanshoutime;// 上门揽收时间

	public String getShangmenlanshoutime() {
		return shangmenlanshoutime;
	}

	public void setShangmenlanshoutime(String shangmenlanshoutime) {
		this.shangmenlanshoutime = shangmenlanshoutime;
	}

	public String getDeliverystateStr() {
		return deliverystateStr;
	}

	public void setDeliverystateStr(String deliverystateStr) {
		this.deliverystateStr = deliverystateStr;
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

	public Date getMobilepodtime() {
		return mobilepodtime;
	}

	public void setMobilepodtime(Date mobilepodtime) {
		this.mobilepodtime = mobilepodtime;
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
		return createtime == null ? "" : createtime;
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

	public long getIsout() {
		return isout;
	}

	public void setIsout(long isout) {
		this.isout = isout;
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

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getPayupid() {
		return payupid;
	}

	public void setPayupid(long payupid) {
		this.payupid = payupid;
	}

	public long getIssendcustomer() {
		return issendcustomer;
	}

	public void setIssendcustomer(long issendcustomer) {
		this.issendcustomer = issendcustomer;
	}

	public long getIsautolinghuo() {
		return isautolinghuo;
	}

	public void setIsautolinghuo(long isautolinghuo) {
		this.isautolinghuo = isautolinghuo;
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

	public BigDecimal getShouldfare() {
		return shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public BigDecimal getInfactfare() {
		return infactfare;
	}

	public void setInfactfare(BigDecimal infactfare) {
		this.infactfare = infactfare;
	}

	@Override
	public String toString() {
		return "DeliveryState [id=" + id + ", cwb=" + cwb + ", deliveryid=" + deliveryid + ", receivedfee=" + receivedfee + ", returnedfee=" + returnedfee + ", businessfee=" + businessfee
				+ ", deliverystate=" + deliverystate + ", cash=" + cash + ", pos=" + pos + ", posremark=" + posremark + ", checkfee=" + checkfee + ", checkremark=" + checkremark
				+ ", receivedfeeuser=" + receivedfeeuser + ", createtime=" + createtime + ", otherfee=" + otherfee + ", podremarkid=" + podremarkid + ", deliverstateremark=" + deliverstateremark
				+ ", isout=" + isout + ", pos_feedback_flag=" + pos_feedback_flag + ", userid=" + userid + ", gcaid=" + gcaid + ", sign_typeid=" + sign_typeid + ", sign_man=" + sign_man
				+ ", sign_time=" + sign_time + ", backreason=" + backreason + ", leavedreason=" + leavedreason + ", deliverybranchid=" + deliverybranchid + ", deliverystateStr=" + deliverystateStr
				+ ", deliverytime=" + deliverytime + ", auditingtime=" + auditingtime + ", pushtime=" + pushtime + ", pushstate=" + pushstate + ", pushremarks=" + pushremarks + ", customerid="
				+ customerid + ", payupid=" + payupid + ", issendcustomer=" + issendcustomer + ", isautolinghuo=" + isautolinghuo + "]";
	}

}
