package cn.explink.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@XmlRootElement
public class CwbOrderView implements Cloneable {
	
	private static Logger logger = LoggerFactory.getLogger(CwbOrderView.class);
	
	long opscwbid; // 主键id
	long startbranchid; // 当前所在机构id
	long nextbranchid; // 下一站目的机构id
	String backtocustomer_awb = "";// 退供货商封包批次号
	String cwbflowflag = ""; // 订单流程类型 1正常件 2中转件 3再投件
	BigDecimal carrealweight = BigDecimal.ZERO; // 货物重量kg
	String cartype = "";// 货物类别
	String carwarehouse = "";// 入库仓库
	String carsize = "";// 商品尺寸
	BigDecimal backcaramount; // 取回货物金额
	long sendcarnum;// 发货数量
	long scannum;// 发货数量
	long backcarnum;// 取货数量
	BigDecimal caramount = BigDecimal.ZERO;;// 货物金额
	String backcarname = ""; // 取回商品名称
	String sendcarname = "";// 发出商品名称
	long deliverid; // 小件员id
	int emailfinishflag;// 库房入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	int reacherrorflag;// 站点入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	long orderflowid;// 对订单的最后一次操作对应的监控记录
	long flowordertype; // 操作类型，具体对应枚举类FlowOrderTypeEnum
	long cwbreachbranchid;// 订单入库机构id
	long cwbreachdeliverbranchid;// 订单到货派送机构id
	String podfeetoheadflag = "";// 站点收款是否已上交总部 0未上交 1已上交
	String podfeetoheadtime = "";// 站点上交款时间
	String podfeetoheadchecktime = "";// 站点交款总部审核时
	String podfeetoheadcheckflag = "";// 总部交款审核状态
	long leavedreasonid;// 滞留原因id
	String deliversubscribeday = "";// 滞留预约派送日
	String customerwarehouseid = "";// 客户发货仓库id
	String emaildate = "";// 邮件时间
	String shiptime = "";// 供货商发货时间
	long serviceareaid;// 服务区域id
	long customerid;// 供货商id
	String shipcwb = "";// 配送单号
	String consigneeno = "";// 收件人编号
	String consigneename = "";// 收件人名称
	String consigneeaddress = "";// 收件人地址
	String consigneepostcode = "";// 收件人邮编
	String consigneephone = "";// 收件人电话
	String cwbremark = "";// 订单备注
	String customercommand = "";// 客户要求
	String transway = "";// 运输方式
	String cwbprovince = "";// 省
	String cwbcity = "";// 市
	String cwbcounty = "";// 区县
	BigDecimal receivablefee = BigDecimal.ZERO;;// 代收货款应收金额
	BigDecimal paybackfee = BigDecimal.ZERO;;// 上门退货应退金额
	String cwb = "";// 订单号
	long shipperid;// 退供货商承运商id
	String cwbordertypeid = "";// 订单类型（1配送 2上门退 3上门换）
	String consigneemobile = "";// 收件人手机
	String transcwb = "";// 运单号
	String destination = "";// 目的地
	String cwbdelivertypeid = "";// 订单入库机构id
	String exceldeliver = "";// 指定小件员（用于地址库匹配）
	String excelbranch = "";// 指定派送分站（用于地址库匹配）
	String timelimited;// 地址库匹配时效
	long excelimportuserid;// 导入操作员id
	long state;// 是否显示的状态
	String delivername = ""; // 小件员的名称
	String customername = "";// 供货商的名称
	String branchname = ""; // 站点的名称
	String commonname = ""; // 承运商
	String newfollownotes = ""; // 最新跟踪记录
	long marksflag; // 标记状态
	String marksflagmen = ""; // 标记人
	long commonid; // 承运商id
	String allfollownotes = "";// 完整跟踪记录
	BigDecimal primitivemoney = BigDecimal.ZERO;; // 原始金额
	String marksflagtime = ""; // 标记时间
	String edittime = ""; // 修改时间
	String editman = ""; // 修改人
	String signinman = ""; // 签收人
	String signintime = ""; // 签收时间

	String returngoodsremark = "";// 退货备注

	String commonnumber = ""; // 承运商编号
	long auditstate; // 审核状态
	String auditor = ""; // 审核人
	String audittime = ""; // 审核时间
	String editsignintime = ""; // 签收时间
	int floworderid; // 订单操作过程id
	int branchid; // 站点id
	int emaildateid; // 时间表的id
	String instoreroomtime = "";// 入库时间
	String remark1 = "";// 备注1
	String remark2 = "";// 备注1
	String remark3 = "";// 备注1
	String remark4 = "";// 备注1
	String remark5 = "";// 备注1
	String startbranchname = "";// 上一站机构名称
	String currentbranchname = "";
	String nextbranchname = "";// 下一站机构名称
	String outstoreroomtime = "";// 出库时间
	String inSitetime = "";// 到站时间
	String inSiteBranchname = "";// 到站站点名称
	String pickGoodstime = "";// 小件员领货时间
	String sendSuccesstime = "";// 配送成功时间
	String jushoutime = "";// 拒收时间
	String gobacktime = "";// 反馈时间
	String goclasstime = "";// 归班时间
	String nowtime = "";// 最新修改时间
	String tuigonghuoshangchukutime = "";// 退供货商出库时间
	String leavedreasonStr = "";// 滞留原因
	String inhouse = "";// 入库仓库
	BigDecimal realweight = BigDecimal.ZERO; // 称重重量
	String goodsremark = "";// 货品备注（超大、易碎）
	String paytype_old;

	String paytype = "";// 支付方式
	String carwarehousename = "";// 入库仓库名称
	String customerwarehousename = "";// 客户发货仓库名称
	String deliverybranch;// 配送站点
	long cwbstate;// 订单现在处于的流程状态（1配送 2退货）
	long currentbranchid; // 当前机构

	// -------------加字段 反馈表---------------------、、

