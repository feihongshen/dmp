package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.PaytypeEnum;

public class QuickSelectView {
	long opscwbid; // 主键id
	long startbranchid; // 上一个机构id
	long currentbranchid; // 当前机构
	long nextbranchid; // 下一站目的机构id
	long deliverybranchid; // 配送站点
	String backtocustomer_awb;// 退供货商封包批次号
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
	String oldconsigneeaddress;//原地址
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
	String timelimited;// 地址库匹配时效
	String consigneenameOfkf;
	String consigneemobileOfkf;
	String consigneephoneOfkf;
	//zhili01.liang 20160830 货物尺寸类型修改审核
	int goodsSizeType;

	

	private String city;//通过百度API匹配的城市
	private String area;//通过百度API匹配的城区区域


	/**
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return this.area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	public String getTimelimited() {
		return this.timelimited;
	}

	public void setTimelimited(String timelimited) {
		this.timelimited = timelimited;
	}

	long excelimportuserid;// 导入操作员id
	long state;// 是否显示的状态
	String printtime;// 打印时间（针对上门退订单）
	long commonid;// （承运商id）
	String commoncwb;// （承运商订单号）
	String modelname;// 模版名称
	long scannum;// 一票多件扫描件数
	long isaudit;// 客服是否审核，可退供货商出库
	String backreason;// 退货备注
	String leavedreason;// 滞留原因
	long paywayid;// 支付方式（导入数据的时候会导入）
	String newpaywayid;// 新的支付方式（反馈的时候可能会更改的支付方式）
	long tuihuoid;// 退货站id
	long cwbstate;// 订单现在处于的流程状态（1配送 2退货）
	private long deliveryid;
	private BigDecimal receivedfee = BigDecimal.ZERO;
	private BigDecimal returnedfee = BigDecimal.ZERO;
	private BigDecimal businessfee = BigDecimal.ZERO;
	private long deliverystate;
	private BigDecimal cash = BigDecimal.ZERO;
	private BigDecimal pos = BigDecimal.ZERO;
	private String posremark;
	private Timestamp mobilepodtime;
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
	private String shangmenlanshoutime;// 上门揽收时间

	public String getShangmenlanshoutime() {
		return this.shangmenlanshoutime;
	}

	public void setShangmenlanshoutime(String shangmenlanshoutime) {
		this.shangmenlanshoutime = shangmenlanshoutime;
	}

	List<OrderFlowView> orderFlowList;

	List<TranscwbOrderFlowView> transcwborderFlowList;

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

	public long getCurrentbranchid() {
		return this.currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public long getNextbranchid() {
		return this.nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public long getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getBacktocustomer_awb() {
		return this.backtocustomer_awb == null ? "" : this.backtocustomer_awb;
	}

	public void setBacktocustomer_awb(String backtocustomer_awb) {
		this.backtocustomer_awb = backtocustomer_awb;
	}

	public BigDecimal getCarrealweight() {
		if (this.carrealweight == null) {
			this.carrealweight = new BigDecimal("0.00");
		}
		return this.carrealweight;
	}

	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}

	public String getCartype() {
		return this.cartype == null ? "" : this.cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getCarwarehouse() {
		return this.carwarehouse == null ? "" : this.carwarehouse;
	}

	public void setCarwarehouse(String carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public String getCarsize() {
		return this.carsize == null ? "" : this.carsize;
	}

	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}

	public BigDecimal getBackcaramount() {
		if (this.backcaramount == null) {
			this.backcaramount = new BigDecimal("0.00");
		}
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
		if (this.caramount == null) {
			this.caramount = new BigDecimal("0.00");
		}
		return this.caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	public String getBackcarname() {
		return this.backcarname == null ? "" : this.backcarname;
	}

	public void setBackcarname(String backcarname) {
		this.backcarname = backcarname;
	}

	public String getSendcarname() {
		return this.sendcarname == null ? "" : this.sendcarname;
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
		return this.podfeetoheadflag == null ? "" : this.podfeetoheadflag;
	}

	public void setPodfeetoheadflag(String podfeetoheadflag) {
		this.podfeetoheadflag = podfeetoheadflag;
	}

	public String getPodfeetoheadtime() {
		return this.podfeetoheadtime == null ? "" : this.podfeetoheadtime;
	}

	public void setPodfeetoheadtime(String podfeetoheadtime) {
		this.podfeetoheadtime = podfeetoheadtime;
	}

	public String getPodfeetoheadchecktime() {
		return this.podfeetoheadchecktime == null ? "" : this.podfeetoheadchecktime;
	}

	public void setPodfeetoheadchecktime(String podfeetoheadchecktime) {
		this.podfeetoheadchecktime = podfeetoheadchecktime;
	}

	public String getPodfeetoheadcheckflag() {
		return this.podfeetoheadcheckflag == null ? "" : this.podfeetoheadcheckflag;
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
		return this.deliversubscribeday == null ? "" : this.deliversubscribeday;
	}

	public void setDeliversubscribeday(String deliversubscribeday) {
		this.deliversubscribeday = deliversubscribeday;
	}

	public String getCustomerwarehouseid() {
		return this.customerwarehouseid == null ? "" : this.customerwarehouseid;
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
		return this.emaildate == null ? "" : this.emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public long getServiceareaid() {
		return this.serviceareaid;
	}

	public void setServiceareaid(long serviceareaid) {
		this.serviceareaid = serviceareaid;
	}

	public long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getShipcwb() {
		return this.shipcwb == null ? "" : this.shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		this.shipcwb = shipcwb;
	}

	public String getConsigneeno() {
		return this.consigneeno == null ? "" : this.consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = consigneeno;
	}

	public String getConsigneename() {
		return this.consigneename == null ? "" : this.consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return this.consigneeaddress == null ? "" : this.consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	public String getConsigneepostcode() {
		return this.consigneepostcode == null ? "" : this.consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public String getConsigneephone() {
		return this.consigneephone == null ? "" : this.consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getCwbremark() {
		return this.cwbremark == null ? "" : this.cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public String getCustomercommand() {
		return this.customercommand == null ? "" : this.customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	public String getTransway() {
		return this.transway == null ? "" : this.transway;
	}

	public void setTransway(String transway) {
		this.transway = transway;
	}

	public String getCwbprovince() {
		return this.cwbprovince == null ? "" : this.cwbprovince;
	}

	public void setCwbprovince(String cwbprovince) {
		this.cwbprovince = cwbprovince;
	}

	public String getCwbcity() {
		return this.cwbcity == null ? "" : this.cwbcity;
	}

	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}

	public String getCwbcounty() {
		return this.cwbcounty == null ? "" : this.cwbcounty;
	}

	public void setCwbcounty(String cwbcounty) {
		this.cwbcounty = cwbcounty;
	}

	public BigDecimal getReceivablefee() {
		if (this.receivablefee == null) {
			this.receivablefee = new BigDecimal("0.00");
		}
		return this.receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getPaybackfee() {
		if (this.paybackfee == null) {
			this.paybackfee = new BigDecimal("0.00");
		}
		return this.paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public String getCwb() {
		return this.cwb == null ? "" : this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getShipperid() {
		return this.shipperid;
	}

	public void setShipperid(long shipperid) {
		this.shipperid = shipperid;
	}

	public int getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getConsigneemobile() {
		return this.consigneemobile == null ? "" : this.consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	public String getTranscwb() {
		return this.transcwb == null ? "" : this.transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public String getDestination() {
		return this.destination == null ? "" : this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCwbdelivertypeid() {
		return this.cwbdelivertypeid == null ? "" : this.cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(String cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	public String getExceldeliver() {
		return this.exceldeliver == null ? "" : this.exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getExcelbranch() {
		return this.excelbranch == null ? "" : this.excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public long getExcelimportuserid() {
		return this.excelimportuserid;
	}

	public void setExcelimportuserid(long excelimportuserid) {
		this.excelimportuserid = excelimportuserid;
	}

	public long getState() {
		return this.state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public String getPrinttime() {
		return this.printtime == null ? "" : this.printtime;
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
		return this.commoncwb == null ? "" : this.commoncwb;
	}

	public void setCommoncwb(String commoncwb) {
		this.commoncwb = commoncwb;
	}

	public String getModelname() {
		return this.modelname == null ? "" : this.modelname;
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
		return this.backreason == null ? "" : this.backreason;
	}

	public void setBackreason(String backreason) {
		this.backreason = backreason;
	}

	public String getLeavedreason() {
		return this.leavedreason == null ? "" : this.leavedreason;
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
		return this.newpaywayid == null ? "" : this.newpaywayid;
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

	public long getCwbstate() {
		return this.cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public String getOrderType() {
		return CwbOrderTypeIdEnum.getByValue(this.cwbordertypeid).getText();
	}

	public String getPaytypeNameOld() {
		PaytypeEnum text = PaytypeEnum.getByValue((int) this.paywayid);
		return text == null ? "" : text.getText();
		// return (paywayid + "").replace("1",
		// PaytypeEnum.Xianjin.getText()).replace("2",
		// PaytypeEnum.Pos.getText()).replace("3",
		// PaytypeEnum.Zhipiao.getText()).replace("4",
		// PaytypeEnum.Qita.getText());
	}

	public String getPaytypeName() {
		if(this.newpaywayid==null||"".equals(this.newpaywayid)){
			return "";
		}
	
		PaytypeEnum text = PaytypeEnum.getByValue(Integer.parseInt(this.newpaywayid));
		return text == null ? "" : text.getText();
		// return (newpaywayid + "").replace("1",
		// PaytypeEnum.Xianjin.getText()).replace("2",
		// PaytypeEnum.Pos.getText()).replace("3",
		// PaytypeEnum.Zhipiao.getText()).replace("4",
		// PaytypeEnum.Qita.getText());
	}

	public String getFlowordertypeMethod() {
		CwbFlowOrderTypeEnum text = CwbFlowOrderTypeEnum.getText(this.flowordertype);
		return text == null ? "" : text.getText();
	}

	public List<OrderFlowView> getOrderFlowList() {
		return this.orderFlowList;
	}

	public void setOrderFlowList(List<OrderFlowView> orderFlowList) {
		this.orderFlowList = orderFlowList;
	}

	public List<TranscwbOrderFlowView> getTranscwborderFlowList() {
		return this.transcwborderFlowList;
	}

	public void setTranscwborderFlowList(List<TranscwbOrderFlowView> transcwborderFlowList) {
		this.transcwborderFlowList = transcwborderFlowList;
	}

	public long getDeliveryid() {
		return this.deliveryid;
	}

	public void setDeliveryid(long deliveryid) {
		this.deliveryid = deliveryid;
	}

	public BigDecimal getReceivedfee() {
		if (this.receivedfee == null) {
			this.receivedfee = new BigDecimal("0.00");
		}
		return this.receivedfee;
	}

	public void setReceivedfee(BigDecimal receivedfee) {
		this.receivedfee = receivedfee;
	}

	public BigDecimal getReturnedfee() {
		if (this.returnedfee == null) {
			this.returnedfee = new BigDecimal("0.00");
		}
		return this.returnedfee;
	}

	public void setReturnedfee(BigDecimal returnedfee) {
		this.returnedfee = returnedfee;
	}

	public BigDecimal getBusinessfee() {
		if (this.businessfee == null) {
			this.businessfee = new BigDecimal("0.00");
		}
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
		if (this.cash == null) {
			this.cash = new BigDecimal("0.00");
		}
		return this.cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getPos() {
		if (this.pos == null) {
			this.pos = new BigDecimal("0.00");
		}
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

	public Timestamp getMobilepodtime() {
		return this.mobilepodtime;
	}

	public void setMobilepodtime(Timestamp mobilepodtime) {
		this.mobilepodtime = mobilepodtime;
	}

	public BigDecimal getCheckfee() {
		if (this.checkfee == null) {
			this.checkfee = new BigDecimal("0.00");
		}
		return this.checkfee;
	}

	public void setCheckfee(BigDecimal checkfee) {
		this.checkfee = checkfee;
	}

	public String getCheckremark() {
		return this.checkremark == null ? "" : this.checkremark;
	}

	public void setCheckremark(String checkremark) {
		this.checkremark = checkremark;
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
		if (this.otherfee == null) {
			this.otherfee = new BigDecimal("0.00");
		}
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
		return this.deliverstateremark == null ? "" : this.deliverstateremark;
	}

	public void setDeliverstateremark(String deliverstateremark) {
		this.deliverstateremark = deliverstateremark;
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

	public int getSign_typeid() {
		return this.sign_typeid;
	}

	public void setSign_typeid(int sign_typeid) {
		this.sign_typeid = sign_typeid;
	}

	public String getSign_man() {
		return this.sign_man == null ? "" : this.sign_man;
	}

	public void setSign_man(String sign_man) {
		this.sign_man = sign_man;
	}

	public String getSign_time() {
		return this.sign_time == null ? "" : this.sign_time;
	}

	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}

	public String getCwbdelivertypeStr() {
		return this.cwbdelivertypeid.equals("2") ? "加急" : "普通";
	}

	public String getConsigneenameOfkf() {
		return this.consigneenameOfkf;
	}

	public void setConsigneenameOfkf(String consigneenameOfkf) {
		this.consigneenameOfkf = consigneenameOfkf;
	}

	public String getConsigneemobileOfkf() {
		return this.consigneemobileOfkf;
	}

	public void setConsigneemobileOfkf(String consigneemobileOfkf) {
		this.consigneemobileOfkf = consigneemobileOfkf;
	}

	public String getConsigneephoneOfkf() {
		return this.consigneephoneOfkf;
	}

	public void setConsigneephoneOfkf(String consigneephoneOfkf) {
		this.consigneephoneOfkf = consigneephoneOfkf;
	}

	public String getDeliveryStateText() {
		for (DeliveryStateEnum fote : DeliveryStateEnum.values()) {
			if (fote.getValue() == this.deliverystate) {
				return fote.getText();
			}
		}
		return "";
	}
	
	public String getOldconsigneeaddress() {
		return oldconsigneeaddress;
	}

	public void setOldconsigneeaddress(String oldconsigneeaddress) {
		this.oldconsigneeaddress = oldconsigneeaddress;
	}

	//zhili01.liang 20160830 货物尺寸类型修改审核====Begin====
	public int getGoodsSizeType() {
		return goodsSizeType;
	}

	public void setGoodsSizeType(int goodsSizeType) {
		this.goodsSizeType = goodsSizeType;
	}
	//zhili01.liang 20160830 货物尺寸类型修改审核====End====

	
	
}
