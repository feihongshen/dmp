package cn.explink.domain;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import cn.explink.enumutil.CwbOrderTypeIdEnum;

public class CwbDetailView {

	long opscwbid; // 主键id
	long startbranchid; // 上一个机构id
	long currentbranchid; // 当前机构
	long nextbranchid; // 下一站目的机构id
	long deliverybranchid; // 配送站点
	String backtocustomer_awb;// 退供货商封包批次号
	String cwbflowflag; // 订单流程类型 1正常件 2中转件 3再投件
	BigDecimal carrealweight; // 货物重量kg
	String cartype;// 货物类别
	String carwarehouse;// 发货仓库
	String carsize;// 商品尺寸
	BigDecimal backcaramount; // 取回货物金额
	long sendcarnum;// 发货数量
	long backcarnum;// 取货数量
	BigDecimal caramount;// 货物金额
	String backcarname; // 取回商品名称
	String sendcarname;// 发出商品名称
	long deliverid; // 小件员id
	long exceldliverid; // 地址库匹配小件员ID
	int deliverystate;
	int emailfinishflag;// 库房入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	int reacherrorflag;// 站点入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	long orderflowid;// 对订单的最后一次操作对应的监控记录
	long flowordertype; // 操作类型，具体对应枚举类FlowOrderTypeEnum
	long cwbreachbranchid;// 订单入库机构id
	long cwbreachdeliverbranchid;// 订单到货派送机构id
	String podfeetoheadflag;// 站点收款是否已上交总部 0未上交 1已上交
	String podfeetoheadtime;// 站点上交款时间
	String podfeetoheadchecktime;// 站点交款总部审核时
	String podfeetoheadcheckflag;// 总部交款审核状态
	long leavedreasonid;// 滞留原因id
	String deliversubscribeday;// 滞留预约派送日
	String customerwarehouseid;// 客户发货仓库id
	long emaildateid;// 发货时间Id
	String emaildate;// 发货时间
	long serviceareaid;// 服务区域id
	long customerid;// 供货商id
	String shipcwb;// 供货商运单号
	String consigneeno;// 收件人编号
	String consigneename;// 收件人名称
	String consigneeaddress;// 收件人地址
	String consigneepostcode;// 收件人邮编
	String consigneephone;// 收件人电话
	String cwbremark;// 订单备注
	String customercommand;// 客户要求
	String transway;// 运输方式
	String cwbprovince;// 省
	String cwbcity;// 市
	String cwbcounty;// 区县
	BigDecimal receivablefee;// 代收货款应收金额
	BigDecimal paybackfee;// 上门退货应退金额
	String cwb;// 订单号
	long shipperid;// 退供货商承运商id
	int cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
	String consigneemobile;// 收件人手机
	String transcwb;// 运单号
	String destination;// 目的地
	String cwbdelivertypeid;// 订单入库机构id
	String exceldeliver;// 指定小件员（用于地址库匹配）
	String excelbranch;// 指定派送分站（用于地址库匹配）
	long excelimportuserid;// 导入操作员id
	long state;// 是否显示的状态
	String printtime;// 打印时间（针对上门退订单）
	long commonid;// （承运商id）
	String commoncwb;// （承运商订单号）

	long signtypeid; // 20120822 zhang 签收状态
	String podrealname; // 签收人
	String podtime; // 签收时间
	String podsignremark; // 签收备注
	String modelname;// 模版名称
	long scannum;// 一票多件扫描件数
	long isaudit;// 客服是否审核，可退供货商出库
	String backreason;// 退货备注
	String leavedreason;// 滞留原因
	long paywayid;// 支付方式（导入数据的时候会导入）
	String newpaywayid;// 新的支付方式（反馈的时候可能会更改的支付方式）
	long tuihuoid;// 退货站id
	long cwbstate;// 订单现在处于的流程状态（1配送 2退货）
	String remark1;// 备注一
	String remark2;// 备注二
	String remark3;// 备注三
	String remark4;// 备注四
	String remark5;// 备注五
	private long backreasonid; // 退货原因Id
	private String multi_shipcwb; // 供货商运单号多个逗号隔开
	private String packagecode;// 扫描使用的包号

