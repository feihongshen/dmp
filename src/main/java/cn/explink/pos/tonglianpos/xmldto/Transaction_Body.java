package cn.explink.pos.tonglianpos.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class Transaction_Body {

	private String delivery_man;
	private String delivery_name;
	private String delivery_zone;
	private String order_no; // 订单号
	private String transcwb; // 电商订单号

	private double order_payable_amt;
	private int pay_type;
	private String terminal_id; // 终端号
	private String trace_no; // 凭证号 流水号
	private String alipay_trace_no;
	private String trade_no;
	private double order_amt;
	private String acq_type;
	private String password;

	@XmlElement(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement(name = "delivery_man")
	public String getDelivery_man() {
		return this.delivery_man;
	}

	@XmlElement(name = "E_order_no")
	public String getTranscwb() {
		return this.transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	public void setDelivery_man(String delivery_man) {
		this.delivery_man = delivery_man;
	}

	@XmlElement(name = "delivery_name")
	public String getDelivery_name() {
		return this.delivery_name;
	}

	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}

	@XmlElement(name = "delivery_zone")
	public String getDelivery_zone() {
		return this.delivery_zone;
	}

	public void setDelivery_zone(String delivery_zone) {
		this.delivery_zone = delivery_zone;
	}

	@XmlElement(name = "order_no")
	public String getOrder_no() {
		return this.order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@XmlElement(name = "order_payable_amt")
	public double getOrder_payable_amt() {
		return this.order_payable_amt;
	}

	public void setOrder_payable_amt(double order_payable_amt) {
		this.order_payable_amt = order_payable_amt;
	}

	@XmlElement(name = "pay_type")
	public int getPay_type() {
		return this.pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	@XmlElement(name = "terminal_id")
	public String getTerminal_id() {
		return this.terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	@XmlElement(name = "trace_no")
	public String getTrace_no() {
		return this.trace_no;
	}

	public void setTrace_no(String trace_no) {
		this.trace_no = trace_no;
	}

	@XmlElement(name = "alipay_trace_no")
	public String getAlipay_trace_no() {
		return this.alipay_trace_no;
	}

	public void setAlipay_trace_no(String alipay_trace_no) {
		this.alipay_trace_no = alipay_trace_no;
	}

	@XmlElement(name = "trade_no")
	public String getTrade_no() {
		return this.trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	@XmlElement(name = "order_amt")
	public double getOrder_amt() {
		return this.order_amt;
	}

	public void setOrder_amt(double order_amt) {
		this.order_amt = order_amt;
	}

	@XmlElement(name = "acq_type")
	public String getAcq_type() {
		return this.acq_type;
	}

	public void setAcq_type(String acq_type) {
		this.acq_type = acq_type;
	}

	@XmlElement(name = "signee")
	public String getSignee() {
		return this.signee;
	}

	public void setSignee(String signee) {
		this.signee = signee;
	}

	@XmlElement(name = "consignee_sign_flag")
	public String getConsignee_sign_flag() {
		return this.consignee_sign_flag;
	}

	public void setConsignee_sign_flag(String consignee_sign_flag) {
		this.consignee_sign_flag = consignee_sign_flag;
	}

	@XmlElement(name = "void_amt")
	public double getVoid_amt() {
		return this.void_amt;
	}

	public void setVoid_amt(double void_amt) {
		this.void_amt = void_amt;
	}

	@XmlElement(name = "void_trace_no")
	public String getVoid_trace_no() {
		return this.void_trace_no;
	}

	public void setVoid_trace_no(String void_trace_no) {
		this.void_trace_no = void_trace_no;
	}

	@XmlElement(name = "ori_alipay_trace_no")
	public String getOri_alipay_trace_no() {
		return this.ori_alipay_trace_no;
	}

	public void setOri_alipay_trace_no(String ori_alipay_trace_no) {
		this.ori_alipay_trace_no = ori_alipay_trace_no;
	}

	@XmlElement(name = "ex_code")
	public String getEx_code() {
		return this.ex_code;
	}

	public void setEx_code(String ex_code) {
		this.ex_code = ex_code;
	}

	@XmlElement(name = "ex_desc")
	public String getEx_desc() {
		return this.ex_desc;
	}

	public void setEx_desc(String ex_desc) {
		this.ex_desc = ex_desc;
	}

	@XmlElement(name = "amt")
	public double getAmt() {
		return this.amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	@XmlElement(name = "merchant_code")
	public String getMerchant_code() {
		return this.merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	@XmlElement(name = "account_keyword")
	public String getAccount_keyword() {
		return this.account_keyword;
	}

	public void setAccount_keyword(String account_keyword) {
		this.account_keyword = account_keyword;
	}

	@XmlElement(name = "merchant_biz_no")
	public String getMerchant_biz_no() {
		return this.merchant_biz_no;
	}

	public void setMerchant_biz_no(String merchant_biz_no) {
		this.merchant_biz_no = merchant_biz_no;
	}

	@XmlElement(name = "merchant_biz_type")
	public String getMerchant_biz_type() {
		return this.merchant_biz_type;
	}

	public void setMerchant_biz_type(String merchant_biz_type) {
		this.merchant_biz_type = merchant_biz_type;
	}

	@XmlElement(name = "desc")
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String signee;
	private String consignee_sign_flag;
	private double void_amt;
	private String void_trace_no;
	private String ori_alipay_trace_no;
	private String ex_code; // 异常码
	private String ex_desc; // 异常原因
	private double amt;
	private double infactfee; // 实收运费

	private String merchant_code;
	private String account_keyword;
	private String merchant_biz_no;
	private String merchant_biz_type;
	private String desc;
	private String return_type; // 上门退方式 1 全退， 2部分退 3拒退

	@XmlElement(name = "return_type")
	public String getReturn_type() {
		return this.return_type;
	}

	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}

	@XmlElement(name = "infactfee")
	public double getInfactfee() {
		return this.infactfee;
	}

	public void setInfactfee(double infactfee) {
		this.infactfee = infactfee;
	}
}
