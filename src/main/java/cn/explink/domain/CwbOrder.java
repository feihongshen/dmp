package cn.explink.domain;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.SecurityUtil;

public class CwbOrder {

	long opscwbid; // 主键id
	long startbranchid; // 上一个机构id
	long currentbranchid; // 当前机构
	long nextbranchid; // 下一站目的机构id
	long deliverybranchid; // 配送站点
	String backtocustomer_awb;// 退供货商封包批次号
	String cwbflowflag; // 订单流程类型 1正常件 2中转件 3再投件
	BigDecimal carrealweight = BigDecimal.ZERO;; // 货物重量kg
	String cartype;// 货物类别
	String carwarehouse;// 发货仓库
	String carsize;// 商品尺寸
	BigDecimal backcaramount = BigDecimal.ZERO;; // 取回货物金额
	long sendcarnum;// 发货数量
	long backcarnum;// 取货数量
	BigDecimal caramount = BigDecimal.ZERO;;// 货物金额
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
	String podfeetoheadtime;// 站点上交款时间总部交款审
	String podfeetoheadchecktime;// 站点交款总部审核时
	String podfeetoheadcheckflag;// 核状态
	long leavedreasonid;// 滞留原因id
	String deliversubscribeday;// 滞留预约派送日
	String customerwarehouseid;// 客户发货仓库id
	long emaildateid;// 发货时间Id
	String emaildate;// 发货时间
	long serviceareaid;// 服务区域id
	long customerid;// 供货商id
	String shipcwb;// 供货商运单号
	String consigneeno;// 收件人编号
	String consigneename = "";// 收件人名称
	String consigneeaddress;// 收件人地址
	String consigneepostcode;// 收件人邮编
	String consigneephone;// 收件人电话
	String cwbremark;// 订单备注
	String customercommand;// 客户要求
	String transway;// 运输方式
	String cwbprovince;// 省
	String cwbcity;// 市
	String cwbcounty;// 区县
	BigDecimal receivablefee = BigDecimal.ZERO;;// 代收货款应收金额
	BigDecimal paybackfee = BigDecimal.ZERO;;// 上门退货应退金额
	String cwb;// 订单号
	long shipperid;// 退供货商承运商id
	int cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
	String consigneemobile;// 收件人手机
	String transcwb;// 运单号
	String destination;// 目的地
	String cwbdelivertypeid;// 订单入库机构id
	String exceldeliver;// 指定小件员（用于地址库匹配）
	long exceldeliverid; //指定小件员ID
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
	private String consigneenameOfkf;
	private String consigneemobileOfkf;
	private String consigneephoneOfkf;
	private long fnorgbillid; // 订单站点代收货款账单id
	private long fnorgfreightbillid; // 订单站点运费账单id
	private long zhongzhuanreasonid;// 中转id
	private String zhongzhuanreason;// 中转原因
	private BigDecimal fnorgoffset = BigDecimal.ZERO;// 站点账单回写的冲抵金额
	private int fnorgoffsetflag;// 订单在站点账单中被冲抵标志位，0：未收款，1：已收款
	private int firstlevelid; // 一级滞留原因id
	private String city;// 通过百度API匹配的城市
	private String area;// 通过百度API匹配的城区区域

	private long pickbranchid; // 提货站点id
	private long oxopickstate; // oxo揽件状态 。取值参考 CwbOXOStateEnum枚举类
	private long oxodeliverystate; // oxo派件状态 。取值参考 CwbOXOStateEnum枚举类

	private int branchfeebillexportflag;// 加盟商派费账单导出标志
	
	private int deliverypermit;//上门退订单是否可领货 0可领 1不可领
	private int vipclub;// 业务类型
	//zhili01.liang 20160830 货物尺寸类型修改审核
	private int goodsSizeType;//货物尺寸类型 

	// 以下是快递业务新增字段