	long backreturnreasonid;// 退货再投原因id
	String backreturnreason;// 退货再投原因

	long handleresult;// 异常订单处理结果id
	long handleperson;// 异常订单处理责任人id
	String handlereason;// 异常订单处理原因

	int addresscodeedittype;// 匹配类型
	String resendtime;// 滞留订单再次配送时间
	private long weishuakareasonid;// 未刷卡原因id
	private String weishuakareason;// 未刷卡原因

	private long losereasonid;// 货物丢失原因id
	private String losereason;// 货物丢失原因
	private Object remarkView;// 根据系统设置中的显示相应的备注
	private String customername;// 供货商名称

	private String outstoreroomtime = "";// 库房出库时间
	private String inSitetime = "";// 到站时间
	private String pickGoodstime = "";// 小件员领货时间
	private String timelimited;// 地址库匹配时效

	private String pickaddress; //揽件地址
	private String cwbordertype;//订单类型
	private String checkstateresultname; //审核状态
	
	
	//Hps_Concerto add 2016年6月27日 11:19:53
	private String cwbstatetext;
	private String flowordertypetext;
	public String getCwbstatetext() {
		return cwbstatetext;
	}

	public void setCwbstatetext(String cwbstatetext) {
		this.cwbstatetext = cwbstatetext;
	}
	public String getFlowordertypetext() {
		return flowordertypetext;
	}

	public void setFlowordertypetext(String flowordertypetext) {
		this.flowordertypetext = flowordertypetext;
	}
	//***********
	public String getCheckstateresultname() {
		return checkstateresultname;
	}

	public void setCheckstateresultname(String checkstateresultname) {
		this.checkstateresultname = checkstateresultname;
	}

	public String getTimelimited() {
		return timelimited;
	}

	public void setTimelimited(String timelimited) {
		this.timelimited = timelimited;
	}