	private long fdeliverid; // 反馈小件员id
	private String fdelivername; // 反馈小件员姓名
	private BigDecimal receivedfee = BigDecimal.ZERO; // 收到总金额
	private BigDecimal returnedfee = BigDecimal.ZERO; // 退还金额
	private BigDecimal businessfee = BigDecimal.ZERO; // 应处理金额
	private long deliverystate; // 配送状态（配送成功、上门退成功、上门换成功、全部退货、部分退货、分站滞留、上门拒退、货物丢失）
	private BigDecimal cash = BigDecimal.ZERO; // 现金实收
	private BigDecimal pos = BigDecimal.ZERO; // pos实收
	private String posremark; // pos备注
	private Timestamp mobilepodtime; // pos反馈时间
	private BigDecimal checkfee = BigDecimal.ZERO; // 支票实收
	private String checkremark; // 支票号备注
	private long receivedfeeuser; // 收款人
	private long statisticstate; // 归班状态(1未归班 2已归班 3暂不处理)
	private String statisticstateStr; // 归班状态名称(id转换中文)

	private String createtime; // 创建时间
	private BigDecimal otherfee = BigDecimal.ZERO; // 其他金额
	private long podremarkid; // 配送结果备注id
	private String deliverstateremark; // 反馈的备注输入内容
	private int gcaid; // 归班审核记录的id对应这条反馈现在属于那次归班审核
	private long gobackid; // 反馈id
	private long payupbranchid; // 反馈站点
	private String payupbranchname; // 反馈站点名称
	private String podremarkStr; // 配送结果备注
	private String receivedfeeuserName; // 收款人姓名

	// --------------上交款表---
	private String payuprealname; // 上交款人姓名
	// -------------统计有单无货 和有货无单---------
	private long youdanwuhuoBranchid; // 有单无货站点
	private long youhuowudanBranchid; // 有货无单站点

	// ------------投递率统计--------
	private long tuotouTime; // 发货妥投时间
	private long youjieguoTime; // 发货有结果时间

	private long rukutuotouTime; // 入库妥投时间
	private long rukuyoujieguoTime; // 入库有结果时间

	private long daozhantuotouTime; // 到站妥投时间
	private long daozhanyoujieguoTime; // 到站有结果时间

	// ---------订单是否上缴款-------

	private long ispayUp = 0; // 1为已上缴，0为未上交

	// -------- 订单是否欠款-----------
	private long isQiankuan = 0; // 1为欠款，0为未欠款

	// -----------退货再投----------
	private long auditEganstate = 0; // 退货再投审核

	// ---------当当对接需要 ：当前操作员20120920-----------------
	private String operatorName;

	private long ruku_dangdang_flag;// 入库 推送当当状态 0未执行，1已执行，2，推送成功，3推送失败)
	private long chuku_dangdang_flag;// 小件员领货 推送当当状态 0未执行，1已执行，2，推送成功，3推送失败)
	private long deliverystate_dangdang_flag;// 反馈时 推送当当状态
												// 0未执行，1已执行，2，推送成功，3推送失败)

	long backreasonid;// 退货原因id
	String backreason;// 退货原因
	// --------------添加新字段----------
	private String expt_code;// 供货商异常编码
	private String expt_msg;// 供货商异常原因

	private long orderResultType;// 订单配送结果

	// ===新加字段
	private long targetcarwarehouse;// 目标仓库
	private String targetcarwarehouseName;// 目标仓库名称
	private String multi_shipcwb; // 运单号多个逗号隔开

	// ===11-19新加字段
	long tuihuoid;// 退货站id
	long zhongzhuanid;// 中转站id

	// 入库备注
	private String inwarhouseremark;
	// 供货商拒收反库备注
	private String customerbrackhouseremark;

	private String resendtime;// 滞留订单再次配送时间
	private String tuihuozhaninstoreroomtime = "";// 退货站入库时间
	private String packagecode;// 扫描使用的包号
	private String scancwb = "";// 扫描单号
	private long handleresult;// 异常订单处理结果id
	private long handleperson;// 异常订单处理责任人id
	private String handlereason;// 异常订单处理原因

	private long currentsitetype;// 所在机构类型
	private long applytuihuobranchid;// 申请退货站点id
	private String applytuihuobranchname;// 申请退货站点
	private String applytuihuoremark;// 申请退货备注
	private long applyishandle;// 申请退货处理状态
	private String applyhandleremark;// 退货申请审核备注
	private String applyhandleusername;// 退货申请审核人
	private String applyhandletime;// 退货申请审核时间

	private long applyzhongzhuanbranchid;// 申请中转站点id
	private String applyzhongzhuanbranchname;// 申请中转站点
	private String applyzhongzhuanremark;// 申请中转备注
	private long applyzhongzhuanishandle;// 申请中转处理状态
	private String applyzhongzhuanhandleremark;// 中转申请审核备注
	private String applyzhongzhuanhandleusername;// 中转申请审核人
	private String applyzhongzhuanhandletime;// 中转申请审核时间

	// 新加显示字段2015-5-27
	private String cwbordertypename;// 订单类型名(原类型)
	private String matchbranchid;// 匹配站点id
	private String matchbranchname;// 匹配站点名称
	private String newcwbordertypename;// 现类型
	private BigDecimal newreceivefee;// 现在金额
	private String nowState;// 订单当前状态
	private String applytype;// 申请类型

	// 重置状态反馈
	private String deliveryname;// 配送结果
	private String cwbstatename;// 订单状态
	private String resetfeedusername;// 反馈人
	private String resetfeedtime;// 反馈时间
	private String donepeople;// 操作人
	private String donetime;// 操作时间

	// 支付信息修改部分（合成字段）
	private String oldnewCwbordertypename;
	private String oldnewReceivablefee;
	private String oldnewPaytype;

	private String applyuser;// 申请人
	private String applytime;// 申请时间
	private String confirmname;// 确认人
	private String confirmtime;// 确认时间
	private long nowapplystate;// 支付信息审核状态与支付信息修改确认的订单当前状态

