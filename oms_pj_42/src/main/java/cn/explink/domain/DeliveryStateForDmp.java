package cn.explink.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DeliveryStateForDmp {
	private long id;
	private String cwb;
	private long deliveryid;
	private BigDecimal receivedfee;
	private BigDecimal returnedfee;
	private BigDecimal businessfee;
	private String cwbordertypeid;
	private long deliverystate;
	private BigDecimal cash;
	private BigDecimal pos;
	private String posremark;
	private Timestamp mobilepodtime;
	private BigDecimal checkfee;
	private String checkremark;
	private long receivedfeeuser;
	private long statisticstate;
	private String createtime;
	private BigDecimal otherfee;
	private long podremarkid;
	private String deliverstateremark;
	private long isout;
	// 附加的功能性字段
	private long customerid;// 供货商编号
	private String consigneeaddress;// 送货地址
	private String emaildate;
	private long sendcarnum;// 发货数量
	private String sendcarname;// 发货商品名
	private long backcarnum;// 取货货数量
	private String backcarname;// 取回货物商品名
	private String deliverealname;// 小件员姓名

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

	public Timestamp getMobilepodtime() {
		return mobilepodtime;
	}

	public void setMobilepodtime(Timestamp mobilepodtime) {
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

	public long getStatisticstate() {
		return statisticstate;
	}

	public void setStatisticstate(long statisticstate) {
		this.statisticstate = statisticstate;
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

}
