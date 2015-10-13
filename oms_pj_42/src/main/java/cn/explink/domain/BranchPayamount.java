package cn.explink.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

/**
 * 站点交款审核记录表
 * 
 * @author Administrator
 *
 */
public class BranchPayamount {

	private long branchpayid; // 主键id
	private long payUpId; // 交款提交的id
	private long branchid; // 站点id
	private String branchname = ""; // 站点名称
	private String deliverpaydate; // 员工交款日期
	private int cwbnum; // 员工交款票数
	private String checkdate; // 审核日期
	private String branchpaydatetime; // 站点交款时间
	private String branchpaydate; // 分站交款日期
	private BigDecimal receivablefee = BigDecimal.ZERO; // 应收金额
	private BigDecimal receivedfee = BigDecimal.ZERO; // 实收金额
	private BigDecimal paybackfee = BigDecimal.ZERO; // 应退金额
	private BigDecimal paybackedfee = BigDecimal.ZERO; // 实退金额
	private BigDecimal payheadfee = BigDecimal.ZERO; // 应交总部现金金额
	private BigDecimal payedheadfee = BigDecimal.ZERO; // 审核到账现金
	private String payremark = ""; // 交款备注
	private String checkremark = ""; // 审核备注
	private String paywayname = ""; // 交款方式
	private String payproveid = ""; // 付款凭证号
	private BigDecimal receivedfeecash = BigDecimal.ZERO; // 现金实收
	private BigDecimal receivedfeepos = BigDecimal.ZERO; // 公司POS实收
	private BigDecimal receivedfeecheque = BigDecimal.ZERO; // 支票实收
	private BigDecimal receivedfeepos_checked = BigDecimal.ZERO; // 已审核到账POS
	private BigDecimal receivedfeecheque_checked = BigDecimal.ZERO; // 已审核支票实收
	private BigDecimal otherbranchfee = BigDecimal.ZERO; // 其他款项
	private BigDecimal otherbranchfee_checked = BigDecimal.ZERO; // 其他款项-已审核
	private BigDecimal receivedfee_halfback = BigDecimal.ZERO; // 半退收款
	private BigDecimal receivedfee_success = BigDecimal.ZERO; // 成功收款
	private BigDecimal totaldebtfee = BigDecimal.ZERO; // 累计欠款
	private String checkposdate; // pos审核时间
	private int cashnum; // 现金票数
	private int posnum; // POS票数
	private int upstate; // 审核状态
	private int ordercount; // 员工确认票数
	private int userid; // c操作人id
	private String orderStr = ""; // 订单编号组合串
	private String username = ""; // 操作人姓名
	private String payuprealname = ""; // 上交款人姓名
	private long upbranchid; // 上交款对应的财务
	private long payup_type; // 上交款类型 1 货款 2 罚款
	private long way; // 上交款方式 1.银行转账 2.现金

