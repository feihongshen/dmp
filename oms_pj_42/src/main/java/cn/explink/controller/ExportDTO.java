package cn.explink.controller;

import java.math.BigDecimal;

import cn.explink.enumutil.FlowOrderTypeEnum;

public class ExportDTO {

	private String cwb;// 订单号
	private String commonname;// 承运商
	private String customername;// 订单号
	private String transcwb;// 运单号
	private String consigneename;// 收件人名称
	private String consigneeaddress;// 收件人地址
	private String consigneepostcode;// 收件人邮编
	private String consigneephone;// 收件人电话
	private String consigneemobile;// 收件人手机
	private String sendcarname;// 发出商品名称
	private String backcarname;// 取回商品名称
	private BigDecimal carrealweight;// 货物重量
	private BigDecimal receivablefee;// 代收货款应收金额
	private String cwbremark;// 订单备注
	private String exceldeliver;// 指定小件员
	private String excelbranch;// 指定派送分站
	private String shipcwb;// 配送单号
	private String consigneeno;// 收件人编号
	private BigDecimal caramount;// 货物金额
	private String customercommand;// 客户要求
	private String cartype;// 货物类型
	private BigDecimal carsize;// 货物尺寸
	private BigDecimal backcaramount;// 取回货物金额
	private String destination;// 目的地
	private String transway;// 运输方式
	private String shipperid;// 退供货商承运商
	private String sendcarnum;// 发货数量
	private String backcarnum;// 取货数量
	private String cwbprovince;// 省
	private String cwbcity;// 市
	private String cwbcounty;// 区县
	private String carwarehouse;// 发货仓库
	private String edittime;// 修改时间
	long flowordertype; // 操作类型，具体对应枚举类FlowOrderTypeEnum
	private String auditor;// 审核人
	private String newfollownotes;// 最新跟踪状态
	private String marksflagmen;// 标记人
	private BigDecimal primitivemoney;// 订单原始金额
	private String signintime;// 签收时间
	private String signinman;// 签收人
	private String editman;// 修改人
	private String branchname;// 机构id
	private long cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
	// private String sditsignintime;//修改的签收时间

	private String credate;// 跟踪记录时间
	private long userid; // 操作员id(用户id)

	private BigDecimal receivedfee;// 收到总金额
	private BigDecimal returnedfee;// 退还金额
	private BigDecimal businessfee;// 应处理金额
	private long deliverystate;// 配送状态（配送成功、上门退成功、上门换成功、全部退货、部分退货、分站滞留、上门拒退、货物丢失）
	private BigDecimal cash;// 现金实收
	private BigDecimal pos;// pos实收
	private String posremark;// pos备注
	private String mobilepodtime;// pos反馈时间
	private BigDecimal checkfee;// 支票实收
	private String checkremark;// 支票号备注
	private String receivedfeeuser;// 收款人
	private long statisticstate;// 归班状态(1未归班 2已归班 3暂不处理)
	private BigDecimal otherfee;// 其他金额
	private String createtime;// 反馈时间

	// private String returngoodsremark; //反馈的备注输入内容

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getCommonname() {
		return commonname;
	}

	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
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

	public String getSendcarname() {
		return sendcarname;
	}

	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}

	public String getBackcarname() {
		return backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public BigDecimal getCarrealweight() {
		return carrealweight;
	}

	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public String getCwbremark() {
		return cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getExceldeliver() {
		return exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getExcelbranch() {
		return excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public String getShipcwb() {
		return shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		this.shipcwb = shipcwb;
	}

	public String getConsigneeno() {
		return consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = consigneeno;
	}

	public BigDecimal getCaramount() {
		return caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	public String getCustomercommand() {
		return customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public BigDecimal getCarsize() {
		return carsize;
	}

	public void setCarsize(BigDecimal carsize) {
		this.carsize = carsize;
	}

	public BigDecimal getBackcaramount() {
		return backcaramount;
	}

	public void setBackcaramount(BigDecimal backcaramount) {
		this.backcaramount = backcaramount;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTransway() {
		return transway;
	}

	public void setTransway(String transway) {
		this.transway = transway;
	}

	public String getShipperid() {
		return shipperid;
	}

	public void setShipperid(String shipperid) {
		this.shipperid = shipperid;
	}

	public String getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(String sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public String getBackcarnum() {
		return backcarnum;
	}

	public void setBackcarnum(String backcarnum) {
		this.backcarnum = backcarnum;
	}

	public String getCwbprovince() {
		return cwbprovince;
	}

	public void setCwbprovince(String cwbprovince) {
		this.cwbprovince = cwbprovince;
	}

	public String getCwbcity() {
		return cwbcity;
	}

	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}

	public String getCwbcounty() {
		return cwbcounty;
	}

	public void setCwbcounty(String cwbcounty) {
		this.cwbcounty = cwbcounty;
	}

	public String getCarwarehouse() {
		return carwarehouse;
	}

	public void setCarwarehouse(String carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public long getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(long cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getText();
		}
		return "";
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getNewfollownotes() {
		return newfollownotes;
	}

	public void setNewfollownotes(String newfollownotes) {
		this.newfollownotes = newfollownotes;
	}

	public String getMarksflagmen() {
		return marksflagmen;
	}

	public void setMarksflagmen(String marksflagmen) {
		this.marksflagmen = marksflagmen;
	}

	public BigDecimal getPrimitivemoney() {
		return primitivemoney;
	}

	public void setPrimitivemoney(BigDecimal primitivemoney) {
		this.primitivemoney = primitivemoney;
	}

	public String getSignintime() {
		return signintime;
	}

	public void setSignintime(String signintime) {
		this.signintime = signintime;
	}

	public String getSigninman() {
		return signinman;
	}

	public void setSigninman(String signinman) {
		this.signinman = signinman;
	}

	public String getEditman() {
		return editman;
	}

	public void setEditman(String editman) {
		this.editman = editman;
	}

	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
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

	public String getMobilepodtime() {
		return mobilepodtime;
	}

	public void setMobilepodtime(String mobilepodtime) {
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

	public String getReceivedfeeuser() {
		return receivedfeeuser;
	}

	public void setReceivedfeeuser(String receivedfeeuser) {
		this.receivedfeeuser = receivedfeeuser;
	}

	public long getStatisticstate() {
		return statisticstate;
	}

	public void setStatisticstate(long statisticstate) {
		this.statisticstate = statisticstate;
	}

	public BigDecimal getOtherfee() {
		return otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public BigDecimal getBusinessfee() {
		return businessfee;
	}

	public void setBusinessfee(BigDecimal businessfee) {
		this.businessfee = businessfee;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

}
