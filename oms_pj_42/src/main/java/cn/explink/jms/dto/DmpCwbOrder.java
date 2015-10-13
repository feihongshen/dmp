package cn.explink.jms.dto;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EmailFinishFlagEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DmpCwbOrder {

	private long opscwbid; // 主键id
	private long startbranchid; // 上一个机构id
	private long currentbranchid; // 当前机构
	private long nextbranchid; // 下一站目的机构id
	private long deliverybranchid; // 配送站点
	private String backtocustomer_awb;// 退供货商封包批次号
	private String cwbflowflag; // 订单流程类型 1正常件 2中转件 3再投件
	private BigDecimal carrealweight; // 货物重量kg
	private String cartype;// 货物类别
	private String carwarehouse;// 发货仓库
	private String carsize;// 商品尺寸
	private BigDecimal backcaramount; // 取回货物金额
	private long sendcarnum;// 发货数量
	private long backcarnum;// 取货数量
	private BigDecimal caramount;// 货物金额
	private String backcarname; // 取回商品名称
	private String sendcarname;// 发出商品名称
	private long deliverid; // 小件员id
	private int emailfinishflag;// 库房入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	private int reacherrorflag;// 站点入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	private long orderflowid;// 对订单的最后一次操作对应的监控记录
	private long flowordertype; // 操作类型，具体对应枚举类FlowOrderTypeEnum
	private long cwbreachbranchid;// 订单入库机构id
	private long cwbreachdeliverbranchid;// 订单到货派送机构id
	private String podfeetoheadflag;// 站点收款是否已上交总部 0未上交 1已上交
	private String podfeetoheadtime;// 站点上交款时间
	private String podfeetoheadchecktime;// 站点交款总部审核时
	private String podfeetoheadcheckflag;// 总部交款审核状态
	private long leavedreasonid = 0l;// 滞留原因id
	private long firstlevelid = 0l;// 一级滞留原因id
	private String deliversubscribeday;// 滞留预约派送日
	private String customerwarehouseid;// 客户发货仓库id
	long emaildateid;// 发货时间Id
	private String emaildate;// 发货时间
	private long serviceareaid;// 服务区域id
	private long customerid;// 供货商id
	private String shipcwb;// 供货商运单号
	private String consigneeno;// 收件人编号
	private String consigneename;// 收件人名称
	private String consigneeaddress;// 收件人地址
	private String consigneepostcode;// 收件人邮编
	private String consigneephone;// 收件人电话
	private String cwbremark;// 订单备注
	private String customercommand;// 客户要求
	private String transway;// 运输方式
	private String cwbprovince;// 省
	private String cwbcity;// 市
	private String cwbcounty;// 区县
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额
	private String cwb;// 订单号
	private long shipperid;// 退供货商承运商id
	private String cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
	private String consigneemobile;// 收件人手机
	private String transcwb;// 运单号
	private String destination;// 目的地
	private String cwbdelivertypeid;// 订单入库机构id
	private String exceldeliver;// 指定小件员（用于地址库匹配）
	private String excelbranch;// 指定派送分站（用于地址库匹配）
	private long excelimportuserid;// 导入操作员id
	private long state;// 是否显示的状态
	private String printtime;// 打印时间（针对上门退订单）
	private long commonid;// （承运商id）
	private String commoncwb;// （承运商订单号）

	private long signtypeid; // 20120822 zhang 签收状态
	private String podrealname; // 签收人
	private String podtime; // 签收时间
	private String podsignremark; // 签收备注
	private String modelname;// 模版名称
	private long scannum;// 一票多件扫描件数
	private long isaudit;// 客服是否审核，可退供货商出库
	private String backreason;// 退货备注
	private String leavedreason;// 滞留原因
	private long paywayid;// 支付方式（导入数据的时候会导入）
	private String newpaywayid;// 新的支付方式（反馈的时候可能会更改的支付方式）
	private long tuihuoid;// 退货站id
	private long cwbstate;// 订单现在处于的流程状态（1配送 2退货）
	private String remark1;// 备注一
	private String remark2;// 备注二
	private String remark3;// 备注三
	private String remark4;// 备注四
	private String remark5;// 备注五
	private long backreasonid; // 退货原因id
	private String multi_shipcwb; // 供货商运单号多个逗号隔开

	private long weishuakareasonid;// 未刷卡原因id
	private String weishuakareason;// 未刷卡原因

	private long losereasonid;// 货物丢失原因id
	private String losereason;// 货物丢失原因

	private String resendtime;// 滞留订单再次配送时间
	private String customerbrackhouseremark; // 供货商拒收返库备注

	private String historybranchname;// 历史配送站点
	// 2013-12-10 新加字段
	private long zhongzhuanreasonid;//中转id
	private String zhongzhuanreason;//中转原因
	/**
	 * 广州通路对接接口需要添加的字段
	 */
	private String consigneenameOfkf;
	private String consigneemobileOfkf;
	private String consigneephoneOfkf;
	
	private BigDecimal shouldfare;
	private BigDecimal infactfare;
	

	public String getConsigneenameOfkf() {
		return consigneenameOfkf;
	}

	public void setConsigneenameOfkf(String consigneenameOfkf) {
		this.consigneenameOfkf = consigneenameOfkf;
	}

	public String getConsigneemobileOfkf() {
		return consigneemobileOfkf;
	}

	public void setConsigneemobileOfkf(String consigneemobileOfkf) {
		this.consigneemobileOfkf = consigneemobileOfkf;
	}

	public String getConsigneephoneOfkf() {
		return consigneephoneOfkf;
	}

	public void setConsigneephoneOfkf(String consigneephoneOfkf) {
		this.consigneephoneOfkf = consigneephoneOfkf;
	}

	public long getZhongzhuanreasonid() {
		return zhongzhuanreasonid;
	}

	public void setZhongzhuanreasonid(long zhongzhuanreasonid) {
		this.zhongzhuanreasonid = zhongzhuanreasonid;
	}

	public String getZhongzhuanreason() {
		return zhongzhuanreason;
	}

	public void setZhongzhuanreason(String zhongzhuanreason) {
		this.zhongzhuanreason = zhongzhuanreason;
	}

	public String getHistorybranchname() {
		return historybranchname;
	}

	public void setHistorybranchname(String historybranchname) {
		this.historybranchname = historybranchname;
	}

	public String getCustomerbrackhouseremark() {
		return customerbrackhouseremark;
	}

	public void setCustomerbrackhouseremark(String customerbrackhouseremark) {
		this.customerbrackhouseremark = customerbrackhouseremark;
	}

	public String getResendtime() {
		return resendtime;
	}

	public void setResendtime(String resendtime) {
		this.resendtime = resendtime;
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

	public String getConsigneenameSpecial() {
		return consigneename.replaceAll("&", "").replaceAll("<", "").replaceAll(">", "").replaceAll("'", "").replaceAll("\"", "");
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

	public BigDecimal getBusinessFee() {
		return this.receivablefee.add(this.paybackfee).abs();
	}

	public boolean isOut() {
		return this.receivablefee.compareTo(this.paybackfee) < 0;
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

	public long getFirstlevelid() {
		return firstlevelid;
	}

	public void setFirstlevelid(long firstlevelid) {
		this.firstlevelid = firstlevelid;
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

}
