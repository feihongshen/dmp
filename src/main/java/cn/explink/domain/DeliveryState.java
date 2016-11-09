package cn.explink.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.DateDeserializer;
import org.codehaus.jackson.map.ser.std.DateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
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
	private int cwbordertypeid;
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
	private String sign_man_phone;
	private String sign_img;//签收人签名图片url
	public String getSign_man_phone() {
		return sign_man_phone;
	}

	public void setSign_man_phone(String sign_man_phone) {
		this.sign_man_phone = sign_man_phone;
	}

	// 加字段
	private long customerid;// 供货商id

	private long payupid;// 上交款id
	private long issendcustomer;// 是否已推送给第三方
	private long isautolinghuo;// 是否滞留自动领货 0 否 1 是

	private BigDecimal codpos = BigDecimal.ZERO;// 支付宝COD扫码支付

	private String shangmenlanshoutime;// 上门揽收时间

	public String getShangmenlanshoutime() {
		return this.shangmenlanshoutime;
	}

	public void setShangmenlanshoutime(String shangmenlanshoutime) {
		this.shangmenlanshoutime = shangmenlanshoutime;
	}

	public String getDeliverystateStr() {
		return this.deliverystateStr;
	}

	public void setDeliverystateStr(String deliverystateStr) {
		this.deliverystateStr = deliverystateStr;
	}

	public String getCwb() {
		return this.cwb;
	}

	public int getSign_typeid() {
		return this.sign_typeid;
	}

	public void setSign_typeid(int sign_typeid) {
		this.sign_typeid = sign_typeid;
	}

	public String getSign_man() {
		return this.sign_man;
	}

	public void setSign_man(String sign_man) {
		this.sign_man = sign_man;
	}

	public String getSign_time() {
		return this.sign_time;
	}

	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getDeliveryid() {
		return this.deliveryid;
	}

	public void setDeliveryid(long deliveryid) {
		this.deliveryid = deliveryid;
	}

	public BigDecimal getReceivedfee() {
		return this.receivedfee;
	}

	public void setReceivedfee(BigDecimal receivedfee) {
		this.receivedfee = receivedfee;
	}

	public BigDecimal getReturnedfee() {
		return this.returnedfee;
	}

	public void setReturnedfee(BigDecimal returnedfee) {
		this.returnedfee = returnedfee;
	}

	public BigDecimal getBusinessfee() {
		return this.businessfee;
	}

	public void setBusinessfee(BigDecimal businessfee) {
		this.businessfee = businessfee;
	}

	public long getDeliverystate() {
		return this.deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public BigDecimal getCash() {
		return this.cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getPos() {
		return this.pos;
	}

	public void setPos(BigDecimal pos) {
		this.pos = pos;
	}

	public String getPosremark() {
		return this.posremark;
	}

	public void setPosremark(String posremark) {
		this.posremark = posremark;
	}

	public Date getMobilepodtime() {
		return this.mobilepodtime;
	}

	public void setMobilepodtime(Date mobilepodtime) {
		this.mobilepodtime = mobilepodtime;
	}

	public BigDecimal getCheckfee() {
		return this.checkfee;
	}

	public void setCheckfee(BigDecimal checkfee) {
		this.checkfee = checkfee;
	}

	public String getCheckremark() {
		return this.checkremark;
	}

	public void setCheckremark(String checkremark) {
		this.checkremark = checkremark;
	}

	

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public long getReceivedfeeuser() {
		return this.receivedfeeuser;
	}

	public void setReceivedfeeuser(long receivedfeeuser) {
		this.receivedfeeuser = receivedfeeuser;
	}

	public String getCreatetime() {
		return this.createtime == null ? "" : this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public BigDecimal getOtherfee() {
		return this.otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public long getPodremarkid() {
		return this.podremarkid;
	}

	public void setPodremarkid(long podremarkid) {
		this.podremarkid = podremarkid;
	}

	public String getDeliverstateremark() {
		return this.deliverstateremark;
	}

	public void setDeliverstateremark(String deliverstateremark) {
		this.deliverstateremark = deliverstateremark;
	}

	public String getBackreason() {
		return this.backreason;
	}

	public void setBackreason(String backreason) {
		this.backreason = backreason;
	}

	public String getLeavedreason() {
		return this.leavedreason;
	}

	public void setLeavedreason(String leavedreason) {
		this.leavedreason = leavedreason;
	}

	public long getIsout() {
		return this.isout;
	}

	public void setIsout(long isout) {
		this.isout = isout;
	}

	public long getPos_feedback_flag() {
		return this.pos_feedback_flag;
	}

	public void setPos_feedback_flag(long pos_feedback_flag) {
		this.pos_feedback_flag = pos_feedback_flag;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getGcaid() {
		return this.gcaid;
	}

	public void setGcaid(long gcaid) {
		this.gcaid = gcaid;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getDeliverytime() {
		return this.deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getAuditingtime() {
		return this.auditingtime;
	}

	public void setAuditingtime(String auditingtime) {
		this.auditingtime = auditingtime;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getPayupid() {
		return this.payupid;
	}

	public void setPayupid(long payupid) {
		this.payupid = payupid;
	}

	public long getIssendcustomer() {
		return this.issendcustomer;
	}

	public void setIssendcustomer(long issendcustomer) {
		this.issendcustomer = issendcustomer;
	}

	public long getIsautolinghuo() {
		return this.isautolinghuo;
	}

	public void setIsautolinghuo(long isautolinghuo) {
		this.isautolinghuo = isautolinghuo;
	}

	public String getPushtime() {
		return this.pushtime;
	}

	public void setPushtime(String pushtime) {
		this.pushtime = pushtime;
	}

	public long getPushstate() {
		return this.pushstate;
	}

	public void setPushstate(long pushstate) {
		this.pushstate = pushstate;
	}

	public String getPushremarks() {
		return this.pushremarks;
	}

	public void setPushremarks(String pushremarks) {
		this.pushremarks = pushremarks;
	}

	public BigDecimal getCodpos() {
		return this.codpos;
	}

	public void setCodpos(BigDecimal codpos) {
		this.codpos = codpos;
	}

	public BigDecimal getShouldfare() {
		return this.shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public BigDecimal getInfactfare() {
		return this.infactfare;
	}

	public void setInfactfare(BigDecimal infactfare) {
		this.infactfare = infactfare;
	}

	public String getSign_img() {
		return sign_img;
	}

	public void setSign_img(String sign_img) {
		this.sign_img = sign_img;
	}

	@Override
	public String toString() {
		return "DeliveryState [id=" + this.id + ", cwb=" + this.cwb + ", deliveryid=" + this.deliveryid + ", receivedfee=" + this.receivedfee + ", returnedfee=" + this.returnedfee + ", businessfee="
				+ this.businessfee + ", deliverystate=" + this.deliverystate + ", cash=" + this.cash + ", pos=" + this.pos + ", posremark=" + this.posremark + ", checkfee=" + this.checkfee
				+ ", checkremark=" + this.checkremark + ", receivedfeeuser=" + this.receivedfeeuser + ", createtime=" + this.createtime + ", otherfee=" + this.otherfee + ", podremarkid="
				+ this.podremarkid + ", deliverstateremark=" + this.deliverstateremark + ", isout=" + this.isout + ", pos_feedback_flag=" + this.pos_feedback_flag + ", userid=" + this.userid
				+ ", gcaid=" + this.gcaid + ", sign_typeid=" + this.sign_typeid + ", sign_man=" + this.sign_man + ", sign_time=" + this.sign_time + ", backreason=" + this.backreason
				+ ", leavedreason=" + this.leavedreason + ", deliverybranchid=" + this.deliverybranchid + ", deliverystateStr=" + this.deliverystateStr + ", deliverytime=" + this.deliverytime
				+ ", auditingtime=" + this.auditingtime + ", pushtime=" + this.pushtime + ", pushstate=" + this.pushstate + ", pushremarks=" + this.pushremarks + ", customerid=" + this.customerid
				+ ", payupid=" + this.payupid + ", issendcustomer=" + this.issendcustomer + ", isautolinghuo=" + this.isautolinghuo + "]";
	}

}