	public CwbDetailView() {
		if (this.sendcarnum == 0 && this.backcarnum == 0) {
			if (cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {
				this.sendcarnum = 1;
				this.backcarnum = 0;
			} else if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				this.sendcarnum = 1;
				this.backcarnum = 0;
			} else if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				this.sendcarnum = 0;
				this.backcarnum = 0;
			}
		}
	}

	public int getAddresscodeedittype() {
		return addresscodeedittype;
	}

	public void setAddresscodeedittype(int addresscodeedittype) {
		this.addresscodeedittype = addresscodeedittype;
	}

	public String getPackagecode() {
		return packagecode;
	}

	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}

	public String getMulti_shipcwb() {
		return multi_shipcwb;
	}

	public void setMulti_shipcwb(String multi_shipcwb) {
		this.multi_shipcwb = multi_shipcwb;
	}

	public long getBackreasonid() {
		return backreasonid;
	}

	public void setBackreasonid(long backreasonid) {
		this.backreasonid = backreasonid;
	}

	public String getPodrealname() {
		return podrealname;
	}

	public void setPodrealname(String podrealname) {
		this.podrealname = podrealname;
	}

	public String getPodtime() {
		return podtime;
	}

	public void setPodtime(String podtime) {
		this.podtime = podtime;
	}

	public String getPodsignremark() {
		return podsignremark;
	}

	public void setPodsignremark(String podsignremark) {
		this.podsignremark = podsignremark;
	}

	public long getSigntypeid() {
		return signtypeid;
	}

	public void setSigntypeid(long signtypeid) {
		this.signtypeid = signtypeid;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public BigDecimal getCarrealweight() {
		return carrealweight;
	}

	public long getExcelimportuserid() {
		return excelimportuserid;
	}

	public void setExcelimportuserid(long excelimportuserid) {
		this.excelimportuserid = excelimportuserid;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
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
		if (consigneeaddress == null) {
			this.consigneeaddress = "";
			return;
		}
		this.consigneeaddress = consigneeaddress.replaceAll("[*]", "").replaceAll(" ", "");// .replaceAll("#",
																							// "").replaceAll("\\p{P}",
																							// "");
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

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = (consigneemobile);
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public String getCwbremark() {
		return cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getShipcwb() {
		return shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		this.shipcwb = shipcwb;
	}

	public String getExceldeliver() {
		return exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getConsigneeno() {
		return consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = (consigneeno);
	}

	public String getExcelbranch() {
		return excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public String getCustomercommand() {
		return customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
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

	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public long getServiceareaid() {
		return serviceareaid;
	}

	public void setServiceareaid(long serviceareaid) {
		this.serviceareaid = serviceareaid;
	}

	public long getShipperid() {
		return shipperid;
	}

	public void setShipperid(long shipperid) {
		this.shipperid = shipperid;
	}

	public long getOpscwbid() {
		return opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public long getStartbranchid() {
		return startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public String getBacktocustomer_awb() {
		return backtocustomer_awb;
	}

	public void setBacktocustomer_awb(String backtocustomer_awb) {
		this.backtocustomer_awb = backtocustomer_awb;
	}

	public String getCwbflowflag() {
		return cwbflowflag;
	}

	public void setCwbflowflag(String cwbflowflag) {
		this.cwbflowflag = cwbflowflag;
	}

	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getCarwarehouse() {
		return carwarehouse;
	}

	public void setCarwarehouse(String carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public String getCarsize() {
		return carsize;
	}

	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}

	public BigDecimal getBackcaramount() {
		return backcaramount;
	}

	public void setBackcaramount(BigDecimal backcaramount) {
		this.backcaramount = backcaramount;
	}

	public long getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public long getBackcarnum() {
		return backcarnum;
	}

	public void setBackcarnum(long backcarnum) {
		this.backcarnum = backcarnum;
	}

	public BigDecimal getCaramount() {
		return caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
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

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public int getEmailfinishflag() {
		return emailfinishflag;
	}

	public void setEmailfinishflag(int emailfinishflag) {
		this.emailfinishflag = emailfinishflag;
	}

	public int getReacherrorflag() {
		return reacherrorflag;
	}

	public void setReacherrorflag(int reacherrorflag) {
		this.reacherrorflag = reacherrorflag;
	}

	public long getOrderflowid() {
		return orderflowid;
	}

	public void setOrderflowid(long orderflowid) {
		this.orderflowid = orderflowid;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getCwbreachbranchid() {
		return cwbreachbranchid;
	}

	public void setCwbreachbranchid(long cwbreachbranchid) {
		this.cwbreachbranchid = cwbreachbranchid;
	}

	public long getCwbreachdeliverbranchid() {
		return cwbreachdeliverbranchid;
	}

	public void setCwbreachdeliverbranchid(long cwbreachdeliverbranchid) {
		this.cwbreachdeliverbranchid = cwbreachdeliverbranchid;
	}

	public String getPodfeetoheadflag() {
		return podfeetoheadflag;
	}

	public void setPodfeetoheadflag(String podfeetoheadflag) {
		this.podfeetoheadflag = podfeetoheadflag;
	}

	public String getPodfeetoheadtime() {
		return podfeetoheadtime;
	}

	public String getPodfeetoheadchecktime() {
		return podfeetoheadchecktime;
	}

	public String getPodfeetoheadcheckflag() {
		return podfeetoheadcheckflag;
	}

	public void setPodfeetoheadcheckflag(String podfeetoheadcheckflag) {
		this.podfeetoheadcheckflag = podfeetoheadcheckflag;
	}

	public long getLeavedreasonid() {
		return leavedreasonid;
	}

	public void setLeavedreasonid(long leavedreasonid) {
		this.leavedreasonid = leavedreasonid;
	}

	public String getDeliversubscribeday() {
		return deliversubscribeday;
	}

	public String getCustomerwarehouseid() {
		return customerwarehouseid;
	}

	public void setCustomerwarehouseid(String customerwarehouseid) {
		this.customerwarehouseid = customerwarehouseid;
	}

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getCwbdelivertypeid() {
		return cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(String cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	public String getPrinttime() {
		return printtime;
	}

	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}

	public long getCommonid() {
		return commonid;
	}

	public void setCommonid(long commonid) {
		this.commonid = commonid;
	}

	public String getCommoncwb() {
		return commoncwb;
	}

	public void setCommoncwb(String commoncwb) {
		this.commoncwb = commoncwb;
	}

	public String getModelname() {
		return modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public long getScannum() {
		return scannum;
	}

	public void setScannum(long scannum) {
		this.scannum = scannum;
	}

	public long getIsaudit() {
		return isaudit;
	}

	public void setIsaudit(long isaudit) {
		this.isaudit = isaudit;
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

	public long getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}

	public String getNewpaywayid() {
		return newpaywayid;
	}

	public void setNewpaywayid(String newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	public long getTuihuoid() {
		return tuihuoid;
	}

	public void setTuihuoid(long tuihuoid) {
		this.tuihuoid = tuihuoid;
	}

	@JsonIgnore
	public BigDecimal getBusinessFee() {
		return this.receivablefee.add(this.paybackfee).abs();
	}

	@JsonIgnore
	public boolean isOut() {
		return this.receivablefee.compareTo(this.paybackfee) > 0;
	}

	public long getCurrentbranchid() {
		return currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getCwbstate() {
		return cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public long getDeliverybranchid() {
		return deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public int getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(int deliverystate) {
		this.deliverystate = deliverystate;
	}

	public long getBackreturnreasonid() {
		return backreturnreasonid;
	}

	public void setBackreturnreasonid(long backreturnreasonid) {
		this.backreturnreasonid = backreturnreasonid;
	}

	public String getBackreturnreason() {
		return backreturnreason;
	}

	public void setBackreturnreason(String backreturnreason) {
		this.backreturnreason = backreturnreason;
	}

	public long getHandleresult() {
		return handleresult;
	}

	public void setHandleresult(long handleresult) {
		this.handleresult = handleresult;
	}

	public long getHandleperson() {
		return handleperson;
	}

	public void setHandleperson(long handleperson) {
		this.handleperson = handleperson;
	}

	public String getHandlereason() {
		return handlereason;
	}

	public void setHandlereason(String handlereason) {
		this.handlereason = handlereason;
	}

	public String getResendtime() {
		return resendtime;
	}

	public void setResendtime(String resendtime) {
		this.resendtime = resendtime;
	}

	public long getWeishuakareasonid() {
		return weishuakareasonid;
	}

	public void setWeishuakareasonid(long weishuakareasonid) {
		this.weishuakareasonid = weishuakareasonid;
	}

	public String getWeishuakareason() {
		return weishuakareason;
	}

	public void setWeishuakareason(String weishuakareason) {
		this.weishuakareason = weishuakareason;
	}

	public long getLosereasonid() {
		return losereasonid;
	}

	public void setLosereasonid(long losereasonid) {
		this.losereasonid = losereasonid;
	}

	public String getLosereason() {
		return losereason;
	}

	public void setLosereason(String losereason) {
		this.losereason = losereason;
	}

	public Object getRemarkView() {
		return remarkView;
	}

	public void setRemarkView(Object remarkView) {
		this.remarkView = remarkView;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
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

	public String getPickGoodstime() {
		return pickGoodstime;
	}

	public void setPickGoodstime(String pickGoodstime) {
		this.pickGoodstime = pickGoodstime;
	}

	public String getPickaddress() {
		return pickaddress;
	}

	public void setPickaddress(String pickaddress) {
		this.pickaddress = pickaddress;
	}

	public String getCwbordertype() {
		return cwbordertype;
	}

	public void setCwbordertype(String cwbordertype) {
		this.cwbordertype = cwbordertype;
	}

	public long getExceldliverid() {
		return exceldliverid;
	}

	public void setExceldliverid(long exceldliverid) {
		this.exceldliverid = exceldliverid;
	}

	public void setPodfeetoheadtime(String podfeetoheadtime) {
		this.podfeetoheadtime = podfeetoheadtime;
	}

	public void setPodfeetoheadchecktime(String podfeetoheadchecktime) {
		this.podfeetoheadchecktime = podfeetoheadchecktime;
	}

	public void setDeliversubscribeday(String deliversubscribeday) {
		this.deliversubscribeday = deliversubscribeday;
	}
}
