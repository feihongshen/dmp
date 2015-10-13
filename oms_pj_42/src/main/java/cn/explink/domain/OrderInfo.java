package cn.explink.domain;

import java.math.BigDecimal;

public class OrderInfo {
	long opscwbid; // 主键id
	long startbranchid; // 当前所在机构id
	long nextbranchid; // 下一站目的机构id
	long outwarehousegroupid;// 出库批次编号
	String backtocustomer_awb;// 退供货商封包批次号
	String cwbflowflag; // 订单流程类型 1正常件 2中转件 3再投件
	String carwarehouse;// 发货仓库
	BigDecimal backcaramount; // 取回货物金额
	long sendcarnum;// 发货数量
	long backcarnum;// 取货数量
	BigDecimal caramount;// 货物金额
	long deliverid; // 派送员id
	int emailfinishflag;// 库房入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	int reacherrorflag;// 站点入库状态 1正常入库 2有货无单 3有单无货 默认值为-1
	long cwbreachbranchid;// 订单入库机构id
	long cwbreachdeliverbranchid;// 订单到货派送机构id
	String podfeetoheadflag;// 站点收款是否已上交总部 0未上交 1已上交
	String podfeetoheadtime;// 站点上交款时间
	String podfeetoheadchecktime;// 站点交款总部审核时
	String podfeetoheadcheckflag;// 总部交款审核状态
	long leavedreasonid;// 滞留原因id
	String deliversubscribeday;// 滞留预约派送日
	String customerwarehouseid;// 客户发货仓库id
	String emaildate;// 邮件时间
	String shiptime;// 供货商发货时间
	long serviceareaid;// 服务区域id
	long customerid;// 供货商id
	String shipcwb;// 配送单号
	String consigneename;// 收件人名称
	String consigneeaddress;// 收件人地址
	String consigneephone;// 收件人电话
	String customercommand;// 客户要求
	BigDecimal receivablefee;// 代收货款应收金额
	BigDecimal paybackfee;// 上门退货应退金额
	String cwb;// 订单号
	long shipperid;// 退供货商承运商id
	String cwbordertypeid;// 订单类型（1配送 2上门退 3上门换）
	String consigneemobile;// 收件人手机
	String transcwb;// 运单号
	String destination;// 目的地
	String cwbdelivertypeid;// 订单入库机构id
	String exceldeliver;// 指定派送员（用于地址库匹配）
	String excelbranch;// 指定派送分站（用于地址库匹配）
	long excelimportuserid;// 导入操作员id
	long branchgroupid;// 站点批次id
}
