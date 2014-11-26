package cn.explink.pos.unionpay.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespContent {

	private String ordernum; // 订单号
	private String enterprisename; // 商户名
	private String accountreceived; // 实际刷卡金额
	private String bank;// 银行
	private String bankaccount;// 付款人账号
	private String bankcardtype;// 银行卡类型,默认1 1=储蓄卡 2=信用卡
	/**
	 * state 0=正常 1=冲正 2=对账插入 3=出错（错误码见)errorcode) 4=消费撤销 5=人工录入
	 */
	private String state; // 支付状态,默认0

	/**
	 * 1=没有出错时（冲正和对账插入不算出错） 2=masget支付金额与银联支付金额不等 3=masget派送物流与银联派送物流不同
	 * 4=masget终端与银联终端物流不同 5=银联账单没有对应记录 6=原始单证公司与银联不一致
	 */
	private String errorcode; // 默认1

	private String merchantid; // 商户编号
	private String masgetsettlementflag; // Masget清分标志,默认1 1=Masget清分
											// 2=非masget清分

	private String terminalnumber; // 交易终端
	private String terminaldealid; // 终端交易流水号
	private String unionpaydealid;// 银联交易流水号
	/**
	 * 付款方支付方式,. 1=现金 2=POS刷卡 3=支票 4=银联线上 5=手机支付 6=cfca支付
	 */
	private String paymenttype;
	private String businesstime;// 交易时间

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getEnterprisename() {
		return enterprisename;
	}

	public void setEnterprisename(String enterprisename) {
		this.enterprisename = enterprisename;
	}

	public String getAccountreceived() {
		return accountreceived;
	}

	public void setAccountreceived(String accountreceived) {
		this.accountreceived = accountreceived;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getBankcardtype() {
		return bankcardtype;
	}

	public void setBankcardtype(String bankcardtype) {
		this.bankcardtype = bankcardtype;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getMasgetsettlementflag() {
		return masgetsettlementflag;
	}

	public void setMasgetsettlementflag(String masgetsettlementflag) {
		this.masgetsettlementflag = masgetsettlementflag;
	}

	public String getTerminalnumber() {
		return terminalnumber;
	}

	public void setTerminalnumber(String terminalnumber) {
		this.terminalnumber = terminalnumber;
	}

	public String getTerminaldealid() {
		return terminaldealid;
	}

	public void setTerminaldealid(String terminaldealid) {
		this.terminaldealid = terminaldealid;
	}

	public String getUnionpaydealid() {
		return unionpaydealid;
	}

	public void setUnionpaydealid(String unionpaydealid) {
		this.unionpaydealid = unionpaydealid;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getBusinesstime() {
		return businesstime;
	}

	public void setBusinesstime(String businesstime) {
		this.businesstime = businesstime;
	}

}