	/**
	 * 寄件人客户编码
	 */
	private String sendercustomcode;
	/**
	 * 寄件人姓名
	 */
	private String sendername;
	/**
	 * 寄件人证件号
	 */
	private String senderid;
	/**
	 * 寄件人省id
	 */
	private int senderprovinceid;
	/**
	 * 寄件人省
	 */
	private String senderprovince;
	/**
	 * 寄件人市id
	 */
	private int sendercityid;
	/**
	 * 寄件人市
	 */
	private String sendercity;
	/**
	 * 寄件人区id
	 */
	private int sendercountyid;
	/**
	 * 寄件人区
	 */
	private String sendercounty;
	/**
	 * 寄件人街道
	 */
	private int senderstreetid;
	/**
	 * 寄件人街道
	 */
	private String senderstreet;
	/**
	 * 寄件人手机
	 */
	private String sendercellphone;
	/**
	 * 寄件人固话
	 */
	private String sendertelephone;
	/**
	 * 寄件人地址
	 */
	private String senderaddress;
	/**
	 * 收件人公司
	 */
	private Integer reccustomerid;
	/**
	 * 收件人证件号
	 */
	private String recid;
	/**
	 * 收件人省id
	 */
	private int recprovinceid;
	/**
	 * 收件人市id
	 */
	private int reccityid;
	/**
	 * 收件人区id
	 */
	private int reccountyid;
	/**
	 * 收件人街道id
	 */
	private int recstreetid;
	/**
	 * 收件人街道
	 */
	private String recstreet;
	/**
	 * 是否补录（0：否，1：是）
	 */
	private int isadditionflag;
	/**
	 * 委托内容/名称
	 */
	private String entrustname;
	/**
	 * 数量
	 */
	private int sendnum;
	/**
	 * 长度（cm）
	 */
	private int length;
	/**
	 * 宽度（cm）
	 */
	private int width;
	/**
	 * 高度（cm）
	 */
	private int height;
	/**
	 * kgs
	 */
	private double kgs;
	/**
	 * 是否有代收货款（0：否，1：是）
	 */
	private int hascod;
	/**
	 * 包装费用（元）
	 */
	private BigDecimal packagefee = BigDecimal.ZERO;
	/**
	 * 是否有保价（0：否，1：是）
	 */
	private int hasinsurance;
	/**
	 * 保价声明价值（元）
	 */
	private BigDecimal announcedvalue = BigDecimal.ZERO;
	/**
	 * 保价费用（元）
	 */
	private BigDecimal insuredfee = BigDecimal.ZERO;
	/**
	 * 计费重量（kg）
	 */
	private double chargeweight;
	/**
	 * 实际重量（kg）
	 */
	private double realweight;
	/**
	 * 费用合计（运费+包装+保价）
	 */
	private BigDecimal totalfee = BigDecimal.ZERO;
	/**
	 * 始发地代码
	 */
	private String sendareacode;
	/**
	 * 目的地代码
	 */
	private String recareacode;
	/**
	 * 付款方式（1：现付，2：到付，0：月结） //3：第三方支付
	 */
	private int paymethod;
	/**
	 * 月结账号
	 */
	private String monthsettleno;
	/**
	 * 揽件员id
	 */
	private int collectorid;
	/**
	 * 揽件员姓名
	 */
	private String collectorname;
	/**
	 * 入站操作人id
	 */
	private int instationhandlerid;
	/**
	 * 入站操作人姓名
	 */
	private String instationhandlername;
	/**
	 * 入站时间
	 */
	private String instationdatetime;
	/**
	 * 出站操作人id
	 */
	private int outstationhandlerid;
	/**
	 * 出站操作人姓名
	 */
	private String outstationhandlername;
	/**
	 * 出站时间
	 */
	private String outstationdatetime;
	/**
	 * 录入操作人id
	 */
	private int inputhandlerid;
	/**
	 * 录入操作人姓名
	 */
	private String inputhandlername;
	/**
	 * 录入时间
	 */
	private String inputdatetime;
	/**
	 * 补录人id
	 */
	private int completehandlerid;
	/**
	 * 补录人姓名
	 */
	private String completehandlername;
	/**
	 * 补录时间
	 */
	private String completedatetime;

	/**
	 * 入站id
	 */
	private long instationid;
	/**
	 * 入站名称
	 */
	private String instationname;