	private String jobnum;

	private BigDecimal shouldfare = BigDecimal.ZERO; // 应收运费
	private String expressPayWay;// 支付方式

	private int mpsswitch;//mps开关（0：未开启，1：开启库房集单，2：开启站点集单）
	private int ismpsflag;//是否一票多件：0默认；1是一票多件
	private String vipclub;//业务类型：注意这里已经是label 
	
	// 添加揽件省 added by songkaojun 2015-11-17
	private String senderprovince;

	public String getJobnum() {
		return this.jobnum;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public long getNowapplystate() {
		return this.nowapplystate;
	}

	public void setNowapplystate(long nowapplystate) {
		this.nowapplystate = nowapplystate;
	}

	public String getConfirmname() {
		return this.confirmname;
	}

	public void setConfirmname(String confirmname) {
		this.confirmname = confirmname;
	}

	public String getConfirmtime() {
		return this.confirmtime;
	}

	public void setConfirmtime(String confirmtime) {
		this.confirmtime = confirmtime;
	}

	public String getApplytime() {
		return this.applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

	public String getApplyuser() {
		return this.applyuser;
	}

	public void setApplyuser(String applyuser) {
		this.applyuser = applyuser;
	}

	// 反馈状态申请
	private int reasonid;

	// 退供应商成功确认状态
	private String auditstatename;

	public String getAuditstatename() {
		return this.auditstatename;
	}

	public void setAuditstatename(String auditstatename) {
		this.auditstatename = auditstatename;
	}

	public int getReasonid() {
		return this.reasonid;
	}

	public void setReasonid(int reasonid) {
		this.reasonid = reasonid;
	}

	public String getOldnewCwbordertypename() {
		return this.oldnewCwbordertypename;
	}

	public void setOldnewCwbordertypename(String oldnewCwbordertypename) {
		this.oldnewCwbordertypename = oldnewCwbordertypename;
	}

	public String getOldnewReceivablefee() {
		return this.oldnewReceivablefee;
	}

	public void setOldnewReceivablefee(String oldnewReceivablefee) {
		this.oldnewReceivablefee = oldnewReceivablefee;
	}

	public String getOldnewPaytype() {
		return this.oldnewPaytype;
	}

	public void setOldnewPaytype(String oldnewPaytype) {
		this.oldnewPaytype = oldnewPaytype;
	}

	public String getDeliveryname() {
		return this.deliveryname;
	}

	public void setDeliveryname(String deliveryname) {
		this.deliveryname = deliveryname;
	}

	public String getCwbstatename() {
		return this.cwbstatename;
	}

	public void setCwbstatename(String cwbstatename) {
		this.cwbstatename = cwbstatename;
	}

	public String getResetfeedusername() {
		return this.resetfeedusername;
	}

	public void setResetfeedusername(String resetfeedusername) {
		this.resetfeedusername = resetfeedusername;
	}

	public String getResetfeedtime() {
		return this.resetfeedtime;
	}

	public void setResetfeedtime(String resetfeedtime) {
		this.resetfeedtime = resetfeedtime;
	}

	public String getDonepeople() {
		return this.donepeople;
	}

	public void setDonepeople(String donepeople) {
		this.donepeople = donepeople;
	}

	public String getDonetime() {
		return this.donetime;
	}

	public void setDonetime(String donetime) {
		this.donetime = donetime;
	}

	public String getApplytype() {
		return this.applytype;
	}

	public void setApplytype(String applytype) {
		this.applytype = applytype;
	}

	public String getNowState() {
		return this.nowState;
	}

	public void setNowState(String nowState) {
		this.nowState = nowState;
	}

	public String getNewcwbordertypename() {
		return this.newcwbordertypename;
	}

	public void setNewcwbordertypename(String newcwbordertypename) {
		this.newcwbordertypename = newcwbordertypename;
	}

	public BigDecimal getNewreceivefee() {
		return this.newreceivefee;
	}

	public void setNewreceivefee(BigDecimal newreceivefee) {
		this.newreceivefee = newreceivefee;
	}

	public String getMatchbranchid() {
		return this.matchbranchid;
	}

	public void setMatchbranchid(String matchbranchid) {
		this.matchbranchid = matchbranchid;
	}

	public String getMatchbranchname() {
		return this.matchbranchname;
	}

	public void setMatchbranchname(String matchbranchname) {
		this.matchbranchname = matchbranchname;
	}

	public String getCwbordertypename() {
		return this.cwbordertypename;
	}

	public void setCwbordertypename(String cwbordertypename) {
		this.cwbordertypename = cwbordertypename;
	}

	public String getMulti_shipcwb() {
		return this.multi_shipcwb == null ? "" : this.multi_shipcwb;
	}

	public void setMulti_shipcwb(String multi_shipcwb) {
		this.multi_shipcwb = multi_shipcwb;
	}

	public long getTargetcarwarehouse() {
		return this.targetcarwarehouse;
	}

	public void setTargetcarwarehouse(long targetcarwarehouse) {
		this.targetcarwarehouse = targetcarwarehouse;
	}

	public String getTargetcarwarehouseName() {
		return this.targetcarwarehouseName == null ? "" : this.targetcarwarehouseName;
	}

	public void setTargetcarwarehouseName(String targetcarwarehouseName) {
		this.targetcarwarehouseName = targetcarwarehouseName;
	}

	public long getOrderResultType() {
		return this.orderResultType;
	}

	public void setOrderResultType(long orderResultType) {
		this.orderResultType = orderResultType;
	}

	public String getOrderResultTypeText() {
		for (DeliveryStateEnum fote : DeliveryStateEnum.values()) {
			if (fote.getValue() == this.orderResultType) {
				return fote.getText();
			}
		}
		return "";
	}

	public String getExpt_code() {
		return this.expt_code == null ? "" : this.expt_code;
	}

	public void setExpt_code(String expt_code) {
		this.expt_code = expt_code;
	}

	public String getExpt_msg() {
		return this.expt_msg == null ? "" : this.expt_msg;
	}

	public void setExpt_msg(String expt_msg) {
		this.expt_msg = expt_msg;
	}

	public String getPaytype_old() {
		return this.paytype_old == null ? "" : this.paytype_old;
	}

	public void setPaytype_old(String paytype_old) {
		this.paytype_old = paytype_old;
	}

	public long getBackreasonid() {
		return this.backreasonid;
	}

	public void setBackreasonid(long backreasonid) {
		this.backreasonid = backreasonid;
	}

	public String getBackreason() {
		return this.backreason == null ? "" : this.backreason;
	}

	public void setBackreason(String backreason) {
		this.backreason = backreason;
	}

	public long getRuku_dangdang_flag() {
		return this.ruku_dangdang_flag;
	}

	public void setRuku_dangdang_flag(long ruku_dangdang_flag) {
		this.ruku_dangdang_flag = ruku_dangdang_flag;
	}

	public long getChuku_dangdang_flag() {
		return this.chuku_dangdang_flag;
	}

	public void setChuku_dangdang_flag(long chuku_dangdang_flag) {
		this.chuku_dangdang_flag = chuku_dangdang_flag;
	}

	public long getDeliverystate_dangdang_flag() {
		return this.deliverystate_dangdang_flag;
	}

	public void setDeliverystate_dangdang_flag(long deliverystate_dangdang_flag) {
		this.deliverystate_dangdang_flag = deliverystate_dangdang_flag;
	}

	public String getOperatorName() {
		return this.operatorName == null ? "" : this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public long getAuditEganstate() {
		return this.auditEganstate;
	}

	public void setAuditEganstate(long auditEganstate) {
		this.auditEganstate = auditEganstate;
	}

	public long getIspayUp() {
		return this.ispayUp;
	}

	public void setIspayUp(long ispayUp) {
		this.ispayUp = ispayUp;
	}

	public long getIsQiankuan() {
		return this.isQiankuan;
	}

	public void setIsQiankuan(long isQiankuan) {
		this.isQiankuan = isQiankuan;
	}

	public String getDelivername() {
		return this.delivername == null ? "" : this.delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getCustomername() {
		return this.customername == null ? "" : this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getBranchname() {
		return this.branchname == null ? "" : this.branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
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

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public String getCwb() {
		return this.cwb == null ? "" : this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
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
		if (consigneeaddress == null) {
			this.consigneeaddress = "";
			return;
		}
		// this.consigneeaddress = consigneeaddress.replaceAll("#",
		// "").replaceAll("[*]", "").replaceAll(" ", "").replaceAll("\\p{P}",
		// "");
		this.consigneeaddress = consigneeaddress.replaceAll("#", "").replaceAll("[*]", "").replaceAll(" ", "").replaceAll("\\p{Pc}|\\p{Ps}|\\p{Pe}|\\p{Pi}|\\p{Pf}|\\p{Po}", "");
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

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			this.setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return this.consigneemobile == null ? "" : this.consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = (consigneemobile);
	}

	public BigDecimal getReceivablefee() {
		return this.receivablefee;
	}

	public void setReceivablefee(String receivablefee) {
		try {
			this.receivablefee = new BigDecimal(receivablefee);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("費用不是有效的數字格式:" + receivablefee);
		}
	}

	public BigDecimal getPaybackfee() {
		return this.paybackfee;
	}

	public void setPaybackfee(String paybackfee) {
		try {
			this.paybackfee = new BigDecimal(paybackfee);
		} catch (Exception e) {
			throw new IllegalArgumentException("退費不是有效的數字格式:" + paybackfee);
		}
	}

	public String getCwbremark() {
		return this.cwbremark == null ? "" : this.cwbremark;
	}

	public void setCwbremark(String cwbremark) {
		this.cwbremark = cwbremark;
	}

	public void setShiptime(String shiptime) {
		this.shiptime = shiptime;
	}

	public String getShipcwb() {
		return this.shipcwb == null ? "" : this.shipcwb;
	}

	public void setShipcwb(String shipcwb) {
		this.shipcwb = shipcwb;
	}

	public String getExceldeliver() {
		return this.exceldeliver == null ? "" : this.exceldeliver;
	}

	public void setExceldeliver(String exceldeliver) {
		this.exceldeliver = exceldeliver;
	}

	public String getConsigneeno() {
		return this.consigneeno == null ? "" : this.consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = (consigneeno);
	}

	public String getExcelbranch() {
		return this.excelbranch == null ? "" : this.excelbranch;
	}

	public void setExcelbranch(String excelbranch) {
		this.excelbranch = excelbranch;
	}

	public String getCustomercommand() {
		return this.customercommand == null ? "" : this.customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	public String getDestination() {
		return this.destination == null ? "" : this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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

	public String getTranscwb() {
		return this.transcwb == null ? "" : this.transcwb;
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
		return this.backtocustomer_awb == null ? "" : this.backtocustomer_awb;
	}

	public void setBacktocustomer_awb(String backtocustomer_awb) {
		this.backtocustomer_awb = backtocustomer_awb;
	}

	public String getCwbflowflag() {
		return this.cwbflowflag == null ? "" : this.cwbflowflag;
	}

	public void setCwbflowflag(String cwbflowflag) {
		this.cwbflowflag = cwbflowflag;
	}

	public void setCarrealweight(String carrealweight) {
		try {
			this.carrealweight = new BigDecimal(carrealweight);
		} catch (Exception e) {
			throw new IllegalArgumentException("货物重量不是有效的數字格式:" + carrealweight);
		}
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
		return this.backcaramount;
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
		return this.sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public long getScannum() {
		return this.scannum;
	}

	public void setScannum(long scannum) {
		this.scannum = scannum;
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

	public String getEmaildate() {
		return this.emaildate == null ? "" : this.emaildate;
	}

	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}

	public String getCwbordertypeid() {
		return this.cwbordertypeid == null ? "" : this.cwbordertypeid;
	}

	public void setCwbordertypeid(String cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getCwbdelivertypeid() {
		return this.cwbdelivertypeid == null ? "" : this.cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(String cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	public String getShiptime() {
		return this.shiptime == null ? "" : this.shiptime;
	}

	public String getCommonname() {
		return this.commonname == null ? "" : this.commonname;
	}

	public void setCommonname(String commonname) {
		this.commonname = commonname;
	}

	public String getNewfollownotes() {
		return this.newfollownotes == null ? "" : this.newfollownotes;
	}

	public void setNewfollownotes(String newfollownotes) {
		this.newfollownotes = newfollownotes;
	}

	public long getMarksflag() {
		return this.marksflag;
	}

	public String getMarksflagStr() {
		return this.marksflag == 0 ? "否" : "是";
	}

	public void setMarksflag(long marksflag) {
		this.marksflag = marksflag;
	}

	public String getMarksflagmen() {
		return this.marksflagmen == null ? "" : this.marksflagmen;
	}

	public void setMarksflagmen(String marksflagmen) {
		this.marksflagmen = marksflagmen;
	}

	public long getCommonid() {
		return this.commonid;
	}

	public void setCommonid(long commonid) {
		this.commonid = commonid;
	}

	public String getAllfollownotes() {
		return this.allfollownotes;
	}

	public void setAllfollownotes(String allfollownotes) {
		this.allfollownotes = allfollownotes;
	}

	public BigDecimal getPrimitivemoney() {
		return (this.primitivemoney != null) && (this.primitivemoney.doubleValue() > 0) ? this.primitivemoney : this.caramount;
	}

	public void setPrimitivemoney(BigDecimal primitivemoney) {
		this.primitivemoney = primitivemoney;
	}

	public String getMarksflagtime() {
		return this.marksflagtime == null ? "" : this.marksflagtime;
	}

	public void setMarksflagtime(String marksflagtime) {
		this.marksflagtime = marksflagtime;
	}

	public String getEdittime() {
		return this.edittime == null ? "" : this.edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public String getEditman() {
		return this.editman == null ? "" : this.editman;
	}

	public void setEditman(String editman) {
		this.editman = editman;
	}

	public String getSigninman() {
		return this.signinman == null ? "" : this.signinman;
	}

	public void setSigninman(String signinman) {
		this.signinman = signinman;
	}

	public String getSignintime() {
		return this.signintime == null ? "" : this.signintime;
	}

	public void setSignintime(String signintime) {
		this.signintime = signintime;
	}

	public String getReturngoodsremark() {
		return this.returngoodsremark == null ? "" : this.returngoodsremark;
	}

	public void setReturngoodsremark(String returngoodsremark) {
		this.returngoodsremark = returngoodsremark;
	}

	public String getCommonnumber() {
		return this.commonnumber == null ? "" : this.commonnumber;
	}

	public void setCommonnumber(String commonnumber) {
		this.commonnumber = commonnumber;
	}

	public long getAuditstate() {
		return this.auditstate;
	}

	public String getDeliverybranch() {
		return this.deliverybranch == null ? "" : this.deliverybranch;
	}

	public void setDeliverybranch(String deliverybranch) {
		this.deliverybranch = deliverybranch;
	}

	public String getAuditstateStr() {
		return this.auditstate == 0 ? "否" : "是";
	}

	public void setAuditstate(long auditstate) {
		this.auditstate = auditstate;
	}

	public String getAuditor() {
		return this.auditor == null ? "" : this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAudittime() {
		return this.audittime == null ? "" : this.audittime;
	}

	public void setAudittime(String audittime) {
		this.audittime = audittime;
	}

	public String getEditsignintime() {
		return this.editsignintime == null ? "" : this.editsignintime;
	}

	public void setEditsignintime(String editsignintime) {
		this.editsignintime = editsignintime;
	}

	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

	public long getBeforDay() {
		long quot = 0;
		if ((this.emaildate != null) && (this.emaildate != "")) {
			try {
				Date date2 = this.ft.parse(this.emaildate);
				quot = new Date().getTime() - date2.getTime();
				quot = quot / 1000 / 60 / 60 / 24;
			} catch (ParseException e) {
				logger.error("", e);
			}
		}
		return quot;
	}

	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == this.flowordertype) {
				return fote.getText();
			}
		}
		return "";
	}

	public int getFloworderid() {
		return this.floworderid;
	}

	public void setFloworderid(int floworderid) {
		this.floworderid = floworderid;
	}

	public int getBranchid() {
		return this.branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public int getEmaildateid() {
		return this.emaildateid;
	}

	public void setEmaildateid(int emaildateid) {
		this.emaildateid = emaildateid;
	}

	public String getInstoreroomtime() {
		return this.instoreroomtime == null ? "" : this.instoreroomtime;
	}

	public void setInstoreroomtime(String instoreroomtime) {
		this.instoreroomtime = instoreroomtime;
	}

	public String getRemark1() {
		return this.remark1 == null ? "" : this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2 == null ? "" : this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return this.remark3 == null ? "" : this.remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return this.remark4 == null ? "" : this.remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return this.remark5 == null ? "" : this.remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getStartbranchname() {
		return this.startbranchname == null ? "" : this.startbranchname;
	}

	public void setStartbranchname(String startbranchname) {
		this.startbranchname = startbranchname;
	}

	public String getNextbranchname() {
		return this.nextbranchname == null ? "" : this.nextbranchname;
	}

	public void setNextbranchname(String nextbranchname) {
		this.nextbranchname = nextbranchname;
	}

	public String getOutstoreroomtime() {
		return this.outstoreroomtime == null ? "" : this.outstoreroomtime;
	}

	public void setOutstoreroomtime(String outstoreroomtime) {
		this.outstoreroomtime = outstoreroomtime;
	}

	public String getInSitetime() {
		return this.inSitetime == null ? "" : this.inSitetime;
	}

	public void setInSitetime(String inSitetime) {
		this.inSitetime = inSitetime;
	}

	public String getPickGoodstime() {
		return this.pickGoodstime == null ? "" : this.pickGoodstime;
	}

	public void setPickGoodstime(String pickGoodstime) {
		this.pickGoodstime = pickGoodstime;
	}

	public String getSendSuccesstime() {
		return this.sendSuccesstime == null ? "" : this.sendSuccesstime;
	}

	public void setSendSuccesstime(String sendSuccesstime) {
		this.sendSuccesstime = sendSuccesstime;
	}

	public String getGobacktime() {
		return this.gobacktime == null ? "" : this.gobacktime;
	}

	public void setGobacktime(String gobacktime) {
		this.gobacktime = gobacktime;
	}

	public String getGoclasstime() {
		return this.goclasstime == null ? "" : this.goclasstime;
	}

	public void setGoclasstime(String goclasstime) {
		this.goclasstime = goclasstime;
	}

	public String getNowtime() {
		return this.nowtime == null ? "" : this.nowtime;
	}

	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}

	public String getLeavedreasonStr() {
		return this.leavedreasonStr == null ? "" : this.leavedreasonStr;
	}

	public void setLeavedreasonStr(String leavedreasonStr) {
		this.leavedreasonStr = leavedreasonStr;
	}

	public String getInhouse() {
		return this.inhouse == null ? "" : this.inhouse;
	}

	public void setInhouse(String inhouse) {
		this.inhouse = inhouse;
	}

	public BigDecimal getRealweight() {
		return this.realweight;
	}

	public void setRealweight(BigDecimal realweight) {
		this.realweight = realweight;
	}

	public String getGoodsremark() {
		return this.goodsremark == null ? "" : this.goodsremark;
	}

	public void setGoodsremark(String goodsremark) {
		this.goodsremark = goodsremark;
	}

	public String getPaytype() {
		return this.paytype == null ? "" : this.paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getCarwarehousename() {
		return this.carwarehousename == null ? "" : this.carwarehousename;
	}

	public void setCarwarehousename(String carwarehousename) {
		this.carwarehousename = carwarehousename;
	}

	public String getCustomerwarehousename() {
		return this.customerwarehousename == null ? "" : this.customerwarehousename;
	}

	public void setCustomerwarehousename(String customerwarehousename) {
		this.customerwarehousename = customerwarehousename;
	}

	public long getFdeliverid() {
		return this.fdeliverid;
	}

	public void setFdeliverid(long fdeliverid) {
		this.fdeliverid = fdeliverid;
	}

	public String getFdelivername() {
		return this.fdelivername == null ? "" : this.fdelivername;
	}

	public void setFdelivername(String fdelivername) {
		this.fdelivername = fdelivername;
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
		return this.posremark == null ? "" : this.posremark;
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

	public long getStatisticstate() {
		return this.statisticstate;
	}

	public void setStatisticstate(long statisticstate) {
		this.statisticstate = statisticstate;
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
		return this.deliverstateremark == null ? "" : this.deliverstateremark;
	}

	public void setDeliverstateremark(String deliverstateremark) {
		this.deliverstateremark = deliverstateremark;
	}

	public int getGcaid() {
		return this.gcaid;
	}

	public void setGcaid(int gcaid) {
		this.gcaid = gcaid;
	}

	public long getGobackid() {
		return this.gobackid;
	}

	public void setGobackid(long gobackid) {
		this.gobackid = gobackid;
	}

	public long getPayupbranchid() {
		return this.payupbranchid;
	}

	public void setPayupbranchid(long payupbranchid) {
		this.payupbranchid = payupbranchid;
	}

	public String getPayupbranchname() {
		return this.payupbranchname == null ? "" : this.payupbranchname;
	}

	public void setPayupbranchname(String payupbranchname) {
		this.payupbranchname = payupbranchname;
	}

	public String getPodremarkStr() {
		return this.podremarkStr == null ? "" : this.podremarkStr;
	}

	public void setPodremarkStr(String podremarkStr) {
		this.podremarkStr = podremarkStr;
	}

	public String getReceivedfeeuserName() {
		return this.receivedfeeuserName == null ? "" : this.receivedfeeuserName;
	}

	public void setReceivedfeeuserName(String receivedfeeuserName) {
		this.receivedfeeuserName = receivedfeeuserName;
	}

	public String getPayuprealname() {
		return this.payuprealname == null ? "" : this.payuprealname;
	}

	public void setPayuprealname(String payuprealname) {
		this.payuprealname = payuprealname;
	}

	public long getYoudanwuhuoBranchid() {
		return this.youdanwuhuoBranchid;
	}

	public void setYoudanwuhuoBranchid(long youdanwuhuoBranchid) {
		this.youdanwuhuoBranchid = youdanwuhuoBranchid;
	}

	public long getYouhuowudanBranchid() {
		return this.youhuowudanBranchid;
	}

	public void setYouhuowudanBranchid(long youhuowudanBranchid) {
		this.youhuowudanBranchid = youhuowudanBranchid;
	}

	public long getTuotouTime() {
		return this.tuotouTime;
	}

	public void setTuotouTime(long tuotouTime) {
		this.tuotouTime = tuotouTime;
	}

	public long getYoujieguoTime() {
		return this.youjieguoTime;
	}

	public void setYoujieguoTime(long youjieguoTime) {
		this.youjieguoTime = youjieguoTime;
	}

	public long getRukutuotouTime() {
		return this.rukutuotouTime;
	}

	public void setRukutuotouTime(long rukutuotouTime) {
		this.rukutuotouTime = rukutuotouTime;
	}

	public long getRukuyoujieguoTime() {
		return this.rukuyoujieguoTime;
	}

	public void setRukuyoujieguoTime(long rukuyoujieguoTime) {
		this.rukuyoujieguoTime = rukuyoujieguoTime;
	}

	public long getDaozhantuotouTime() {
		return this.daozhantuotouTime;
	}

	public void setDaozhantuotouTime(long daozhantuotouTime) {
		this.daozhantuotouTime = daozhantuotouTime;
	}

	public long getDaozhanyoujieguoTime() {
		return this.daozhanyoujieguoTime;
	}

	public void setDaozhanyoujieguoTime(long daozhanyoujieguoTime) {
		this.daozhanyoujieguoTime = daozhanyoujieguoTime;
	}

	public long getTuihuoid() {
		return this.tuihuoid;
	}

	public void setTuihuoid(long tuihuoid) {
		this.tuihuoid = tuihuoid;
	}

	public long getZhongzhuanid() {
		return this.zhongzhuanid;
	}

	public void setZhongzhuanid(long zhongzhuanid) {
		this.zhongzhuanid = zhongzhuanid;
	}

	public static String getFlowTypeNameById(long id) {
		String flowtypename = "";
		for (FlowOrderTypeEnum f : FlowOrderTypeEnum.values()) {
			if (f.getValue() == id) {
				flowtypename = f.getText();
				break;
			}
		}
		return flowtypename;
	}

	public String getOrderType() {
		String flowtypename = "";
		for (CwbOrderTypeIdEnum f : CwbOrderTypeIdEnum.values()) {
			if ("".equals(this.cwbordertypeid)) {
				flowtypename = "配送";
			} else if (this.cwbordertypeid.equals("-1")) {
				flowtypename = "配送";
			} else if (f.getValue() == Integer.parseInt(this.cwbordertypeid)) {
				flowtypename = f.getText();
				break;
			}
		}
		return flowtypename;
	}

	public String getPaytypeName() {
		String paytypeName = "";
		if ((this.paytype != null) && !"".equals(this.paytype)) {
			if ("2".equals(this.paytype)) {
				paytypeName = "POS";
			} else {
				paytypeName = this.paytype.replace("1", "现金").replace("2", "POS").replace("3", "支票").replace("4", "其他");
				paytypeName = !"".equals(this.paytype) && (this.paytype.trim().length() > 1) ? paytypeName.substring(0, paytypeName.length()) : paytypeName;
			}
		}
		return paytypeName;
	}

	// 原支付方式
	public String getPaytypeNameOld() {
		String paytypeName = "";
		if ((this.paytype_old != null) && !"".equals(this.paytype_old)) {
			if ("2".equals(this.paytype_old)) {
				paytypeName = "POS";
			} else {
				paytypeName = this.paytype_old.replace("1", "现金").replace("2", "POS").replace("3", "支票").replace("4", "其他");
				paytypeName = !"".equals(this.paytype_old) && (this.paytype_old.trim().length() > 1) ? paytypeName.substring(0, paytypeName.length() - 1) : paytypeName;
			}
		}
		return paytypeName;
	}

	public String getStatisticstateStr() {
		String returnStr = "";
		if (this.statisticstate == 1) {
			returnStr = "未归班";
		} else if (this.statisticstate == 2) {
			returnStr = "未归班";
		} else if (this.statisticstate == 3) {
			returnStr = "暂不处理";
		}
		return !"".equals(returnStr) ? returnStr : this.statisticstateStr;
	}

	public void setStatisticstateStr(String statisticstateStr) {
		this.statisticstateStr = statisticstateStr;
	}

	public String getIspayUpStr() {
		return this.ispayUp == 1 ? "已上缴" : "未上交";
	}

	public String getIsQiankuanStr() {
		return this.isQiankuan == 1 ? "有欠款" : "未欠款";
	}

	public String getAuditEganstateStr() {
		return this.auditEganstate == 0 ? "未审核" : "已审核";
	}

	public String getCwbdelivertypeStr() {
		return this.cwbdelivertypeid.equals("2") ? "加急" : "普通";
	}

	public String getCurrentbranchname() {
		return this.currentbranchname == null ? "" : this.currentbranchname;
	}

	public void setCurrentbranchname(String currentbranchname) {
		this.currentbranchname = currentbranchname;
	}

	public String getInwarhouseremark() {
		return this.inwarhouseremark == null ? "" : this.inwarhouseremark;
	}

	public void setInwarhouseremark(String inwarhouseremark) {
		this.inwarhouseremark = inwarhouseremark;
	}

	public String getCustomerbrackhouseremark() {
		return this.customerbrackhouseremark;
	}

	public void setCustomerbrackhouseremark(String customerbrackhouseremark) {
		this.customerbrackhouseremark = customerbrackhouseremark;
	}

	public String getDeliverStateText() {
		for (DeliveryStateEnum fote : DeliveryStateEnum.values()) {
			if (fote.getValue() == this.deliverystate) {
				return fote.getText();
			}
		}
		return "";
	}

	public long getCwbstate() {
		return this.cwbstate;
	}

	public void setCwbstate(long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public long getCurrentbranchid() {
		return this.currentbranchid;
	}

	public void setCurrentbranchid(long currentbranchid) {
		this.currentbranchid = currentbranchid;
	}

	public String getJushoutime() {
		return this.jushoutime;
	}

	public void setJushoutime(String jushoutime) {
		this.jushoutime = jushoutime;
	}

	public String getTuigonghuoshangchukutime() {
		return this.tuigonghuoshangchukutime;
	}

	public void setTuigonghuoshangchukutime(String tuigonghuoshangchukutime) {
		this.tuigonghuoshangchukutime = tuigonghuoshangchukutime;
	}

	public String getInSiteBranchname() {
		return this.inSiteBranchname;
	}

	public void setInSiteBranchname(String inSiteBranchname) {
		this.inSiteBranchname = inSiteBranchname;
	}

	public String getResendtime() {
		return this.resendtime;
	}

	public void setResendtime(String resendtime) {
		this.resendtime = resendtime;
	}

	public String getTuihuozhaninstoreroomtime() {
		return this.tuihuozhaninstoreroomtime;
	}

	public void setTuihuozhaninstoreroomtime(String tuihuozhaninstoreroomtime) {
		this.tuihuozhaninstoreroomtime = tuihuozhaninstoreroomtime;
	}

	public String getPackagecode() {
		return this.packagecode;
	}

	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}

	public String getScancwb() {
		return this.scancwb;
	}

	public void setScancwb(String scancwb) {
		this.scancwb = scancwb;
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

	public long getCurrentsitetype() {
		return this.currentsitetype;
	}

	public void setCurrentsitetype(long currentsitetype) {
		this.currentsitetype = currentsitetype;
	}

	public long getApplytuihuobranchid() {
		return this.applytuihuobranchid;
	}

	public void setApplytuihuobranchid(long applytuihuobranchid) {
		this.applytuihuobranchid = applytuihuobranchid;
	}

	public String getApplytuihuoremark() {
		return this.applytuihuoremark;
	}

	public void setApplytuihuoremark(String applytuihuoremark) {
		this.applytuihuoremark = applytuihuoremark;
	}

	public String getApplytuihuobranchname() {
		return this.applytuihuobranchname;
	}

	public void setApplytuihuobranchname(String applytuihuobranchname) {
		this.applytuihuobranchname = applytuihuobranchname;
	}

	public long getApplyishandle() {
		return this.applyishandle;
	}

	public void setApplyishandle(long applyishandle) {
		this.applyishandle = applyishandle;
	}

	public String getApplyhandleremark() {
		return this.applyhandleremark;
	}

	public void setApplyhandleremark(String applyhandleremark) {
		this.applyhandleremark = applyhandleremark;
	}

	public String getApplyhandleusername() {
		return this.applyhandleusername;
	}

	public void setApplyhandleusername(String applyhandleusername) {
		this.applyhandleusername = applyhandleusername;
	}

	public String getApplyhandletime() {
		return this.applyhandletime;
	}

	public void setApplyhandletime(String applyhandletime) {
		this.applyhandletime = applyhandletime;
	}

	public long getApplyzhongzhuanbranchid() {
		return this.applyzhongzhuanbranchid;
	}

	public void setApplyzhongzhuanbranchid(long applyzhongzhuanbranchid) {
		this.applyzhongzhuanbranchid = applyzhongzhuanbranchid;
	}

	public String getApplyzhongzhuanbranchname() {
		return this.applyzhongzhuanbranchname;
	}

	public void setApplyzhongzhuanbranchname(String applyzhongzhuanbranchname) {
		this.applyzhongzhuanbranchname = applyzhongzhuanbranchname;
	}

	public String getApplyzhongzhuanremark() {
		return this.applyzhongzhuanremark;
	}

	public void setApplyzhongzhuanremark(String applyzhongzhuanremark) {
		this.applyzhongzhuanremark = applyzhongzhuanremark;
	}

	public long getApplyzhongzhuanishandle() {
		return this.applyzhongzhuanishandle;
	}

	public void setApplyzhongzhuanishandle(long applyzhongzhuanishandle) {
		this.applyzhongzhuanishandle = applyzhongzhuanishandle;
	}

	public String getApplyzhongzhuanhandleremark() {
		return this.applyzhongzhuanhandleremark;
	}

	public void setApplyzhongzhuanhandleremark(String applyzhongzhuanhandleremark) {
		this.applyzhongzhuanhandleremark = applyzhongzhuanhandleremark;
	}

	public String getApplyzhongzhuanhandleusername() {
		return this.applyzhongzhuanhandleusername;
	}

	public void setApplyzhongzhuanhandleusername(String applyzhongzhuanhandleusername) {
		this.applyzhongzhuanhandleusername = applyzhongzhuanhandleusername;
	}

	public String getApplyzhongzhuanhandletime() {
		return this.applyzhongzhuanhandletime;
	}

	public void setApplyzhongzhuanhandletime(String applyzhongzhuanhandletime) {
		this.applyzhongzhuanhandletime = applyzhongzhuanhandletime;
	}

	public String getTimelimited() {
		return this.timelimited;
	}

	public void setTimelimited(String timelimited) {
		this.timelimited = timelimited;
	}

	public int getMpsswitch() {
		return this.mpsswitch;
	}

	public void setMpsswitch(int mpsswitch) {
		this.mpsswitch = mpsswitch;
	}

	public int getIsmpsflag() {
		return this.ismpsflag;
	}

	public void setIsmpsflag(int ismpsflag) {
		this.ismpsflag = ismpsflag;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final CwbOrderView other = (CwbOrderView) obj;
		if ((this.getTranscwb().equals(other.getTranscwb())) && (this.getCwb().equals(other.getCwb()))) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String consigneeaddress = "中国北京北京市丰台区西罗园一区22-6-503100077";
		String infos = consigneeaddress.replaceAll("#", "").replaceAll("[*]", "").replaceAll(" ", "").replaceAll("\\p{Pc}|\\p{Ps}|\\p{Pe}|\\p{Pi}|\\p{Pf}|\\p{Po}", "");
		logger.info(infos);

	}

	public BigDecimal getShouldfare() {
		return this.shouldfare;
	}

	public void setShouldfare(BigDecimal shouldfare) {
		this.shouldfare = shouldfare;
	}

	public String getExpressPayWay() {
		return this.expressPayWay;
	}

	public void setExpressPayWay(String expressPayWay) {
		this.expressPayWay = expressPayWay;
	}

	public String getSenderprovince() {
		return this.senderprovince;
	}

	public void setSenderprovince(String senderprovince) {
		this.senderprovince = senderprovince;
	}

	public String getVipclub() {
		return vipclub;
	}

	public void setVipclub(String vipclub) {
		this.vipclub = vipclub;
	}
}
