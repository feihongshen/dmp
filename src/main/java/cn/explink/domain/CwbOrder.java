package cn.explink.domain;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import cn.explink.enumutil.CwbOrderTypeIdEnum;

public class CwbOrder {

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

	private String fankuitime;// 反馈时间
	private String shenhetime;// 审核时间
	private String chuzhantime;// 出站时间
	private BigDecimal shouldfare = BigDecimal.ZERO;// 应收运费
	private BigDecimal infactfare = BigDecimal.ZERO;
	private String timelimited;// 地址库匹配时效

	private String historybranchname;// 历史配送站点

	private int goodsType = 0;// 货物类型(重庆华宇,大件,贵品，大件+贵品,普件)

	private int outareaflag = 0;// 超区标识.

	
	private BigDecimal fnorgoffset;//站点账单回写的冲抵金额
	private int fnorgoffsetflag;//订单在站点账单中被冲抵标志位，0：未收款，1：已收款
	
	private String changereason; //中转原因
	private long firstchangereasonid; //一级中转原因
	public long getFirstchangereasonid() {
		return firstchangereasonid;
	}

	public void setFirstchangereasonid(long firstchangereasonid) {
		this.firstchangereasonid = firstchangereasonid;
	}


	private long changereasonid;
	

	public String getChangereason() {
		return changereason;
	}

	public void setChangereason(String changereason) {
		this.changereason = changereason;
	}

	

	public long getChangereasonid() {
		return changereasonid;
	}

	public void setChangereasonid(long changereasonid) {
		this.changereasonid = changereasonid;
	}

	public CwbOrder() {
		if ((this.sendcarnum == 0) && (this.backcarnum == 0)) {
			if (this.cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {
				this.sendcarnum = 1;
				this.backcarnum = 0;
			} else if (this.cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				this.sendcarnum = 1;
				this.backcarnum = 0;
			} else if (this.cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				this.sendcarnum = 0;
				this.backcarnum = 0;
			}
		}
	}

	public String getHistorybranchname() {
		return this.historybranchname;
	}

	public void setHistorybranchname(String historybranchname) {
		this.historybranchname = historybranchname;
	}

	public String getChuzhantime() {
		return this.chuzhantime;
	}

	public void setChuzhantime(String chuzhantime) {
		this.chuzhantime = chuzhantime;
	}

	public String getFankuitime() {
		return this.fankuitime;
	}

	public void setFankuitime(String fankuitime) {
		this.fankuitime = fankuitime;
	}

	public String getShenhetime() {
		return this.shenhetime;
	}

	public void setShenhetime(String shenhetime) {
		this.shenhetime = shenhetime;
	}

	public int getAddresscodeedittype() {
		return this.addresscodeedittype;
	}

	public void setAddresscodeedittype(int addresscodeedittype) {
		this.addresscodeedittype = addresscodeedittype;
	}

	public String getPackagecode() {
		return this.packagecode;
	}

	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}

	public String getMulti_shipcwb() {
		return this.multi_shipcwb;
	}

	public void setMulti_shipcwb(String multi_shipcwb) {
		this.multi_shipcwb = multi_shipcwb;
	}

	public long getBackreasonid() {
		return this.backreasonid;
	}

	public void setBackreasonid(long backreasonid) {
		this.backreasonid = backreasonid;
	}

	public String getPodrealname() {
		return this.podrealname;
	}

	public void setPodrealname(String podrealname) {
		this.podrealname = podrealname;
	}

	public String getPodtime() {
		return this.podtime;
	}

	public void setPodtime(String podtime) {
		this.podtime = podtime;
	}

	public String getPodsignremark() {
		return this.podsignremark;
	}

	public void setPodsignremark(String podsignremark) {
		this.podsignremark = podsignremark;
	}

	public long getSigntypeid() {
		return this.signtypeid;
	}

	public void setSigntypeid(long signtypeid) {
		this.signtypeid = signtypeid;
	}