	/**
	 * 客户运费账单id
	 */
	private int customerfreightbillid;
	/**
	 * 站点运费账单id
	 */
	private int branchfreightbillid;
	/**
	 * 跨省应收运费账单id
	 */
	private int provincereceivablefreightbillid;
	/**
	 * 跨省应收货款账单id
	 */
	private int provincereceivablecodbillid;
	/**
	 * 是否交接标示
	 */
	private int ishandover;
	/**
	 * 交接人id
	 */
	private int instationhandoverid;
	/**
	 * 交接人姓名
	 */
	private String instationhandovername;
	/**
	 * 交接时间
	 */
	private String instationhandovertime;
	
	/**
	 * 服务产品类型
	 */
	private int expressProductType;

    private BigDecimal cargovolume;

    public int getIshandover() {
		return this.ishandover;
	}

	public void setIshandover(int ishandover) {
		this.ishandover = ishandover;
	}

	private long fncustomerpayablebillid; // 应付客户账单id
	private long fncustomerposbillid; // 客户pos抵扣账单 id
	private long fncustomerbillid; // 应收客户账单id

	private int mpsoptstate;// 一票多件操作状态（multiple package shipment,取值同订单操作状态）
	private int mpsallarrivedflag;// 一票多件是否到齐（0：未到齐，1：到齐） MPSAllArrivedFlagEnum

	private int ismpsflag; // 是否一票多件：0默认；1是一票多件 注意：这里只描述开启集单模式才起作用
	
	private String tpstranscwb;// tps运单号
	
	private String orderSource;// 订单类型
	
	private int exchangeflag;//换货标志：0不是，1是
	private String exchangecwb;//换货关联的配送订单号或上门退订单号

	public int getInstationhandoverid() {
		return this.instationhandoverid;
	}

	public void setInstationhandoverid(int instationhandoverid) {
		this.instationhandoverid = instationhandoverid;
	}

	public String getInstationhandovername() {
		return this.instationhandovername;
	}

	public void setInstationhandovername(String instationhandovername) {
		this.instationhandovername = instationhandovername;
	}

	public String getInstationhandovertime() {
		return this.instationhandovertime;
	}

	public void setInstationhandovertime(String instationhandovertime) {
		this.instationhandovertime = instationhandovertime;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * @param city
	 *            the city to set
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
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @param podfeetoheadtime
	 *            the podfeetoheadtime to set
	 */
	public void setPodfeetoheadtime(String podfeetoheadtime) {
		this.podfeetoheadtime = podfeetoheadtime;
	}

	/**
	 * @param podfeetoheadchecktime
	 *            the podfeetoheadchecktime to set
	 */
	public void setPodfeetoheadchecktime(String podfeetoheadchecktime) {
		this.podfeetoheadchecktime = podfeetoheadchecktime;
	}

	/**
	 * @param deliversubscribeday
	 *            the deliversubscribeday to set
	 */
	public void setDeliversubscribeday(String deliversubscribeday) {
		this.deliversubscribeday = deliversubscribeday;
	}

	private String changereason; // 中转原因
	private long firstchangereasonid; // 一级中转原因

	public long getFirstchangereasonid() {
		return this.firstchangereasonid;
	}

	public void setFirstchangereasonid(long firstchangereasonid) {
		this.firstchangereasonid = firstchangereasonid;
	}

	private long changereasonid;

	public String getChangereason() {
		return this.changereason;
	}

	public void setChangereason(String changereason) {
		this.changereason = changereason;
	}

	public long getChangereasonid() {
		return this.changereasonid;
	}

	public void setChangereasonid(long changereasonid) {
		this.changereasonid = changereasonid;
	}

	public int getFirstlevelid() {
		return this.firstlevelid;
	}

	public void setFirstlevelid(int firstlevelid) {
		this.firstlevelid = firstlevelid;
	}

	public int getFnorgoffsetflag() {
		return this.fnorgoffsetflag;
	}

	public void setFnorgoffsetflag(int fnorgoffsetflag) {
		this.fnorgoffsetflag = fnorgoffsetflag;
	}

	public BigDecimal getFnorgoffset() {
		return this.fnorgoffset;
	}

	public void setFnorgoffset(BigDecimal fnorgoffset) {
		this.fnorgoffset = fnorgoffset;
	}

	public long getZhongzhuanreasonid() {
		return this.zhongzhuanreasonid;
	}

	public void setZhongzhuanreasonid(long zhongzhuanreasonid) {
		this.zhongzhuanreasonid = zhongzhuanreasonid;
	}

	public String getZhongzhuanreason() {
		return this.zhongzhuanreason;
	}

	public void setZhongzhuanreason(String zhongzhuanreason) {
		this.zhongzhuanreason = zhongzhuanreason;
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
		return SecurityUtil.getInstance().decrypt(this.consigneephone);
	}

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			this.setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return SecurityUtil.getInstance().decrypt(this.consigneemobile);
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
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
		// if (!(this.ismpsflag == IsmpsflagEnum.yes.getValue())) {
		return this.scannum;
		// } else {
		// (YpdjHandleRecordDAO)
		// ApplicationContextUtil.getBean("ypdjHandleRecordDAO");
		// long scannedNumber = ().getScannedNumber(this.cwb,
		// this.currentbranchid, this.flowordertype);
		// return this.sendcarnum - scannedNumber;
		// }

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
		if (this.receivablefee == null) {
			return BigDecimal.ZERO.add(this.paybackfee).abs();
		}
		return this.receivablefee.add(this.paybackfee).abs();
	}

