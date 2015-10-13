package cn.explink.domain;

import java.math.BigDecimal;

import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;

// default package

/**
 * CwbOrderTail entity. @author MyEclipse Persistence Tools
 */

public class CwbOrderTail implements java.io.Serializable {
	// Fields
	private long id;
	private String cwb;
	private String newpaywayid;// 现支付方式
	private Long customerid;// 供应商ID
	private String customerstr;
	private Integer cwbordertypeid;// 订单类型
	private String cwbordertypestr;
	private String paywayid;// 原支付方式
	private BigDecimal receivablefee;// 代收货款应收金额
	private BigDecimal paybackfee;// 上门退货应退金额
	private String branchid;// 当前站点
	private String deliverybranchid;// 配送站点
	private String nextbranchid;// 下一站
	private Integer deliverystate;// 配送结果
	private String deliverystateStr;// 配送结果
	private Long cwbstate;// 订单状态
	private String gobackstate;// 归班状态
	private String emaildatetime;// 发货时间
	private String getgoodstime;// 提货时间
	private String intowarehoustime;// 入库时间
	private String outwarehousetime;// 出库时间
	private String housetohousetime;// 库对库出库时间
	private String substationgoodstime;// 到货扫描时间
	private String receivegoodstime;// 分站领货时间
	private String couplebacktime;// 反馈时间
	private String checktime;// 审核时间
	private String returngoodsoutwarehousetime;// 退货出站扫描时间
	private String tuihuointowarehoustime;// 退货站入库时间
	private String customerbacktime;// 退供应商出库时间
	private String changeintowarehoustime;// 中转站入库时间
	private String changeouttowarehoustime;// 中转站出库时间
	private String curcwdtime;// 用于页面查询
	private String curquerytimetype;// 页面当前时间类型
	private String curquerytimecolumn;// 当前时间字段
	private String begintime;// 页面时间段开始点
	private String endtime;// 页面时间段结束点
	private int goodsType;// 货物类型
	private String strGoodsType;
	private int financeAuditStatus;// 财务审核状态.
	private String strFinanceAuditStatus;
	private String[] dispatchbranchids;
	private String[] curdispatchbranchids;
	private String[] nextdispatchbranchids;
	private String[] customerids;
	private String[] cwbordertypeids;
	private String[] operationOrderResultTypes;
	// 加字段
	private Long flowordertype;// 订单当前操作状态 要转化成 oms的flowtype
	private String zhandianouttozhongzhuantime;// 站点中转出站时间
	private String tuihuoouttozhandiantime;// 退货站退货出库出给站点时间
	private String gonghuoshangchenggongtime;// 退供货商成功时间
	private String gonghuoshangjushoutime;// 供货商拒收返库时间

	// ALTER TABLE `commen_cwb_order_tail`
	// ADD INDEX `tail_cwb_zhandianouttozhongzhuantime`
	// (`zhandianouttozhongzhuantime`),
	// ADD INDEX `tail_cwb_tuihuoouttozhandiantime` (`tuihuoouttozhandiantime`),
	// ADD INDEX `tail_cwb_gonghuoshangchenggongtime`
	// (`gonghuoshangchenggongtime`),
	// ADD INDEX `tail_cwb_gonghuoshangjushoutime` (`gonghuoshangjushoutime`);

	public String getCwbordertypestr() {
		return cwbordertypestr;
	}