	public long getState() {
		return this.state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public BigDecimal getCarrealweight() {
		return this.carrealweight;
	}

	public long getExcelimportuserid() {
		return this.excelimportuserid;
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
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getConsigneename() {
		return this.consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return this.consigneeaddress;
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
		return this.consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public String getConsigneephone() {
		return this.consigneephone;
	}

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			this.setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return this.consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = (consigneemobile);
	}

	public BigDecimal getReceivablefee() {
		return this.receivablefee;
	}

	public BigDecimal getPaybackfee() {
		return this.paybackfee;
	}

	public String getCwbremark() {
		return this.cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getShipcwb() {
		return this.shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		this.shipcwb = shipcwb;
	}

	public String getExceldeliver() {
		return this.exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getConsigneeno() {
		return this.consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = (consigneeno);
	}

	public String getExcelbranch() {
		return this.excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public String getCustomercommand() {
		return this.customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTransway() {
		return this.transway;
	}

	public void setTransway(String transway) {
		this.transway = transway;
	}

	public String getCwbprovince() {
		return this.cwbprovince;
	}

	public void setCwbprovince(String cwbprovince) {
		this.cwbprovince = cwbprovince;
	}

	public String getCwbcity() {
		return this.cwbcity;
	}

	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}

	public String getCwbcounty() {
		return this.cwbcounty;
	}

	public void setCwbcounty(String cwbcounty) {
		this.cwbcounty = cwbcounty;
	}

	public String getTranscwb() {
		return this.transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public long getServiceareaid() {
		return this.serviceareaid;
	}

	public void setServiceareaid(long serviceareaid) {
		this.serviceareaid = serviceareaid;
	}

	public long getShipperid() {
		return this.shipperid;
	}

	public void setShipperid(long shipperid) {
		this.shipperid = shipperid;
	}

	public long getOpscwbid() {
		return this.opscwbid;
	}

	public void setOpscwbid(long opscwbid) {
		this.opscwbid = opscwbid;
	}

	public long getStartbranchid() {
		return this.startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public long getNextbranchid() {
		return this.nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public String getBacktocustomer_awb() {
		return this.backtocustomer_awb;
	}

	public void setBacktocustomer_awb(String backtocustomer_awb) {
		this.backtocustomer_awb = backtocustomer_awb;
	}

	public String getCwbflowflag() {
		return this.cwbflowflag;
	}

	public void setCwbflowflag(String cwbflowflag) {
		this.cwbflowflag = cwbflowflag;
	}

	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}

	public String getCartype() {
		return this.cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getCarwarehouse() {
		return this.carwarehouse;
	}

	public void setCarwarehouse(String carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public String getCarsize() {
		return this.carsize;
	}

	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}

	public BigDecimal getBackcaramount() {
		return this.backcaramount;
	}

	public void setBackcaramount(BigDecimal backcaramount) {
		this.backcaramount = backcaramount;
	}

	public long getSendcarnum() {
		return this.sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public long getBackcarnum() {
		return this.backcarnum;
	}

	public void setBackcarnum(long backcarnum) {
		this.backcarnum = backcarnum;
	}

	public BigDecimal getCaramount() {
		return this.caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	public String getBackcarname() {
		return this.backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public String getSendcarname() {
		return this.sendcarname;
	}

	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}

	public long getDeliverid() {
		return this.deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public int getEmailfinishflag() {
		return this.emailfinishflag;
	}

	public void setEmailfinishflag(int emailfinishflag) {
		this.emailfinishflag = emailfinishflag;
	}

	public int getReacherrorflag() {
		return this.reacherrorflag;
	}

	public void setReacherrorflag(int reacherrorflag) {
		this.reacherrorflag = reacherrorflag;
	}

	public long getOrderflowid() {
		return this.orderflowid;
	}

	public void setOrderflowid(long orderflowid) {
		this.orderflowid = orderflowid;
	}

	public long getFlowordertype() {
		return this.flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getCwbreachbranchid() {
		return this.cwbreachbranchid;
	}

	public void setCwbreachbranchid(long cwbreachbranchid) {
		this.cwbreachbranchid = cwbreachbranchid;
	}

	public long getCwbreachdeliverbranchid() {
		return this.cwbreachdeliverbranchid;
	}

	public void setCwbreachdeliverbranchid(long cwbreachdeliverbranchid) {
		this.cwbreachdeliverbranchid = cwbreachdeliverbranchid;
	}

	public String getPodfeetoheadflag() {
		return this.podfeetoheadflag;
	}

	public void setPodfeetoheadflag(String podfeetoheadflag) {
		this.podfeetoheadflag = podfeetoheadflag;
	}

	public String getPodfeetoheadtime() {
		return this.podfeetoheadtime;
	}

	public String getPodfeetoheadchecktime() {
		return this.podfeetoheadchecktime;
	}

	public String getPodfeetoheadcheckflag() {
		return this.podfeetoheadcheckflag;
	}

	public void setPodfeetoheadcheckflag(String podfeetoheadcheckflag) {
		this.podfeetoheadcheckflag = podfeetoheadcheckflag;
	}

	public long getLeavedreasonid() {
		return this.leavedreasonid;
	}

	public void setLeavedreasonid(long leavedreasonid) {
		this.leavedreasonid = leavedreasonid;
	}

	public String getDeliversubscribeday() {
		return this.deliversubscribeday;
	}

	public String getCustomerwarehouseid() {
		return this.customerwarehouseid;
	}

	public void setCustomerwarehouseid(String customerwarehouseid) {
		this.customerwarehouseid = customerwarehouseid;
	}

	public long getEmaildateid() {
		return this.emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public String getEmaildate() {
		return this.emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public int getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getCwbdelivertypeid() {
		return this.cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(String cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	public String getPrinttime() {
		return this.printtime;
	}

	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}

	public long getCommonid() {
		return this.commonid;
	}

	public void setCommonid(long commonid) {
		this.commonid = commonid;
	}

	public String getCommoncwb() {
		return this.commoncwb;
	}

	public void setCommoncwb(String commoncwb) {
		this.commoncwb = commoncwb;
	}

	public String getModelname() {
		return this.modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public long getScannum() {
		return this.scannum;
	}

	public void setScannum(long scannum) {
		this.scannum = scannum;
	}

	public long getIsaudit() {
		return this.isaudit;
	}

	public void setIsaudit(long isaudit) {
		this.isaudit = isaudit;
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

	public long getPaywayid() {
		return this.paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}

	public String getNewpaywayid() {
		return this.newpaywayid;
	}

	public void setNewpaywayid(String newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	public long getTuihuoid() {
		return this.tuihuoid;
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
		return this.currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getCwbstate() {
		return this.cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return this.remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return this.remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return this.remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public int getDeliverystate() {
		return this.deliverystate;
	}

	public void setDeliverystate(int deliverystate) {
		this.deliverystate = deliverystate;
	}

	public long getBackreturnreasonid() {
		return this.backreturnreasonid;
	}

	public void setBackreturnreasonid(long backreturnreasonid) {
		this.backreturnreasonid = backreturnreasonid;
	}

	public String getBackreturnreason() {
		return this.backreturnreason;
	}

	public void setBackreturnreason(String backreturnreason) {
		this.backreturnreason = backreturnreason;
	}

	public long getHandleresult() {
		return this.handleresult;
	}

	public void setHandleresult(long handleresult) {
		this.handleresult = handleresult;
	}

	public long getHandleperson() {
		return this.handleperson;
	}

	public void setHandleperson(long handleperson) {
		this.handleperson = handleperson;
	}

	public String getHandlereason() {
		return this.handlereason;
	}

	public void setHandlereason(String handlereason) {
		this.handlereason = handlereason;
	}

	public String getResendtime() {
		return this.resendtime;
	}

	public void setResendtime(String resendtime) {
		this.resendtime = resendtime;
	}

	public long getWeishuakareasonid() {
		return this.weishuakareasonid;
	}

	public void setWeishuakareasonid(long weishuakareasonid) {
		this.weishuakareasonid = weishuakareasonid;
	}

	public String getWeishuakareason() {
		return this.weishuakareason;
	}

	public void setWeishuakareason(String weishuakareason) {
		this.weishuakareason = weishuakareason;
	}

	public long getLosereasonid() {
		return this.losereasonid;
	}

	public void setLosereasonid(long losereasonid) {
		this.losereasonid = losereasonid;
	}

	public String getLosereason() {
		return this.losereason;
	}

	public void setLosereason(String losereason) {
		this.losereason = losereason;
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

	public String getTimelimited() {
		return this.timelimited;
	}

	public void setTimelimited(String timelimited) {
		this.timelimited = timelimited;
	}

	public int getGoodsType() {
		return this.goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getOutareaflag() {
		return this.outareaflag;
	}

	public void setOutareaflag(int outareaflag) {
		this.outareaflag = outareaflag;
	}
	
	
	public int getFnorgoffsetflag() {
		return fnorgoffsetflag;
	}


	public void setFnorgoffsetflag(int fnorgoffsetflag) {
		this.fnorgoffsetflag = fnorgoffsetflag;
	}


	public BigDecimal getFnorgoffset() {
		return fnorgoffset;
	}


	public void setFnorgoffset(BigDecimal fnorgoffset) {
		this.fnorgoffset = fnorgoffset;
	}


}