	@JsonIgnore
	public boolean isOut() {
		if (this.receivablefee == null) {
			return BigDecimal.ZERO.compareTo(this.paybackfee) > 0;
		}
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

	public String getConsigneenameOfkf() {
		return this.consigneenameOfkf;
	}

	public void setConsigneenameOfkf(String consigneenameOfkf) {
		this.consigneenameOfkf = consigneenameOfkf;
	}

	public String getConsigneemobileOfkf() {
		return SecurityUtil.getInstance().decrypt(this.consigneemobileOfkf);
	}

	public void setConsigneemobileOfkf(String consigneemobileOfkf) {
		this.consigneemobileOfkf = consigneemobileOfkf;
	}

	public String getConsigneephoneOfkf() {
		return SecurityUtil.getInstance().decrypt(this.consigneephoneOfkf);
	}

	public void setConsigneephoneOfkf(String consigneephoneOfkf) {
		this.consigneephoneOfkf = consigneephoneOfkf;
	}

	public long getPickbranchid() {
		return this.pickbranchid;
	}

	public void setPickbranchid(long pickbranchid) {
		this.pickbranchid = pickbranchid;
	}

	public long getOxopickstate() {
		return this.oxopickstate;
	}

	public void setOxopickstate(long oxopickstate) {
		this.oxopickstate = oxopickstate;
	}

	public long getOxodeliverystate() {
		return this.oxodeliverystate;
	}

	public void setOxodeliverystate(long oxodeliverystate) {
		this.oxodeliverystate = oxodeliverystate;
	}

	public int getBranchfeebillexportflag() {
		return this.branchfeebillexportflag;
	}

	public void setBranchfeebillexportflag(int branchfeebillexportflag) {
		this.branchfeebillexportflag = branchfeebillexportflag;
	}

	public String getSendercustomcode() {
		return this.sendercustomcode;
	}

	public long getFnorgbillid() {
		return this.fnorgbillid;
	}

	public void setFnorgbillid(long fnorgbillid) {
		this.fnorgbillid = fnorgbillid;
	}

	public long getFnorgfreightbillid() {
		return this.fnorgfreightbillid;
	}

	public void setFnorgfreightbillid(long fnorgfreightbillid) {
		this.fnorgfreightbillid = fnorgfreightbillid;
	}

	public long getFncustomerpayablebillid() {
		return this.fncustomerpayablebillid;
	}

	public void setFncustomerpayablebillid(long fncustomerpayablebillid) {
		this.fncustomerpayablebillid = fncustomerpayablebillid;
	}

	public long getFncustomerposbillid() {
		return this.fncustomerposbillid;
	}

	public void setFncustomerposbillid(long fncustomerposbillid) {
		this.fncustomerposbillid = fncustomerposbillid;
	}

	public long getFncustomerbillid() {
		return this.fncustomerbillid;
	}

	public void setFncustomerbillid(long fncustomerbillid) {
		this.fncustomerbillid = fncustomerbillid;
	}

	public void setSendercustomcode(String sendercustomcode) {
		this.sendercustomcode = sendercustomcode;
	}

	public String getSendername() {
		return this.sendername;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}

	public String getSenderid() {
		return this.senderid;
	}

	public void setSenderid(String senderid) {
		this.senderid = senderid;
	}

	public int getSenderprovinceid() {
		return this.senderprovinceid;
	}

	public void setSenderprovinceid(int senderprovinceid) {
		this.senderprovinceid = senderprovinceid;
	}

	public String getSenderprovince() {
		return this.senderprovince;
	}

	public void setSenderprovince(String senderprovince) {
		this.senderprovince = senderprovince;
	}

	public int getSendercityid() {
		return this.sendercityid;
	}

	public void setSendercityid(int sendercityid) {
		this.sendercityid = sendercityid;
	}

	public String getSendercity() {
		return this.sendercity;
	}

	public void setSendercity(String sendercity) {
		this.sendercity = sendercity;
	}

	public int getSendercountyid() {
		return this.sendercountyid;
	}

	public void setSendercountyid(int sendercountyid) {
		this.sendercountyid = sendercountyid;
	}

	public String getSendercounty() {
		return this.sendercounty;
	}

	public void setSendercounty(String sendercounty) {
		this.sendercounty = sendercounty;
	}

	public int getSenderstreetid() {
		return this.senderstreetid;
	}

	public void setSenderstreetid(int senderstreetid) {
		this.senderstreetid = senderstreetid;
	}

	public String getSenderstreet() {
		return this.senderstreet;
	}

	public void setSenderstreet(String senderstreet) {
		this.senderstreet = senderstreet;
	}

	public String getSendercellphone() {
		return this.sendercellphone;
	}

	public void setSendercellphone(String sendercellphone) {
		this.sendercellphone = sendercellphone;
	}

	public String getSendertelephone() {
		return this.sendertelephone;
	}

	public void setSendertelephone(String sendertelephone) {
		this.sendertelephone = sendertelephone;
	}

	public String getSenderaddress() {
		return this.senderaddress;
	}

	public void setSenderaddress(String senderaddress) {
		this.senderaddress = senderaddress;
	}

	public Integer getReccustomerid() {
		return this.reccustomerid;
	}

	public void setReccustomerid(Integer reccustomerid) {
		this.reccustomerid = reccustomerid;
	}

	public String getRecid() {
		return this.recid;
	}

	public void setRecid(String recid) {
		this.recid = recid;
	}

	public int getRecprovinceid() {
		return this.recprovinceid;
	}

	public void setRecprovinceid(int recprovinceid) {
		this.recprovinceid = recprovinceid;
	}

	public int getReccityid() {
		return this.reccityid;
	}

	public void setReccityid(int reccityid) {
		this.reccityid = reccityid;
	}

	public int getReccountyid() {
		return this.reccountyid;
	}

	public void setReccountyid(int reccountyid) {
		this.reccountyid = reccountyid;
	}

	public int getRecstreetid() {
		return this.recstreetid;
	}

	public void setRecstreetid(int recstreetid) {
		this.recstreetid = recstreetid;
	}

	public String getRecstreet() {
		return this.recstreet;
	}

	public void setRecstreet(String recstreet) {
		this.recstreet = recstreet;
	}

	public int getIsadditionflag() {
		return this.isadditionflag;
	}

	public void setIsadditionflag(int isadditionflag) {
		this.isadditionflag = isadditionflag;
	}

	public String getEntrustname() {
		return this.entrustname;
	}

	public void setEntrustname(String entrustname) {
		this.entrustname = entrustname;
	}

	public int getSendnum() {
		return this.sendnum;
	}

	public void setSendnum(int sendnum) {
		this.sendnum = sendnum;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getKgs() {
		return this.kgs;
	}

	public void setKgs(double kgs) {
		this.kgs = kgs;
	}

	public int getHascod() {
		return this.hascod;
	}

	public void setHascod(int hascod) {
		this.hascod = hascod;
	}

	public BigDecimal getPackagefee() {
		return this.packagefee;
	}

	public void setPackagefee(BigDecimal packagefee) {
		this.packagefee = packagefee;
	}

	public int getHasinsurance() {
		return this.hasinsurance;
	}

	public void setHasinsurance(int hasinsurance) {
		this.hasinsurance = hasinsurance;
	}

	public BigDecimal getAnnouncedvalue() {
		return this.announcedvalue;
	}

	public void setAnnouncedvalue(BigDecimal announcedvalue) {
		this.announcedvalue = announcedvalue;
	}

	public BigDecimal getInsuredfee() {
		return this.insuredfee;
	}

	public void setInsuredfee(BigDecimal insuredfee) {
		this.insuredfee = insuredfee;
	}

	public double getChargeweight() {
		return this.chargeweight;
	}

	public void setChargeweight(double chargeweight) {
		this.chargeweight = chargeweight;
	}

	public double getRealweight() {
		return this.realweight;
	}

	public void setRealweight(double realweight) {
		this.realweight = realweight;
	}

	public BigDecimal getTotalfee() {
		return this.totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public String getSendareacode() {
		return this.sendareacode;
	}

	public void setSendareacode(String sendareacode) {
		this.sendareacode = sendareacode;
	}

	public String getRecareacode() {
		return this.recareacode;
	}

	public void setRecareacode(String recareacode) {
		this.recareacode = recareacode;
	}

	public int getPaymethod() {
		return this.paymethod;
	}

	public void setPaymethod(int paymethod) {
		this.paymethod = paymethod;
	}

	public String getMonthsettleno() {
		return this.monthsettleno;
	}

	public void setMonthsettleno(String monthsettleno) {
		this.monthsettleno = monthsettleno;
	}

	public int getCollectorid() {
		return this.collectorid;
	}

	public void setCollectorid(int collectorid) {
		this.collectorid = collectorid;
	}

	public String getCollectorname() {
		return this.collectorname;
	}

	public void setCollectorname(String collectorname) {
		this.collectorname = collectorname;
	}

	public int getInstationhandlerid() {
		return this.instationhandlerid;
	}

	public void setInstationhandlerid(int instationhandlerid) {
		this.instationhandlerid = instationhandlerid;
	}

	public String getInstationhandlername() {
		return this.instationhandlername;
	}

	public void setInstationhandlername(String instationhandlername) {
		this.instationhandlername = instationhandlername;
	}

	public int getOutstationhandlerid() {
		return this.outstationhandlerid;
	}

	public void setOutstationhandlerid(int outstationhandlerid) {
		this.outstationhandlerid = outstationhandlerid;
	}

	public String getOutstationhandlername() {
		return this.outstationhandlername;
	}

	public void setOutstationhandlername(String outstationhandlername) {
		this.outstationhandlername = outstationhandlername;
	}

	public int getInputhandlerid() {
		return this.inputhandlerid;
	}

	public void setInputhandlerid(int inputhandlerid) {
		this.inputhandlerid = inputhandlerid;
	}

	public String getInputhandlername() {
		return this.inputhandlername;
	}

	public void setInputhandlername(String inputhandlername) {
		this.inputhandlername = inputhandlername;
	}

	public int getCompletehandlerid() {
		return this.completehandlerid;
	}

	public void setCompletehandlerid(int completehandlerid) {
		this.completehandlerid = completehandlerid;
	}

	public String getCompletehandlername() {
		return this.completehandlername;
	}

	public void setCompletehandlername(String completehandlername) {
		this.completehandlername = completehandlername;
	}

	public long getInstationid() {
		return this.instationid;
	}

	public void setInstationid(long instationid) {
		this.instationid = instationid;
	}

	public String getInstationname() {
		return this.instationname;
	}

	public void setInstationname(String instationname) {
		this.instationname = instationname;
	}

	public int getCustomerfreightbillid() {
		return this.customerfreightbillid;
	}

	public void setCustomerfreightbillid(int customerfreightbillid) {
		this.customerfreightbillid = customerfreightbillid;
	}

	public int getBranchfreightbillid() {
		return this.branchfreightbillid;
	}

	public void setBranchfreightbillid(int branchfreightbillid) {
		this.branchfreightbillid = branchfreightbillid;
	}

	public int getProvincereceivablefreightbillid() {
		return this.provincereceivablefreightbillid;
	}

	public void setProvincereceivablefreightbillid(int provincereceivablefreightbillid) {
		this.provincereceivablefreightbillid = provincereceivablefreightbillid;
	}

	public int getProvincereceivablecodbillid() {
		return this.provincereceivablecodbillid;
	}

	public void setProvincereceivablecodbillid(int provincereceivablecodbillid) {
		this.provincereceivablecodbillid = provincereceivablecodbillid;
	}

	public String getInstationdatetime() {
		return this.instationdatetime;
	}

	public void setInstationdatetime(String instationdatetime) {
		this.instationdatetime = instationdatetime;
	}

	public String getOutstationdatetime() {
		return this.outstationdatetime;
	}

	public void setOutstationdatetime(String outstationdatetime) {
		this.outstationdatetime = outstationdatetime;
	}

	public String getInputdatetime() {
		return this.inputdatetime;
	}

	public void setInputdatetime(String inputdatetime) {
		this.inputdatetime = inputdatetime;
	}

	public String getCompletedatetime() {
		return this.completedatetime;
	}

	public void setCompletedatetime(String completedatetime) {
		this.completedatetime = completedatetime;
	}

	public int getMpsoptstate() {
		return this.mpsoptstate;
	}

	public void setMpsoptstate(int mpsoptstate) {
		this.mpsoptstate = mpsoptstate;
	}

	public int getMpsallarrivedflag() {
		return this.mpsallarrivedflag;
	}

	public void setMpsallarrivedflag(int mpsallarrivedflag) {
		this.mpsallarrivedflag = mpsallarrivedflag;
	}

	public int getIsmpsflag() {
		return this.ismpsflag;
	}

	public void setIsmpsflag(int ismpsflag) {
		this.ismpsflag = ismpsflag;
	}

	public int getDeliverypermit() {
		return deliverypermit;
	}

	public void setDeliverypermit(int deliverypermit) {
		this.deliverypermit = deliverypermit;
	}

	public int getVipclub() {
		return vipclub;
	}

	public void setVipclub(int vipclub) {
		this.vipclub = vipclub;
	}
	public int getExpressProductType() {
		return expressProductType;
	}

	public void setExpressProductType(int expressProductType) {
		this.expressProductType = expressProductType;
	}
	
    public void setCargovolume(BigDecimal cargovolume) {
        this.cargovolume = cargovolume;
    }

    public BigDecimal getCargovolume() {
        return cargovolume;
    }

	public long getExceldeliverid() {
		return exceldeliverid;
	}

	public void setExceldeliverid(long exceldeliverid) {
		this.exceldeliverid = exceldeliverid;
	}
		public String getTpstranscwb() {
		return tpstranscwb;
	}

	public void setTpstranscwb(String tpstranscwb) {
		this.tpstranscwb = tpstranscwb;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	//zhili01.liang 20160830 货物尺寸类型修改审核=====Begin=====
	public int getGoodsSizeType() {
		return goodsSizeType;
	}

	public void setGoodsSizeType(int goodsSizeType) {
		this.goodsSizeType = goodsSizeType;
	}
	//zhili01.liang 20160830 货物尺寸类型修改审核=====Begin=====

	public int getExchangeflag() {
		return exchangeflag;
	}

	public void setExchangeflag(int exchangeflag) {
		this.exchangeflag = exchangeflag;
	}

	public String getExchangecwb() {
		return exchangecwb;
	}

	public void setExchangecwb(String exchangecwb) {
		this.exchangecwb = exchangecwb;
	}
	
}