	public Long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(Long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCwbordertypestr(String cwbordertypestr) {
		this.cwbordertypestr = cwbordertypestr;
	}

	public String getCustomerstr() {
		return customerstr;
	}

	public void setCustomerstr(String customerstr) {
		this.customerstr = customerstr;
	}

	public String[] getCwbordertypeids() {
		return cwbordertypeids;
	}

	public void setCwbordertypeids(String[] cwbordertypeids) {
		this.cwbordertypeids = cwbordertypeids;
	}

	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == this.flowordertype)
				return fote.getText();
		}
		return "";
	}

	// CwbOrderTypeIdEnum
	public String getIsAudit() {
		if (this.flowordertype == 36) {
			return "已审核";
		} else {
			return "未审核";
		}

	}

	public String getPayType() {

		return PaytypeEnum.getByValue(Integer.parseInt(this.paywayid)).getText();
	}

	public String getCurPayType() {

		return PaytypeEnum.getByValue(Integer.parseInt(this.newpaywayid)).getText();
	}

	public String getCwbOrderTypeMethod() {
		if (this.cwbordertypeid != -1)
			return CwbOrderTypeIdEnum.getByValue(this.cwbordertypeid).getText();
		else
			return "";
	}

	// Constructors

	public String[] getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String[] customerids) {
		this.customerids = customerids;
	}

	public String[] getDispatchbranchids() {
		return dispatchbranchids;
	}

	public void setDispatchbranchids(String[] dispatchbranchids) {
		this.dispatchbranchids = dispatchbranchids;
	}

	public String[] getCurdispatchbranchids() {
		return curdispatchbranchids;
	}

	public void setCurdispatchbranchids(String[] curdispatchbranchids) {
		this.curdispatchbranchids = curdispatchbranchids;
	}

	public String[] getNextdispatchbranchids() {
		return nextdispatchbranchids;
	}

	public void setNextdispatchbranchids(String[] nextdispatchbranchids) {
		this.nextdispatchbranchids = nextdispatchbranchids;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCurquerytimecolumn() {
		return curquerytimecolumn;
	}

	public void setCurquerytimecolumn(String curquerytimecolumn) {
		this.curquerytimecolumn = curquerytimecolumn;
	}

	public String getCurcwdtime() {
		return curcwdtime;
	}

	public String getCurquerytimetype() {
		return curquerytimetype;
	}

	public void setCurquerytimetype(String curquerytimetype) {
		this.curquerytimetype = curquerytimetype;
	}

	public void setCurcwdtime(String curcwdtime) {
		this.curcwdtime = curcwdtime;
	}

	/** default constructor */
	public CwbOrderTail() {
	}

	/** full constructor */
	public CwbOrderTail(String newpaywayid, Long customerid, Integer cwbordertypeid, String paywayid, BigDecimal receivablefee, BigDecimal paybackfee, String branchid, String deliverybranchid,
			String nextbranchid, Integer deliverystate, Long cwbstate, String gobackstate, String emaildatetime, String couplebacktime, String checktime, String customerbacktime,
			String tuihuointowarehoustime, String changeouttowarehoustime, String housetohousetime, String getgoodstime, String intowarehoustime, String outwarehousetime, String substationgoodstime,
			String receivegoodstime, String returngoodsoutwarehousetime, String changeintowarehoustime) {
		this.newpaywayid = newpaywayid;
		this.customerid = customerid;
		this.cwbordertypeid = cwbordertypeid;
		this.paywayid = paywayid;
		this.receivablefee = receivablefee;
		this.paybackfee = paybackfee;
		this.branchid = branchid;
		this.deliverybranchid = deliverybranchid;
		this.nextbranchid = nextbranchid;
		this.deliverystate = deliverystate;
		this.cwbstate = cwbstate;
		this.gobackstate = gobackstate;
		this.emaildatetime = emaildatetime;
		this.couplebacktime = couplebacktime;
		this.checktime = checktime;
		this.customerbacktime = customerbacktime;
		this.tuihuointowarehoustime = tuihuointowarehoustime;
		this.changeouttowarehoustime = changeouttowarehoustime;
		this.housetohousetime = housetohousetime;
		this.getgoodstime = getgoodstime;
		this.intowarehoustime = intowarehoustime;
		this.outwarehousetime = outwarehousetime;
		this.substationgoodstime = substationgoodstime;
		this.receivegoodstime = receivegoodstime;
		this.returngoodsoutwarehousetime = returngoodsoutwarehousetime;
		this.changeintowarehoustime = changeintowarehoustime;
	}

	// Property accessors

	public String getNewpaywayid() {
		return this.newpaywayid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public void setNewpaywayid(String newpaywayid) {
		this.newpaywayid = newpaywayid;
	}

	public Long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(Long customerid) {
		this.customerid = customerid;
	}

	public Integer getCwbordertypeid() {
		return this.cwbordertypeid;
	}

	public void setCwbordertypeid(Integer cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	public String getPaywayid() {
		return this.paywayid;
	}

	public void setPaywayid(String paywayid) {
		this.paywayid = paywayid;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public String getBranchid() {
		return this.branchid;
	}

	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}

	public String getDeliverybranchid() {
		return this.deliverybranchid;
	}

	public void setDeliverybranchid(String deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}

	public String getNextbranchid() {
		return this.nextbranchid;
	}

	public void setNextbranchid(String nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public Integer getDeliverystate() {
		return this.deliverystate;
	}

	public void setDeliverystate(Integer deliverystate) {
		this.deliverystate = deliverystate;
	}

	public Long getCwbstate() {
		return cwbstate;
	}

	public void setCwbstate(Long cwbstate) {
		this.cwbstate = cwbstate;
	}

	public String getGobackstate() {
		return this.gobackstate;
	}

	public void setGobackstate(String gobackstate) {
		this.gobackstate = gobackstate;
	}

	public String getEmaildatetime() {
		return this.emaildatetime;
	}

	public void setEmaildatetime(String emaildatetime) {
		this.emaildatetime = emaildatetime;
	}

	public String getCouplebacktime() {
		return this.couplebacktime;
	}

	public void setCouplebacktime(String couplebacktime) {
		this.couplebacktime = couplebacktime;
	}

	public String getChecktime() {
		return this.checktime;
	}

	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}

	public String getCustomerbacktime() {
		return this.customerbacktime;
	}

	public void setCustomerbacktime(String customerbacktime) {
		this.customerbacktime = customerbacktime;
	}

	public String getTuihuointowarehoustime() {
		return this.tuihuointowarehoustime;
	}

	public void setTuihuointowarehoustime(String tuihuointowarehoustime) {
		this.tuihuointowarehoustime = tuihuointowarehoustime;
	}

	public String getChangeouttowarehoustime() {
		return this.changeouttowarehoustime;
	}

	public void setChangeouttowarehoustime(String changeouttowarehoustime) {
		this.changeouttowarehoustime = changeouttowarehoustime;
	}

	public String getHousetohousetime() {
		return this.housetohousetime;
	}

	public void setHousetohousetime(String housetohousetime) {
		this.housetohousetime = housetohousetime;
	}

	public String getGetgoodstime() {
		return this.getgoodstime;
	}

	public void setGetgoodstime(String getgoodstime) {
		this.getgoodstime = getgoodstime;
	}

	public String getIntowarehoustime() {
		return this.intowarehoustime;
	}

	public void setIntowarehoustime(String intowarehoustime) {
		this.intowarehoustime = intowarehoustime;
	}

	public String getOutwarehousetime() {
		return this.outwarehousetime;
	}

	public void setOutwarehousetime(String outwarehousetime) {
		this.outwarehousetime = outwarehousetime;
	}

	public String getSubstationgoodstime() {
		return this.substationgoodstime;
	}

	public void setSubstationgoodstime(String substationgoodstime) {
		this.substationgoodstime = substationgoodstime;
	}

	public String getReceivegoodstime() {
		return this.receivegoodstime;
	}

	public void setReceivegoodstime(String receivegoodstime) {
		this.receivegoodstime = receivegoodstime;
	}

	public String getReturngoodsoutwarehousetime() {
		return this.returngoodsoutwarehousetime;
	}

	public void setReturngoodsoutwarehousetime(String returngoodsoutwarehousetime) {
		this.returngoodsoutwarehousetime = returngoodsoutwarehousetime;
	}

	public String getChangeintowarehoustime() {
		return this.changeintowarehoustime;
	}

	public void setChangeintowarehoustime(String changeintowarehoustime) {
		this.changeintowarehoustime = changeintowarehoustime;
	}

	public String[] getOperationOrderResultTypes() {
		return operationOrderResultTypes;
	}

	public void setOperationOrderResultTypes(String[] operationOrderResultTypes) {
		this.operationOrderResultTypes = operationOrderResultTypes;
	}

	public String getZhandianouttozhongzhuantime() {
		return zhandianouttozhongzhuantime;
	}

	public void setZhandianouttozhongzhuantime(String zhandianouttozhongzhuantime) {
		this.zhandianouttozhongzhuantime = zhandianouttozhongzhuantime;
	}

	public String getTuihuoouttozhandiantime() {
		return tuihuoouttozhandiantime;
	}

	public void setTuihuoouttozhandiantime(String tuihuoouttozhandiantime) {
		this.tuihuoouttozhandiantime = tuihuoouttozhandiantime;
	}

	public String getGonghuoshangchenggongtime() {
		return gonghuoshangchenggongtime;
	}

	public void setGonghuoshangchenggongtime(String gonghuoshangchenggongtime) {
		this.gonghuoshangchenggongtime = gonghuoshangchenggongtime;
	}

	public String getGonghuoshangjushoutime() {
		return gonghuoshangjushoutime;
	}

	public void setGonghuoshangjushoutime(String gonghuoshangjushoutime) {
		this.gonghuoshangjushoutime = gonghuoshangjushoutime;
	}

	public String getDeliverystateStr() {
		return deliverystateStr;
	}

	public void setDeliverystateStr(String deliverystateStr) {
		this.deliverystateStr = deliverystateStr;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getFinanceAuditStatus() {
		return financeAuditStatus;
	}

	public void setFinanceAuditStatus(int financeAuditStatus) {
		this.financeAuditStatus = financeAuditStatus;
	}

	public String getStrGoodsType() {
		return strGoodsType;
	}

	public void setStrGoodsType(String strGoodsType) {
		this.strGoodsType = strGoodsType;
	}

	public String getStrFinanceAuditStatus() {
		return strFinanceAuditStatus;
	}

	public void setStrFinanceAuditStatus(String strFinanceAuditStatus) {
		this.strFinanceAuditStatus = strFinanceAuditStatus;
	}

}