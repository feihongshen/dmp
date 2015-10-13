package cn.explink.domain;

import java.math.BigDecimal;

import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

public class CwbOrderCopyForDmp {
	long opscwbid; // 主键id
	long startbranchid; // 当前所在机构id
	long nextbranchid; // 下一站目的机构id
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
	String emaildate;// 发货时间
	long serviceareaid;// 服务区域id
	long customerid;// 供货商id
	String shipcwb;// 配送单号
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
	String cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
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
	int floworderid; // 订单操作过程id
	int branchid; // 站点id
	int emaildateid; // 时间表的id
	String remark1 = "";// 备注1
	String remark2 = "";// 备注1
	String remark3 = "";// 备注1
	String remark4 = "";// 备注1
	String remark5 = "";// 备注1
	private String paytype; // 支付方式（枚举，逗号隔开）
	private DeliveryState deliverstate; // 派送反馈表

	// ===新加字段 对接查询部分====2012-11-05=====
	private long targetcarwarehouse;// 目标仓库
	String nowtime = "";// 最新修改时间
	String leavedreasonStr = "";// 滞留原因
	String inhouse = "";// 入库仓库
	BigDecimal realweight = BigDecimal.ZERO; // 称重重量
	String goodsremark = "";// 货品备注（超大、易碎）
	String paytype_old;
	private String expt_code;// 供货商异常编码
	private String expt_msg;// 供货商异常原因
	private String credate; // 创建时间
	String startbranchname;// 当前所在的站点
	String nextbranchname; // 目的站点
	private User deliverUser; // 派送员对象
	private User operatorUser; // 操作员对象
	private String multi_shipcwb; // 供货商订单号,多个逗号隔开
	private long backreasonid; // 拒收原因id
	private String backreason; // 拒收原因
	private String leavedreason; // 滞留原因
	String carwarehousename = "";// 入库仓库名称
	String customerwarehousename = "";// 客户发货仓库名称

	public DeliveryState getDeliverstate() {
		return deliverstate;
	}

	public void setDeliverstate(DeliveryState deliverstate) {
		this.deliverstate = deliverstate;
	}

	public long getBackreasonid() {
		return backreasonid;
	}

	public void setBackreasonid(long backreasonid) {
		this.backreasonid = backreasonid;
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

	public String getMulti_shipcwb() {
		return multi_shipcwb;
	}

	public void setMulti_shipcwb(String multi_shipcwb) {
		this.multi_shipcwb = multi_shipcwb;
	}

	public User getOperatorUser() {
		return operatorUser;
	}

	public void setOperatorUser(User operatorUser) {
		this.operatorUser = operatorUser;
	}

	public User getDeliverUser() {
		return deliverUser;
	}

	public void setDeliverUser(User deliverUser) {
		this.deliverUser = deliverUser;
	}

	public String getNextbranchname() {
		return nextbranchname;
	}

	public void setNextbranchname(String nextbranchname) {
		this.nextbranchname = nextbranchname;
	}

	public String getStartbranchname() {
		return startbranchname;
	}

	public void setStartbranchname(String startbranchname) {
		this.startbranchname = startbranchname;
	}

	public String getCredate() {
		return credate;
	}

	public void setCredate(String credate) {
		this.credate = credate;
	}

	public String getNowtime() {
		return nowtime;
	}

	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}

	public String getLeavedreasonStr() {
		return leavedreasonStr;
	}

	public void setLeavedreasonStr(String leavedreasonStr) {
		this.leavedreasonStr = leavedreasonStr;
	}

	public String getInhouse() {
		return inhouse;
	}

	public void setInhouse(String inhouse) {
		this.inhouse = inhouse;
	}

	public BigDecimal getRealweight() {
		return realweight;
	}

	public void setRealweight(BigDecimal realweight) {
		this.realweight = realweight;
	}

	public String getGoodsremark() {
		return goodsremark;
	}

	public void setGoodsremark(String goodsremark) {
		this.goodsremark = goodsremark;
	}

	public String getPaytype_old() {
		return paytype_old;
	}

	public void setPaytype_old(String paytype_old) {
		this.paytype_old = paytype_old;
	}

	public String getExpt_code() {
		return expt_code;
	}

	public void setExpt_code(String expt_code) {
		this.expt_code = expt_code;
	}

	public String getExpt_msg() {
		return expt_msg;
	}

	public void setExpt_msg(String expt_msg) {
		this.expt_msg = expt_msg;
	}

	public String getCarwarehousename() {
		return carwarehousename;
	}

	public void setCarwarehousename(String carwarehousename) {
		this.carwarehousename = carwarehousename;
	}

	public String getCustomerwarehousename() {
		return customerwarehousename;
	}

	public void setCustomerwarehousename(String customerwarehousename) {
		this.customerwarehousename = customerwarehousename;
	}

	public long getTargetcarwarehouse() {
		return targetcarwarehouse;
	}

	public void setTargetcarwarehouse(long targetcarwarehouse) {
		this.targetcarwarehouse = targetcarwarehouse;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
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
		this.consigneeaddress = consigneeaddress.replaceAll("#", "").replaceAll("[*]", "").replaceAll(" ", "").replaceAll("\\p{P}", "");
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

	public void setReceivablefee(String receivablefee) {
		try {
			this.receivablefee = new BigDecimal(receivablefee);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("費用不是有效的數字格式:" + receivablefee);
		}
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(String paybackfee) {
		try {
			this.paybackfee = new BigDecimal(paybackfee);
		} catch (Exception e) {
			throw new IllegalArgumentException("退費不是有效的數字格式:" + paybackfee);
		}
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

	public BigDecimal getcarrealweight() {
		return carrealweight;
	}

	public void setCarrealweight(String carrealweight) {
		try {
			this.carrealweight = new BigDecimal(carrealweight);
		} catch (Exception e) {
			throw new IllegalArgumentException("货物重量不是有效的数字格式:" + carrealweight);
		}
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

	public void setBackcaramount(String backcaramount) {
		try {
			this.backcaramount = new BigDecimal(backcaramount);
		} catch (Exception e) {
			throw new IllegalArgumentException("取回货物金额不是有效的數字格式:" + backcaramount);
		}
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

	public String getEmailfinishflagName() {
		for (EmailFinishFlagEnum effe : EmailFinishFlagEnum.values()) {
			if (effe.getValue() == this.emailfinishflag)
				return effe.getText();
		}
		return "";
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

	public String getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getCwbordertypeName() {
		for (CwbOrderTypeIdEnum cotie : CwbOrderTypeIdEnum.values()) {
			if (cotie.getValue() == Integer.parseInt(this.cwbordertypeid))
				return cotie.getText();
		}
		return "";
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

	public int getFloworderid() {
		return floworderid;
	}

	public void setFloworderid(int floworderid) {
		this.floworderid = floworderid;
	}

	public int getBranchid() {
		return branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public int getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(int emaildateid) {
		this.emaildateid = emaildateid;
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

	public void setPodfeetoheadtime(String podfeetoheadtime) {
		this.podfeetoheadtime = podfeetoheadtime;
	}

	public void setPodfeetoheadchecktime(String podfeetoheadchecktime) {
		this.podfeetoheadchecktime = podfeetoheadchecktime;
	}

	public void setDeliversubscribeday(String deliversubscribeday) {
		this.deliversubscribeday = deliversubscribeday;
	}

	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getText();
		}
		return "";
	}

}