	public BranchPayamount() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		checkdate = sdf.format(new Date());
	}

	public long getBranchpayid() {
		return branchpayid;
	}

	public void setBranchpayid(long branchpayid) {
		this.branchpayid = branchpayid;
	}

	public long getPayUpId() {
		return payUpId;
	}

	public void setPayUpId(long payUpId) {
		this.payUpId = payUpId;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getDeliverpaydate() {
		if (deliverpaydate != null && deliverpaydate.length() > 10) {
			deliverpaydate = deliverpaydate.substring(0, 10);
		}
		return deliverpaydate;
	}

	public void setDeliverpaydate(String deliverpaydate) {
		this.deliverpaydate = deliverpaydate;
	}

	public int getCwbnum() {
		return cwbnum;
	}

	public void setCwbnum(int cwbnum) {
		this.cwbnum = cwbnum;
	}

	public String getCheckdate() {
		if (checkdate != null && checkdate.length() > 19) {
			checkdate = checkdate.substring(0, 19);
		}
		return checkdate;
	}

	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}

	public String getBranchpaydatetime() {
		if (branchpaydatetime != null && branchpaydatetime.length() > 19) {
			branchpaydatetime = branchpaydatetime.substring(0, 19);
		}
		return branchpaydatetime;
	}

	public void setBranchpaydatetime(String branchpaydatetime) {
		this.branchpaydatetime = branchpaydatetime;
	}

	public String getBranchpaydate() {
		return branchpaydate;
	}

	public void setBranchpaydate(String branchpaydate) {
		this.branchpaydate = branchpaydate;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getReceivedfee() {
		return receivedfee;
	}

	public void setReceivedfee(BigDecimal receivedfee) {
		this.receivedfee = receivedfee;
	}

	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	public BigDecimal getPaybackedfee() {
		return paybackedfee;
	}

	public void setPaybackedfee(BigDecimal paybackedfee) {
		this.paybackedfee = paybackedfee;
	}

	public BigDecimal getPayheadfee() {
		return payheadfee;
	}

	public void setPayheadfee(BigDecimal payheadfee) {
		this.payheadfee = payheadfee;
	}

	public BigDecimal getPayedheadfee() {
		return payedheadfee;
	}

	public void setPayedheadfee(BigDecimal payedheadfee) {
		this.payedheadfee = payedheadfee;
	}

	public String getPayremark() {
		return payremark;
	}

	public void setPayremark(String payremark) {
		this.payremark = payremark;
	}

	public String getCheckremark() {
		return checkremark;
	}

	public void setCheckremark(String checkremark) {
		this.checkremark = checkremark;
	}

	public String getPaywayname() {
		return paywayname;
	}

	public void setPaywayname(String paywayname) {
		this.paywayname = paywayname;
	}

	public String getPayproveid() {
		return payproveid;
	}

	public void setPayproveid(String payproveid) {
		this.payproveid = payproveid;
	}

	public BigDecimal getReceivedfeecash() {
		return receivedfeecash;
	}

	public void setReceivedfeecash(BigDecimal receivedfeecash) {
		this.receivedfeecash = receivedfeecash;
	}

	public BigDecimal getReceivedfeepos() {
		return receivedfeepos;
	}

	public void setReceivedfeepos(BigDecimal receivedfeepos) {
		this.receivedfeepos = receivedfeepos;
	}

	public BigDecimal getReceivedfeecheque() {
		return receivedfeecheque;
	}

	public void setReceivedfeecheque(BigDecimal receivedfeecheque) {
		this.receivedfeecheque = receivedfeecheque;
	}

	public BigDecimal getReceivedfeepos_checked() {
		return receivedfeepos_checked;
	}

	public void setReceivedfeepos_checked(BigDecimal receivedfeepos_checked) {
		this.receivedfeepos_checked = receivedfeepos_checked;
	}

	public BigDecimal getReceivedfeecheque_checked() {
		return receivedfeecheque_checked;
	}

	public void setReceivedfeecheque_checked(BigDecimal receivedfeecheque_checked) {
		this.receivedfeecheque_checked = receivedfeecheque_checked;
	}

	public BigDecimal getOtherbranchfee() {
		return otherbranchfee;
	}

	public void setOtherbranchfee(BigDecimal otherbranchfee) {
		this.otherbranchfee = otherbranchfee;
	}

	public BigDecimal getOtherbranchfee_checked() {
		return otherbranchfee_checked;
	}

	public void setOtherbranchfee_checked(BigDecimal otherbranchfee_checked) {
		this.otherbranchfee_checked = otherbranchfee_checked;
	}

	public BigDecimal getReceivedfee_halfback() {
		return receivedfee_halfback;
	}

	public void setReceivedfee_halfback(BigDecimal receivedfee_halfback) {
		this.receivedfee_halfback = receivedfee_halfback;
	}

	public BigDecimal getReceivedfee_success() {
		return receivedfee_success;
	}

	public void setReceivedfee_success(BigDecimal receivedfee_success) {
		this.receivedfee_success = receivedfee_success;
	}

	public BigDecimal getTotaldebtfee() {
		return totaldebtfee;
	}

	public void setTotaldebtfee(BigDecimal totaldebtfee) {
		this.totaldebtfee = totaldebtfee;
	}

	public String getCheckposdate() {
		return checkposdate;
	}

	public void setCheckposdate(String checkposdate) {
		this.checkposdate = checkposdate;
	}

	public int getCashnum() {
		return cashnum;
	}

	public void setCashnum(int cashnum) {
		this.cashnum = cashnum;
	}

	public int getPosnum() {
		return posnum;
	}

	public void setPosnum(int posnum) {
		this.posnum = posnum;
	}

	public int getUpstate() {
		return upstate;
	}

	public void setUpstate(int upstate) {
		this.upstate = upstate;
	}

	public int getOrdercount() {
		return ordercount;
	}

	public void setOrdercount(int ordercount) {
		this.ordercount = ordercount;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPayuprealname() {
		return payuprealname;
	}

	public void setPayuprealname(String payuprealname) {
		this.payuprealname = payuprealname;
	}

	public long getPayup_type() {
		return payup_type;
	}

	public void setPayup_type(long payup_type) {
		this.payup_type = payup_type;
	}

	public long getWay() {
		return way;
	}

	public void setWay(long way) {
		this.way = way;
	}

	public long getUpbranchid() {
		return upbranchid;
	}

	public void setUpbranchid(long upbranchid) {
		this.upbranchid = upbranchid;
	}

	public JSONObject getObjctToJSON() {
		JSONObject reJson = new JSONObject();
		reJson.put("payUpId", this.payUpId);
		reJson.put("checkremark", this.checkremark);
		return reJson;
	}

}
