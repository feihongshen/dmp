package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeliveryStateView {
	private long id;
	private String cwb;
	private long deliveryid;
	private BigDecimal receivedfee = BigDecimal.ZERO;
	private BigDecimal returnedfee = BigDecimal.ZERO;
	private BigDecimal businessfee = BigDecimal.ZERO;
	private int cwbordertypeid;
	private long deliverystate;
	private BigDecimal cash = BigDecimal.ZERO;
	private BigDecimal pos = BigDecimal.ZERO;
	private String posremark;
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
	private String changereason; //中转原因
	private String sign_man_phone;
	private boolean editFlag;//是否允许修改反馈-- 刘武强20160831

	public String getSign_man_phone() {
		return sign_man_phone;
	}

	public void setSign_man_phone(String sign_man_phone) {
		this.sign_man_phone = sign_man_phone;
	}

	private long customerid;
	private String customername;// 供货商
	private String emaildate;// 发货时间
	private String consigneename;// 收件人
	private String consigneemobile;// 收件人手机
	private String consigneephone;// 收件人电话
	private String consigneeaddress;// 收件人地址
	private String backcarname;// 取回商品
	private String sendcarname;// 发出商品
	private String deliverealname;// 负责人
	private long flowordertype;
	private String cwbremark;
	private String deliverytime;

	private BigDecimal codpos = BigDecimal.ZERO;
	private BigDecimal shouldfare = BigDecimal.ZERO;
	private BigDecimal infactfare = BigDecimal.ZERO;

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

	public String getChangereason() {
		return changereason;
	}

	public void setChangereason(String changereason) {
		this.changereason = changereason;
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

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
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

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getBackcarname() {
		return backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public String getSendcarname() {
		return sendcarname;
	}

	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}

	public String getDeliverealname() {
		return deliverealname;
	}

	public void setDeliverealname(String deliverealname) {
		this.deliverealname = deliverealname;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String isHistory() {
		if (!this.createtime.substring(0, 10).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
			return "{历}";
		} else {
			return "";
		}
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
		if (this.codpos.compareTo(BigDecimal.ZERO) > 0) {
			reStr += reStr.length() > 0 ? "/" : "";
			reStr += "支付宝COD扫码支付";
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

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCwbremark() {
		return cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
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

	public boolean getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}
	
	
}
